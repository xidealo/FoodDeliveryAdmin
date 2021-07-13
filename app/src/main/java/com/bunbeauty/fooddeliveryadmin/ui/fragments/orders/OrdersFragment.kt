package com.bunbeauty.fooddeliveryadmin.ui.fragments.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.bunbeauty.common.Constants.CAFE_ADDRESS_REQUEST_KEY
import com.bunbeauty.common.Constants.SELECTED_CAFE_ADDRESS_KEY
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentOrdersBinding
import com.bunbeauty.fooddeliveryadmin.extensions.invisible
import com.bunbeauty.fooddeliveryadmin.extensions.visible
import com.bunbeauty.presentation.view_model.state.ExtendedState
import com.bunbeauty.presentation.view_model.state.State
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.bunbeauty.fooddeliveryadmin.ui.items.OrderItem
import com.bunbeauty.presentation.list.CafeAddress
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersFragment : BaseFragment<FragmentOrdersBinding>() {

    override val viewModel: com.bunbeauty.presentation.view_model.order.OrdersViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemAdapter = ItemAdapter<OrderItem>()
        val fastAdapter = FastAdapter.with(itemAdapter)

        with(binding) {
            fragmentOrdersRvResult.adapter = fastAdapter
            fastAdapter.onClickListener = { _, _, orderItem, _ ->
                viewModel.goToOrderDetails(orderItem.order)
                false
            }
            setFragmentResultListener(CAFE_ADDRESS_REQUEST_KEY) { _, bundle ->
                bundle.getParcelable<CafeAddress>(SELECTED_CAFE_ADDRESS_KEY)?.let { cafeAddress ->
                    viewModel.saveSelectedCafeAddress(cafeAddress)
                }
            }
            fragmentOrdersNcvAddress.setOnClickListener {
                viewModel.goToAddressList()
            }
            viewModel.addressState.onEach { state ->
                when (state) {
                    is com.bunbeauty.presentation.view_model.state.State.Success -> {
                        fragmentOrdersNcvAddress.cardText = state.data
                    }
                    else -> Unit
                }
            }.startedLaunch(viewLifecycleOwner)

            viewModel.orderListState.onEach { state ->
                when (state) {
                    is com.bunbeauty.presentation.view_model.state.ExtendedState.Loading -> {
                        fragmentOrdersLpiLoading.visible()
                    }
                    is com.bunbeauty.presentation.view_model.state.ExtendedState.AddedSuccess -> {
                        fragmentOrdersLpiLoading.invisible()
                        fragmentOrdersRvResult.smoothScrollToPosition(0)
                        itemAdapter.set(state.data)
                    }
                    is com.bunbeauty.presentation.view_model.state.ExtendedState.UpdatedSuccess -> {
                        fragmentOrdersLpiLoading.invisible()
                        itemAdapter.set(state.data)
                    }
                    else -> Unit
                }
            }.startedLaunch(viewLifecycleOwner)
        }
    }
}