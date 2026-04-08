package com.bunbeauty.shared.feature.menulist

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.model.menuproduct.MenuProduct
import com.bunbeauty.domain.usecase.GetSeparatedMenuProductListUseCase
import com.bunbeauty.domain.usecase.UpdateVisibleMenuProductUseCase
import com.bunbeauty.shared.extension.mapToStateFlow
import com.bunbeauty.shared.model.MenuListDataState
import com.bunbeauty.shared.model.MenuListEvent
import com.bunbeauty.shared.model.MenuListViewState
import com.bunbeauty.shared.model.MenuProductItem
import com.bunbeauty.shared.viewmodel.base.BaseViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MenuListViewModel(
    private val getSeparatedMenuProductListUseCase: GetSeparatedMenuProductListUseCase,
    private val updateVisibleMenuProductUseCase: UpdateVisibleMenuProductUseCase,
) : BaseViewModel() {
    private val mutableState = MutableStateFlow(MenuListDataState())
    val menuState =
        mutableState.mapToStateFlow(viewModelScope) { state ->
            mapState(state)
        }

    private val exceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            mutableState.update { oldState ->
                oldState.copy(state = MenuListDataState.State.ERROR, throwable = throwable)
            }
        }

    fun loadData() {
        viewModelScope.launch(exceptionHandler) {
            mutableState.update { oldState ->
                oldState.copy(
                    state =
                        if (oldState.isEmptyMenuProductListSize) {
                            MenuListDataState.State.LOADING
                        } else {
                            MenuListDataState.State.SUCCESS
                        },
                    throwable = null,
                )
            }

            val items =
                getSeparatedMenuProductListUseCase(takeRemote = mutableState.value.isEmptyMenuProductListSize)

            mutableState.update { oldState ->
                oldState.copy(
                    visibleMenuProductItems = items.visibleList.map(::toItemModel),
                    hiddenMenuProductItems = items.hiddenList.map(::toItemModel),
                    state = MenuListDataState.State.SUCCESS,
                    isRefreshing = false,
                )
            }
        }
    }

    fun refreshData() {
        viewModelScope.launch(exceptionHandler) {
            mutableState.update { oldState ->
                oldState.copy(
                    isRefreshing = true,
                    throwable = null,
                )
            }

            val items = getSeparatedMenuProductListUseCase(takeRemote = true)

            mutableState.update { oldState ->
                oldState.copy(
                    visibleMenuProductItems = items.visibleList.map(::toItemModel),
                    hiddenMenuProductItems = items.hiddenList.map(::toItemModel),
                    isRefreshing = false,
                )
            }
        }
    }

    fun updateVisible(menuProductItem: MenuProductItem) {
        viewModelScope.launch(exceptionHandler) {
            updateVisibleMenuProductUseCase(
                menuProductUuid = menuProductItem.uuid,
                isVisible = !menuProductItem.visible,
            )
            loadData()
        }
    }

    fun goToEditMenuProduct(menuProductItemUuid: String) {
        mutableState.update { oldState ->
            oldState +
                MenuListEvent.GoToEditMenuProductList(
                    menuProductItemUuid,
                )
        }
    }

    private fun toItemModel(menuProduct: MenuProduct): MenuProductItem =
        MenuProductItem(
            uuid = menuProduct.uuid,
            name = menuProduct.name,
            photoLink = menuProduct.photoLink,
            visible = menuProduct.isVisible,
        )

    private fun mapState(dataState: MenuListDataState): MenuListViewState =
        MenuListViewState(
            state =
                when (dataState.state) {
                    MenuListDataState.State.SUCCESS -> {
                        MenuListViewState.State.Success(
                            visibleMenuProductItems = dataState.visibleMenuProductItems,
                            hiddenMenuProductItems = dataState.hiddenMenuProductItems,
                        )
                    }

                    MenuListDataState.State.LOADING -> MenuListViewState.State.Loading
                    MenuListDataState.State.ERROR -> MenuListViewState.State.Error(dataState.throwable)
                },
            eventList = dataState.eventList,
            isRefreshing = dataState.isRefreshing,
        )

    fun consumeEvents(events: List<MenuListEvent>) {
        mutableState.update { dataState ->
            dataState - events
        }
    }
}
