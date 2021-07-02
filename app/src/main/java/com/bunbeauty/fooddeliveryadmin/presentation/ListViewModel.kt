package com.bunbeauty.fooddeliveryadmin.presentation

import androidx.lifecycle.SavedStateHandle
import com.bunbeauty.fooddeliveryadmin.extensions.navArgs
import com.bunbeauty.fooddeliveryadmin.ui.fragments.ListBottomSheetArgs
import com.bunbeauty.fooddeliveryadmin.ui.items.ListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : BaseViewModel() {

    private val args: ListBottomSheetArgs by savedStateHandle.navArgs()

    val isTitleVisible: Boolean
        get() = args.title != null

    val title: String?
        get() = args.title

    val list: List<ListItem>
        get() = args.list.map { listModel ->
            ListItem(listModel)
        }

    val selectedKey: String
        get() = args.selectedKey

    val requestKey: String
        get() = args.requestKey


}