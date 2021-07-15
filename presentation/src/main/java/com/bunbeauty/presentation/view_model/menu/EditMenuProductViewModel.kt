package com.bunbeauty.presentation.view_model.menu

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.Constants.MENU_PRODUCT_ARGS_KEY
import com.bunbeauty.common.Constants.PRODUCT_CODE_REQUEST_KEY
import com.bunbeauty.common.Constants.PRODUCT_COMBO_DESCRIPTION_ERROR_KEY
import com.bunbeauty.common.Constants.PRODUCT_COST_ERROR_KEY
import com.bunbeauty.common.Constants.PRODUCT_DISCOUNT_COST_ERROR_KEY
import com.bunbeauty.common.Constants.PRODUCT_NAME_ERROR_KEY
import com.bunbeauty.common.Constants.SELECTED_PRODUCT_CODE_KEY
import com.bunbeauty.domain.enums.ProductCode
import com.bunbeauty.domain.model.menu_product.MenuProduct
import com.bunbeauty.domain.repo.MenuProductRepo
import com.bunbeauty.presentation.utils.IResourcesProvider
import com.bunbeauty.presentation.extension.toByteArray
import com.bunbeauty.presentation.model.ListData
import com.bunbeauty.presentation.R
import com.bunbeauty.presentation.extension.navArg
import com.bunbeauty.presentation.model.list.MenuProductCode
import com.bunbeauty.presentation.navigation_event.EditMenuProductNavigationEvent
import com.bunbeauty.presentation.utils.IStringUtil
import com.bunbeauty.presentation.view_model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
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

    private val menuProduct: MenuProduct = savedStateHandle.navArg(MENU_PRODUCT_ARGS_KEY)!!

    private val menuProductCodeList = ProductCode.values().map { productCode ->
        MenuProductCode(title = stringUtil.getProductCodeString(productCode))
    }

    private val mutableIsVisible: MutableStateFlow<Boolean> = MutableStateFlow(menuProduct.visible)
    val isVisible: StateFlow<Boolean> = mutableIsVisible.asStateFlow()

    private val mutableIsComboDescriptionVisible =
        MutableStateFlow(menuProduct.productCode == ProductCode.COMBO)
    val isComboDescriptionVisible = mutableIsComboDescriptionVisible.asStateFlow()

    val photoLink: String = menuProduct.photoLink
    val name: String = menuProduct.name
    val productCodeString: String = stringUtil.getProductCodeString(menuProduct.productCode)
    val cost: String = menuProduct.cost.toString()
    val discountCost: String = menuProduct.discountCost?.toString() ?: ""
    val weight: String = menuProduct.weight?.toString() ?: ""
    val description: String = menuProduct.description
    val comboDescription: String = menuProduct.comboDescription ?: ""

    var photo: Bitmap? = null

    fun switchVisibility() {
        mutableIsVisible.value = !mutableIsVisible.value
    }

    fun setProductCode(productCode: MenuProductCode) {
        mutableIsComboDescriptionVisible.value =
            (productCode.title == stringUtil.getProductCodeString(ProductCode.COMBO))
    }

    fun deleteMenuProduct() {
        viewModelScope.launch(IO) {
            menuProductRepo.deleteMenuProductPhoto(photoLink)
            menuProductRepo.deleteMenuProduct(menuProduct.uuid!!)
            finishDeleting()
        }
    }

    fun saveMenuProduct(
        name: String,
        productCodeString: String,
        cost: String,
        discountCost: String,
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

        val costInt = cost.toIntOrNull()
        if (costInt == null) {
            sendFieldError(
                PRODUCT_COST_ERROR_KEY,
                resourcesProvider.getString(R.string.error_edit_menu_product_cost_incorrect)
            )
            return
        }

        val discountCostInt = discountCost.toIntOrNull()
        if (discountCostInt != null && discountCostInt >= costInt) {
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
            viewModelScope.launch(IO) {
                menuProductRepo.updateMenuProduct(menuProduct)
                finishEditing(name)
            }
        } else {
            viewModelScope.launch(Default) {
                val photoByteArray = photo!!.toByteArray()
                withContext(IO) {
                    menuProductRepo.deleteMenuProductPhoto(photoLink)
                    menuProductRepo.saveMenuProductPhoto(photoByteArray).collect { photoLink ->
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
                    }
                }
            }
        }
    }

    private fun finishDeleting() {
        sendMessage(name + resourcesProvider.getString(R.string.msg_product_deleted))
        goBack()
    }

    private fun finishEditing(productName: String) {
        sendMessage(productName + resourcesProvider.getString(R.string.msg_product_updated))
        goBack()
    }

    fun goToProductCodeList() {
        val listData = ListData(
            title = resourcesProvider.getString(R.string.title_edit_menu_product_select_category),
            listItem = menuProductCodeList,
            requestKey = PRODUCT_CODE_REQUEST_KEY,
            selectedKey = SELECTED_PRODUCT_CODE_KEY
        )
        goTo(EditMenuProductNavigationEvent.ToProductCodeList(listData))
    }
}