package com.bunbeauty.fooddeliveryadmin.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bunbeauty.data.model.order.Order
import com.bunbeauty.fooddeliveryadmin.databinding.ElementOrderBinding
import com.bunbeauty.domain.string_helper.IStringHelper
import java.lang.ref.WeakReference
import javax.inject.Inject

class OrdersAdapter @Inject constructor(
    private val context: Context,
    private val stringHelper: IStringHelper
) : BaseAdapter<OrdersAdapter.OrderViewHolder, Order>() {

    lateinit var ordersNavigator: WeakReference<com.bunbeauty.presentation.navigator.OrdersNavigator>

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): OrderViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ElementOrderBinding.inflate(inflater, viewGroup, false)

        return OrderViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, i: Int) {
        holder.binding?.context = context
        holder.binding?.stringHelper = stringHelper
        holder.setListener(itemList[i])
        holder.binding?.order = itemList[i]
    }

    inner class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = DataBindingUtil.bind<ElementOrderBinding>(view)

        fun setListener(order: Order) {
            binding?.elementOrderMvcMain?.setOnClickListener {
                ordersNavigator.get()?.showChangeStatus(order)
            }
        }
    }
}