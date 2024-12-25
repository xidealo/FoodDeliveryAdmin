package com.bunbeauty.fooddeliveryadmin.screen.cafelist

import android.content.res.Resources
import com.bunbeauty.domain.model.cafe.CafeStatus
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.screen.cafelist.item.CafeUiItem
import com.bunbeauty.presentation.feature.cafelist.CafeListDataState

class CafeStateMapper(private val resources: Resources) {

    fun map(cafeListDataState: CafeListDataState): CafeListUiState {
        return when (cafeListDataState.state) {
            CafeListDataState.State.LOADING -> CafeListUiState.Loading
            CafeListDataState.State.ERORR -> CafeListUiState.Error
            CafeListDataState.State.SUCCESS -> CafeListUiState.Success(
                cafeItemList = cafeListDataState.cafeList?.map { cafe ->
                    CafeUiItem(
                        uuid = cafe.uuid,
                        address = cafe.address,
                        workingHours = cafe.workingHours,
                        cafeStatusText = when (val status = cafe.status) {
                            CafeStatus.Open -> resources.getString(R.string.msg_cafe_open)
                            CafeStatus.Closed -> resources.getString(R.string.msg_cafe_closed)
                            is CafeStatus.CloseSoon -> resources.getQuantityString(
                                R.plurals.msg_cafe_close_soon,
                                status.minutes,
                                status.minutes
                            )
                        },
                        cafeStatus = cafe.status
                    )
                } ?: emptyList()
            )
        }
    }
}
