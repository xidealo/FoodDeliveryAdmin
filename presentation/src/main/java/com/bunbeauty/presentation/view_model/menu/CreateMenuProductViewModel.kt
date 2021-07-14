package com.bunbeauty.presentation.view_model.menu

import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.Constants.PRODUCT_CODE_REQUEST_KEY
import com.bunbeauty.common.Constants.PRODUCT_COMBO_DESCRIPTION_ERROR_KEY
import com.bunbeauty.common.Constants.PRODUCT_COST_ERROR_KEY
import com.bunbeauty.common.Constants.PRODUCT_DISCOUNT_COST_ERROR_KEY
import com.bunbeauty.common.Constants.PRODUCT_NAME_ERROR_KEY
import com.bunbeauty.common.Constants.SELECTED_PRODUCT_CODE_KEY
import com.bunbeauty.domain.enums.ProductCode
import com.bunbeauty.domain.model.menu_product.MenuProduct
import com.bunbeauty.domain.repo.MenuProductRepo
import com.bunbeauty.domain.util.resources.IResourcesProvider
import com.bunbeauty.presentation.extension.toByteArray
import com.bunbeauty.presentation.model.ListData
import com.bunbeauty.presentation.R
import com.bunbeauty.presentation.list.MenuProductCode
import com.bunbeauty.presentation.navigation_event.CreateMenuProductNavigationEvent
import com.bunbeauty.presentation.utils.IStringUtil
import com.bunbeauty.presentation.view_model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateMenuProductViewModel @Inject constructor(
    private val stringUtil: IStringUtil,
    private val resourcesProvider: IResourcesProvider,
    private val menuProductRepo: MenuProductRepo,
) : BaseViewModel() {

    private val menuProductCodeList = ProductCode.values().map { productCode ->
        MenuProductCode(title = stringUtil.getProductCodeString(productCode))
    }

    private var isVisible = true
    private val mutableVisibilityIcon =
        MutableStateFlow(resourcesProvider.getDrawable(R.drawable.ic_visible))
    val visibilityIcon = mutableVisibilityIcon.asStateFlow()

    private val mutableIsComboDescriptionVisible = MutableStateFlow(false)
    val isComboDescriptionVisible = mutableIsComboDescriptionVisible.asStateFlow()

    var photo: Bitmap? = null

    fun switchVisibility() {
        isVisible = !isVisible
        if (isVisible) {
            mutableVisibilityIcon.value = resourcesProvider.getDrawable(R.drawable.ic_visible)
        } else {
            mutableVisibilityIcon.value = resourcesProvider.getDrawable(R.drawable.ic_invisible)
        }
    }

    fun setProductCode(productCode: MenuProductCode) {
        mutableIsComboDescriptionVisible.value =
            (productCode.title == stringUtil.getProductCodeString(ProductCode.COMBO))
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
        if (photo == null) {
            sendError(resourcesProvider.getString(R.string.error_create_menu_product_image_not_loaded))
            return
        }

        if (name.isEmpty()) {
            sendFieldError(
                PRODUCT_NAME_ERROR_KEY,
                resourcesProvider.getString(R.string.error_create_menu_product_empty_name)
            )
            return
        }

        val productCode = stringUtil.getProductCode(productCodeString)
        if (productCode == null) {
            sendError(resourcesProvider.getString(R.string.error_create_menu_product_category_not_selected))
            return
        }

        val costInt = cost.toIntOrNull()
        if (costInt == null) {
            sendFieldError(
                PRODUCT_COST_ERROR_KEY,
                resourcesProvider.getString(R.string.error_create_menu_product_cost_incorrect)
            )
            return
        }

        val discountCostInt = discountCost.toIntOrNull()
        if (discountCostInt != null && discountCostInt >= costInt) {
            sendFieldError(
                PRODUCT_DISCOUNT_COST_ERROR_KEY,
                resourcesProvider.getString(R.string.error_create_menu_product_discount_cost_incorrect)
            )
            return
        }

        if (productCode == ProductCode.COMBO && comboDescription.isEmpty()) {
            sendFieldError(
                PRODUCT_COMBO_DESCRIPTION_ERROR_KEY,
                resourcesProvider.getString(R.string.error_create_menu_product_combo_description_incorrect)
            )
            return
        }

        val nullableComboDescription = if (productCode == ProductCode.COMBO) {
            comboDescription
        } else {
            null
        }
        viewModelScope.launch(IO) {
            val photoByteArray = withContext(Default) {
                photo!!.toByteArray()
            }
            menuProductRepo.saveMenuProductPhoto(photoByteArray).collect { photoLink ->
                val uuid = UUID.randomUUID().toString()
                val menuProduct = MenuProduct(
                    uuid = uuid,
                    name = name,
                    cost = costInt,
                    discountCost = discountCostInt,
                    weight = weight.toIntOrNull(),
                    description = description,
                    comboDescription = nullableComboDescription,
                    photoLink = photoLink,
                    onFire = false,
                    inOven = false,
                    productCode = productCode,
                    barcode = null,
                    visible = isVisible
                )
                menuProductRepo.saveMenuProduct(menuProduct)
                finishCreation(name)
            }
        }
    }

    private fun finishCreation(productName: String) {
        sendMessage(productName + resourcesProvider.getString(R.string.msg_product_created))
        goBack()
    }

    fun goToProductCodeList() {
        val listData = ListData(
            title = resourcesProvider.getString(R.string.title_create_menu_product_select_category),
            listItem = menuProductCodeList,
            requestKey = PRODUCT_CODE_REQUEST_KEY,
            selectedKey = SELECTED_PRODUCT_CODE_KEY
        )
        goTo(CreateMenuProductNavigationEvent.ToProductCodeList(listData))
    }
}