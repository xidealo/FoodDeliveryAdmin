package com.bunbeauty.presentation.list

import kotlinx.parcelize.Parcelize

@Parcelize
data class Period(
    override val title: String
) : ListItemModel()
