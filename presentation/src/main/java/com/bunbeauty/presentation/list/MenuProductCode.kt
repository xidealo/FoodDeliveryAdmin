package com.bunbeauty.presentation.list

import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuProductCode(
    override val title: String
) : ListItemModel()
