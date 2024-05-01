package com.bunbeauty.domain.feature.menu.addmenuproduct

import com.bunbeauty.domain.model.Category
import com.bunbeauty.domain.repo.CategoryRepo
import javax.inject.Inject

//todo Tests
class GetCategoryListUseCase @Inject constructor(
    private val categoryRepo: CategoryRepo
) {
    suspend operator fun invoke(): List<Category> {
        return categoryRepo.categoryList()
    }
}