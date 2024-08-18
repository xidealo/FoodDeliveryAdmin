package com.bunbeauty.presentation.feature.menulist.addmenuproduct

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.exception.updateproduct.MenuProductCategoriesException
import com.bunbeauty.domain.exception.updateproduct.MenuProductDescriptionException
import com.bunbeauty.domain.exception.updateproduct.MenuProductNameException
import com.bunbeauty.domain.exception.updateproduct.MenuProductNewPriceException
import com.bunbeauty.domain.exception.updateproduct.MenuProductOldPriceException
import com.bunbeauty.domain.exception.updateproduct.MenuProductImageException
import com.bunbeauty.domain.exception.updateproduct.MenuProductUploadingImageException
import com.bunbeauty.domain.feature.menu.addmenuproduct.CreateMenuProductUseCase
import com.bunbeauty.domain.feature.menu.addmenuproduct.GetSelectableCategoryListUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddMenuProductViewModel @Inject constructor(
    private val createMenuProductUseCase: CreateMenuProductUseCase,
    private val getSelectableCategoryListUseCase: GetSelectableCategoryListUseCase
) : BaseStateViewModel<AddMenuProduct.DataState, AddMenuProduct.Action, AddMenuProduct.Event>(
    initState = AddMenuProduct.DataState(
        name = "",
        hasNameError = false,
        description = "",
        hasDescriptionError = false,
        newPrice = "",
        hasNewPriceError = false,
        oldPrice = "",
        hasOldPriceError = false,
        nutrition = "",
        utils = "",
        comboDescription = "",
        originalImageUri = null,
        croppedImageUri = null,
        isLoadingButton = false,
        isVisibleInMenu = true,
        isVisibleInRecommendation = false,
        categoryList = listOf(),
        hasImageError = false,
        hasCategoriesError = false,
        sendingToServer = false,
    )
) {

    override fun reduce(action: AddMenuProduct.Action, dataState: AddMenuProduct.DataState) {
        when (action) {
            AddMenuProduct.Action.Init -> loadData()

            AddMenuProduct.Action.OnBackClick -> {
                sendEvent {
                    AddMenuProduct.Event.Back
                }
            }

            is AddMenuProduct.Action.OnComboDescriptionTextChanged -> setState {
                copy(
                    comboDescription = action.comboDescription
                )
            }

            is AddMenuProduct.Action.OnDescriptionTextChanged -> setState {
                copy(
                    description = action.description
                )
            }

            is AddMenuProduct.Action.OnNameTextChanged -> setState {
                copy(
                    name = action.name
                )
            }

            is AddMenuProduct.Action.OnNewPriceTextChanged -> setState {
                copy(
                    newPrice = action.newPrice
                )
            }

            is AddMenuProduct.Action.OnNutritionTextChanged -> setState {
                copy(
                    nutrition = action.nutrition
                )
            }

            is AddMenuProduct.Action.OnOldPriceTextChanged -> setState {
                copy(
                    oldPrice = action.oldPrice
                )
            }

            is AddMenuProduct.Action.OnUtilsTextChanged -> setState {
                copy(
                    utils = action.utils
                )
            }

            AddMenuProduct.Action.OnCreateMenuProductClick -> addMenuProduct()
            AddMenuProduct.Action.OnShowCategoryListClick -> sendEvent {
                AddMenuProduct.Event.GoToCategoryList(
                    dataState.selectedCategoryList.map { selectableCategory ->
                        selectableCategory.category.uuid
                    }
                )
            }

            is AddMenuProduct.Action.OnRecommendationVisibleChangeClick -> {
                setState {
                    copy(
                        isVisibleInRecommendation = action.isVisible
                    )
                }
            }

            is AddMenuProduct.Action.OnVisibleInMenuChangeClick -> {
                setState {
                    copy(
                        isVisibleInMenu = action.isVisible
                    )
                }
            }

            AddMenuProduct.Action.OnClearPhotoClick -> setState {
                copy(
                    originalImageUri = null,
                    croppedImageUri = null,
                )
            }

            is AddMenuProduct.Action.SelectCategoryList -> setState {
                copy(
                    categoryList = categoryList.map { category ->
                        category.copy(
                            selected = action.categoryUuidList.contains(category.category.uuid)
                        )
                    }
                )
            }

            is AddMenuProduct.Action.SetImage -> setState {
                copy(
                    originalImageUri = action.originalImageUri,
                    croppedImageUri = action.croppedImageUri,
                )
            }

            AddMenuProduct.Action.SomethingWentWrong -> sendEvent {
                AddMenuProduct.Event.ShowSomethingWentWrong
            }
        }
    }

    private fun loadData() {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(categoryList = getSelectableCategoryListUseCase())
                }
            },
            onError = {
                // No handling
            }
        )
    }

    private fun addMenuProduct() {
        setState {
            copy(
                hasNameError = false,
                hasNewPriceError = false,
                hasOldPriceError = false,
                hasDescriptionError = false,
                hasImageError = false,
                hasCategoriesError = false,
                sendingToServer = true,
            )
        }
        viewModelScope.launchSafe(
            block = {
                createMenuProductUseCase(
                    params = state.value.run {
                        CreateMenuProductUseCase.Params(
                            name = name,
                            newPrice = newPrice,
                            oldPrice = oldPrice,
                            utils = utils,
                            nutrition = nutrition,
                            description = description,
                            comboDescription = comboDescription,
                            imageUri = croppedImageUri,
                            isVisible = isVisibleInMenu,
                            isRecommended = isVisibleInRecommendation,
                            categories = selectedCategoryList,
                        )
                    }
                )
                sendEvent {
                    AddMenuProduct.Event.AddedMenuProduct(menuProductName = state.value.name)
                }
            },
            onError = { throwable ->
                setState { copy(sendingToServer = false) }
                when (throwable) {
                    is MenuProductNameException -> {
                        setState {
                            copy(hasNameError = true)
                        }
                    }

                    is MenuProductNewPriceException -> {
                        setState {
                            copy(hasNewPriceError = true)
                        }
                    }

                    is MenuProductOldPriceException -> {
                        setState {
                            copy(hasOldPriceError = true)
                        }
                    }

                    is MenuProductDescriptionException -> {
                        setState {
                            copy(hasDescriptionError = true)
                        }
                    }

                    is MenuProductCategoriesException -> {
                        setState {
                            copy(hasCategoriesError = true)
                        }
                    }

                    is MenuProductImageException -> {
                        setState {
                            copy(hasImageError = true)
                        }
                    }

                    is MenuProductUploadingImageException -> {
                        sendEvent {
                            AddMenuProduct.Event.ShowImageUploadingFailed
                        }
                    }

                    else -> {
                        sendEvent {
                            AddMenuProduct.Event.ShowSomethingWentWrong
                        }
                    }
                }
            }
        )
    }
}
