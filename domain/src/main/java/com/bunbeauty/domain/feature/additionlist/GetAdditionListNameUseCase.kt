package com.bunbeauty.domain.feature.additionlist

import com.bunbeauty.common.Constants
import com.bunbeauty.domain.model.addition.Addition

//todo tests
class GetAdditionListNameUseCase {

    operator fun invoke(additionList: List<Addition>): String? {
        return additionList.takeIf { list ->
            list.isNotEmpty()
        }?.joinToString(separator = " ${Constants.BULLET_SYMBOL} ") { selectableCategory ->
            selectableCategory.name
        }
    }
}