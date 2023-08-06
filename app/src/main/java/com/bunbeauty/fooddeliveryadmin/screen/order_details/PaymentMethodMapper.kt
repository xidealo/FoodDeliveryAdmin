package com.bunbeauty.fooddeliveryadmin.screen.order_details

import android.content.res.Resources
import com.bunbeauty.domain.model.order.details.PaymentMethod
import com.bunbeauty.fooddeliveryadmin.R
import javax.inject.Inject

class PaymentMethodMapper @Inject constructor(private val resources: Resources) {

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