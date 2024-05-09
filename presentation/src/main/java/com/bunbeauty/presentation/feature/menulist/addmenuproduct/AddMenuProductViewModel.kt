package com.bunbeauty.presentation.feature.menulist.addmenuproduct

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.menu.addmenuproduct.GetCategoryListUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddMenuProductViewModel @Inject constructor(
    private val getCategoryListUseCase: GetCategoryListUseCase
) :
    BaseStateViewModel<AddMenuProduct.ViewDataState, AddMenuProduct.Action, AddMenuProduct.Event>(
        initState = AddMenuProduct.ViewDataState(
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
            throwable = null,
            selectableCategoryList = listOf()
        )
    ) {

    override fun reduce(action: AddMenuProduct.Action, dataState: AddMenuProduct.ViewDataState) {
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

            AddMenuProduct.Action.OnCreateMenuProductClick -> createMenuProduct()
            AddMenuProduct.Action.OnSelectCategoriesClick -> addEvent {
                AddMenuProduct.Event.OpenSelectCategoriesBottomSheet(dataState.selectableCategoryList)
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
                        selectableCategoryList = getCategoryListUseCase().map { category ->
                            AddMenuProduct.ViewDataState.SelectableCategory(
                                category = category,
                                selected = false
                            )
                        }
                    )
                }
            },
            onError = { throwable ->
                setState {
                    copy(throwable = throwable)
                }
            }
        )
    }

    private fun createMenuProduct() {
        setState {
            copy(
                hasNameError = false,
                hasNewPriceError = false,
                hasOldPriceError = false
            )
        }

        if (mutableDataState.value.name.isEmpty()) {
            setState {
                copy(
                    hasNameError = true
                )
            }
            return
        }

        val newPrice = mutableDataState.value.newPrice.toIntOrNull()

        if (newPrice == null) {
            setState {
                copy(
                    hasNewPriceError = true
                )
            }
            return
        }

        val oldPrice = mutableDataState.value.oldPrice.toIntOrNull()

        if (oldPrice != null && oldPrice <= newPrice) {
            setState {
                copy(
                    hasOldPriceError = true
                )
            }
            return
        }

        if (mutableDataState.value.description.isEmpty()) {
            setState {
                copy(
                    hasDescriptionError = true
                )
            }
            return
        }
    }

    fun goToProductCodeList() {
    }
}
