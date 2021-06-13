package com.bunbeauty.fooddeliveryadmin.ui.fragments.orders

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.bunbeauty.common.ExtendedState
import com.bunbeauty.common.State
import com.bunbeauty.common.extensions.launchWhenStarted
import com.bunbeauty.domain.string.IStringUtil
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentOrdersBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ActivityComponent
import com.bunbeauty.fooddeliveryadmin.presentation.OrdersViewModel
import com.bunbeauty.fooddeliveryadmin.ui.adapter.OrderItem
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.bunbeauty.fooddeliveryadmin.ui.fragments.orders.OrdersFragmentDirections.toAddressListBottomSheet
import com.bunbeauty.fooddeliveryadmin.ui.fragments.orders.OrdersFragmentDirections.toOrdersDetailsFragment
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class OrdersFragment : BaseFragment<FragmentOrdersBinding, OrdersViewModel>() {

    override fun inject(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    @Inject
    lateinit var stringUtil: IStringUtil

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemAdapter = ItemAdapter<OrderItem>()
        val fastAdapter = FastAdapter.with(itemAdapter)
        viewDataBinding.fragmentOrdersRvResult.adapter = fastAdapter
        fastAdapter.onClickListener = { _, _, orderItem, _ ->
            router.navigate(toOrdersDetailsFragment(orderItem.orderUI))
            false
        }
        viewDataBinding.fragmentOrdersMcvAddress.setOnClickListener {
            router.navigate(toAddressListBottomSheet())
        }

        viewModel.cafeAddressStateFlow.onEach { state ->
            when (state) {
                is State.Success -> {
                    viewDataBinding.fragmentOrdersMcvAddress.cardText = state.data
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