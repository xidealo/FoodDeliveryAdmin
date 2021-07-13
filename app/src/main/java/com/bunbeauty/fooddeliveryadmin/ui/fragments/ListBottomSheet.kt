package com.bunbeauty.fooddeliveryadmin.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bunbeauty.fooddeliveryadmin.databinding.BottomSheetListBinding
import com.bunbeauty.fooddeliveryadmin.extensions.toggleVisibility
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseBottomSheetDialog
import com.bunbeauty.fooddeliveryadmin.ui.items.ListItem
import com.bunbeauty.presentation.view_model.ListViewModel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListBottomSheet: BaseBottomSheetDialog<BottomSheetListBinding>() {

    private val args: ListBottomSheetArgs by navArgs()
    override val viewModel: ListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.listData = args.listData

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
        val listItems = viewModel.list.map { listModel ->
            ListItem(listModel)
        }
        itemAdapter.set(listItems)
    }
}