package com.bunbeauty.presentation.view_model.menu.edit_menu_product

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.exception.updateproduct.MenuProductDescriptionException
import com.bunbeauty.domain.exception.updateproduct.MenuProductNameException
import com.bunbeauty.domain.exception.updateproduct.MenuProductNewPriceException
import com.bunbeauty.domain.model.Suggestion
import com.bunbeauty.domain.use_case.GetMenuProductUseCase
import com.bunbeauty.domain.use_case.UpdateMenuProductUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.extension.mapToStateFlow
import com.bunbeauty.presentation.view_model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class EditMenuProductViewModel @Inject constructor(
    private val getMenuProductUseCase: GetMenuProductUseCase,
    private val updateMenuProductUseCase: UpdateMenuProductUseCase
) : BaseViewModel() {

    private val mutableState = MutableStateFlow(EditMenuProductDataState())
    val editMenuProductUiState = mutableState.mapToStateFlow(viewModelScope) { state ->
        mapState(state)
    }

    fun loadData(productUuid: String) {
        viewModelScope.launchSafe(
            block = {
                val menuProduct = getMenuProductUseCase(productUuid)
                mutableState.update { oldState ->
                    oldState.copy(
                        menuProduct = menuProduct,
                        name = menuProduct.name,
                        constName = menuProduct.name,
                        description = menuProduct.description,
                        newPrice = menuProduct.newPrice.toString(),
                        oldPrice = menuProduct.oldPrice?.toString() ?: "",
                        state = EditMenuProductDataState.State.SUCCESS,
                        nutrition = menuProduct.nutrition?.toString() ?: "",
                        utils = menuProduct.utils ?: "",
                        isVisible = menuProduct.isVisible,
                    )
                }
            },
            onError = {
                mutableState.update { oldState ->
                    oldState.copy(
                        state = EditMenuProductDataState.State.ERROR
                    )
                }
            }
        )
    }

    fun updateMenuProduct() {
        viewModelScope.launchSafe(
            block = {
                mutableState.update { oldState ->
                    oldState.copy(
                        isLoadingButton = true,
                        hasNameError = false,
                        hasDescriptionError = false,
                        hasNewPriceError = false,
                        hasOldPriceError = false,
                        hasNutritionError = false,
                    )
                }

                val name = mutableState.value.name.trim()
                mutableState.value.menuProduct?.let { menuProduct ->
                    updateMenuProductUseCase(
                        menuProduct = menuProduct.copy(
                            name = name,
                            description = mutableState.value.description.trim(),
                            newPrice = mutableState.value.newPrice.toIntOrNull() ?: 0,
                            oldPrice = mutableState.value.oldPrice
                                .ifEmpty { null }
                                ?.toInt() ?: 0,
                            utils = if (mutableState.value.nutrition.isEmpty()) {
                                mutableState.value.utils
                            } else {
                                ""
                            },
                            nutrition = mutableState.value.nutrition.toIntOrNull(),
                            isVisible = mutableState.value.isVisible
                        )
                    )
                    mutableState.update { oldState ->
                        oldState + EditMenuProductEvent.ShowUpdatProductSuccess(productName = name)
                    }
                }
            },
            onError = {
                mutableState.update { oldState ->
                    when (it) {
                        is MenuProductNameException -> {
                            oldState.copy(
                                isLoadingButton = false,
                                hasNameError = true
                            )
                        }
                        is MenuProductNewPriceException -> {
                            oldState.copy(
                                isLoadingButton = false,
                                hasNewPriceError = true
                            )
                        }
                        is MenuProductDescriptionException -> {
                            oldState.copy(
                                isLoadingButton = false,
                                hasDescriptionError = true
                            )
                        }
                        else -> {
                            oldState.copy(
                                isLoadingButton = false,
                                eventList = oldState.eventList + EditMenuProductEvent.ShowUpdateProductError(
                                    mutableState.value.menuProduct?.name ?: ""
                                )
                            )
                        }
                    }
                }
            }
        )
    }

    fun onNameTextChanged(name: String) {
        mutableState.update { state ->
            state.copy(name = name)
        }
    }

    fun onDescriptionTextChanged(description: String) {
        mutableState.update { state ->
            state.copy(description = description)
        }
    }

    fun onNewPriceTextChanged(newPrice: String) {
        mutableState.update { state ->
            state.copy(newPrice = newPrice)
        }
    }

    fun onNutritionTextChanged(nutrition: String) {
        mutableState.update { state ->
            state.copy(nutrition = nutrition)
        }
    }

    fun onSuggestedUtilsSelected(suggestion: Suggestion) {
        mutableState.update { state ->
            state.copy(utils = suggestion.value)
        }
    }

    fun onOldPriceTextChanged(oldPrice: String) {
        mutableState.update { state ->
            state.copy(oldPrice = oldPrice)
        }
    }

    fun onVisibleChanged(isVisible: Boolean) {
        mutableState.update { state ->
            state.copy(isVisible = isVisible)
        }
    }

    fun consumeEvents(events: List<EditMenuProductEvent>) {
        mutableState.update { dataState ->
            dataState - events
        }
    }

    private fun mapState(dataState: EditMenuProductDataState): EditMenuProductUIState {
        return EditMenuProductUIState(
            title = dataState.constName,
            editMenuProductState = when (dataState.state) {
                EditMenuProductDataState.State.SUCCESS -> {
                    EditMenuProductUIState.EditMenuProductState.Success(
                        name = dataState.name,
                        hasNameError = dataState.hasNameError,
                        description = dataState.description,
                        hasDescriptionError = dataState.hasDescriptionError,
                        newPrice = dataState.newPrice,
                        hasNewPriceError = dataState.hasNewPriceError,
                        oldPrice = dataState.oldPrice,
                        hasOldPriceError = dataState.hasOldPriceError,
                        utils = dataState.utils,
                        nutrition = dataState.nutrition,
                        hasNutritionError = dataState.hasNutritionError,
                        isLoadingButton = dataState.isLoadingButton,
                        isVisible = dataState.isVisible
                    )
                }
                EditMenuProductDataState.State.LOADING -> EditMenuProductUIState.EditMenuProductState.Loading
                EditMenuProductDataState.State.ERROR -> EditMenuProductUIState.EditMenuProductState.Error
            },
            eventList = dataState.eventList
        )
    }

}