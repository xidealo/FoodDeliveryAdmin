package com.bunbeauty.presentation.feature.menulist.common

import com.bunbeauty.domain.model.additiongroup.AdditionGroup

data class AdditionGroupListFieldData(
    override val value: List<AdditionGroup>,
    override val isError: Boolean
) : FieldData<List<AdditionGroup>>()
