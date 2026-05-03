package com.bunbeauty.domain.feature.additionlist

import com.bunbeauty.domain.model.addition.Addition
import common.Constants

class GetAdditionListNameUseCase {
    operator fun invoke(additionList: List<Addition>): String? =
        additionList
            .takeIf { list ->
                list.isNotEmpty()
            }?.joinToString(separator = " ${Constants.BULLET_SYMBOL} ") { selectableCategory ->
                selectableCategory.name
            }
}
