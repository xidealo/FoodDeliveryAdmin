package com.bunbeauty.fooddeliveryadmin.ui.address_list

import android.os.Bundle
import android.view.View
import com.bunbeauty.fooddeliveryadmin.BR
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.data.model.AddressItem
import com.bunbeauty.fooddeliveryadmin.databinding.BottomSheetAddressListBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ViewModelComponent
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseBottomSheetDialog
import com.bunbeauty.fooddeliveryadmin.utils.string.IStringHelper
import com.bunbeauty.fooddeliveryadmin.view_model.AddressListViewModel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import java.lang.ref.WeakReference
import javax.inject.Inject

class AddressListBottomSheet : BaseBottomSheetDialog<BottomSheetAddressListBinding, AddressListViewModel>(), AddressListNavigator  {

    override var layoutId = R.layout.bottom_sheet_address_list
    override var viewModelVariable = BR.viewModel

    @Inject
    lateinit var stringHelper: IStringHelper

    override fun inject(viewModelComponent: ViewModelComponent) {
        viewModelComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.navigator = WeakReference(this)

        val itemAdapter = ItemAdapter<AddressItem>()
        val fastAdapter = FastAdapter.with(itemAdapter)
        viewDataBinding.bottomSheetAddressListRvList.adapter = fastAdapter
        fastAdapter.onClickListener = { _, _, addressItem, _ ->
            viewModel.setCafe(addressItem.address)
            dismiss()
            false
        }
        viewModel.cafeAddressListLiveData.observe(viewLifecycleOwner) { addressList ->
            val addressItemList = addressList.map { AddressItem(it, stringHelper) }
            itemAdapter.add(addressItemList)
        }
    }
}