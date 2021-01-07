package com.bunbeauty.fooddeliveryadmin.ui.orders

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.BR
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.data.model.order.OrderWithCartProducts
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentOrdersBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ViewModelComponent
import com.bunbeauty.fooddeliveryadmin.ui.adapter.OrdersAdapter
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.bunbeauty.fooddeliveryadmin.ui.main.MainActivity
import com.bunbeauty.fooddeliveryadmin.ui.orders.OrdersFragmentDirections.actionOrdersFragmentToChangeStatusDialog
import com.bunbeauty.fooddeliveryadmin.view_model.OrdersViewModel
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

        ordersAdapter.ordersNavigator = this
        viewDataBinding.fragmentOrdersRvResult.adapter = ordersAdapter
        viewModel.iApiRepository.getOrderWithCartProducts().observe(viewLifecycleOwner) {
            ordersAdapter.addOrReplaceItem(it)
            viewDataBinding.fragmentOrdersRvResult.smoothScrollToPosition(0)
            //(activity as MainActivity).createNotification()
        }
    }

    override fun showChangeStatus(orderWithCartProducts: OrderWithCartProducts) {
        findNavController().currentDestination?.getAction(
            actionOrdersFragmentToChangeStatusDialog(
                orderWithCartProducts.order
            ).actionId
        ) ?: return

        findNavController().navigate(actionOrdersFragmentToChangeStatusDialog(orderWithCartProducts.order))
    }



}