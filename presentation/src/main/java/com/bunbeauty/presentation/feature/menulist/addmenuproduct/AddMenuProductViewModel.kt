package com.bunbeauty.presentation.feature.menulist.addmenuproduct

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.exception.updateproduct.MenuProductCategoriesException
import com.bunbeauty.domain.exception.updateproduct.MenuProductDescriptionException
import com.bunbeauty.domain.exception.updateproduct.MenuProductNameException
import com.bunbeauty.domain.exception.updateproduct.MenuProductNewPriceException
import com.bunbeauty.domain.exception.updateproduct.MenuProductPhotoLinkException
import com.bunbeauty.domain.feature.menu.addmenuproduct.AddMenuProductUseCase
import com.bunbeauty.domain.model.menuproduct.MenuProductPost
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddMenuProductViewModel @Inject constructor(
    private val addMenuProductUseCase: AddMenuProductUseCase
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
            selectableCategoryList = listOf(),
            photoLink = "",
            hasPhotoLinkError = false,
            hasCategoriesError = false,
        )
    ) {

    override fun reduce(action: AddMenuProduct.Action, dataState: AddMenuProduct.DataState) {
        when (action) {
            AddMenuProduct.Action.Init -> loadData()

            AddMenuProduct.Action.OnBackClick -> {
                addEvent {
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
            AddMenuProduct.Action.OnShowCategoryListClick -> addEvent {
                AddMenuProduct.Event.GoToCategoryList(
                    dataState.getSelectedCategory()
                        .map { selectableCategory -> selectableCategory.category.uuid }
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

            AddMenuProduct.Action.OnAddPhotoClick -> addEvent {
                AddMenuProduct.Event.GoToGallery
            }
        }
    }

    private fun loadData() {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(
                        hasError = false
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
                hasPhotoLinkError = false,
                hasCategoriesError = false,
                hasError = false
            )
        }
        viewModelScope.launchSafe(
            block = {
                addMenuProductUseCase(
                    menuProductPost = state.value.run {
                        MenuProductPost(
                            name = name,
                            newPrice = newPrice.toIntOrNull() ?: 0,
                            oldPrice = oldPrice.toIntOrNull(),
                            utils = utils,
                            nutrition = nutrition.toIntOrNull(),
                            description = description,
                            comboDescription = comboDescription,
                            photoLink = "",
                            barcode = 0,
                            isVisible = isVisibleInMenu,
                            categories = selectableCategoryList
                                .filter { selectableCategory -> selectableCategory.selected }
                                .map { selectableCategory ->
                                    selectableCategory.category.uuid
                                },
                            isRecommended = isVisibleInRecommendation
                        )
                    }
                )
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

                        is MenuProductDescriptionException -> {
                            copy(hasDescriptionError = true)
                        }

                        is MenuProductPhotoLinkException -> {
                            copy(hasPhotoLinkError = true)
                        }

                        is MenuProductCategoriesException -> {
                            copy(hasCategoriesError = true)
                        }

                        else -> copy(hasError = true)
                    }
                }
            }
        )
    }
}
