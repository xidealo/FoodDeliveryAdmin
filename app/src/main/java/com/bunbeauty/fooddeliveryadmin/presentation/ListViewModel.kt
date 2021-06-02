package com.bunbeauty.fooddeliveryadmin.presentation

import androidx.lifecycle.SavedStateHandle
import com.bunbeauty.fooddeliveryadmin.extensions.navArgs
import com.bunbeauty.fooddeliveryadmin.ui.fragments.ListBottomSheetArgs
import com.bunbeauty.fooddeliveryadmin.ui.items.list.ListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : BaseViewModel() {

    private val args: ListBottomSheetArgs by savedStateHandle.navArgs()

    val isTitleVisible: Boolean = args.title != null

    val title: String? = args.title

    val list: List<ListItem> = args.list.map { listModel ->
        ListItem(listModel)
    }

    val selectedKey: String = args.selectedKey

    val requestKey: String = args.requestKey

}