package com.bunbeauty.domain.model.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserAddress(
   val street: String,
   val house: String,
   val flat: String?,
   val entrance: String?,
   val intercom: String?,
   val floor: String?
): Parcelable
