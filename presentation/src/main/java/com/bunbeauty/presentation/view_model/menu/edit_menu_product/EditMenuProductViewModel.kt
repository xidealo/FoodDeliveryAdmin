package com.bunbeauty.presentation.view_model.menu.edit_menu_product

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.enums.ProductCode
import com.bunbeauty.domain.model.Suggestion
import com.bunbeauty.domain.use_case.GetMenuProductUseCase
import com.bunbeauty.domain.use_case.UpdateMenuProductUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.extension.mapToStateFlow
import com.bunbeauty.presentation.model.list.MenuProductCode
import com.bunbeauty.presentation.utils.IStringUtil
import com.bunbeauty.presentation.view_model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditMenuProductViewModel @Inject constructor(
    private val stringUtil: IStringUtil,
    private val getMenuProductUseCase: GetMenuProductUseCase,
    private val updateMenuProductUseCase: UpdateMenuProductUseCase
) : BaseViewModel() {


    private val menuProductCodeList = ProductCode.values()
        .map { productCode ->
            MenuProductCode(title = stringUtil.getProductCodeString(productCode))
        }

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
                        utils = menuProduct.utils,
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

    fun deleteMenuProduct() {
        viewModelScope.launch(IO) {
            finishDeleting()
        }
    }

    fun updateMenuProduct() {
        viewModelScope.launchSafe(
            block = {
                val name = mutableState.value.name.trim()
                mutableState.value.menuProduct?.let { menuProduct ->
                    updateMenuProductUseCase(
                        menuProduct = menuProduct.copy(
                            name = name,
                            description = mutableState.value.description.trim(),
                            newPrice = mutableState.value.newPrice.toInt(),
                            oldPrice = mutableState.value.oldPrice.ifEmpty { null }?.toInt() ?: 0,
                        )
                    )
                    mutableState.update { oldState ->
                        oldState + EditMenuProductEvent.ShowUpdatProductSuccess(productName = name)
                    }
                }
            },
            onError = {
                mutableState.update { oldState ->
                    oldState + EditMenuProductEvent.ShowUpdateProductError(
                        mutableState.value.menuProduct?.name ?: ""
                    )
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

    private fun finishDeleting() {
        //sendMessage(name + resourcesProvider.getString(R.string.msg_product_deleted))
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
                        hasNutritionError = false
                    )
                }
                EditMenuProductDataState.State.LOADING -> EditMenuProductUIState.EditMenuProductState.Loading
                EditMenuProductDataState.State.ERROR -> EditMenuProductUIState.EditMenuProductState.Error
            },
            eventList = dataState.eventList
        )
    }

}