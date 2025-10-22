package com.bunbeauty.domain.model.additiongroup

import com.bunbeauty.domain.model.addition.Addition

data class AdditionGroupWithAdditions(
    val additionGroup: AdditionGroup,
    val additionList: List<Addition>
)
