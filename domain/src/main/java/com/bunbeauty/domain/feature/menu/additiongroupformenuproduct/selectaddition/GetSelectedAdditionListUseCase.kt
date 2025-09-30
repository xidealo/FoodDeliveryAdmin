package com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.selectaddition

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.model.addition.Addition
import com.bunbeauty.domain.repo.AdditionRepo
import com.bunbeauty.domain.repo.DataStoreRepo

data class SelectedAdditionForMenu(
    val selectedAdditionList: List<Addition>,
    val notSelectedAdditionList: List<Addition>
)

class GetSelectedAdditionListUseCase(
    private val additionRepo: AdditionRepo,
    private val dataStoreRepo: DataStoreRepo
) {
    suspend operator fun invoke(
        selectedGroupAdditionUuid: String?,
        menuProductUuid: String
    ): SelectedAdditionForMenu {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()

        return SelectedAdditionForMenu(
            selectedAdditionList = emptyList(),
            notSelectedAdditionList = additionRepo.getAdditionList(
                token = token
            )
        )
    }
}
