package com.bunbeauty.fooddeliveryadmin.screen.orderdetails

import android.content.res.Resources
import com.bunbeauty.domain.model.order.details.PaymentMethod
import com.bunbeauty.fooddeliveryadmin.R

class PaymentMethodMapper(private val resources: Resources) {

    fun map(paymentMethod: PaymentMethod): String {
        return when (paymentMethod) {
            PaymentMethod.CASH -> R.string.msg_payment_cash
            PaymentMethod.CARD -> R.string.msg_payment_card
            PaymentMethod.CARD_NUMBER -> R.string.msg_payment_card_number
            PaymentMethod.PHONE_NUMBER -> R.string.msg_payment_phone_number
        }.let { nameResId ->
            resources.getString(nameResId)
        }
    }
}
