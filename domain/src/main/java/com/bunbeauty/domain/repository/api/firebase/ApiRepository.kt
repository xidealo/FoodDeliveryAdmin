package com.bunbeauty.domain.repository.api.firebase

import android.util.Log
import com.bunbeauty.common.utils.IDataStoreHelper
import com.bunbeauty.data.model.Cafe
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
    private val firebaseInstance = FirebaseDatabase.getInstance()

    private val orderList = LinkedList<Order>()
    override val addedOrderListStateFlow = MutableStateFlow<List<Order>>(listOf())
    override val updatedOrderListStateFlow = MutableStateFlow<List<Order>>(listOf())

    @ExperimentalCoroutinesApi
    override fun subscribeOnOrderList(cafeId: String) {
        val ordersReference = firebaseInstance
            .getReference(OrderEntity.ORDERS)
            .child(APP_ID)
            .child(cafeId)
            .orderByChild(OrderEntity.TIMESTAMP)
            .startAt(DateTime.now().minusDays(2).millis.toDouble())
        getOrderWithCartProducts(ordersReference, cafeId)
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

    override fun updateOrder(order: Order) {
        val orderRef = firebaseDatabase
            .getReference(OrderEntity.ORDERS)
            .child(APP_ID)
            .child(order.cafeId)
            .child(order.uuid)
            .child(OrderEntity.ORDER_ENTITY)
            .child(OrderEntity.ORDER_STATUS)
        orderRef.setValue(order.orderEntity.orderStatus)
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
                addedOrderListStateFlow.value = orderList
            }

            override fun onChildChanged(orderSnapshot: DataSnapshot, previousChildName: String?) {
                val order = getOrderValue(orderSnapshot, cafeId)
                val index = orderList.indexOfFirst { it.uuid == order.uuid }
                if (index != -1) {
                    orderList[index] = order
                    updatedOrderListStateFlow.value = orderList
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
        val ordersRef = firebaseDatabase
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
                            getOrderValue(orderSnapshot, cafeId)
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
        val ordersRef = firebaseDatabase
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
                                getOrderValue(orderSnapshot, "")
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
        private const val MENU_PRODUCTS: String = "menu_products"
        private const val NOTIFICATION_TOPIC: String = "notification"
    }

}