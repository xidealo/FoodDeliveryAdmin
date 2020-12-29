package com.bunbeauty.fooddeliveryadmin.data.api.firebase

import com.bunbeauty.fooddeliveryadmin.data.model.Company
import com.bunbeauty.fooddeliveryadmin.data.model.order.Order
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.math.BigInteger
import java.security.MessageDigest
import javax.inject.Inject

class ApiRepository @Inject constructor() : IApiRepository {
    //добавить лайв дату в которую класить, чтобы тригерило в фрагменте
    
    private val firebaseInstance = FirebaseDatabase.getInstance()

    override fun login(login: String, password: String) {
        val query = FirebaseDatabase.getInstance().getReference(Company.COMPANY)
            .orderByChild(Company.LOGIN).equalTo(login)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(companysSnapshot: DataSnapshot) {
                val passwordHash = md5(password)

                if (companysSnapshot.childrenCount == 0L) {
                    //set false
                    return
                }

                val companySnapshot = companysSnapshot.children.iterator().next()
                val companyPassword = companySnapshot.child(Company.PASSWORD).value as String

                if(passwordHash == companyPassword){
                    //set true
                }else{
                    //set false
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun updateOrder(order: Order) {

    }

    fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

}