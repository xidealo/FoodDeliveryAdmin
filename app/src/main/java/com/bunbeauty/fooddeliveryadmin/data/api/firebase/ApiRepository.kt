package com.bunbeauty.fooddeliveryadmin.data.api.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bunbeauty.fooddeliveryadmin.BuildConfig.APP_ID
import com.bunbeauty.fooddeliveryadmin.data.local.storage.IDataStoreHelper
import com.bunbeauty.fooddeliveryadmin.data.model.CartProduct
import com.bunbeauty.fooddeliveryadmin.data.model.Company
import com.bunbeauty.fooddeliveryadmin.data.model.MenuProduct
import com.bunbeauty.fooddeliveryadmin.data.model.order.Order
import com.bunbeauty.fooddeliveryadmin.data.model.order.OrderWithCartProducts
import com.bunbeauty.fooddeliveryadmin.enums.OrderStatus
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import org.joda.time.DateTime
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ApiRepository @Inject constructor(
    private val dataStoreHelper: IDataStoreHelper
) : IApiRepository, CoroutineScope {

    private val firebaseInstance = FirebaseDatabase.getInstance()

    override val coroutineContext: CoroutineContext
        get() = Job()

    private val orderList = LinkedList<OrderWithCartProducts>()
    override val addedOrderListLiveData = object: MutableLiveData<List<OrderWithCartProducts>>(emptyList()) {
        private var orderListener: ChildEventListener? = null
        private val ordersReference = firebaseInstance
                .getReference(Order.ORDERS)
                .child(APP_ID)
                .orderByChild(Order.TIMESTAMP)
                .startAt(DateTime.now().minusDays(2).millis.toDouble())

        override fun onActive() {
            super.onActive()

            orderListener = getOrderWithCartProducts(ordersReference)
        }

        override fun onInactive() {
            orderList.clear()
            orderListener?.let {
                ordersReference.removeEventListener(it)
            }

            super.onInactive()
        }
    }
    override val updatedOrderListLiveData = MutableLiveData<List<OrderWithCartProducts>>(emptyList())


    override fun login(login: String, passwordHash: String): LiveData<Boolean> {
        val isAuthorized = MutableLiveData<Boolean>()

        val companyRef = firebaseInstance.getReference(Company.COMPANY).child(login)
        companyRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(companySnapshot: DataSnapshot) {
                if (companySnapshot.childrenCount == 0L) {
                    isAuthorized.value = false
                    return
                }

                val companyPassword = companySnapshot.child(Company.PASSWORD).value as String
                if (passwordHash == companyPassword) {
                    updateToken(login)
                    isAuthorized.value = true
                } else {
                    isAuthorized.value = false
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                isAuthorized.value = false
            }
        })

        return isAuthorized
    }

    override fun updateToken(login: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@addOnCompleteListener
            }

            val token = task.result!!
            firebaseInstance.getReference(Company.COMPANY)
                    .child(login)
                    .child(Company.TOKEN)
                    .setValue(token)
            launch(IO) {
                dataStoreHelper.saveToken(token)
            }
        }
    }

    override fun updateOrder(uuid: String, newStatus: OrderStatus) {
        val orderRef = firebaseInstance
                .getReference(Order.ORDERS)
                .child(APP_ID)
                .child(uuid)
                .child(Order.ORDER_STATUS)
        orderRef.setValue(newStatus)
    }

    private fun getOrderWithCartProducts(ordersReference: Query): ChildEventListener {
        return ordersReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                orderList.addFirst(getOrderWithCartProductsFromSnapshot(snapshot))
                addedOrderListLiveData.value = orderList
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val order = getOrderWithCartProductsFromSnapshot(snapshot)
                val index = orderList.indexOfFirst { it.uuid == order.uuid }
                if (index != -1) {
                    orderList[index] = order
                    updatedOrderListLiveData.value = orderList
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}

        })
    }


    override fun getOrderWithCartProductsList(): LiveData<List<OrderWithCartProducts>> {
        val ordersRef = firebaseInstance
                .getReference(Order.ORDERS)
                .child(APP_ID)
                .orderByChild(Order.TIMESTAMP)
                .startAt(DateTime.now().minusDays(2).millis.toDouble())

        val ordersWithCartProductsLiveData = MutableLiveData<List<OrderWithCartProducts>>()
        ordersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(ordersSnapshot: DataSnapshot) {
                launch {
                    val ordersWithCartProductsList = arrayListOf<OrderWithCartProducts>()
                    for (orderSnapshot in ordersSnapshot.children.reversed()) {
                        ordersWithCartProductsList.add(
                                getOrderWithCartProductsFromSnapshot(orderSnapshot)
                        )
                    }
                    withContext(Dispatchers.Main) {
                        ordersWithCartProductsLiveData.value = ordersWithCartProductsList
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        return ordersWithCartProductsLiveData
    }

    /*override fun getOrderWithCartProducts(): LiveData<OrderWithCartProducts> {
        val ordersRef = firebaseInstance
            .getReference(Order.ORDERS)
            .child(APP_ID)
            .orderByChild(Order.TIMESTAMP)
            .startAt(DateTime.now().minusDays(2).millis.toDouble())

        val orderWithCartProductsLiveData = MutableLiveData<OrderWithCartProducts>()
        ordersRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.child(CartProduct.CART_PRODUCTS).childrenCount != 0L)
                    orderWithCartProductsLiveData.value =
                        getOrderWithCartProductsFromSnapshot(snapshot)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                orderWithCartProductsLiveData.value =
                    getOrderWithCartProductsFromSnapshot(snapshot)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}

        })
        return orderWithCartProductsLiveData
    }*/

    fun getOrderWithCartProductsFromSnapshot(orderSnapshot: DataSnapshot): OrderWithCartProducts {
        val orderWithCartProducts = OrderWithCartProducts()
        orderWithCartProducts.uuid = orderSnapshot.key ?: ""
        orderWithCartProducts.order.uuid = orderSnapshot.key ?: ""
        orderWithCartProducts.order.street = orderSnapshot.child(Order.STREET).value as String
        orderWithCartProducts.order.house = orderSnapshot.child(Order.HOUSE).value as String
        orderWithCartProducts.order.flat = orderSnapshot.child(Order.FLAT).value as String
        orderWithCartProducts.order.entrance = orderSnapshot.child(Order.ENTRANCE).value as String
        orderWithCartProducts.order.intercom = orderSnapshot.child(Order.INTERCOM).value as String
        orderWithCartProducts.order.floor = orderSnapshot.child(Order.FLOOR).value as String
        orderWithCartProducts.order.comment = orderSnapshot.child(Order.COMMENT).value as String
        orderWithCartProducts.order.phone = orderSnapshot.child(Order.PHONE).value as String
        orderWithCartProducts.order.orderStatus =
            OrderStatus.valueOf(orderSnapshot.child(Order.ORDER_STATUS).value as String)
        orderWithCartProducts.order.time = orderSnapshot.child(Order.TIMESTAMP).value as Long

        val cartProducts = mutableListOf<CartProduct>()
        for (cartProductSnapshot in orderSnapshot.child(CartProduct.CART_PRODUCTS).children) {
            cartProducts.add(
                CartProduct(
                    count = (cartProductSnapshot.child(CartProduct.COUNT).value as Long).toInt(),
                    orderUuid = orderSnapshot.key ?: "",
                    menuProduct = MenuProduct(
                        name = cartProductSnapshot.child(MenuProduct.NAME).value as String,
                        cost = (cartProductSnapshot.child(MenuProduct.COST).value as Long).toInt()
                    )
                )
            )
        }

        orderWithCartProducts.cartProducts = cartProducts
        return orderWithCartProducts
    }

}