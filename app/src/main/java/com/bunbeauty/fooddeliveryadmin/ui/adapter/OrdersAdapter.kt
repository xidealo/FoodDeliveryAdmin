package com.bunbeauty.fooddeliveryadmin.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bunbeauty.fooddeliveryadmin.data.model.order.OrderWithCartProducts
import com.bunbeauty.fooddeliveryadmin.databinding.ElementOrderBinding
import com.bunbeauty.fooddeliveryadmin.ui.orders.OrdersNavigator
import javax.inject.Inject

class OrdersAdapter @Inject constructor() :
    BaseAdapter<OrdersAdapter.OrderViewHolder, OrderWithCartProducts>() {

    lateinit var ordersNavigator: OrdersNavigator

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): OrderViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ElementOrderBinding.inflate(inflater, viewGroup, false)

        return OrderViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, i: Int) {
        holder.setListener(itemList[i])
        holder.binding?.orderWithCartProducts = itemList[i]
    }

    inner class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = DataBindingUtil.bind<ElementOrderBinding>(view)

        fun setListener(orderWithCartProducts: OrderWithCartProducts) {
            binding?.elementOrderMvcMain?.setOnClickListener {
                ordersNavigator.showChangeStatus(orderWithCartProducts)
            }
        }
    }
}