package com.bunbeauty.data.dao

import androidx.room.Dao
import com.bunbeauty.domain.model.order.OrderEntity

@Dao
interface OrderDao : BaseDao<OrderEntity> {

}