package com.bunbeauty.presentation.feature.editcafe

import com.bunbeauty.presentation.viewmodel.base.Event

sealed interface EditCafeEvent: Event {

    data object GoBack:  EditCafeEvent
    data object ShowFetchDataError:  EditCafeEvent
    data object ShowUpdateDataError:  EditCafeEvent
    data object ShowSaveDataError:  EditCafeEvent
    data object ShowDeleteDataError:  EditCafeEvent
    data class ShowConfirmDeletion(val uuid: String):  EditCafeEvent

}