package com.bunbeauty.presentation.feature.menulist.editmenuproduct

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.menu.common.category.GetSelectableCategoryListUseCase
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductCategoriesException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductDescriptionException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductImageException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductNameException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductNewPriceException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductNutritionException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductOldPriceException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductUploadingImageException
import com.bunbeauty.domain.feature.menu.editmenuproduct.GetMenuProductUseCase
import com.bunbeauty.domain.feature.menu.editmenuproduct.UpdateMenuProductUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.feature.menulist.common.CategoriesFieldData
import com.bunbeauty.presentation.feature.menulist.common.TextFieldData
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditMenuProductViewModel @Inject constructor(
    private val getMenuProductUseCase: GetMenuProductUseCase,
    private val getSelectableCategoryListUseCase: GetSelectableCategoryListUseCase,
    private val updateMenuProductUseCase: UpdateMenuProductUseCase
) : BaseStateViewModel<EditMenuProduct.DataState, EditMenuProduct.Action, EditMenuProduct.Event>(
    initState = EditMenuProduct.DataState(
        state = EditMenuProduct.DataState.State.LOADING,
        productUuid = null,
        productName = "",
        nameField = TextFieldData.empty,
        newPriceField = TextFieldData.empty,
        oldPriceField = TextFieldData.empty,
        descriptionField = TextFieldData.empty,
        nutritionField = TextFieldData.empty,
        units = "",
        comboDescription = "",
        categoriesField = CategoriesFieldData(
            value = listOf(),
            isError = false
        ),
        isVisibleInMenu = true,
        isVisibleInRecommendations = false,
        imageField = EditMenuProduct.ImageFieldData(
            value = null,
            isError = false
        ),
        sendingToServer = false
    )
) {

    override fun reduce(action: EditMenuProduct.Action, dataState: EditMenuProduct.DataState) {
        when (action) {
            is EditMenuProduct.Action.LoadData -> {
                loadData(productUuid = action.productUuid)
            }

            EditMenuProduct.Action.BackClick -> {
                sendEvent {
                    EditMenuProduct.Event.NavigateBack
                }
            }

            is EditMenuProduct.Action.ChangeNameText -> {
                setState {
                    copy(
                        nameField = nameField.copy(
                            value = action.name
                        )
                    )
                }
            }

            is EditMenuProduct.Action.ChangeNewPriceText -> {
                setState {
                    copy(
                        newPriceField = newPriceField.copy(
                            value = action.newPrice
                        )
                    )
                }
            }

            is EditMenuProduct.Action.ChangeOldPriceText -> {
                setState {
                    copy(
                        oldPriceField = oldPriceField.copy(
                            value = action.oldPrice
                        )
                    )
                }
            }

            is EditMenuProduct.Action.ChangeNutritionText -> {
                setState {
                    copy(
                        nutritionField = nutritionField.copy(
                            value = action.nutrition
                        )
                    )
                }
            }

            is EditMenuProduct.Action.ChangeUtilsText -> {
                setState {
                    copy(
                        units = action.units
                    )
                }
            }

            is EditMenuProduct.Action.ChangeDescriptionText -> {
                setState {
                    copy(
                        descriptionField = descriptionField.copy(
                            value = action.description
                        )
                    )
                }
            }

            is EditMenuProduct.Action.ChangeComboDescriptionText -> {
                setState {
                    copy(
                        comboDescription = action.comboDescription
                    )
                }
            }

            EditMenuProduct.Action.CategoriesClick -> {
                sendEvent {
                    EditMenuProduct.Event.NavigateToCategoryList(
                        dataState.categoriesField.selectedCategoryList.map { selectableCategory ->
                            selectableCategory.category.uuid
                        }
                    )
                }
            }

            is EditMenuProduct.Action.SelectCategories -> {
                setState {
                    copy(
                        categoriesField = categoriesField.copy(
                            value = categoriesField.value.map { selectableCategory ->
                                selectableCategory.copy(
                                    selected = action.categoryUuidList.contains(selectableCategory.category.uuid)
                                )
                            }
                        )
                    )
                }
            }

            is EditMenuProduct.Action.ToggleVisibilityInMenu -> {
                setState {
                    copy(isVisibleInMenu = !isVisibleInMenu)
                }
            }

            is EditMenuProduct.Action.ToggleVisibilityInRecommendations -> {
                setState {
                    copy(isVisibleInRecommendations = !isVisibleInRecommendations)
                }
            }

            is EditMenuProduct.Action.SetImage -> {
                setState {
                    copy(
                        imageField = imageField.copy(
                            value = imageField.value?.copy(
                                newImageUri = action.croppedImageUri
                            )
                        )
                    )
                }
            }

            EditMenuProduct.Action.SaveMenuProductClick -> {
                updateMenuProduct()
            }
        }
    }

    private fun loadData(productUuid: String) {
        viewModelScope.launchSafe(
            block = {
                val menuProduct = getMenuProductUseCase(productUuid)
                setState {
                    copy(
                        state = EditMenuProduct.DataState.State.SUCCESS,
                        productUuid = menuProduct.uuid,
                        productName = menuProduct.name,

                        nameField = TextFieldData(
                            value = menuProduct.name,
                            isError = false
                        ),
                        newPriceField = TextFieldData(
                            value = menuProduct.newPrice.toString(),
                            isError = false
                        ),
                        oldPriceField = TextFieldData(
                            value = menuProduct.oldPrice?.toString() ?: "",
                            isError = false
                        ),
                        descriptionField = TextFieldData(
                            value = menuProduct.description,
                            isError = false
                        ),
                        nutritionField = TextFieldData(
                            value = menuProduct.nutrition?.takeIf { nutrition ->
                                nutrition != 0
                            }?.toString() ?: "",
                            isError = false
                        ),
                        units = menuProduct.units ?: "",
                        comboDescription = menuProduct.comboDescription ?: "",
                        categoriesField = CategoriesFieldData(
                            value = getSelectableCategoryListUseCase(
                                selectedCategoryUuidList = menuProduct.categoryUuids
                            ),
                            isError = false
                        ),
                        isVisibleInMenu = menuProduct.isVisible,
                        isVisibleInRecommendations = menuProduct.isRecommended,
                        imageField = EditMenuProduct.ImageFieldData(
                            value = EditMenuProduct.MenuProductImage(
                                photoLink = menuProduct.photoLink,
                                newImageUri = null
                            ),
                            isError = false
                        )
                    )
                }
            },
            onError = {
                setState {
                    copy(state = EditMenuProduct.DataState.State.ERROR)
                }
            }
        )
    }

    private fun updateMenuProduct() {
        setState {
            copy(
                nameField = nameField.copy(isError = false),
                newPriceField = newPriceField.copy(isError = false),
                oldPriceField = oldPriceField.copy(isError = false),
                nutritionField = nutritionField.copy(isError = false),
                descriptionField = descriptionField.copy(isError = false),
                categoriesField = categoriesField.copy(isError = false),
                imageField = imageField.copy(isError = false),
                sendingToServer = true
            )
        }
        viewModelScope.launchSafe(
            block = {
                updateMenuProductUseCase(
                    params = state.value.run {
                        UpdateMenuProductUseCase.Params(
                            uuid = productUuid ?: error("Uuid can't be null"),
                            name = nameField.value,
                            newPrice = newPriceField.value,
                            oldPrice = oldPriceField.value,
                            nutrition = nutritionField.value,
                            units = units,
                            description = descriptionField.value,
                            comboDescription = comboDescription,
                            selectedCategories = categoriesField.selectedCategoryList,
                            isVisible = isVisibleInMenu,
                            isRecommended = isVisibleInRecommendations,
                            photoLink = imageField.value?.photoLink,
                            newImageUri = imageField.value?.newImageUri
                        )
                    }
                )
                sendEvent { dataState ->
                    EditMenuProduct.Event.ShowUpdateProductSuccess(
                        productName = dataState.nameField.value
                    )
                }
            },
            onError = { throwable ->
                setState { copy(sendingToServer = false) }
                handleUpdateMenuProductError(throwable = throwable)
            }
        )
    }

    private fun handleUpdateMenuProductError(throwable: Throwable) {
        when (throwable) {
            is MenuProductNameException -> {
                setState {
                    copy(
                        nameField = nameField.copy(
                            isError = true
                        )
                    )
                }
            }

            is MenuProductNewPriceException -> {
                setState {
                    copy(
                        newPriceField = newPriceField.copy(
                            isError = true
                        )
                    )
                }
            }

            is MenuProductOldPriceException -> {
                setState {
                    copy(
                        oldPriceField = oldPriceField.copy(
                            isError = true
                        )
                    )
                }
            }

            is MenuProductNutritionException -> {
                setState {
                    copy(
                        nutritionField = nutritionField.copy(
                            isError = true
                        )
                    )
                }
            }

            is MenuProductDescriptionException -> {
                setState {
                    copy(
                        descriptionField = descriptionField.copy(
                            isError = true
                        )
                    )
                }
            }

            is MenuProductCategoriesException -> {
                setState {
                    copy(
                        categoriesField = categoriesField.copy(
                            isError = true
                        )
                    )
                }
            }

            is MenuProductImageException -> {
                setState {
                    copy(
                        imageField = imageField.copy(
                            isError = true
                        )
                    )
                }
            }

            is MenuProductUploadingImageException -> {
                sendEvent {
                    EditMenuProduct.Event.ShowImageUploadingFailed
                }
            }

            else -> {
                sendEvent {
                    EditMenuProduct.Event.ShowSomethingWentWrong
                }
            }
        }
    }
}
