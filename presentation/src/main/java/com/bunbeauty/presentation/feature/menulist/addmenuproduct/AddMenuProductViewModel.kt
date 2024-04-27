package com.bunbeauty.presentation.feature.menulist.addmenuproduct

import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddMenuProductViewModel @Inject constructor(

) : BaseStateViewModel<AddMenuProduct.ViewDataState, AddMenuProduct.Action, AddMenuProduct.Event>(
    initState = AddMenuProduct.ViewDataState(
        name = "",
        hasNameError = false,
        description = "",
        hasDescriptionError = false,
        newPrice = "",
        hasNewPriceError = false,
        oldPrice = "",
        nutrition = "",
        utils = "",
        comboDescription = "",
        isLoadingButton = false,
        isVisible = false,
        throwable = null
    )
) {

    override fun reduce(action: AddMenuProduct.Action, dataState: AddMenuProduct.ViewDataState) {
        when (action) {
            AddMenuProduct.Action.Init -> {

            }

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

        }
    }

    fun createMenuProduct(
        name: String,
        productCodeString: String,
        cost: String,
        discountCost: String,
        weight: String,
        description: String,
        comboDescription: String
    ) {

        if (name.isEmpty()) {
            /*  sendFieldError(
                  PRODUCT_NAME_ERROR_KEY,
                  resourcesProvider.getString(R.string.error_create_menu_product_empty_name)
              )*/
            return
        }


        val costInt = cost.toIntOrNull()
        if (costInt == null) {
            /* sendFieldError(
                 PRODUCT_COST_ERROR_KEY,
                 resourcesProvider.getString(R.string.error_create_menu_product_cost_incorrect)
             )*/
            return
        }

        val discountCostInt = discountCost.toIntOrNull()
        if (discountCostInt != null && discountCostInt >= costInt) {
            /*     sendFieldError(
                     PRODUCT_DISCOUNT_COST_ERROR_KEY,
                     resourcesProvider.getString(R.string.error_create_menu_product_discount_cost_incorrect)
                 )*/
            return
        }

    }

    fun goToProductCodeList() {

    }

}
