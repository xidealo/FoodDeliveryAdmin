package com.bunbeauty.fooddeliveryadmin.ui.orders

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.BR
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.data.model.order.Order
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentOrdersBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ViewModelComponent
import com.bunbeauty.fooddeliveryadmin.ui.adapter.OrdersAdapter
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.bunbeauty.fooddeliveryadmin.ui.orders.OrdersFragmentDirections.actionOrdersFragmentToChangeStatusDialog
import com.bunbeauty.fooddeliveryadmin.ui.orders.OrdersFragmentDirections.toAddressListBottomSheet
import com.bunbeauty.fooddeliveryadmin.view_model.OrdersViewModel
import java.lang.ref.WeakReference
import javax.inject.Inject


class OrdersFragment : BaseFragment<FragmentOrdersBinding, OrdersViewModel>(), OrdersNavigator {

    override var viewModelVariable: Int = BR.viewModel
    override var layoutId: Int = R.layout.fragment_orders
    override var viewModelClass = OrdersViewModel::class.java
    override fun inject(viewModelComponent: ViewModelComponent) {
        viewModelComponent.inject(this)
    }

    @Inject
    lateinit var ordersAdapter: OrdersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ordersAdapter.ordersNavigator = WeakReference(this)
        viewDataBinding.fragmentOrdersRvResult.adapter = ordersAdapter
        viewDataBinding.fragmentOrdersMcvAddress.setOnClickListener {
            findNavController().navigate(toAddressListBottomSheet())
        }

        viewModel.cafeAddressLiveData.observe(viewLifecycleOwner) { cafeAddress ->
            viewDataBinding.fragmentOrdersTvAddress.text = cafeAddress
        }
        viewModel.addedOrderListLiveData.observe(viewLifecycleOwner) { orderList ->
            viewDataBinding.fragmentOrdersRvResult.smoothScrollToPosition(0)
            ordersAdapter.setItemList(orderList)
        }
        viewModel.updatedOrderListLiveData.observe(viewLifecycleOwner) { orderList ->
            ordersAdapter.setItemList(orderList)
        }
    }

    override fun showChangeStatus(order: Order) {
        findNavController().currentDestination?.getAction(
            actionOrdersFragmentToChangeStatusDialog(
                order
            ).actionId
        ) ?: return

        findNavController().navigate(actionOrdersFragmentToChangeStatusDialog(order))
    }
}