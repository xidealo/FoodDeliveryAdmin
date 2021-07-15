package com.bunbeauty.presentation.view_model

import androidx.lifecycle.SavedStateHandle
import com.bunbeauty.common.Constants.LIST_DATA_ARGS_KEY
import com.bunbeauty.presentation.extension.navArg
import com.bunbeauty.presentation.model.ListData
import com.bunbeauty.presentation.model.list.ListItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val listData: ListData = savedStateHandle.navArg(LIST_DATA_ARGS_KEY)!!

    val isTitleVisible: Boolean = listData.title != null

    val title: String? = listData.title

    val listItem: List<ListItemModel> = listData.listItem

    val selectedKey: String = listData.selectedKey

    val requestKey: String = listData.requestKey

}