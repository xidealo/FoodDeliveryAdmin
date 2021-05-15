package com.bunbeauty.fooddeliveryadmin.ui.fragments.orders

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.bunbeauty.common.State
import com.bunbeauty.common.extensions.launchWhenStarted
import com.bunbeauty.data.model.order.Order
import com.bunbeauty.domain.string_helper.IStringHelper
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentOrdersBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ViewModelComponent
import com.bunbeauty.fooddeliveryadmin.ui.adapter.OrdersAdapter
import com.bunbeauty.fooddeliveryadmin.presentation.OrdersViewModel
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.bunbeauty.fooddeliveryadmin.ui.fragments.orders.OrdersFragmentDirections.actionOrdersFragmentToChangeStatusDialog
import com.bunbeauty.fooddeliveryadmin.ui.fragments.orders.OrdersFragmentDirections.toAddressListBottomSheet
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class OrdersFragment : BaseFragment<FragmentOrdersBinding, OrdersViewModel>() {

    override fun inject(viewModelComponent: ViewModelComponent) {
        viewModelComponent.inject(this)
    }

    @Inject
    lateinit var ordersAdapter: OrdersAdapter

    @Inject
    lateinit var iStringHelper: IStringHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ordersAdapter.onItemClickListener = { order ->
            showChangeStatus(order)
        }

        viewDataBinding.fragmentOrdersRvResult.adapter = ordersAdapter
        viewDataBinding.fragmentOrdersMcvAddress.setOnClickListener {
            router.navigate(toAddressListBottomSheet())
        }

        viewModel.cafeStateFlow.onEach { state ->
            when (state) {
                is State.Loading -> {
                    //show loading
                }
                is State.Success -> {
                        viewDataBinding.fragmentOrdersTvAddress.text = iStringHelper.toString(state.data.address!!)
                        viewModel.getOrders(state.data.cafeEntity.id)
                }
            }

        }.launchWhenStarted(lifecycleScope)

        viewModel.addedOrderListStateFlow.onEach { state ->
            when (state) {
                is State.Loading -> {
                    //show loading
                }
                is State.Success -> {
                    viewDataBinding.fragmentOrdersRvResult.smoothScrollToPosition(0)
                    ordersAdapter.setItemList(state.data)
                }
                else -> {
                }
            }
        }.launchWhenStarted(lifecycleScope)

        viewModel.updatedOrderListStateFlow.onEach { state ->
            when (state) {
                is State.Loading -> {
                    //show loading
                }
                is State.Success -> {
                    ordersAdapter.setItemList(state.data)
                }
                else -> {

                }
            }
        }.launchWhenStarted(lifecycleScope)
        viewModel.getAddress()
    }

    fun showChangeStatus(order: Order) {
        router.navigate(actionOrdersFragmentToChangeStatusDialog(order))
    }
}