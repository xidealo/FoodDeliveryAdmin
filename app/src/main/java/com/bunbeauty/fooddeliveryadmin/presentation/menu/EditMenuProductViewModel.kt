package com.bunbeauty.fooddeliveryadmin.presentation.menu

import android.graphics.Bitmap
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.Constants.LIST_ARGS_KEY
import com.bunbeauty.common.Constants.PRODUCT_CODE_REQUEST_KEY
import com.bunbeauty.common.Constants.PRODUCT_COMBO_DESCRIPTION_ERROR_KEY
import com.bunbeauty.common.Constants.PRODUCT_COST_ERROR_KEY
import com.bunbeauty.common.Constants.PRODUCT_DISCOUNT_COST_ERROR_KEY
import com.bunbeauty.common.Constants.PRODUCT_NAME_ERROR_KEY
import com.bunbeauty.common.Constants.REQUEST_KEY_ARGS_KEY
import com.bunbeauty.common.Constants.SELECTED_KEY_ARGS_KEY
import com.bunbeauty.common.Constants.SELECTED_PRODUCT_CODE_KEY
import com.bunbeauty.common.Constants.TITLE_ARGS_KEY
import com.bunbeauty.domain.enums.ProductCode
import com.bunbeauty.domain.model.menu_product.MenuProduct
import com.bunbeauty.domain.repo.MenuProductRepo
import com.bunbeauty.domain.util.resources.IResourcesProvider
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.extensions.navArgs
import com.bunbeauty.fooddeliveryadmin.extensions.toByteArray
import com.bunbeauty.fooddeliveryadmin.presentation.BaseViewModel
import com.bunbeauty.fooddeliveryadmin.ui.ErrorEvent
import com.bunbeauty.fooddeliveryadmin.ui.fragments.menu.EditMenuProductFragmentArgs
import com.bunbeauty.fooddeliveryadmin.ui.items.list.MenuProductCode
import com.bunbeauty.fooddeliveryadmin.utils.IStringUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
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

    private val args: EditMenuProductFragmentArgs by savedStateHandle.navArgs()

    private val menuProductCodeList = ProductCode.values().map { productCode ->
        MenuProductCode(title = stringUtil.getProductCodeString(productCode))
    }.toTypedArray()

    private var isVisible = args.menuProduct.visible
    private val mutableVisibilityIcon = MutableStateFlow(
        resourcesProvider.getDrawable(
            if (isVisible) {
                R.drawable.ic_visible
            } else {
                R.drawable.ic_invisible
            }
        )
    )
    val visibilityIcon = mutableVisibilityIcon.asStateFlow()

    private val mutableIsComboDescriptionVisible =
        MutableStateFlow(args.menuProduct.productCode == ProductCode.COMBO)
    val isComboDescriptionVisible = mutableIsComboDescriptionVisible.asStateFlow()

    val photoLink: String = args.menuProduct.photoLink
    val name: String = args.menuProduct.name
    val productCodeString: String = stringUtil.getProductCodeString(args.menuProduct.productCode)
    val cost: String = args.menuProduct.cost.toString()
    val discountCost: String = args.menuProduct.discountCost?.toString() ?: ""
    val weight: String = args.menuProduct.weight?.toString() ?: ""
    val description: String = args.menuProduct.description
    val comboDescription: String = args.menuProduct.comboDescription ?: ""

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

    fun deleteMenuProduct() {
        viewModelScope.launch(IO) {
            menuProductRepo.deleteMenuProductPhoto(photoLink)
            menuProductRepo.deleteMenuProduct(args.menuProduct.uuid!!)
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
            viewModelScope.launch {
                mutableError.emit(
                    ErrorEvent.FieldError(
                        key = PRODUCT_NAME_ERROR_KEY,
                        message = resourcesProvider.getString(R.string.error_create_menu_product_empty_name)
                    )
                )
            }
            return
        }

        val costInt = cost.toIntOrNull()
        if (costInt == null) {
            viewModelScope.launch {
                mutableError.emit(
                    ErrorEvent.FieldError(
                        key = PRODUCT_COST_ERROR_KEY,
                        message = resourcesProvider.getString(R.string.error_create_menu_product_cost_incorrect)
                    )
                )
            }
            return
        }

        val discountCostInt = discountCost.toIntOrNull()
        if (discountCostInt != null && discountCostInt >= costInt) {
            viewModelScope.launch {
                mutableError.emit(
                    ErrorEvent.FieldError(
                        key = PRODUCT_DISCOUNT_COST_ERROR_KEY,
                        message = resourcesProvider.getString(R.string.error_create_menu_product_discount_cost_incorrect)
                    )
                )
            }
            return
        }

        val productCode = stringUtil.getProductCode(productCodeString)
        if (productCode == ProductCode.COMBO && comboDescription.isEmpty()) {
            viewModelScope.launch {
                mutableError.emit(
                    ErrorEvent.FieldError(
                        key = PRODUCT_COMBO_DESCRIPTION_ERROR_KEY,
                        message = resourcesProvider.getString(R.string.error_create_menu_product_combo_description_incorrect)
                    )
                )
            }
            return
        }

        val nullableComboDescription = if (productCode == ProductCode.COMBO) {
            comboDescription
        } else {
            null
        }
        if (photo == null) {
            val menuProduct = MenuProduct(
                uuid = args.menuProduct.uuid,
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
                visible = isVisible
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
                            uuid = args.menuProduct.uuid,
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
                            visible = isVisible
                        )
                        menuProductRepo.updateMenuProduct(menuProduct)
                        finishEditing(name)
                    }
                }
            }
        }
    }

    private suspend fun finishDeleting() {
        withContext(Dispatchers.Main) {
            mutableMessage.emit(
                name + resourcesProvider.getString(R.string.msg_product_deleted)
            )
            goBack()
        }
    }

    private suspend fun finishEditing(productName: String) {
        withContext(Dispatchers.Main) {
            mutableMessage.emit(
                productName + resourcesProvider.getString(R.string.msg_product_updated)
            )
            goBack()
        }
    }

    fun goToProductCodeList() {
        router.navigate(
            R.id.to_listBottomSheet,
            bundleOf(
                TITLE_ARGS_KEY to resourcesProvider.getString(R.string.msg_create_menu_product_select_category),
                LIST_ARGS_KEY to menuProductCodeList,
                SELECTED_KEY_ARGS_KEY to SELECTED_PRODUCT_CODE_KEY,
                REQUEST_KEY_ARGS_KEY to PRODUCT_CODE_REQUEST_KEY,
            )
        )
    }
}