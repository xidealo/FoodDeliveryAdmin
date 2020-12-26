package com.bunbeauty.fooddeliveryadmin.data.local.db.order

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.bunbeauty.fooddeliveryadmin.data.local.db.BaseDao
import com.bunbeauty.fooddeliveryadmin.data.model.order.Order
import com.bunbeauty.fooddeliveryadmin.data.model.order.OrderWithCartProducts

@Dao
interface OrderDao : BaseDao<Order> {

    @Query("SELECT * FROM `Order`")
    fun getOrders(): LiveData<List<OrderWithCartProducts>>
}