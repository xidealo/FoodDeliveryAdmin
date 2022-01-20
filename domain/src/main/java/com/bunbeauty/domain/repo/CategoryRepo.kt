package com.bunbeauty.domain.repo

interface CategoryRepo {
    suspend fun refreshCategories(token: String, companyUuid: String)
}