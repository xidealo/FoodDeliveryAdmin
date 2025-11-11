package com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.selectaddition

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.editadditiongroupformenuproduct.GetFilteredAdditionGroupWithAdditionsForMenuProductUseCase
import com.bunbeauty.domain.model.addition.Addition
import com.bunbeauty.domain.model.menuProcutToAdditionGroupToAddition.MenuProductToAdditionGroupToAddition
import com.bunbeauty.domain.repo.AdditionRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductToAdditionGroupToAdditionRepository

data class SelectedAdditionForMenu(
    val selectedAdditionList: List<Addition>,
    val notSelectedAdditionList: List<Addition>,
)

// todo Tests
class GetSelectedAdditionListUseCase(
    private val additionRepo: AdditionRepo,
    private val dataStoreRepo: DataStoreRepo,
    private val menuProductToAdditionGroupToAdditionRepository: MenuProductToAdditionGroupToAdditionRepository,
    private val getFilteredAdditionGroupWithAdditionsForMenuProductUseCase: GetFilteredAdditionGroupWithAdditionsForMenuProductUseCase,
) {
    suspend operator fun invoke(
        selectedGroupAdditionUuid: String?,
        menuProductUuid: String,
    ): SelectedAdditionForMenu {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()

        val uuidList =
            getMenuProductAdditionUuidList(
                menuProductUuid = menuProductUuid,
                selectedGroupAdditionUuid = selectedGroupAdditionUuid,
            )

        val menuProductToAdditionGroupToAdditionList =
            menuProductToAdditionGroupToAdditionRepository.getMenuProductToAdditionGroupToAdditionList(
                uuidList = uuidList,
            )

        val commonAdditionList =
            additionRepo.getAdditionList(
                token = token,
            )

        return SelectedAdditionForMenu(
            selectedAdditionList =
                getAdditionContainedInMenuProduct(
                    commonAdditionList = commonAdditionList,
                    additionListFromMenuProduct = menuProductToAdditionGroupToAdditionList,
                ),
            notSelectedAdditionList =
                getAdditionNotContainedInMenuProduct(
                    commonAdditionList = commonAdditionList,
                    additionListFromMenuProduct = menuProductToAdditionGroupToAdditionList,
                ),
        )
    }

    private suspend fun getMenuProductAdditionUuidList(
        menuProductUuid: String,
        selectedGroupAdditionUuid: String?,
    ): List<String> {
        if (selectedGroupAdditionUuid == null) return emptyList()

        return getFilteredAdditionGroupWithAdditionsForMenuProductUseCase(
            menuProductUuid = menuProductUuid,
            additionGroupForMenuUuid = selectedGroupAdditionUuid,
        ).additionList.map { addition ->
            addition.uuid
        }
    }

    private fun getAdditionContainedInMenuProduct(
        commonAdditionList: List<Addition>,
        additionListFromMenuProduct: List<MenuProductToAdditionGroupToAddition>,
    ): List<Addition> =
        commonAdditionList.filter { addition ->
            addition.uuid in
                additionListFromMenuProduct.map { menuProductToAdditionGroupToAddition ->
                    menuProductToAdditionGroupToAddition.additionUuid
                }
        }

    private fun getAdditionNotContainedInMenuProduct(
        commonAdditionList: List<Addition>,
        additionListFromMenuProduct: List<MenuProductToAdditionGroupToAddition>,
    ): List<Addition> =
        commonAdditionList.filterNot { addition ->
            addition.uuid in
                additionListFromMenuProduct.map { menuProductToAdditionGroupToAddition ->
                    menuProductToAdditionGroupToAddition.additionUuid
                }
        }
}
