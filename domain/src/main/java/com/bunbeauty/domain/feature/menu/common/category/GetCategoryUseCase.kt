package com.bunbeauty.domain.feature.menu.common.category

import com.bunbeauty.domain.exception.NoCompanyUuidException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.exception.updateaddition.NotFoundAdditionException
import com.bunbeauty.domain.feature.menu.common.model.Category
import com.bunbeauty.domain.repo.CategoryRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull

class GetCategoryUseCase(
    private val categoryRepo: CategoryRepo,
    private val dataStoreRepo: DataStoreRepo
) {
    suspend operator fun invoke(categoryUuid: String): Category {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        val companyUuid = dataStoreRepo.companyUuid.firstOrNull() ?: throw NoCompanyUuidException()
        return categoryRepo.getCategory(
            companyUuid = companyUuid,
            categoryUuid = categoryUuid,
            token = token
        ) ?: throw NotFoundAdditionException()
    }
}
