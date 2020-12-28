package com.bunbeauty.fooddeliveryadmin.data.api.firebase

import com.bunbeauty.fooddeliveryadmin.data.model.order.Order
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class ApiRepository @Inject constructor() : IApiRepository {
    //добавить лайв дату в которую класить, чтобы тригерило в фрагменте
    
    private val firebaseInstance = FirebaseDatabase.getInstance()

    override fun login(login: String, password: String) {

    }

    override fun updateOrder(order: Order) {

    }

}