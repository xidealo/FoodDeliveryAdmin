package com.bunbeauty.fooddeliveryadmin.ui.adapter

import android.content.Context
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bunbeauty.data.model.MenuProduct
import com.bunbeauty.domain.product.IProductHelper
import com.bunbeauty.fooddeliveryadmin.databinding.ElementMenuProductBinding
import javax.inject.Inject

class MenuProductsAdapter @Inject constructor(
    private val context: Context,
    private val productHelper: IProductHelper
) : BaseAdapter<MenuProductsAdapter.MenuProductViewHolder, MenuProduct>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MenuProductViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ElementMenuProductBinding.inflate(inflater, viewGroup, false)

        return MenuProductViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MenuProductViewHolder, i: Int) {
        holder.binding?.context = context
        holder.binding?.productHelper = productHelper
        holder.binding?.menuProduct = itemList[i]
        if (holder.binding?.elementMenuProductTvCostOld != null) {
            holder.binding.elementMenuProductTvCostOld.paintFlags =
                holder.binding.elementMenuProductTvCostOld.paintFlags or STRIKE_THRU_TEXT_FLAG
        }
    }

    inner class MenuProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = DataBindingUtil.bind<ElementMenuProductBinding>(view)
    }
}