package com.bunbeauty.fooddeliveryadmin.data.local.db.order

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.bunbeauty.fooddeliveryadmin.data.local.db.BaseDao
import com.bunbeauty.fooddeliveryadmin.data.model.order.OrderEntity
import com.bunbeauty.fooddeliveryadmin.data.model.order.Order

@Dao
interface OrderDao : BaseDao<OrderEntity> {

    /*@Query("SELECT * FROM OrderEntity")
    fun getOrders(): LiveData<List<Order>>*/
}