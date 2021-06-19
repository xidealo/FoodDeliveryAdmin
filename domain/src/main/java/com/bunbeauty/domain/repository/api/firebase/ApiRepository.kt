package com.bunbeauty.domain.repository.api.firebase

import android.util.Log
import com.bunbeauty.data.enums.OrderStatus
import com.bunbeauty.data.model.Cafe
import com.bunbeauty.data.model.Cafe.Companion.CAFES
import com.bunbeauty.data.model.Company
import com.bunbeauty.data.model.MenuProduct
import com.bunbeauty.data.model.firebase.MenuProductFirebase
import com.bunbeauty.data.model.order.Order
import com.bunbeauty.data.model.order.OrderEntity
import com.bunbeauty.domain.BuildConfig.APP_ID
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import org.joda.time.DateTime
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ApiRepository @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : IApiRepository, CoroutineScope {

    override val coroutineContext: CoroutineContext = Job() + IO

    private val orderList = LinkedList<Order>()

    @ExperimentalCoroutinesApi
    override fun getAddedOrderListByCafeId(cafeId: String): Flow<List<Order>> = callbackFlow {
        orderList.clear()
        offer(emptyList())

        val ordersReference = firebaseDatabase
            .getReference(OrderEntity.ORDERS)
            .child(APP_ID)
            .child(cafeId)
            .orderByChild(OrderEntity.TIMESTAMP)
            .startAt(DateTime.now().minusDays(2).millis.toDouble())
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                orderList.addFirst(getOrderValue(snapshot, cafeId))
                offer(orderList)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        }
        ordersReference.addChildEventListener(childEventListener)

        awaitClose {
            ordersReference.removeEventListener(childEventListener)
        }
    }

    @ExperimentalCoroutinesApi
    override fun getUpdatedOrderListByCafeId(cafeId: String): Flow<List<Order>> = callbackFlow {
        orderList.clear()
        offer(orderList)

        val ordersReference = firebaseDatabase
            .getReference(OrderEntity.ORDERS)
            .child(APP_ID)
            .child(cafeId)
            .orderByChild(OrderEntity.TIMESTAMP)
            .startAt(DateTime.now().minusDays(2).millis.toDouble())
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val order = getOrderValue(snapshot, cafeId)
                val index = orderList.indexOfFirst { it.uuid == order.uuid }
                if (index != -1) {
                    orderList[index] = order
                    offer(orderList)
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        }
        ordersReference.addChildEventListener(childEventListener)

        awaitClose {
            ordersReference.removeEventListener(childEventListener)
        }
    }

    @ExperimentalCoroutinesApi
    override fun getCafeList(): SharedFlow<List<Cafe>> {
        val cafeListSharedFlow = MutableSharedFlow<List<Cafe>>()
        val contactInfoRef = firebaseDatabase
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

    @ExperimentalCoroutinesApi
    override fun login(login: String, passwordHash: String): Flow<Boolean> = callbackFlow {
        val companyReference = firebaseDatabase.getReference(COMPANY).child(login)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(companySnapshot: DataSnapshot) {
                launch(IO) {
                    if (companySnapshot.childrenCount == 0L) {
                        offer(false)
                        return@launch
                    }

                    val companyPassword = companySnapshot.child(Company.PASSWORD).value as String
                    if (passwordHash == companyPassword) {
                        offer(true)
                    } else {
                        offer(false)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                launch(IO) {
                    offer(false)
                }
            }
        }
        companyReference.addListenerForSingleValueEvent(valueEventListener)

        awaitClose { companyReference.removeEventListener(valueEventListener) }
    }

    override fun subscribeOnNotification() {
        Firebase.messaging.subscribeToTopic(NOTIFICATION_TOPIC)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("NotificationTag", "Subscribe to topic is successful")
                } else {
                    Log.d("NotificationTag", "Subscribe to topic is not successful")
                }
            }
    }

    override fun unsubscribeOnNotification() {
        Firebase.messaging.unsubscribeFromTopic(NOTIFICATION_TOPIC)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("NotificationTag", "Unsubscribe to topic is successful")
                } else {
                    Log.d("NotificationTag", "Unsubscribe to topic is not successful")
                }
            }
    }

    override fun insert(menuProduct: MenuProductFirebase) {
        //TODO("Not yet implemented")
    }

    override fun updateOrderStatus(cafeId: String, orderUuid: String, status: OrderStatus) {
        val orderRef = firebaseDatabase
            .getReference(OrderEntity.ORDERS)
            .child(APP_ID)
            .child(cafeId)
            .child(orderUuid)
            .child(OrderEntity.ORDER_ENTITY)
            .child(OrderEntity.ORDER_STATUS)
        orderRef.setValue(status)
    }

    override fun updateMenuProduct(menuProduct: MenuProductFirebase, uuid: String) {
        val menuProductRef = firebaseDatabase
            .getReference(COMPANY)
            .child(APP_ID)
            .child(MENU_PRODUCTS)
            .child(uuid)
        menuProductRef.setValue(menuProduct)
    }

    override fun delete(order: Order) {
        if (order.uuid.isEmpty()) return
        /* val orderReference = firebaseInstance
             .getReference(ORDERS)
             .child(APP_ID)
             .child(order.cafeId)
             .child(order.uuid)
         orderReference.removeValue()*/
    }

    private fun getOrderWithCartProducts(
        ordersReference: Query,
        cafeId: String
    ): ChildEventListener {
        return ordersReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(orderSnapshot: DataSnapshot, previousChildName: String?) {
                orderList.addFirst(getOrderValue(orderSnapshot, cafeId))
                //addedOrderListStateFlow.value = orderList
            }

            override fun onChildChanged(orderSnapshot: DataSnapshot, previousChildName: String?) {
                val order = getOrderValue(orderSnapshot, cafeId)
                val index = orderList.indexOfFirst { it.uuid == order.uuid }
                if (index != -1) {
                    orderList[index] = order
                    //updatedOrderListStateFlow.emit(orderList)
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    @ExperimentalCoroutinesApi
    override fun getAllOrderList(): Flow<List<Order>> = callbackFlow {
        val cafesReference = firebaseDatabase
            .getReference(OrderEntity.ORDERS)
            .child(APP_ID)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(cafesSnapshot: DataSnapshot) {
                launch(IO) {
                    val orderList = cafesSnapshot.children.flatMap { cafeSnapshot ->
                        cafeSnapshot.children.map { orderSnapshot ->
                            getOrderValue(orderSnapshot, cafeSnapshot.key!!)
                        }
                    }
                    offer(orderList)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        cafesReference.addListenerForSingleValueEvent(valueEventListener)

        awaitClose {
            cafesReference.removeEventListener(valueEventListener)
        }
    }

    @ExperimentalCoroutinesApi
    override fun getOrderListByCafeId(cafeId: String): Flow<List<Order>> = callbackFlow {
        val cafeReference = firebaseDatabase
            .getReference(OrderEntity.ORDERS)
            .child(APP_ID)
            .child(cafeId)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(cafeSnapshot: DataSnapshot) {
                launch(IO) {
                    val orderList = cafeSnapshot.children.map { orderSnapshot ->
                        getOrderValue(orderSnapshot, cafeId)
                    }
                    offer(orderList)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        cafeReference.addListenerForSingleValueEvent(valueEventListener)

        awaitClose {
            cafeReference.removeEventListener(valueEventListener)
        }
    }

    override fun getMenuProductList(): Flow<List<MenuProduct>> {
        val menuProductListSharedFlow = MutableSharedFlow<List<MenuProduct>>()
        val menuProductsRef = firebaseDatabase
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

    fun getOrderValue(orderSnapshot: DataSnapshot, cafeId: String): Order {
        return orderSnapshot.getValue(Order::class.java).apply {
            this?.uuid = orderSnapshot.key.toString()
            this?.cafeId = cafeId
        } ?: Order()
    }

    companion object {
        private const val COMPANY = "COMPANY"
        private const val MENU_PRODUCTS = "menu_products"
        private const val NOTIFICATION_TOPIC = "notification"
    }

}