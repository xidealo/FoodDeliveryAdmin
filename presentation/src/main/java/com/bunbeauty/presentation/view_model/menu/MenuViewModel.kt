package com.bunbeauty.presentation.view_model.menu

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.model.menu_product.MenuProduct
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import com.bunbeauty.domain.util.product.IProductUtil
import com.bunbeauty.presentation.extension.toStateSuccess
import com.bunbeauty.presentation.model.MenuProductItemModel
import com.bunbeauty.presentation.navigation_event.MenuNavigationEvent
import com.bunbeauty.presentation.state.State
import com.bunbeauty.presentation.utils.IStringUtil
import com.bunbeauty.presentation.view_model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val menuProductRepo: MenuProductRepo,
    private val productUtil: IProductUtil,
    private val stringUtil: IStringUtil,
    private val dataStoreRepo: DataStoreRepo
) : BaseViewModel() {

    private val mutableProductListState: MutableStateFlow<State<List<MenuProductItemModel>>> =
        MutableStateFlow(State.Loading())
    val productListState: StateFlow<State<List<MenuProductItemModel>>> =
        mutableProductListState.asStateFlow()

    init {
        viewModelScope.launch {
            menuProductRepo.refreshMenuProductList(dataStoreRepo.companyUuid.first())
            subscribeOnProducts()
        }
    }

    fun goToEditMenuProduct(menuProductItemModel: MenuProductItemModel) {
        goTo(MenuNavigationEvent.ToEditMenuProduct(menuProductItemModel.menuProduct))
    }

    fun goToCreateMenuProduct() {
        goTo(MenuNavigationEvent.ToCreateMenuProduct)
    }

    private suspend fun subscribeOnProducts() {
        menuProductRepo.getMenuProductList().collect { menuProductList ->
            mutableProductListState.value = menuProductList.map(::toItemModel).toStateSuccess()
        }
    }

    private fun toItemModel(menuProduct: MenuProduct): MenuProductItemModel {
/*
        val newCost = productUtil.getMenuProductNewPrice(menuProduct)
        val oldCost = productUtil.getMenuProductOldPrice(menuProduct)
*/

        return MenuProductItemModel(
            name = menuProduct.name,
            photoLink = menuProduct.photoLink,
            visible = menuProduct.isVisible,
            newCost = "",//stringUtil.getCostString(newCost),
            oldCost = "",//stringUtil.getCostString(oldCost),
            menuProduct = menuProduct,
        )
    }
}