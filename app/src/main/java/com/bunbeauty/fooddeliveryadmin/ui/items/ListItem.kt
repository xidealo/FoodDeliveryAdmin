package com.bunbeauty.fooddeliveryadmin.ui.items

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.ElementTextBinding
import com.bunbeauty.presentation.model.list.ListItemModel
import com.mikepenz.fastadapter.binding.AbstractBindingItem

data class ListItem(
    val listItemModel: ListItemModel
) : AbstractBindingItem<ElementTextBinding>() {

    override val type = R.id.element_text_mvc_main

    override fun bindView(binding: ElementTextBinding, payloads: List<Any>) {
        binding.elementTextTvTitle.text = listItemModel.title
    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ElementTextBinding {
        return ElementTextBinding.inflate(inflater, parent, false)
    }
}
