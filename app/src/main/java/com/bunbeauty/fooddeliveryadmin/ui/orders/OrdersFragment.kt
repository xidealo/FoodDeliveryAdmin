package com.bunbeauty.fooddeliveryadmin.ui.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bunbeauty.common.State
import com.bunbeauty.data.enums.OrderStatus
import com.bunbeauty.data.enums.ProductCode
import com.bunbeauty.data.model.order.Order
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentOrdersBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ViewModelComponent
import com.bunbeauty.fooddeliveryadmin.ui.adapter.OrdersAdapter
import com.bunbeauty.fooddeliveryadmin.ui.base.BarsFragment
import com.bunbeauty.fooddeliveryadmin.ui.orders.OrdersFragmentDirections.actionOrdersFragmentToChangeStatusDialog
import com.bunbeauty.fooddeliveryadmin.ui.orders.OrdersFragmentDirections.toAddressListBottomSheet
import com.bunbeauty.presentation.view_model.OrdersViewModel
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class OrdersFragment : BarsFragment<FragmentOrdersBinding>() {

    override var layoutId = R.layout.fragment_orders
    override val viewModel: OrdersViewModel by viewModels { modelFactory }

    override fun inject(viewModelComponent: ViewModelComponent) {
        viewModelComponent.inject(this)
    }

    @Inject
    lateinit var ordersAdapter: OrdersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ordersAdapter.onItemClickListener = {
            showChangeStatus(it)
            //viewModel.removeOrder(it)
        }

        viewDataBinding.fragmentOrdersRvResult.adapter = ordersAdapter
        viewDataBinding.fragmentOrdersMcvAddress.setOnClickListener {
            findNavController().navigate(toAddressListBottomSheet())
        }

        subscribe(viewModel.cafeAddressLiveData) { cafeAddress ->
            viewDataBinding.fragmentOrdersTvAddress.text = cafeAddress
        }

        viewModel.addedOrderListStateFlow.onEach { state ->
            when (state) {
                is State.Loading -> {
                    //show loading
                }
                is State.Success -> {
                    viewDataBinding.fragmentOrdersRvResult.smoothScrollToPosition(0)
                    ordersAdapter.setItemList(state.data)
                }
                else ->{}
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
                else ->{}
            }
        }.launchWhenStarted(lifecycleScope)
    }

    fun showChangeStatus(order: Order) {
        findNavController().currentDestination?.getAction(
            actionOrdersFragmentToChangeStatusDialog(
                order
            ).actionId
        ) ?: return

        findNavController().navigate(actionOrdersFragmentToChangeStatusDialog(order))
    }
}