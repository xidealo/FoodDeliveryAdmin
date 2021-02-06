package com.bunbeauty.fooddeliveryadmin.data.api.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bunbeauty.fooddeliveryadmin.BuildConfig.APP_ID
import com.bunbeauty.fooddeliveryadmin.data.local.storage.IDataStoreHelper
import com.bunbeauty.fooddeliveryadmin.data.model.Address
import com.bunbeauty.fooddeliveryadmin.data.model.CartProduct
import com.bunbeauty.fooddeliveryadmin.data.model.Company
import com.bunbeauty.fooddeliveryadmin.data.model.MenuProduct
import com.bunbeauty.fooddeliveryadmin.data.model.order.OrderEntity
import com.bunbeauty.fooddeliveryadmin.data.model.order.Order
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

    private val orderList = LinkedList<Order>()
    override val addedOrderListLiveData = object : MutableLiveData<List<Order>>(emptyList()) {
        private var orderListener: ChildEventListener? = null
        private val ordersReference = firebaseInstance
            .getReference(OrderEntity.ORDERS)
            .child(APP_ID)
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
    override val updatedOrderListLiveData = MutableLiveData<List<Order>>(emptyList())


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
            .getReference(OrderEntity.ORDERS)
            .child(APP_ID)
            .child(uuid)
            .child(OrderEntity.ORDER_ENTITY)
            .child(OrderEntity.ORDER_STATUS)
        orderRef.setValue(newStatus)
    }

    private fun getOrderWithCartProducts(ordersReference: Query): ChildEventListener {
        return ordersReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(orderSnapshot: DataSnapshot, previousChildName: String?) {
                orderList.addFirst(orderSnapshot.getValue(Order::class.java) ?: Order())
                addedOrderListLiveData.value = orderList
            }

            override fun onChildChanged(orderSnapshot: DataSnapshot, previousChildName: String?) {
                val order = orderSnapshot.getValue(Order::class.java) ?: Order()
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


    override fun getOrderWithCartProductsList(): LiveData<List<Order>> {
        val ordersRef = firebaseInstance
            .getReference(OrderEntity.ORDERS)
            .child(APP_ID)
            .orderByChild(OrderEntity.TIMESTAMP)
            .startAt(DateTime.now().minusDays(2).millis.toDouble())

        val ordersWithCartProductsLiveData = MutableLiveData<List<Order>>()
        ordersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(ordersSnapshot: DataSnapshot) {
                launch {
                    val ordersWithCartProductsList = arrayListOf<Order>()
                    for (orderSnapshot in ordersSnapshot.children.reversed()) {
                        ordersWithCartProductsList.add(
                            orderSnapshot.getValue(Order::class.java) ?: Order()
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

}