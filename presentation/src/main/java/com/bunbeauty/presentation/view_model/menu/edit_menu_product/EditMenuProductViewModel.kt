package com.bunbeauty.presentation.view_model.menu.edit_menu_product

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.Constants.MENU_PRODUCT_ARGS_KEY
import com.bunbeauty.common.Constants.PRODUCT_COMBO_DESCRIPTION_ERROR_KEY
import com.bunbeauty.common.Constants.PRODUCT_COST_ERROR_KEY
import com.bunbeauty.common.Constants.PRODUCT_DISCOUNT_COST_ERROR_KEY
import com.bunbeauty.common.Constants.PRODUCT_NAME_ERROR_KEY
import com.bunbeauty.domain.enums.ProductCode
import com.bunbeauty.domain.model.menu_product.MenuProduct
import com.bunbeauty.domain.repo.MenuProductRepo
import com.bunbeauty.presentation.utils.IResourcesProvider
import com.bunbeauty.presentation.extension.toByteArray
import com.bunbeauty.presentation.R
import com.bunbeauty.presentation.extension.mapToStateFlow
import com.bunbeauty.presentation.extension.navArg
import com.bunbeauty.presentation.model.list.MenuProductCode
import com.bunbeauty.presentation.utils.IStringUtil
import com.bunbeauty.presentation.view_model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditMenuProductViewModel @Inject constructor(
    private val resourcesProvider: IResourcesProvider,
    private val stringUtil: IStringUtil,
    private val menuProductRepo: MenuProductRepo,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val menuProduct: MenuProduct by savedStateHandle.navArg(MENU_PRODUCT_ARGS_KEY)

    private val menuProductCodeList = ProductCode.values().map { productCode ->
        MenuProductCode(title = stringUtil.getProductCodeString(productCode))
    }

    val photoLink: String = menuProduct.photoLink
    val name: String = menuProduct.name

    var photo: Bitmap? = null


    private val mutableState = MutableStateFlow(EditMenuProductDataState())
    val editMenuProductUiState = mutableState.mapToStateFlow(viewModelScope) { state ->
        mapState(state)
    }

    fun setProductCode(productCode: MenuProductCode) {

    }

    fun deleteMenuProduct() {
        viewModelScope.launch(IO) {
            menuProductRepo.deleteMenuProductPhoto(photoLink)
            menuProductRepo.deleteMenuProduct(menuProduct.uuid)
            finishDeleting()
        }
    }

    fun saveMenuProduct(
        name: String,
        productCodeString: String,
        newPrice: String,
        oldPrice: String,
        weight: String,
        description: String,
        comboDescription: String
    ) {
        if (name.isEmpty()) {
            sendFieldError(
                PRODUCT_NAME_ERROR_KEY,
                resourcesProvider.getString(R.string.error_edit_menu_product_empty_name)
            )
            return
        }

        val costInt = newPrice.toIntOrNull()
        if (costInt == null) {
            sendFieldError(
                PRODUCT_COST_ERROR_KEY,
                resourcesProvider.getString(R.string.error_edit_menu_product_cost_incorrect)
            )
            return
        }

        val oldPriceInt = oldPrice.toIntOrNull()
        if (oldPriceInt != null && oldPriceInt <= costInt) {
            sendFieldError(
                PRODUCT_DISCOUNT_COST_ERROR_KEY,
                resourcesProvider.getString(R.string.error_edit_menu_product_discount_cost_incorrect)
            )
            return
        }

        val productCode = stringUtil.getProductCode(productCodeString)
        if (productCode == ProductCode.COMBO && comboDescription.isEmpty()) {
            sendFieldError(
                PRODUCT_COMBO_DESCRIPTION_ERROR_KEY,
                resourcesProvider.getString(R.string.error_edit_menu_product_combo_description_incorrect)
            )
            return
        }

        val nullableComboDescription = if (productCode == ProductCode.COMBO) {
            comboDescription
        } else {
            null
        }

        if (photo == null) {
            val menuProduct = MenuProduct(
                uuid = menuProduct.uuid,
                name = name,
                newPrice = costInt,
                oldPrice = oldPriceInt,
                nutrition = weight.toIntOrNull(),
                description = description,
                comboDescription = nullableComboDescription,
                photoLink = photoLink,
                barcode = null,
                isVisible = false,
                utils = "",
                categories = emptyList()
            )
            viewModelScope.launch(IO) {
                //menuProductRepo.updateMenuProduct(menuProduct)
                finishEditing(name)
            }
        } else {
            viewModelScope.launch(Default) {
                val photoByteArray = photo!!.toByteArray()
                withContext(IO) {
                    menuProductRepo.deleteMenuProductPhoto(photoLink)
                    menuProductRepo.saveMenuProductPhoto(photoByteArray)
                    /*   .collect { photoLink ->
                       val menuProduct = MenuProduct(
                           uuid = menuProduct.uuid,
                           name = name,
                           cost = costInt,
                           discountCost = discountCostInt,
                           weight = weight.toIntOrNull(),
                           description = description,
                           comboDescription = nullableComboDescription,
                           photoLink = photoLink,
                           onFire = false,
                           inOven = false,
                           productCode = productCode!!,
                           barcode = null,
                           visible = isVisible.value
                       )
                       menuProductRepo.updateMenuProduct(menuProduct)
                       finishEditing(name)
                   }*/
                }
            }
        }
    }

    private fun finishDeleting() {
        sendMessage(name + resourcesProvider.getString(R.string.msg_product_deleted))
    }

    private fun finishEditing(productName: String) {
        sendMessage(productName + resourcesProvider.getString(R.string.msg_product_updated))
    }


    private fun mapState(dataState: EditMenuProductDataState): EditMenuProductUIState {
        return EditMenuProductUIState(
            editMenuProductState = when (dataState.state) {
                EditMenuProductDataState.State.SUCCESS -> {
                    EditMenuProductUIState.EditMenuProductState.Success()
                }
                EditMenuProductDataState.State.LOADING -> EditMenuProductUIState.EditMenuProductState.Loading
                EditMenuProductDataState.State.ERROR -> EditMenuProductUIState.EditMenuProductState.Error
            },
            eventList = dataState.eventList
        )
    }

}