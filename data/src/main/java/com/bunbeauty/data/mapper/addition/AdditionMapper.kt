package com.bunbeauty.data.mapper.addition

import com.bunbeauty.data.model.server.addition.AdditionPatchServer
import com.bunbeauty.data.model.server.addition.AdditionServer
import com.bunbeauty.domain.model.addition.Addition
import com.bunbeauty.domain.model.addition.UpdateAddition

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


val mapUpdateAdditionServerToPatchAddition: UpdateAddition.() -> AdditionPatchServer= {
    AdditionPatchServer(
    isVisible = isVisible,
    name = name,
    photoLink = photoLink,
    price = price,
    fullName = fullName,
    priority = priority
    )
}

