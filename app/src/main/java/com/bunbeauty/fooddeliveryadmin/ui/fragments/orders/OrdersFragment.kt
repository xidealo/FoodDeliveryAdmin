package com.bunbeauty.fooddeliveryadmin.ui.fragments.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.bunbeauty.common.Constants.CAFE_ADDRESS_REQUEST_KEY
import com.bunbeauty.common.Constants.SELECTED_CAFE_ADDRESS_KEY
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentOrdersBinding
import com.bunbeauty.fooddeliveryadmin.extensions.invisible
import com.bunbeauty.fooddeliveryadmin.extensions.startedLaunch
import com.bunbeauty.fooddeliveryadmin.extensions.visible
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.bunbeauty.fooddeliveryadmin.ui.items.OrderItem
import com.bunbeauty.presentation.model.list.CafeAddress
import com.bunbeauty.presentation.navigation_event.OrdersNavigationEvent
import com.bunbeauty.presentation.state.ExtendedState
import com.bunbeauty.presentation.state.State
import com.bunbeauty.presentation.view_model.order.OrdersViewModel
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
        binding.run {
            fragmentOrdersRvResult.adapter = fastAdapter
            fastAdapter.onClickListener = { _, _, orderItem, _ ->
                viewModel.goToOrderDetails(orderItem.orderItemModel)
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
                    is State.Success -> {
                        fragmentOrdersNcvAddress.cardText = state.data
                    }
                    is State.Empty -> {
                        fragmentOrdersNcvAddress.cardText =
                            resources.getString(R.string.title_order_details_choice_cafe)
                    }
                    else -> Unit
                }
            }.startedLaunch(viewLifecycleOwner)

            viewModel.orderListState.onEach { state ->
                when (state) {
                    is ExtendedState.Loading -> {
                        fragmentOrdersLpiLoading.visible()
                    }
                    is ExtendedState.AddedSuccess -> {
                        fragmentOrdersLpiLoading.invisible()
                        fragmentOrdersRvResult.smoothScrollToPosition(0)
                        val items = state.data.map { orderItemModel ->
                            OrderItem(orderItemModel)
                        }
                        itemAdapter.set(items)
                    }
                    is ExtendedState.UpdatedSuccess -> {
                        fragmentOrdersLpiLoading.invisible()
                        val items = state.data.map { orderItemModel ->
                            OrderItem(orderItemModel)
                        }
                        itemAdapter.set(items)
                    }
                    is ExtendedState.Empty -> {
                        fragmentOrdersLpiLoading.invisible()
                    }
                    else -> Unit
                }
            }.startedLaunch(viewLifecycleOwner)
        }

        viewModel.navigation.onEach { navigationEvent ->
            when (navigationEvent) {
                is OrdersNavigationEvent.ToOrderDetails ->
                    router.navigate(OrdersFragmentDirections.toOrdersDetailsFragment(navigationEvent.order))
                is OrdersNavigationEvent.ToCafeAddressList -> {
                    router.navigate(OrdersFragmentDirections.toListBottomSheet(navigationEvent.listData))
                }
                else -> Unit
            }
        }.startedLaunch(viewLifecycleOwner)
    }
}