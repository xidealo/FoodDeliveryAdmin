package com.bunbeauty.domain.repository.api.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bunbeauty.common.utils.IDataStoreHelper
import com.bunbeauty.data.model.Cafe
import com.bunbeauty.data.model.Company
import com.bunbeauty.data.model.order.OrderEntity
import com.bunbeauty.data.model.order.Order
import com.bunbeauty.data.enums.OrderStatus
import com.bunbeauty.data.model.MenuProduct
import com.bunbeauty.data.model.firebase.MenuProductFirebase
import com.bunbeauty.domain.BuildConfig.APP_ID
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.joda.time.DateTime
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ApiRepository @Inject constructor(
    private val dataStoreHelper: IDataStoreHelper
) : IApiRepository, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Job()
    override val updatedOrderListLiveData = MutableLiveData<List<Order>>()

    private val firebaseInstance = FirebaseDatabase.getInstance()
    private val orderList = LinkedList<Order>()
    private lateinit var addedOrderListLiveData: MutableLiveData<List<Order>>

    override fun getAddedOrderListLiveData(cafeId: String): LiveData<List<Order>> {
        addedOrderListLiveData = object : MutableLiveData<List<Order>>() {
            private var orderListener: ChildEventListener? = null
            private val ordersReference = firebaseInstance
                .getReference(OrderEntity.ORDERS)
                .child(APP_ID)
                .child(cafeId)
                .orderByChild(OrderEntity.TIMESTAMP)
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

        return addedOrderListLiveData
    }

    @ExperimentalCoroutinesApi
    override fun getCafeList(): SharedFlow<List<Cafe>> {
        val cafeListSharedFlow = MutableSharedFlow<List<Cafe>>()
        val contactInfoRef = firebaseInstance
            .getReference(COMPANY)
            .child(APP_ID)
            .child(Cafe.CAFES)

        contactInfoRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val cafeList = snapshot.children.map { cafeSnapshot ->
                    cafeSnapshot.getValue(Cafe::class.java)!!
                }
                launch(IO) {
                    cafeListSharedFlow.emit(cafeList)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        return cafeListSharedFlow
    }

    override fun login(login: String, passwordHash: String): SharedFlow<Boolean> {
        val isAuthorizedSharedFlow = MutableSharedFlow<Boolean>()

        val companyRef = firebaseInstance.getReference(COMPANY).child(login)
        companyRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(companySnapshot: DataSnapshot) {
                launch(IO) {

                    if (companySnapshot.childrenCount == 0L) {
                        isAuthorizedSharedFlow.emit(false)
                        return@launch
                    }

                    val companyPassword = companySnapshot.child(Company.PASSWORD).value as String
                    if (passwordHash == companyPassword) {
                        updateToken(login)
                        isAuthorizedSharedFlow.emit(true)
                    } else {
                        isAuthorizedSharedFlow.emit(false)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                launch(IO) {
                    isAuthorizedSharedFlow.emit(false)
                }
            }
        })

        return isAuthorizedSharedFlow
    }

    override fun updateToken(login: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@addOnCompleteListener
            }

            val token = task.result!!
            firebaseInstance.getReference(COMPANY)
                .child(login)
                .child(Company.TOKEN)
                .setValue(token)
            launch(IO) {
                dataStoreHelper.saveToken(token)
            }
        }
    }

    override fun updateOrder(cafeId: String, uuid: String, newStatus: OrderStatus) {
        val orderRef = firebaseInstance
            .getReference(OrderEntity.ORDERS)
            .child(APP_ID)
            .child(cafeId)
            .child(uuid)
            .child(OrderEntity.ORDER_ENTITY)
            .child(OrderEntity.ORDER_STATUS)
        orderRef.setValue(newStatus)
    }

    override fun updateMenuProduct(menuProduct: MenuProductFirebase, uuid: String) {
        val menuProductRef = firebaseInstance
            .getReference(COMPANY)
            .child(APP_ID)
            .child(MENU_PRODUCTS)
            .child(uuid)
        menuProductRef.setValue(menuProduct)
    }

    private fun getOrderWithCartProducts(ordersReference: Query): ChildEventListener {
        return ordersReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(orderSnapshot: DataSnapshot, previousChildName: String?) {
                orderList.addFirst(getOrderValue(orderSnapshot))
                addedOrderListLiveData.value = orderList
            }

            override fun onChildChanged(orderSnapshot: DataSnapshot, previousChildName: String?) {
                val order = getOrderValue(orderSnapshot)
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

    override fun getOrderWithCartProductsList(
        cafeId: String,
        daysCount: Int
    ): SharedFlow<List<Order>> {
        val ordersRef = firebaseInstance
            .getReference(OrderEntity.ORDERS)
            .child(APP_ID)
            .child(cafeId)
            .orderByChild(OrderEntity.TIMESTAMP)
            .startAt(DateTime.now().minusDays(daysCount).millis.toDouble())

        val ordersWithCartProductsShareFlow = MutableSharedFlow<List<Order>>()
        ordersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(ordersSnapshot: DataSnapshot) {
                launch {
                    val ordersWithCartProductsList = arrayListOf<Order>()
                    for (orderSnapshot in ordersSnapshot.children.reversed()) {
                        ordersWithCartProductsList.add(
                            getOrderValue(orderSnapshot)
                        )
                    }
                    withContext(Dispatchers.Main) {
                        ordersWithCartProductsShareFlow.emit(ordersWithCartProductsList)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        return ordersWithCartProductsShareFlow
    }

    override fun getOrderWithCartProductsAllCafesList(daysCount: Int): SharedFlow<List<Order>> {
        val ordersRef = firebaseInstance
            .getReference(OrderEntity.ORDERS)
            .child(APP_ID)

        val ordersWithCartProductsShareFlow = MutableSharedFlow<List<Order>>()
        ordersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(cafeOrdersSnapshot: DataSnapshot) {
                launch {
                    val ordersWithCartProductsList = arrayListOf<Order>()
                    for (ordersSnapshot in cafeOrdersSnapshot.children.reversed()) {
                        for (orderSnapshot in ordersSnapshot.children) {
                            ordersWithCartProductsList.add(
                                getOrderValue(orderSnapshot)
                            )
                        }
                    }
                    withContext(Dispatchers.Main) {
                        ordersWithCartProductsShareFlow.emit(ordersWithCartProductsList)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        return ordersWithCartProductsShareFlow
    }

    override fun getMenuProductList(): Flow<List<MenuProduct>> {
        val menuProductListSharedFlow = MutableSharedFlow<List<MenuProduct>>()
        val menuProductsRef = firebaseInstance
            .getReference(COMPANY)
            .child(APP_ID)
            .child(MENU_PRODUCTS)

        menuProductsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val menuProductList = snapshot.children.map { menuProductSnapshot ->
                    menuProductSnapshot.getValue(MenuProduct::class.java)!!
                        .also { it.uuid = menuProductSnapshot.key!! }

                }
                launch(IO) {
                    menuProductListSharedFlow.emit(menuProductList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        return menuProductListSharedFlow
    }

    fun getOrderValue(orderSnapshot: DataSnapshot): Order {
        return orderSnapshot.getValue(Order::class.java).apply {
            this?.uuid = orderSnapshot.key.toString()
        } ?: Order()
    }

    companion object {
        private const val COMPANY = "COMPANY"
        private const val MENU_PRODUCTS: String = "menu_products"
        private const val DELIVERY: String = "delivery"
    }

}