package com.bunbeauty.domain.model.additiongroup

import com.bunbeauty.common.Constants
import com.bunbeauty.domain.model.addition.Addition

data class AdditionGroupWithAdditions(
    val additionGroup: AdditionGroup,
    val additionList: List<Addition>
) {
    val nameListString = additionList.takeIf { list ->
        list.isNotEmpty()
    }?.joinToString(separator = " ${Constants.BULLET_SYMBOL} ") { selectableCategory ->
        selectableCategory.name
    }
}
