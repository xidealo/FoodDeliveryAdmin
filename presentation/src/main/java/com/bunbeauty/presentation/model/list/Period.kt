package com.bunbeauty.presentation.model.list

import kotlinx.parcelize.Parcelize

@Parcelize
data class Period(
    override val title: String
) : ListItemModel()
