package com.bunbeauty.data.mapper.additiongroup

import com.bunbeauty.data.model.server.additiongroup.AdditionGroupServer
import com.bunbeauty.domain.model.additiongroup.AdditionGroup

val mapAdditionGroupServerToAdditionGroup: AdditionGroupServer.() -> AdditionGroup = {
    AdditionGroup(
        isVisible = isVisible,
        name = name,
        uuid = uuid,
        priority = priority,
        singleChoice = singleChoice
    )
}
