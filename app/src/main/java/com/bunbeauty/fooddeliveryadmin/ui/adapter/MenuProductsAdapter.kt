package com.bunbeauty.fooddeliveryadmin.ui.adapter

import com.bunbeauty.domain.util.product.IProductUtil
import javax.inject.Inject

class MenuProductsAdapter @Inject constructor(private val productUtil: IProductUtil) {

    /*override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MenuProductViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ElementMenuProductBinding.inflate(inflater, viewGroup, false)

        return MenuProductViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MenuProductViewHolder, i: Int) {
        holder.binding?.productHelper = productUtil
        holder.binding?.menuProduct = itemList[i]
        if (holder.binding?.elementMenuProductTvCostOld != null) {
            holder.binding.elementMenuProductTvCostOld.paintFlags =
                holder.binding.elementMenuProductTvCostOld.paintFlags or STRIKE_THRU_TEXT_FLAG
        }
        holder.binding?.elementMenuProductMcvMain?.setOnClickListener {
            onItemClickListener?.invoke(itemList[i])
        }
        holder.checkVisible(itemList[i].visible)
    }

    inner class MenuProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = DataBindingUtil.bind<ElementMenuProductBinding>(view)

        fun checkVisible(isVisible: Boolean) {
            binding?.elementMenuProductIvVisible?.toggleVisibility(isVisible)
        }
    }*/
}