package com.bunbeauty.fooddeliveryadmin.data.api.firebase

import com.bunbeauty.fooddeliveryadmin.data.local.storage.IDataStoreHelper
import com.bunbeauty.fooddeliveryadmin.data.model.Company
import com.bunbeauty.fooddeliveryadmin.data.model.order.Order
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
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

    override fun login(login: String, password: String) {
        val query = firebaseInstance.getReference(Company.COMPANY)
            .orderByChild(Company.LOGIN).equalTo(login)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(companysSnapshot: DataSnapshot) {
                launch {
                    val passwordHash = md5(password)

                    if (companysSnapshot.childrenCount == 0L) {
                        return@launch
                    }

                    val companySnapshot = companysSnapshot.children.iterator().next()
                    val companyPassword = companySnapshot.child(Company.PASSWORD).value as String

                    if (passwordHash == companyPassword) {
                        iDataStoreHelper.saveToken(companySnapshot.child(Company.TOKEN).value as String)
                    } else {
                        //set false
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun updateOrder(order: Order) {

    }

    fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

}