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
import com.bunbeauty.domain.model.Delivery
import com.bunbeauty.domain.model.ServerMenuProduct
import com.bunbeauty.domain.model.cafe.server.ServerCafe
import com.bunbeauty.domain.model.order.server.ServerOrder
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

    private val serverOrderList = LinkedList<ServerOrder>()
    private val menuProductList = LinkedList<ServerMenuProduct>()

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

    @ExperimentalCoroutinesApi
    override fun getCafeList(): Flow<List<ServerCafe>> = callbackFlow {
        val cafeReference = firebaseDatabase
            .getReference(COMPANY)
            .child(APP_ID)
            .child(CAFES)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val serverCafeList = snapshot.children.map { cafeSnapshot ->
                    getCafeValue(cafeSnapshot)
                }
                trySend(serverCafeList)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        cafeReference.addValueEventListener(valueEventListener)

        awaitClose {
            cafeReference.removeEventListener(valueEventListener)
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

    @ExperimentalCoroutinesApi
    override fun getMenuProductList(): Flow<List<ServerMenuProduct>> = callbackFlow {
        val menuProductReference = firebaseDatabase
            .getReference(COMPANY)
            .child(APP_ID)
            .child(MENU_PRODUCTS)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val menuProductList = snapshot.children.map(::getMenuProductValue)
                trySend(menuProductList)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        menuProductReference.addValueEventListener(valueEventListener)

        awaitClose {
            menuProductReference.removeEventListener(valueEventListener)
        }
    }

    override fun updateMenuProduct(menuProduct: ServerMenuProduct, uuid: String) {
        val menuProductRef = firebaseDatabase
            .getReference(COMPANY)
            .child(APP_ID)
            .child(MENU_PRODUCTS)
            .child(uuid)
        menuProductRef.setValue(menuProduct)
    }

    override fun saveMenuProduct(menuProduct: ServerMenuProduct) {
        //TODO("Not yet implemented")
    }

    @ExperimentalCoroutinesApi
    override fun getAddedOrderListByCafeId(cafeUid: String): Flow<List<ServerOrder>> = callbackFlow {
        serverOrderList.clear()
        trySend(emptyList())

        val ordersReference = firebaseDatabase
            .getReference(ORDERS)
            .child(APP_ID)
            .child(cafeUid)
            .orderByChild(TIMESTAMP)
            .startAt(DateTime.now().minusDays(2).millis.toDouble())
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                serverOrderList.add(getOrderValue(snapshot, cafeUid))
                trySend(serverOrderList)
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
    override fun getUpdatedOrderListByCafeId(cafeUuid: String): Flow<List<ServerOrder>> = callbackFlow {
        serverOrderList.clear()
        trySend(emptyList())

        val ordersReference = firebaseDatabase
            .getReference(ORDERS)
            .child(APP_ID)
            .child(cafeUuid)
            .orderByChild(TIMESTAMP)
            .startAt(DateTime.now().minusDays(2).millis.toDouble())
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val serverOrder = getOrderValue(snapshot, cafeUuid)
                val index = serverOrderList.indexOfFirst { it.uuid == serverOrder.uuid }
                if (index != -1) {
                    serverOrderList[index] = serverOrder
                    trySend(serverOrderList)
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
    override fun getAllOrderList(): Flow<List<ServerOrder>> = callbackFlow {
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
    override fun getOrderListByCafeId(cafeId: String): Flow<List<ServerOrder>> = callbackFlow {
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

    fun getOrderValue(orderSnapshot: DataSnapshot, _cafeUuid: String): ServerOrder {
        return orderSnapshot.getValue(ServerOrder::class.java)?.apply {
            uuid = orderSnapshot.key.toString()
            cafeUuid = _cafeUuid
        } ?: ServerOrder()
    }

    fun getMenuProductValue(menuProductSnapshot: DataSnapshot): ServerMenuProduct {
        return menuProductSnapshot.getValue(ServerMenuProduct::class.java)?.apply {
            uuid = menuProductSnapshot.key.toString()
        } ?: ServerMenuProduct()
    }

    fun getCafeValue(cafeSnapshot: DataSnapshot): ServerCafe {
        return cafeSnapshot.getValue(ServerCafe::class.java)?.apply {
            uuid = cafeSnapshot.key.toString()
        } ?: ServerCafe()
    }
}