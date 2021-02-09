package com.bunbeauty.fooddeliveryadmin.utils.string

import com.bunbeauty.fooddeliveryadmin.data.model.Address
import com.bunbeauty.fooddeliveryadmin.data.model.CartProduct
import com.bunbeauty.fooddeliveryadmin.data.model.Statistic
import com.bunbeauty.fooddeliveryadmin.data.model.Time
import com.bunbeauty.fooddeliveryadmin.data.model.order.Order
import com.bunbeauty.fooddeliveryadmin.data.model.order.OrderEntity
import java.lang.StringBuilder
import javax.inject.Inject

class StringHelper @Inject constructor() : IStringHelper {
    //TODO(Get strings from resources)
    override fun toString(address: Address): String {
        return checkLastSymbol(
            "${address.street?.name}, " +
                    "Дом:${address.house}, " +
                    flatString(address) +
                    entranceString(address) +
                    intercomString(address) +
                    floorString(address),
            ','
        )
    }

    override fun toString(cartProducts: List<CartProduct>): String {
        var structure = ""

        for (cartProduct in cartProducts)
            structure += "${cartProduct.menuProduct.name} ${cartProduct.count}шт.; "

        return checkLastSymbol(
            "В заказе: \n${structure}",
            ';'
        )
    }

    override fun toString(orderEntity: OrderEntity): String {
        val orderString = StringBuilder()

        if (orderEntity.isDelivery)
            orderString.append("Доставка\n")
        else
            orderString.append("Самовывоз\n")

        orderString.append("Телефон: ${orderEntity.phone}\n")
        orderString.append("Комментарий:${orderEntity.comment}\n")
        orderString.append("Email:${orderEntity.email}")
        return orderString.toString()
    }

    override fun toString(statistic: Statistic): String {
        val statisticString = StringBuilder()
        //statisticString.append(toString(statistic.orderList))
        statisticString.append("Выручка:${statistic.orderList.sumBy { order -> order.cartProducts.sumBy { it.count * it.menuProduct.cost } }}")
        return statisticString.toString()
    }

    override fun toStringTime(orderEntity: OrderEntity): String {
        return Time(orderEntity.time, 3).toStringTimeHHMM()
    }

    override fun toStringTime(order: Order): String {
        return Time(order.timestamp, 3).toStringTimeHHMM()
    }

    fun checkLastSymbol(data: String, symbol: Char): String {
        if (data.trim().last() == symbol)
            return data.substringBeforeLast(symbol)

        return data
    }

    fun flatString(address: Address): String {
        return if (address.flat.isNotEmpty())
            "Квартира:${address.flat}, "
        else
            ""
    }

    fun entranceString(address: Address): String {
        return if (address.entrance.isNotEmpty())
            "Подъезд:${address.entrance}, "
        else
            ""
    }

    fun intercomString(address: Address): String {
        return if (address.intercom.isNotEmpty())
            "Домофон:${address.intercom}, "
        else
            ""
    }

    fun floorString(address: Address): String {
        return if (address.floor.isNotEmpty())
            "Этаж:${address.floor}"
        else
            ""
    }
}