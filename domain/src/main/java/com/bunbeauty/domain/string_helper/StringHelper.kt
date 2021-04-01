package com.bunbeauty.domain.string_helper

import com.bunbeauty.data.model.*
import com.bunbeauty.data.model.order.Order
import com.bunbeauty.data.model.order.OrderEntity
import com.bunbeauty.domain.cost_helper.ICostHelper
import java.lang.StringBuilder
import javax.inject.Inject

class StringHelper @Inject constructor(private val iCostHelper: ICostHelper) : IStringHelper {
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
            structure += "${getPositionName(cartProduct.menuProduct)}: ${cartProduct.count}\n"

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
        //TODO (change calculating)
        statisticStringBuilder.append("Выручка: ${
            statistic.orderList.sumBy { order ->
                order.cartProducts.sumBy {
                    iCostHelper.getCost(it)
                }
            }
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

    override fun toStringCost(statistic: Statistic): String {
        return "${
            statistic.orderList.sumBy { order ->
                order.cartProducts.sumBy {
                    iCostHelper.getCost(it)
                }
            }
        } ₽"
    }

    override fun toStringCost(order: Order): String {
        var fullPrice = 0
        for (cartProduct in order.cartProducts){
            fullPrice += if(cartProduct.menuProduct.discountCost != null){
                cartProduct.count * cartProduct.menuProduct.discountCost!!
            }else{
                cartProduct.count * cartProduct.menuProduct.cost
            }
        }

        return "Стоимость заказа: $fullPrice ₽"
    }

    override fun toStringOrdersCount(statistic: Statistic): String {
        return "${statistic.orderList.size}x"
    }

    override fun toStringTime(orderEntity: OrderEntity): String {
        return Time(orderEntity.time, 3).toStringTimeHHMM()
    }

    override fun toStringTime(order: Order): String {
        return Time(order.timestamp, 3).toStringTimeHHMM()
    }

    fun getPositionName(menuProduct: MenuProduct): String {
        return if (menuProduct.comboDescription.isEmpty()) {
            menuProduct.name
        } else {
            menuProduct.comboDescription
        }
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