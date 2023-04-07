package com.bunbeauty.fooddeliveryadmin.screen.menu

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.model.menu_product.MenuProduct
import com.bunbeauty.fooddeliveryadmin.screen.order_list.domain.GetMenuUseCase
import com.bunbeauty.fooddeliveryadmin.screen.order_list.domain.UpdateVisibleMenuProductUseCase
import com.bunbeauty.presentation.model.MenuState
import com.bunbeauty.presentation.utils.IStringUtil
import com.bunbeauty.presentation.view_model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val stringUtil: IStringUtil,
    private val getMenuUseCase: GetMenuUseCase,
    private val updateVisibleMenuProductUseCase: UpdateVisibleMenuProductUseCase,
) : BaseViewModel() {

    private val mutableProductListState: MutableStateFlow<MenuState> =
        MutableStateFlow(MenuState())
    val menuState: StateFlow<MenuState> = mutableProductListState.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        mutableProductListState.update {
            it.copy(isLoading = false, throwable = throwable)
        }
    }

    fun loadData(isRefreshing: Boolean = false) {
        viewModelScope.launch(exceptionHandler) {
            val items = if (isRefreshing) {
                mutableProductListState.update {
                    it.copy(
                        isRefreshing = true,
                        throwable = null
                    )
                }
                getMenuUseCase(isRefreshing = true).map(::toItemModel)
            } else {
                mutableProductListState.update {
                    it.copy(
                        isLoading = true,
                        throwable = null
                    )
                }
                getMenuUseCase(isRefreshing = false).map(::toItemModel)
            }

            mutableProductListState.update {
                it.copy(
                    menuProductItems = items,
                    isLoading = false,
                    isRefreshing = false
                )
            }
        }
    }

    // TODO TESTS
    fun updateVisible(menuProductItem: MenuState.MenuProductItem) {
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

            loadData()
        }
    }

    fun goToEditMenuProduct(menuProductItemModel: MenuState) {
        // goTo(MenuNavigationEvent.ToEditMenuProduct(menuProductItemModel.menuProduct))
    }

    fun goToCreateMenuProduct() {
        // goTo(MenuNavigationEvent.ToCreateMenuProduct)
    }

    private fun toItemModel(menuProduct: MenuProduct): MenuState.MenuProductItem {
        return MenuState.MenuProductItem(
            uuid = menuProduct.uuid,
            name = menuProduct.name,
            photoLink = menuProduct.photoLink,
            visible = menuProduct.isVisible,
            newCost = stringUtil.getCostString(menuProduct.newPrice),
            oldCost = stringUtil.getCostString(menuProduct.oldPrice),
        )
    }
}
