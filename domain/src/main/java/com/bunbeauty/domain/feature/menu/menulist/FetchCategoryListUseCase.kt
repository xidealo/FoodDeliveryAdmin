package com.bunbeauty.domain.feature.menu.menulist

import com.bunbeauty.domain.exception.NoCompanyUuidException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.repo.CategoryRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

//todo tests
class FetchCategoryListUseCase @Inject constructor(
    private val categoryRepo: CategoryRepo,
    private val dataStoreRepo: DataStoreRepo
) {
    suspend operator fun invoke() {
        categoryRepo.fetchCategories(
            token = dataStoreRepo.getToken() ?: throw NoTokenException(),
            companyUuid = dataStoreRepo.companyUuid.firstOrNull() ?: throw NoCompanyUuidException()
        )
    }
}