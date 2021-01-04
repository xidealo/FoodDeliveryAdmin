package com.bunbeauty.fooddeliveryadmin.data.api.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bunbeauty.fooddeliveryadmin.data.local.storage.IDataStoreHelper
import com.bunbeauty.fooddeliveryadmin.data.model.CartProduct
import com.bunbeauty.fooddeliveryadmin.data.model.Company
import com.bunbeauty.fooddeliveryadmin.data.model.MenuProduct
import com.bunbeauty.fooddeliveryadmin.data.model.order.Order
import com.bunbeauty.fooddeliveryadmin.data.model.order.OrderWithCartProducts
import com.google.firebase.database.*
import kotlinx.coroutines.*
import java.math.BigInteger
import java.security.MessageDigest
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ApiRepository @Inject constructor(
    private val iDataStoreHelper: IDataStoreHelper
) : IApiRepository, CoroutineScope {

    private val firebaseInstance = FirebaseDatabase.getInstance()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.IO

    override fun login(login: String, password: String): LiveData<Boolean> {
        val query = firebaseInstance.getReference(Company.COMPANY)
            .child(login)
        val isAuthorized = MutableLiveData<Boolean>()

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(companySnapshot: DataSnapshot) {
                launch {
                    val passwordHash = md5(password)

                    if (companySnapshot.childrenCount == 0L) {
                        withContext(Dispatchers.Main) {
                            isAuthorized.value = false
                        }
                        return@launch
                    }

                    val companyPassword = companySnapshot.child(Company.PASSWORD).value as String

                    if (passwordHash == companyPassword) {
                        iDataStoreHelper.saveToken(companySnapshot.child(Company.TOKEN).value as String)
                        withContext(Dispatchers.Main) {
                            isAuthorized.value = true
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            isAuthorized.value = false
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        return isAuthorized
    }

    override fun updateOrder(order: Order) {

    }

    override fun getOrderWithCartProducts(login: String): LiveData<List<OrderWithCartProducts>> {
        val ordersRef = firebaseInstance.getReference(Order.ORDERS)
            .child(login)
        val ordersWithCartProductsLiveData = MutableLiveData<List<OrderWithCartProducts>>()
        ordersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(ordersSnapshot: DataSnapshot) {
                launch {
                    val ordersWithCartProductsList = arrayListOf<OrderWithCartProducts>()
                    for (orderSnapshot in ordersSnapshot.children) {
                        ordersWithCartProductsList.add(
                            getOrderWithCartProductsFromSnapshot(
                                orderSnapshot
                            )
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

    fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    fun getOrderWithCartProductsFromSnapshot(orderSnapshot: DataSnapshot): OrderWithCartProducts {
        val orderWithCartProducts = OrderWithCartProducts()
        orderWithCartProducts.order.street = orderSnapshot.child(Order.STREET).value as String

        val cartProducts = mutableListOf<CartProduct>()
        for (cartProductSnapshot in orderSnapshot.child(CartProduct.CART_PRODUCTS).children) {
            cartProducts.add(
                CartProduct(
                    count = (cartProductSnapshot.child(CartProduct.COUNT).value as Long).toInt(),
                    menuProduct = MenuProduct(
                        name = cartProductSnapshot.child(MenuProduct.NAME).value as String
                    )
                )
            )
        }

        orderWithCartProducts.cartProducts = cartProducts
        return orderWithCartProducts
    }

}