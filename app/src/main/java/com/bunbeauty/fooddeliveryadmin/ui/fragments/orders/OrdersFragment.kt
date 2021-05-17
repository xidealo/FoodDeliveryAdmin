package com.bunbeauty.fooddeliveryadmin.ui.fragments.orders

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.bunbeauty.common.ExtendedState
import com.bunbeauty.common.State
import com.bunbeauty.common.extensions.launchWhenStarted
import com.bunbeauty.data.model.order.Order
import com.bunbeauty.domain.string_helper.IStringHelper
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentOrdersBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ViewModelComponent
import com.bunbeauty.fooddeliveryadmin.presentation.OrdersViewModel
import com.bunbeauty.fooddeliveryadmin.ui.adapter.OrderItem
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.bunbeauty.fooddeliveryadmin.ui.fragments.orders.OrdersFragmentDirections.toAddressListBottomSheet
import com.bunbeauty.fooddeliveryadmin.ui.fragments.orders.OrdersFragmentDirections.toChangeStatusDialog
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class OrdersFragment : BaseFragment<FragmentOrdersBinding, OrdersViewModel>() {

    override fun inject(viewModelComponent: ViewModelComponent) {
        viewModelComponent.inject(this)
    }

    @Inject
    lateinit var stringHelper: IStringHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemAdapter = ItemAdapter<OrderItem>()
        val fastAdapter = FastAdapter.with(itemAdapter)
        viewDataBinding.fragmentOrdersRvResult.adapter = fastAdapter
        fastAdapter.onClickListener = { _, _, orderItem, _ ->
            router.navigate(toChangeStatusDialog(orderItem))
            false
        }
        viewDataBinding.fragmentOrdersMcvAddress.setOnClickListener {
            router.navigate(toAddressListBottomSheet())
        }

        viewModel.cafeStateFlow.onEach { state ->
            when (state) {
                is State.Success -> {
                    viewDataBinding.fragmentOrdersTvAddress.text =
                        stringHelper.toString(state.data.address)
                }
                else -> {
                }
            }
        }.launchWhenStarted(lifecycleScope)

        viewModel.orderListState.onEach { state ->
            when (state) {
                is ExtendedState.AddedSuccess -> {
                    viewDataBinding.fragmentOrdersRvResult.smoothScrollToPosition(0)
                    itemAdapter.set(state.data)
                }
                is ExtendedState.UpdatedSuccess -> {
                    itemAdapter.set(state.data)
                }
                else -> {
                }
            }
        }.launchWhenStarted(lifecycleScope)

        viewModel.subscribeOnAddress()
        viewModel.subscribeOnOrders()
    }
}