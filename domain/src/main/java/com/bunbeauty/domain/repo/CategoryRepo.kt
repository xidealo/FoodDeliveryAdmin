package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.category.Category

interface CategoryRepo {
    suspend fun fetchCategories(token: String, companyUuid: String): List<Category>
    suspend fun getCategoryList(token: String, companyUuid: String): List<Category>
}
