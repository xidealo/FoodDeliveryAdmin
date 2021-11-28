package com.bunbeauty.data.mapper.user_address

import com.bunbeauty.domain.model.order.UserAddress
import com.bunbeauty.data.model.server.order.ServerUserAddress
import com.bunbeauty.data.model.server.order.ServerUserStreet
import javax.inject.Inject

class UserAddressMapper @Inject constructor() : IUserAddressMapper {

    override fun from(model: ServerUserAddress?): UserAddress? {
        return if (model == null) {
            null
        } else {
            UserAddress(
                street = model.street.name,
                house = model.house,
                flat = model.flat,
                entrance = model.entrance,
                comment = model.comment,
                floor = model.floor,
            )
        }
    }

    override fun to(model: UserAddress?): ServerUserAddress? {
        return if (model == null) {
            null
        } else {
            ServerUserAddress(
                street = ServerUserStreet(name = model.street),
                house = model.house,
                flat = model.flat,
                entrance = model.entrance,
                comment = model.comment,
                floor = model.floor,
            )
        }
    }
}