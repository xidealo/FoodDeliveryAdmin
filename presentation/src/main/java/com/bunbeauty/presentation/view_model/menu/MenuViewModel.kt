package com.bunbeauty.presentation.view_model.menu

import androidx.core.os.bundleOf
import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.Constants.MENU_PRODUCT_ARGS_KEY
import com.bunbeauty.domain.model.menu_product.MenuProduct
import com.bunbeauty.domain.repo.MenuProductRepo
import com.bunbeauty.domain.util.product.IProductUtil
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.extensions.toStateSuccess
import com.bunbeauty.presentation.view_model.BaseViewModel
import com.bunbeauty.presentation.state.State
import com.bunbeauty.fooddeliveryadmin.ui.items.MenuProductItem
import com.bunbeauty.fooddeliveryadmin.utils.IStringUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val menuProductRepo: MenuProductRepo,
    private val productUtil: IProductUtil,
    private val stringUtil: IStringUtil,
) : BaseViewModel() {

    val productListState: StateFlow<State<List<MenuProductItem>>>
        get() = _productListState
    private val _productListState = MutableStateFlow<State<List<MenuProductItem>>>(State.Loading())

    init {
        subscribeOnProducts()
    }

    fun goToEditMenuProduct(menuProduct: MenuProduct) {
        router.navigate(
            R.id.to_editMenuProductFragment,
            bundleOf(MENU_PRODUCT_ARGS_KEY to menuProduct)
        )
    }

    fun goToCreateMenuProduct() {
        router.navigate(R.id.to_createMenuProductFragment, null)
    }

    private fun subscribeOnProducts() {
        viewModelScope.launch {
            menuProductRepo.menuProductList.collect { menuProductList ->
                _productListState.value = menuProductList.map(::toMenuProductItem).toStateSuccess()
            }
        }
    }

    private fun toMenuProductItem(menuProduct: MenuProduct): MenuProductItem {
        val newCost = productUtil.getMenuProductNewPrice(menuProduct)
        val oldCost = productUtil.getMenuProductOldPrice(menuProduct)

        return MenuProductItem(
            name = menuProduct.name,
            photoLink = menuProduct.photoLink,
            visible = menuProduct.visible,
            newCost = stringUtil.getCostString(newCost),
            oldCost = stringUtil.getCostString(oldCost),
            menuProduct = menuProduct,
        )
    }

}