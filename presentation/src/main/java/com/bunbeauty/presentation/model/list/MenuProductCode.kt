package com.bunbeauty.presentation.model.list

import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuProductCode(
    override val title: String
) : ListItemModel()
