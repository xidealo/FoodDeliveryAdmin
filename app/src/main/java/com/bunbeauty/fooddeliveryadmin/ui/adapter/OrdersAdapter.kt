package com.bunbeauty.fooddeliveryadmin.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bunbeauty.data.model.order.Order
import com.bunbeauty.domain.string.IStringUtil
import com.bunbeauty.fooddeliveryadmin.databinding.ElementOrderBinding
import javax.inject.Inject

class OrdersAdapter @Inject constructor(private val stringUtil: IStringUtil) :
    BaseAdapter<OrdersAdapter.OrderViewHolder, Order>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): OrderViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ElementOrderBinding.inflate(inflater, viewGroup, false)

        return OrderViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, i: Int) {
        //holder.binding?.stringHelper = stringHelper

        holder.binding?.elementOrderMvcMain?.setOnClickListener {
            onItemClickListener?.invoke(itemList[i])
        }
        //holder.binding?.order = itemList[i]
    }

    inner class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = DataBindingUtil.bind<ElementOrderBinding>(view)
    }
}