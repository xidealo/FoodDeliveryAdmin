package com.bunbeauty.presentation.feature.category

import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

class CategoriesViewModel() :
    BaseStateViewModel<CategoriesState.DataState, CategoriesState.Action, CategoriesState.Event>(
        initState = CategoriesState.DataState(
            state = CategoriesState.DataState.State.LOADING
        )
    ) {
    override fun reduce(
        action: CategoriesState.Action,
        dataState: CategoriesState.DataState
    ) {

    }
}