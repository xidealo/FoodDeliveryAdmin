package com.bunbeauty.presentation.feature.menulist.createmenuproduct

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.menu.common.category.GetSelectableCategoryListUseCase
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductCategoriesException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductDescriptionException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductDescriptionLongException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductImageException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductNameException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductNewPriceException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductNutritionException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductOldPriceException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductUploadingImageException
import com.bunbeauty.domain.feature.menu.createmenuproduct.CreateMenuProductUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.feature.image.ImageFieldData
import com.bunbeauty.presentation.feature.menulist.common.CategoriesFieldData
import com.bunbeauty.presentation.feature.menulist.common.TextFieldData
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

class CreateMenuProductViewModel(
    private val createMenuProductUseCase: CreateMenuProductUseCase,
    private val getSelectableCategoryListUseCase: GetSelectableCategoryListUseCase
) : BaseStateViewModel<CreateMenuProduct.DataState, CreateMenuProduct.Action, CreateMenuProduct.Event>(
    initState = CreateMenuProduct.DataState(
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
        imageField = ImageFieldData(
            value = null,
            isError = false
        ),
        sendingToServer = false,
        descriptionStateError = CreateMenuProduct.DataState.DescriptionStateError.NO_ERROR
    )
) {

    override fun reduce(action: CreateMenuProduct.Action, dataState: CreateMenuProduct.DataState) {
        when (action) {
            CreateMenuProduct.Action.Init -> loadData()

            CreateMenuProduct.Action.BackClick -> {
                sendEvent {
                    CreateMenuProduct.Event.NavigateBack
                }
            }

            is CreateMenuProduct.Action.ChangeNameText -> setState {
                copy(
                    nameField = nameField.copy(
                        value = action.name,
                        isError = false
                    )
                )
            }

            is CreateMenuProduct.Action.ChangeNewPriceText -> setState {
                copy(
                    newPriceField = newPriceField.copy(
                        value = action.newPrice,
                        isError = false
                    )
                )
            }

            is CreateMenuProduct.Action.ChangeOldPriceText -> setState {
                copy(
                    oldPriceField = oldPriceField.copy(
                        value = action.oldPrice,
                        isError = false
                    )
                )
            }

            is CreateMenuProduct.Action.ChangeNutritionText -> setState {
                copy(
                    nutritionField = nutritionField.copy(
                        value = action.nutrition,
                        isError = false
                    )
                )
            }

            is CreateMenuProduct.Action.ChangeUnitsText -> setState {
                copy(
                    units = action.units,
                    nutritionField = nutritionField.copy(
                        isError = false
                    )
                )
            }

            is CreateMenuProduct.Action.ChangeDescriptionText -> setState {
                copy(
                    descriptionField = descriptionField.copy(
                        value = action.description,
                        isError = false
                    )
                )
            }

            is CreateMenuProduct.Action.ChangeComboDescriptionText -> setState {
                copy(comboDescription = action.comboDescription)
            }

            CreateMenuProduct.Action.CategoriesClick -> sendEvent {
                CreateMenuProduct.Event.NavigateToCategoryList(
                    dataState.selectedCategoryList.map { selectableCategory ->
                        selectableCategory.category.uuid
                    }
                )
            }

            is CreateMenuProduct.Action.SelectCategories -> setState {
                copy(
                    categoriesField = categoriesField.copy(
                        value = categoriesField.value.map { selectableCategory ->
                            selectableCategory.copy(
                                selected = action.categoryUuidList.contains(selectableCategory.category.uuid)
                            )
                        },
                        isError = false
                    )
                )
            }

            is CreateMenuProduct.Action.ToggleVisibilityInMenu -> {
                setState {
                    copy(isVisibleInMenu = !isVisibleInMenu)
                }
            }

            is CreateMenuProduct.Action.ToggleVisibilityInRecommendations -> {
                setState {
                    copy(isVisibleInRecommendations = !isVisibleInRecommendations)
                }
            }

            is CreateMenuProduct.Action.SetImage -> setState {
                copy(
                    imageField = imageField.copy(
                        value = action.croppedImageUri,
                        isError = false
                    )
                )
            }

            CreateMenuProduct.Action.CreateMenuProductClick -> createMenuProduct()
        }
    }

    private fun loadData() {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(
                        categoriesField = categoriesField.copy(
                            value = getSelectableCategoryListUseCase()
                        )
                    )
                }
            },
            onError = {
                // No handling
            }
        )
    }

    private fun createMenuProduct() {
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
                createMenuProductUseCase(
                    params = state.value.run {
                        CreateMenuProductUseCase.Params(
                            name = nameField.value.trim(),
                            newPrice = newPriceField.value.trim(),
                            oldPrice = oldPriceField.value.trim(),
                            units = units.trim(),
                            nutrition = nutritionField.value.trim(),
                            description = descriptionField.value.trim(),
                            comboDescription = comboDescription.trim(),
                            selectedCategories = selectedCategoryList,
                            isVisible = isVisibleInMenu,
                            isRecommended = isVisibleInRecommendations,
                            newImageUri = imageField.value
                        )
                    }
                )
                sendEvent { state ->
                    CreateMenuProduct.Event.ShowMenuProductCreated(
                        menuProductName = state.nameField.value
                    )
                }
            },
            onError = { throwable ->
                setState { copy(sendingToServer = false) }
                handleCreateMenuProductError(throwable = throwable)
            }
        )
    }

    private fun handleCreateMenuProductError(throwable: Throwable) {
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
                        descriptionStateError = CreateMenuProduct.DataState.DescriptionStateError
                            .EMPTY_DESCRIPTION_ERROR,
                        descriptionField = descriptionField.copy(
                            isError = true
                        )
                    )
                }
            }

            is MenuProductDescriptionLongException -> {
                setState {
                    copy(
                        descriptionStateError = CreateMenuProduct.DataState.DescriptionStateError
                            .LONG_DESCRIPTION_ERROR,
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
                sendEvent {
                    CreateMenuProduct.Event.ShowEmptyPhoto
                }
            }

            is MenuProductUploadingImageException -> {
                sendEvent {
                    CreateMenuProduct.Event.ShowImageUploadingFailed
                }
            }

            else -> {
                sendEvent {
                    CreateMenuProduct.Event.ShowSomethingWentWrong
                }
            }
        }
    }
}
