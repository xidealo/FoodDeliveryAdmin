package com.bunbeauty.data.repository

import android.util.Log
import com.bunbeauty.common.Constants.CAFES
import com.bunbeauty.common.Constants.COMPANY
import com.bunbeauty.common.Constants.DELIVERY
import com.bunbeauty.common.Constants.MENU_PRODUCTS
import com.bunbeauty.common.Constants.NOTIFICATION_TOPIC
import com.bunbeauty.common.Constants.ORDERS
import com.bunbeauty.common.Constants.ORDER_ENTITY
import com.bunbeauty.common.Constants.ORDER_STATUS
import com.bunbeauty.common.Constants.PASSWORD
import com.bunbeauty.common.Constants.TIMESTAMP
import com.bunbeauty.domain.BuildConfig.APP_ID
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.Company
import com.bunbeauty.domain.model.Delivery
import com.bunbeauty.domain.model.MenuProduct
import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.domain.model.firebase.MenuProductFirebase
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.repo.ApiRepo
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.*
import org.joda.time.DateTime
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ApiRepository @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : ApiRepo, CoroutineScope {

    override val coroutineContext: CoroutineContext = Job() + IO

    private val orderList = LinkedList<Order>()

    @ExperimentalCoroutinesApi
    override fun getAddedOrderListByCafeId(cafeId: String): Flow<List<Order>> = callbackFlow {
        orderList.clear()
        trySend(emptyList())

        val ordersReference = firebaseDatabase
            .getReference(ORDERS)
            .child(APP_ID)
            .child(cafeId)
            .orderByChild(TIMESTAMP)
            .startAt(DateTime.now().minusDays(2).millis.toDouble())
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                orderList.addFirst(getOrderValue(snapshot, cafeId))
                trySend(orderList)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        }
        ordersReference.addChildEventListener(childEventListener)
        Log.d("test1", "add addedListener")

        awaitClose {
            Log.d("test1", "remove addedListener")
            ordersReference.removeEventListener(childEventListener)
        }
    }

    @ExperimentalCoroutinesApi
    override fun getUpdatedOrderListByCafeId(cafeId: String): Flow<List<Order>> = callbackFlow {
        orderList.clear()
        trySend(emptyList())

        val ordersReference = firebaseDatabase
            .getReference(ORDERS)
            .child(APP_ID)
            .child(cafeId)
            .orderByChild(TIMESTAMP)
            .startAt(DateTime.now().minusDays(2).millis.toDouble())
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val order = getOrderValue(snapshot, cafeId)
                val index = orderList.indexOfFirst { it.uuid == order.uuid }
                if (index != -1) {
                    orderList[index] = order
                    trySend(orderList)
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        }
        ordersReference.addChildEventListener(childEventListener)
        Log.d("test1", "add changedListener")

        awaitClose {
            Log.d("test1", "remove changedListener")
            ordersReference.removeEventListener(childEventListener)
        }
    }

    @ExperimentalCoroutinesApi
    override fun getCafeList(): Flow<List<Cafe>> = callbackFlow {
        val cafeReference = firebaseDatabase
            .getReference(COMPANY)
            .child(APP_ID)
            .child(CAFES)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val cafeList = snapshot.children.map { cafeSnapshot ->
                    cafeSnapshot.getValue(Cafe::class.java)!!
                }
                trySend(cafeList)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        cafeReference.addListenerForSingleValueEvent(valueEventListener)

        awaitClose {
            cafeReference.removeEventListener(valueEventListener)
        }
    }

    @ExperimentalCoroutinesApi
    override fun login(login: String, passwordHash: String): Flow<Boolean> = callbackFlow {
        val companyReference = firebaseDatabase.getReference(COMPANY).child(login)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(companySnapshot: DataSnapshot) {
                launch(IO) {
                    if (companySnapshot.childrenCount == 0L) {
                        trySend(false)
                        return@launch
                    }

                    val companyPassword = companySnapshot.child(PASSWORD).value as String
                    if (passwordHash == companyPassword) {
                        trySend(true)
                    } else {
                        trySend(false)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                launch(IO) {
                    trySend(false)
                }
            }
        }
        companyReference.addListenerForSingleValueEvent(valueEventListener)

        awaitClose {
            companyReference.removeEventListener(valueEventListener)
        }
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

    override fun saveMenuProduct(menuProduct: MenuProductFirebase) {
        //TODO("Not yet implemented")
    }

    override fun updateOrderStatus(cafeId: String, orderUuid: String, status: OrderStatus) {
        val orderRef = firebaseDatabase
            .getReference(ORDERS)
            .child(APP_ID)
            .child(cafeId)
            .child(orderUuid)
            .child(ORDER_ENTITY)
            .child(ORDER_STATUS)
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

    @ExperimentalCoroutinesApi
    override fun getAllOrderList(): Flow<List<Order>> = callbackFlow {
        val cafesReference = firebaseDatabase
            .getReference(ORDERS)
            .child(APP_ID)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(cafesSnapshot: DataSnapshot) {
                launch(IO) {
                    val orderList = cafesSnapshot.children.flatMap { cafeSnapshot ->
                        cafeSnapshot.children.map { orderSnapshot ->
                            getOrderValue(orderSnapshot, cafeSnapshot.key!!)
                        }
                    }
                    trySend(orderList)
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
            .getReference(ORDERS)
            .child(APP_ID)
            .child(cafeId)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(cafeSnapshot: DataSnapshot) {
                launch(IO) {
                    val orderList = cafeSnapshot.children.map { orderSnapshot ->
                        getOrderValue(orderSnapshot, cafeId)
                    }
                    trySend(orderList)
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

    @ExperimentalCoroutinesApi
    override fun getMenuProductList(): Flow<List<MenuProduct>> = callbackFlow {
        val menuProductReference = firebaseDatabase
            .getReference(COMPANY)
            .child(APP_ID)
            .child(MENU_PRODUCTS)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val menuProductList = snapshot.children.map { menuProductSnapshot ->
                    menuProductSnapshot.getValue(MenuProduct::class.java)!!
                        .also { it.uuid = menuProductSnapshot.key!! }

                }
                trySend(menuProductList)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        menuProductReference.addListenerForSingleValueEvent(valueEventListener)

        awaitClose {
            menuProductReference.removeEventListener(valueEventListener)
        }
    }

    @ExperimentalCoroutinesApi
    override fun getDelivery(): Flow<Delivery> = callbackFlow {
        val deliveryReference = firebaseDatabase
            .getReference(COMPANY)
            .child(APP_ID)
            .child(DELIVERY)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val delivery = snapshot.getValue(Delivery::class.java)!!
                trySend(delivery)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        deliveryReference.addValueEventListener(valueEventListener)

        awaitClose {
            deliveryReference.removeEventListener(valueEventListener)
        }
    }

    fun getOrderValue(orderSnapshot: DataSnapshot, cafeId: String): Order {
        return orderSnapshot.getValue(Order::class.java).apply {
            this?.uuid = orderSnapshot.key.toString()
            this?.cafeId = cafeId
        } ?: Order()
    }
}