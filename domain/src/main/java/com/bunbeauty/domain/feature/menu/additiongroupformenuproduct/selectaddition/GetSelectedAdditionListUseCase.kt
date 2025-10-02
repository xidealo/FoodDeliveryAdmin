package com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.selectaddition

import android.util.Log
import com.bunbeauty.domain.exception.NoCompanyUuidException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.model.addition.Addition
import com.bunbeauty.domain.repo.AdditionRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import com.bunbeauty.domain.repo.MenuProductToAdditionGroupToAdditionRepository
import kotlinx.coroutines.flow.firstOrNull

data class SelectedAdditionForMenu(
    val selectedAdditionList: List<Addition>,
    val notSelectedAdditionList: List<Addition>
)

class GetSelectedAdditionListUseCase(
    private val additionRepo: AdditionRepo,
    private val dataStoreRepo: DataStoreRepo,
    private val menuProductToAdditionGroupToAdditionRepository: MenuProductToAdditionGroupToAdditionRepository,
    private val menuProductRepo: MenuProductRepo
) {
    suspend operator fun invoke(
        selectedGroupAdditionUuid: String?,
        menuProductUuid: String
    ): SelectedAdditionForMenu {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        val menuProduct = menuProductRepo.getMenuProduct(
            menuProductUuid = menuProductUuid,
            companyUuid = dataStoreRepo.companyUuid.firstOrNull() ?: throw NoCompanyUuidException()
        )
        val uuidList = menuProduct?.additionGroups
            ?.find { it.additionGroup.uuid == selectedGroupAdditionUuid }
            ?.additionList
            ?.map {
                it.uuid
            } ?: emptyList()
        val result =
            menuProductToAdditionGroupToAdditionRepository.getMenuProductToAdditionGroupToAdditionList(
                uuidList = uuidList
            )
        Log.d("sasdas", "invoke: $result")
        return SelectedAdditionForMenu(
            selectedAdditionList = emptyList(),
            notSelectedAdditionList = additionRepo.getAdditionList(
                token = token,
            )
        )
    }
}
