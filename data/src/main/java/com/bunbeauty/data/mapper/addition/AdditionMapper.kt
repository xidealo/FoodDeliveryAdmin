package com.bunbeauty.data.mapper.addition

import com.bunbeauty.data.model.server.addition.AdditionPatchServer
import com.bunbeauty.data.model.server.addition.AdditionServer
import com.bunbeauty.data.model.server.additiongroup.AdditionGroupPatchServer
import com.bunbeauty.domain.model.addition.Addition
import com.bunbeauty.domain.model.addition.UpdateAddition
import com.bunbeauty.domain.model.additiongroup.UpdateAdditionGroup

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

val mapUpdateAdditionGroupServerToPatchAdditionGroup: UpdateAdditionGroup.() -> AdditionGroupPatchServer = {
    AdditionGroupPatchServer(
        name = name,
        priority = priority,
        singleChoice = singleChoice,
        isVisible = isVisible,
    )
}

