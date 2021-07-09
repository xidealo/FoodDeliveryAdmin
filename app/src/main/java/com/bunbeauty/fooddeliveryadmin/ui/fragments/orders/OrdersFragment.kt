package com.bunbeauty.fooddeliveryadmin.ui.fragments.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentOrdersBinding
import com.bunbeauty.fooddeliveryadmin.extensions.invisible
import com.bunbeauty.fooddeliveryadmin.extensions.startedLaunch
import com.bunbeauty.fooddeliveryadmin.extensions.visible
import com.bunbeauty.fooddeliveryadmin.presentation.order.OrdersViewModel
import com.bunbeauty.fooddeliveryadmin.presentation.state.ExtendedState
import com.bunbeauty.fooddeliveryadmin.presentation.state.State
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.bunbeauty.fooddeliveryadmin.ui.items.OrderItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class OrdersFragment : BaseFragment<FragmentOrdersBinding>() {

    override val viewModel: OrdersViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemAdapter = ItemAdapter<OrderItem>()
        val fastAdapter = FastAdapter.with(itemAdapter)
        viewDataBinding.fragmentOrdersRvResult.adapter = fastAdapter
        fastAdapter.onClickListener = { _, _, orderItem, _ ->
            viewModel.goToOrderDetails(orderItem.order)
            false
        }
        viewDataBinding.fragmentOrdersMcvAddress.setOnClickListener {
            viewModel.goToAddressList()
        }

        viewModel.cafeAddressState.onEach { state ->
            when (state) {
                is State.Success -> {
                    viewDataBinding.fragmentOrdersMcvAddress.cardText = state.data
                }
                else -> Unit
            }
        }.startedLaunch(lifecycle)

        viewModel.orderListState.onEach { state ->
            when (state) {
                is ExtendedState.Loading -> {
                    viewDataBinding.fragmentOrdersLpiLoading.visible()
                }
                is ExtendedState.AddedSuccess -> {
                    viewDataBinding.fragmentOrdersLpiLoading.invisible()
                    viewDataBinding.fragmentOrdersRvResult.smoothScrollToPosition(0)
                    itemAdapter.set(state.data)
                }
                is ExtendedState.UpdatedSuccess -> {
                    viewDataBinding.fragmentOrdersLpiLoading.invisible()
                    itemAdapter.set(state.data)
                }
                else -> Unit
            }
        }.startedLaunch(lifecycle)
    }
}