package com.bunbeauty.presentation.feature.menulist.createmenuproduct

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.menu.common.GetSelectableCategoryListUseCase
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductCategoriesException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductDescriptionException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductImageException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductNameException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductNewPriceException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductOldPriceException
import com.bunbeauty.domain.feature.menu.common.exception.MenuProductUploadingImageException
import com.bunbeauty.domain.feature.menu.createmenuproduct.CreateMenuProductUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.feature.menulist.common.CategoriesFieldData
import com.bunbeauty.presentation.feature.menulist.common.TextFieldData
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateMenuProductViewModel @Inject constructor(
    private val createMenuProductUseCase: CreateMenuProductUseCase,
    private val getSelectableCategoryListUseCase: GetSelectableCategoryListUseCase
) : BaseStateViewModel<CreateMenuProduct.DataState, CreateMenuProduct.Action, CreateMenuProduct.Event>(
    initState = CreateMenuProduct.DataState(
        nameField = TextFieldData(
            value = "",
            isError = false
        ),
        newPriceField = TextFieldData(
            value = "",
            isError = false
        ),
        oldPriceField = TextFieldData(
            value = "",
            isError = false
        ),
        descriptionField = TextFieldData(
            value = "",
            isError = false
        ),
        nutrition = "",
        utils = "",
        comboDescription = "",
        categoriesField = CategoriesFieldData(
            value = listOf(),
            isError = false
        ),
        isVisibleInMenu = true,
        isVisibleInRecommendations = false,
        imageField = CreateMenuProduct.ImageFieldData(
            value = null,
            isError = false
        ),
        sendingToServer = false
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
                        value = action.name
                    )
                )
            }

            is CreateMenuProduct.Action.ChangeNewPriceText -> setState {
                copy(
                    newPriceField = newPriceField.copy(
                        value = action.newPrice
                    )
                )
            }

            is CreateMenuProduct.Action.ChangeOldPriceText -> setState {
                copy(
                    oldPriceField = oldPriceField.copy(
                        value = action.oldPrice
                    )
                )
            }

            is CreateMenuProduct.Action.ChangeNutritionText -> setState {
                copy(
                    nutrition = action.nutrition
                )
            }

            is CreateMenuProduct.Action.ChangeUtilsText -> setState {
                copy(
                    utils = action.utils
                )
            }

            is CreateMenuProduct.Action.ChangeDescriptionText -> setState {
                copy(
                    descriptionField = descriptionField.copy(
                        value = action.description
                    )
                )
            }

            is CreateMenuProduct.Action.ChangeComboDescriptionText -> setState {
                copy(
                    comboDescription = action.comboDescription
                )
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
                        }
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
                        value = action.croppedImageUri
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
                            name = nameField.value,
                            newPrice = newPriceField.value,
                            oldPrice = oldPriceField.value,
                            utils = utils,
                            nutrition = nutrition,
                            description = descriptionField.value,
                            comboDescription = comboDescription,
                            selectedCategories = selectedCategoryList,
                            isVisible = isVisibleInMenu,
                            isRecommended = isVisibleInRecommendations,
                            imageUri = imageField.value
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
