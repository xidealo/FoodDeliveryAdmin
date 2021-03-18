package com.bunbeauty.domain.string_helper

import com.bunbeauty.data.model.Address
import com.bunbeauty.data.model.CartProduct
import com.bunbeauty.data.model.Statistic
import com.bunbeauty.data.model.Time
import com.bunbeauty.data.model.order.Order
import com.bunbeauty.data.model.order.OrderEntity
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

        if (orderEntity.deferred.isNotEmpty())
            orderString.append("Время доставки: ${orderEntity.deferred}\n")

        if (orderEntity.comment.isNotEmpty())
            orderString.append("Комментарий: ${orderEntity.comment}\n")

        if (orderEntity.email.isNotEmpty())
            orderString.append("Email: ${orderEntity.email}\n")

        orderString.append("Телефон: ${orderEntity.phone}")

        return orderString.toString()
    }

    override fun toString(statistic: Statistic): String {
        val statisticStringBuilder = StringBuilder()

        statisticStringBuilder.append("Выручка: ${
            statistic.orderList.sumBy
            { order -> order.cartProducts.sumBy { it.count * it.menuProduct.cost } }
        } ₽")
        statisticStringBuilder.append("\n")
        statisticStringBuilder.append("Количество заказов: ${statistic.orderList.size}x")
        statisticStringBuilder.append("\n")
        val menuProductList =
            statistic.orderList.flatMap { it.cartProducts }.groupBy { it.menuProduct.name }.toList()
                .sortedByDescending { pair -> pair.second.sumBy { it.count } }
        for ((index, menuProduct) in menuProductList.withIndex()) {
            statisticStringBuilder.append("${index + 1}) ${menuProduct.first}: ${menuProduct.second.sumBy { it.count }} шт.")
            statisticStringBuilder.append("\n")
        }
        return statisticStringBuilder.toString()
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