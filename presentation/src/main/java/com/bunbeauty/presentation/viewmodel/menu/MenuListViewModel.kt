package com.bunbeauty.presentation.viewmodel.menu

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.model.menuproduct.MenuProduct
import com.bunbeauty.domain.usecase.GetSeparatedMenuProductListUseCase
import com.bunbeauty.domain.usecase.UpdateVisibleMenuProductUseCase
import com.bunbeauty.presentation.extension.mapToStateFlow
import com.bunbeauty.presentation.model.MenuDataState
import com.bunbeauty.presentation.model.MenuEvent
import com.bunbeauty.presentation.model.MenuProductItem
import com.bunbeauty.presentation.model.MenuUiState
import com.bunbeauty.presentation.utils.IStringUtil
import com.bunbeauty.presentation.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// TODO TESTS
@HiltViewModel
class MenuListViewModel @Inject constructor(
    private val stringUtil: IStringUtil,
    private val getSeparatedMenuProductListUseCase: GetSeparatedMenuProductListUseCase,
    private val updateVisibleMenuProductUseCase: UpdateVisibleMenuProductUseCase,
) : BaseViewModel() {

    private val mutableState = MutableStateFlow(MenuDataState())
    val menuState = mutableState.mapToStateFlow(viewModelScope) { state ->
        mapState(state)
    }

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        mutableState.update { oldState ->
            oldState.copy(state = MenuDataState.State.ERROR, throwable = throwable)
        }
    }

    fun loadData() {
        viewModelScope.launch(exceptionHandler) {
            mutableState.update { oldState ->
                oldState.copy(
                    state = if (oldState.isEmptyMenuProductListSize) {
                        MenuDataState.State.LOADING
                    } else {
                        MenuDataState.State.SUCCESS
                    },
                    throwable = null
                )
            }

            val items =
                getSeparatedMenuProductListUseCase(takeRemote = mutableState.value.isEmptyMenuProductListSize)

            mutableState.update { oldState ->
                oldState.copy(
                    visibleMenuProductItems = items.visibleList.map(::toItemModel),
                    hiddenMenuProductItems = items.hiddenList.map(::toItemModel),
                    state = MenuDataState.State.SUCCESS,
                    isRefreshing = false
                )
            }
        }
    }

    fun refreshData() {
        viewModelScope.launch(exceptionHandler) {
            mutableState.update { oldState ->
                oldState.copy(
                    isRefreshing = true,
                    throwable = null
                )
            }

            val items = getSeparatedMenuProductListUseCase(takeRemote = true)

            mutableState.update { oldState ->
                oldState.copy(
                    visibleMenuProductItems = items.visibleList.map(::toItemModel),
                    hiddenMenuProductItems = items.hiddenList.map(::toItemModel),
                    isRefreshing = false
                )
            }
        }
    }

    fun updateVisible(menuProductItem: MenuProductItem) {
        viewModelScope.launch(exceptionHandler) {
            updateVisibleMenuProductUseCase(
                menuProductUuid = menuProductItem.uuid,
                isVisible = !menuProductItem.visible
            )
            loadData()
        }
    }

    fun goToEditMenuProduct(menuProductItemUuid: String) {
        mutableState.update { oldState ->
            oldState + MenuEvent.GoToEditMenuProduct(
                menuProductItemUuid
            )
        }
    }

    private fun toItemModel(menuProduct: MenuProduct): MenuProductItem {
        return MenuProductItem(
            uuid = menuProduct.uuid,
            name = menuProduct.name,
            photoLink = menuProduct.photoLink,
            visible = menuProduct.isVisible,
            newCost = stringUtil.getCostString(menuProduct.newPrice),
            oldCost = stringUtil.getCostString(menuProduct.oldPrice),
        )
    }

    private fun mapState(dataState: MenuDataState): MenuUiState {
        return MenuUiState(
            state = when (dataState.state) {
                MenuDataState.State.SUCCESS -> {
                    MenuUiState.State.Success(
                        visibleMenuProductItems = dataState.visibleMenuProductItems,
                        hiddenMenuProductItems = dataState.hiddenMenuProductItems,
                    )
                }

                MenuDataState.State.LOADING -> MenuUiState.State.Loading
                MenuDataState.State.ERROR -> MenuUiState.State.Error(dataState.throwable)
            },
            eventList = dataState.eventList,
            isRefreshing = dataState.isRefreshing,
        )
    }

    fun consumeEvents(events: List<MenuEvent>) {
        mutableState.update { dataState ->
            dataState - events
        }
    }
}
