package com.bunbeauty.shared.feature.category.editcategory

import com.bunbeauty.shared.feature.menulist.common.TextFieldData
import com.bunbeauty.shared.viewmodel.base.BaseAction
import com.bunbeauty.shared.viewmodel.base.BaseDataState
import com.bunbeauty.shared.viewmodel.base.BaseEvent

interface EditCategoryState {
    data class DataState(
        val uuid: String,
        val name: TextFieldData,
        val state: State,
        val isLoading: Boolean,
        val nameStateError: NameStateError,
    ) : BaseDataState {
        enum class NameStateError {
            EMPTY_NAME,
            DUPLICATE_NAME,
            NO_ERROR,
        }

        enum class State {
            LOADING,
            SUCCESS,
            ERROR,
        }
    }

    sealed interface Action : BaseAction {
        data class EditNameCategory(
            val nameEditCategory: String,
        ) : Action

        data object OnBackClicked : Action

        data object Init : Action

        data object OnSaveEditCategoryClick : Action
    }

    sealed interface Event : BaseEvent {
        data object GoBackEvent : Event

        data class ShowUpdateCategorySuccess(
            val categoryName: String,
        ) : Event
    }
}
