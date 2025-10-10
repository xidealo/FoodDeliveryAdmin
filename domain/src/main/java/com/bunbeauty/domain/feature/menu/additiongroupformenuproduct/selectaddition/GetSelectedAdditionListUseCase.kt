package com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.selectaddition

import android.util.Log
import com.bunbeauty.domain.exception.NoCompanyUuidException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.model.addition.Addition
import com.bunbeauty.domain.model.menuProcutToAdditionGroupToAddition.MenuProductToAdditionGroupToAddition
import com.bunbeauty.domain.model.menuproduct.MenuProduct
import com.bunbeauty.domain.repo.AdditionRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import com.bunbeauty.domain.repo.MenuProductToAdditionGroupToAdditionRepository
import kotlinx.coroutines.flow.firstOrNull

data class SelectedAdditionForMenu(
    val selectedAdditionList: List<Addition>,
    val notSelectedAdditionList: List<Addition>
)

//todo Tests
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
        val uuidList = getMenuProductAdditionUuidList(
            menuProduct = menuProduct,
            selectedGroupAdditionUuid = selectedGroupAdditionUuid
        )

        val menuProductToAdditionGroupToAdditionList =
            menuProductToAdditionGroupToAdditionRepository.getMenuProductToAdditionGroupToAdditionList(
                uuidList = uuidList
            )

        val commonAdditionList = additionRepo.getAdditionList(
            token = token,
        )
        return SelectedAdditionForMenu(
            selectedAdditionList = getAdditionContainedInMenuProduct(
                commonAdditionList = commonAdditionList,
                additionListFromMenuProduct = menuProductToAdditionGroupToAdditionList
            ),
            notSelectedAdditionList = getAdditionNotContainedInMenuProduct(
                commonAdditionList = commonAdditionList,
                additionListFromMenuProduct = menuProductToAdditionGroupToAdditionList
            )
        )
    }


    private fun getMenuProductAdditionUuidList(
        menuProduct: MenuProduct?,
        selectedGroupAdditionUuid: String?
    ): List<String> {
        if (menuProduct == null || selectedGroupAdditionUuid == null) return emptyList()

        return menuProduct.additionGroups
            .find { additionGroup ->
                additionGroup.additionGroup.uuid == selectedGroupAdditionUuid
            }
            ?.additionList
            ?.map { addition ->
                addition.uuid
            } ?: emptyList()
    }

    private fun getAdditionContainedInMenuProduct(
        commonAdditionList: List<Addition>,
        additionListFromMenuProduct: List<MenuProductToAdditionGroupToAddition>
    ): List<Addition> {
        return commonAdditionList.filter { addition ->
            addition.uuid in additionListFromMenuProduct.map { menuProductToAdditionGroupToAddition ->
                menuProductToAdditionGroupToAddition.additionUuid
            }
        }
    }

    private fun getAdditionNotContainedInMenuProduct(
        commonAdditionList: List<Addition>,
        additionListFromMenuProduct: List<MenuProductToAdditionGroupToAddition>
    ): List<Addition> {
        return commonAdditionList.filterNot { addition ->
            addition.uuid in additionListFromMenuProduct.map { menuProductToAdditionGroupToAddition ->
                menuProductToAdditionGroupToAddition.additionUuid
            }
        }
    }
}
