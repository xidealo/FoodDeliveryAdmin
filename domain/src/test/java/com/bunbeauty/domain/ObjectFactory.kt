package com.bunbeauty.domain

import com.bunbeauty.domain.feature.menu.common.model.CreateCategory

fun getCreateCategoryProduct(
    name: String = "",
    priority: Int = 0,
) = CreateCategory(
    name = name,
    priority = priority
)
