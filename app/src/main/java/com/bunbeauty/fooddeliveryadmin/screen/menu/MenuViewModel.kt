package com.bunbeauty.fooddeliveryadmin.screen.menu

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.model.menu_product.MenuProduct
import com.bunbeauty.fooddeliveryadmin.screen.order_list.domain.GetMenuUseCase
import com.bunbeauty.fooddeliveryadmin.screen.order_list.domain.UpdateVisibleMenuProductUseCase
import com.bunbeauty.presentation.model.MenuViewState
import com.bunbeauty.presentation.utils.IStringUtil
import com.bunbeauty.presentation.view_model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val stringUtil: IStringUtil,
    private val getMenuUseCase: GetMenuUseCase,
    private val updateVisibleMenuProductUseCase: UpdateVisibleMenuProductUseCase,
) : BaseViewModel() {

    private val mutableProductListState: MutableStateFlow<MenuViewState> =
        MutableStateFlow(MenuViewState())
    val menuViewState: StateFlow<MenuViewState> = mutableProductListState.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        mutableProductListState.update {
            it.copy(isLoading = false, throwable = throwable)
        }
    }

    fun loadData() {
        viewModelScope.launch(exceptionHandler) {
            mutableProductListState.update {
                it.copy(
                    menuProductItems = getMenuUseCase().map(::toItemModel),
                    isLoading = false
                )
            }
        }
    }

    //TODO TESTS
    fun updateVisible(menuProductItem: MenuViewState.MenuProductItem) {
        viewModelScope.launch(exceptionHandler) {

            mutableProductListState.update { oldState ->
                oldState.copy(
                    isLoading = true
                )
            }

            updateVisibleMenuProductUseCase(
                uuid = menuProductItem.uuid,
                isVisible = !menuProductItem.visible
            )

            mutableProductListState.update {
                it.copy(
                    menuProductItems = getMenuUseCase().map(::toItemModel),
                    isLoading = false
                )
            }

        }
    }

    fun goToEditMenuProduct(menuProductItemModel: MenuViewState) {
        //goTo(MenuNavigationEvent.ToEditMenuProduct(menuProductItemModel.menuProduct))
    }

    fun goToCreateMenuProduct() {
        //goTo(MenuNavigationEvent.ToCreateMenuProduct)
    }

    private fun toItemModel(menuProduct: MenuProduct): MenuViewState.MenuProductItem {
        return MenuViewState.MenuProductItem(
            uuid = menuProduct.uuid,
            name = menuProduct.name,
            photoLink = menuProduct.photoLink,
            visible = menuProduct.isVisible,
            newCost = stringUtil.getCostString(menuProduct.newPrice),
            oldCost = stringUtil.getCostString(menuProduct.oldPrice),
        )
    }
}