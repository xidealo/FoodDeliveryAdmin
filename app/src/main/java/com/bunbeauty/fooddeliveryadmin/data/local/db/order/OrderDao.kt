package com.bunbeauty.fooddeliveryadmin.data.local.db.order

import androidx.room.Dao
import com.bunbeauty.fooddeliveryadmin.data.local.db.BaseDao
import com.bunbeauty.data.model.order.OrderEntity

@Dao
interface OrderDao : BaseDao<OrderEntity> {

    /*@Query("SELECT * FROM OrderEntity")
    fun getOrders(): LiveData<List<Order>>*/
}