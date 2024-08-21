package com.bunbeauty.presentation.feature.menulist.addmenuproduct

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.exception.updateproduct.MenuProductCategoriesException
import com.bunbeauty.domain.exception.updateproduct.MenuProductDescriptionException
import com.bunbeauty.domain.exception.updateproduct.MenuProductImageException
import com.bunbeauty.domain.exception.updateproduct.MenuProductNameException
import com.bunbeauty.domain.exception.updateproduct.MenuProductNewPriceException
import com.bunbeauty.domain.exception.updateproduct.MenuProductOldPriceException
import com.bunbeauty.domain.exception.updateproduct.MenuProductUploadingImageException
import com.bunbeauty.domain.feature.menu.addmenuproduct.CreateMenuProductUseCase
import com.bunbeauty.domain.feature.menu.addmenuproduct.GetSelectableCategoryListUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateMenuProductViewModel @Inject constructor(
    private val createMenuProductUseCase: CreateMenuProductUseCase,
    private val getSelectableCategoryListUseCase: GetSelectableCategoryListUseCase
) : BaseStateViewModel<CreateMenuProduct.DataState, CreateMenuProduct.Action, CreateMenuProduct.Event>(
    initState = CreateMenuProduct.DataState(
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
        sendingToServer = false
    )
) {

    override fun reduce(action: CreateMenuProduct.Action, dataState: CreateMenuProduct.DataState) {
        when (action) {
            CreateMenuProduct.Action.Init -> loadData()

            CreateMenuProduct.Action.OnBackClick -> {
                sendEvent {
                    CreateMenuProduct.Event.Back
                }
            }

            is CreateMenuProduct.Action.OnComboDescriptionTextChanged -> setState {
                copy(
                    comboDescription = action.comboDescription
                )
            }

            is CreateMenuProduct.Action.OnDescriptionTextChanged -> setState {
                copy(
                    description = action.description
                )
            }

            is CreateMenuProduct.Action.OnNameTextChanged -> setState {
                copy(
                    name = action.name
                )
            }

            is CreateMenuProduct.Action.OnNewPriceTextChanged -> setState {
                copy(
                    newPrice = action.newPrice
                )
            }

            is CreateMenuProduct.Action.OnNutritionTextChanged -> setState {
                copy(
                    nutrition = action.nutrition
                )
            }

            is CreateMenuProduct.Action.OnOldPriceTextChanged -> setState {
                copy(
                    oldPrice = action.oldPrice
                )
            }

            is CreateMenuProduct.Action.OnUtilsTextChanged -> setState {
                copy(
                    utils = action.utils
                )
            }

            CreateMenuProduct.Action.OnCreateMenuProductClick -> addMenuProduct()
            CreateMenuProduct.Action.OnShowCategoryListClick -> sendEvent {
                CreateMenuProduct.Event.GoToCategoryList(
                    dataState.selectedCategoryList.map { selectableCategory ->
                        selectableCategory.category.uuid
                    }
                )
            }

            is CreateMenuProduct.Action.OnRecommendationVisibleChangeClick -> {
                setState {
                    copy(
                        isVisibleInRecommendation = action.isVisible
                    )
                }
            }

            is CreateMenuProduct.Action.OnVisibleInMenuChangeClick -> {
                setState {
                    copy(
                        isVisibleInMenu = action.isVisible
                    )
                }
            }

            CreateMenuProduct.Action.OnClearPhotoClick -> setState {
                copy(
                    originalImageUri = null,
                    croppedImageUri = null
                )
            }

            is CreateMenuProduct.Action.SelectCategoryList -> setState {
                copy(
                    categoryList = categoryList.map { category ->
                        category.copy(
                            selected = action.categoryUuidList.contains(category.category.uuid)
                        )
                    }
                )
            }

            is CreateMenuProduct.Action.SetImage -> setState {
                copy(
                    originalImageUri = action.originalImageUri,
                    croppedImageUri = action.croppedImageUri
                )
            }

            CreateMenuProduct.Action.SomethingWentWrong -> sendEvent {
                CreateMenuProduct.Event.ShowSomethingWentWrong
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
                sendingToServer = true
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
                            categories = selectedCategoryList
                        )
                    }
                )
                sendEvent {
                    CreateMenuProduct.Event.AddedMenuProduct(menuProductName = state.value.name)
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
        )
    }
}
