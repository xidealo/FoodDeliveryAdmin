package com.bunbeauty.presentation.view_model

import com.bunbeauty.presentation.ListData
import com.bunbeauty.presentation.list.ListModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor() : BaseViewModel() {

    var listData: ListData? = null

    val isTitleVisible: Boolean = listData!!.title != null

    val title: String? = listData!!.title

    val list: List<ListModel> = listData!!.list

    val selectedKey: String = listData!!.selectedKey

    val requestKey: String = listData!!.requestKey

}