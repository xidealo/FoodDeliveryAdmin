package com.bunbeauty.domain.feature.menu.common.category

import com.bunbeauty.domain.exception.NoCompanyUuidException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.menu.common.model.Category
import com.bunbeauty.domain.repo.CategoryRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class GetCategoryListUseCase @Inject constructor(
    private val categoryRepo: CategoryRepo,
    private val dataStoreRepo: DataStoreRepo
) {
    suspend operator fun invoke(): List<Category> {
        return categoryRepo.getCategoryList(
            token = dataStoreRepo.getToken() ?: throw NoTokenException(),
            companyUuid = dataStoreRepo.companyUuid.firstOrNull() ?: throw NoCompanyUuidException()
        ).filter { category -> !isHits(category = category) }
    }

    private fun isHits(category: Category) = category.uuid == ""
}