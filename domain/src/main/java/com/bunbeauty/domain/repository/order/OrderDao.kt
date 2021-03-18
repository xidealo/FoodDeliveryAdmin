package com.bunbeauty.domain.repository.order

import androidx.room.Dao
import com.bunbeauty.data.model.order.OrderEntity
import com.bunbeauty.domain.repository.BaseDao

@Dao
interface OrderDao : BaseDao<OrderEntity> {

    /*@Query("SELECT * FROM OrderEntity")
    fun getOrders(): LiveData<List<Order>>*/
}