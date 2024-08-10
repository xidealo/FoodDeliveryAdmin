package com.bunbeauty.presentation.feature.menulist.addmenuproduct

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.exception.updateproduct.MenuProductCategoriesException
import com.bunbeauty.domain.exception.updateproduct.MenuProductDescriptionException
import com.bunbeauty.domain.exception.updateproduct.MenuProductNameException
import com.bunbeauty.domain.exception.updateproduct.MenuProductNewPriceException
import com.bunbeauty.domain.exception.updateproduct.MenuProductOldPriceException
import com.bunbeauty.domain.exception.updateproduct.MenuProductPhotoLinkException
import com.bunbeauty.domain.feature.menu.addmenuproduct.AddMenuProductUseCase
import com.bunbeauty.domain.feature.menu.addmenuproduct.GetSelectableCategoryListUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddMenuProductViewModel @Inject constructor(
    private val addMenuProductUseCase: AddMenuProductUseCase,
    private val getSelectableCategoryListUseCase: GetSelectableCategoryListUseCase
) :
    BaseStateViewModel<AddMenuProduct.DataState, AddMenuProduct.Action, AddMenuProduct.Event>(
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
            isLoadingButton = false,
            isVisibleInMenu = true,
            isVisibleInRecommendation = false,
            hasError = null,
            categoryList = listOf(),
            photoLink = null,
            hasPhotoLinkError = false,
            hasCategoriesError = false
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

            AddMenuProduct.Action.OnAddPhotoClick -> sendEvent {
                AddMenuProduct.Event.GoToGallery
            }

            AddMenuProduct.Action.OnClearPhotoClick -> setState {
                copy(photoLink = null)
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

            is AddMenuProduct.Action.SelectPhoto -> setState {
                copy(
                    photoLink = action.selectedPhotoUrl
                )
            }
        }
    }

    private fun loadData() {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(
                        hasError = false,
                        categoryList = getSelectableCategoryListUseCase()
                    )
                }
            },
            onError = {
                setState {
                    copy(hasError = true)
                }
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
                hasPhotoLinkError = false,
                hasCategoriesError = false,
                hasError = false
            )
        }
        viewModelScope.launchSafe(
            block = {
                addMenuProductUseCase(
                    params =  state.value.run {
                        AddMenuProductUseCase.Params(
                            name = name,
                            newPrice = newPrice,
                            oldPrice = oldPrice,
                            utils = utils,
                            nutrition = nutrition,
                            description = description,
                            comboDescription = comboDescription,
                            photoLink = photoLink,
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
                setState {
                    when (throwable) {
                        is MenuProductNameException -> {
                            copy(hasNameError = true)
                        }

                        is MenuProductNewPriceException -> {
                            copy(hasNewPriceError = true)
                        }

                        is MenuProductOldPriceException -> {
                            copy(hasOldPriceError = true)
                        }

                        is MenuProductDescriptionException -> {
                            copy(hasDescriptionError = true)
                        }

                        is MenuProductCategoriesException -> {
                            copy(hasCategoriesError = true)
                        }

                        is MenuProductPhotoLinkException -> {
                            copy(hasPhotoLinkError = true)
                        }

                        else -> copy(hasError = true)
                    }
                }
            }
        )
    }
}
