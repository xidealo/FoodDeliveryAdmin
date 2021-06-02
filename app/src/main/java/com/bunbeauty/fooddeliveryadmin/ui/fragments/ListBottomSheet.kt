package com.bunbeauty.fooddeliveryadmin.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.bunbeauty.fooddeliveryadmin.databinding.BottomSheetListBinding
import com.bunbeauty.fooddeliveryadmin.extensions.toggleVisibility
import com.bunbeauty.fooddeliveryadmin.presentation.ListViewModel
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseBottomSheetDialog
import com.bunbeauty.fooddeliveryadmin.ui.items.list.ListItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListBottomSheet: BaseBottomSheetDialog<BottomSheetListBinding>() {

    override val viewModel: ListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bottomSheetListTvTitle.toggleVisibility(viewModel.isTitleVisible)
        binding.bottomSheetListTvTitle.text = viewModel.title

        val itemAdapter = ItemAdapter<ListItem>()
        val fastAdapter = FastAdapter.with(itemAdapter)
        binding.bottomSheetListRvList.adapter = fastAdapter
        fastAdapter.onClickListener = { _, _, listItem, _ ->
            val bundle = bundleOf(viewModel.selectedKey to listItem.listModel)
            setFragmentResult(viewModel.requestKey, bundle)
            viewModel.goBack()
            false
        }
        itemAdapter.set(viewModel.list)
    }
}