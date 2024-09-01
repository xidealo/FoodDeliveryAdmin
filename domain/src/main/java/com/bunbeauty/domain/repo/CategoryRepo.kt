package com.bunbeauty.domain.repo

import com.bunbeauty.domain.feature.menu.common.model.Category

interface CategoryRepo {
    suspend fun fetchCategories(token: String, companyUuid: String): List<Category>
    suspend fun getCategoryList(token: String, companyUuid: String): List<Category>
}
