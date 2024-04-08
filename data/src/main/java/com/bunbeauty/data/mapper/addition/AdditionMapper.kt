package com.bunbeauty.data.mapper.addition

import com.bunbeauty.data.model.server.addition.AdditionServer
import com.bunbeauty.domain.model.addition.Addition

val mapAdditionServerToAddition: AdditionServer.() -> Addition = {
    Addition(
        isVisible = isVisible,
        name = name,
        photoLink = photoLink,
        price = price,
        uuid = uuid,
        fullName = fullName,
        priority = priority
    )
}