package com.bunbeauty.shared.feature.statisticuserdiscount

import com.bunbeauty.shared.feature.menulist.common.TextFieldData
import com.bunbeauty.shared.viewmodel.base.BaseAction
import com.bunbeauty.shared.viewmodel.base.BaseDataState
import com.bunbeauty.shared.viewmodel.base.BaseEvent

interface StatisticUserDiscount {
    data class DataState(
        val phoneNumber: String,
        val percentField: TextFieldData,
        val percentError: PercentError,
        val isLoading: Boolean,
    ) : BaseDataState {
        enum class PercentError {
            INVALID_PERCENT,
            INVALID_PHONE,
            SOMETHING_WENT_WRONG,
            NO_ERROR,
        }
    }

    sealed interface Action : BaseAction {
        data class Init(
            val phoneNumber: String,
            val personalDiscountPercent: Int?,
        ) : Action

        data class PercentChanged(
            val percent: String,
        ) : Action

        data object OnSendClick : Action

        data object BackClick : Action
    }

    sealed interface Event : BaseEvent {
        data object GoBack : Event

        data class ShowSavedMessage(
            val personalDiscountPercent: Int?,
        ) : Event
    }
}
