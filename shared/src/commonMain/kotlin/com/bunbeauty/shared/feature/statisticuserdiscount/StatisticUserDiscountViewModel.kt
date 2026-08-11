package com.bunbeauty.shared.feature.statisticuserdiscount

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.clientuser.UpdateClientUserDiscountUseCase
import com.bunbeauty.domain.feature.clientuser.exception.ClientPhoneNumberException
import com.bunbeauty.domain.feature.clientuser.exception.PercentDiscountException
import com.bunbeauty.shared.extension.launchSafe
import com.bunbeauty.shared.feature.menulist.common.TextFieldData
import com.bunbeauty.shared.viewmodel.base.BaseStateViewModel

class StatisticUserDiscountViewModel(
    private val updateClientUserDiscountUseCase: UpdateClientUserDiscountUseCase,
) : BaseStateViewModel<StatisticUserDiscount.DataState, StatisticUserDiscount.Action, StatisticUserDiscount.Event>(
        initState =
            StatisticUserDiscount.DataState(
                phoneNumber = "",
                percentField = TextFieldData.empty,
                percentError = StatisticUserDiscount.DataState.PercentError.NO_ERROR,
                isLoading = false,
            ),
    ) {
    override fun reduce(
        action: StatisticUserDiscount.Action,
        dataState: StatisticUserDiscount.DataState,
    ) {
        when (action) {
            is StatisticUserDiscount.Action.Init ->
                handleInit(
                    phoneNumber = action.phoneNumber,
                    personalDiscountPercent = action.personalDiscountPercent,
                )

            is StatisticUserDiscount.Action.PercentChanged -> handlePercentChanged(action.percent)

            StatisticUserDiscount.Action.OnSendClick -> handleSendClick(dataState)

            StatisticUserDiscount.Action.BackClick ->
                sendEvent {
                    StatisticUserDiscount.Event.GoBack
                }
        }
    }

    private fun handleInit(
        phoneNumber: String,
        personalDiscountPercent: Int?,
    ) {
        if (state.value.phoneNumber == phoneNumber) {
            return
        }
        val initialPercentValue = personalDiscountPercent?.toString().orEmpty()
        setState {
            copy(
                phoneNumber = phoneNumber,
                percentField =
                    percentField.copy(
                        value = initialPercentValue,
                        isError = false,
                    ),
                percentError = StatisticUserDiscount.DataState.PercentError.NO_ERROR,
            )
        }
    }

    private fun handlePercentChanged(percent: String) {
        setState {
            copy(
                percentField =
                    percentField.copy(
                        value = percent,
                        isError = false,
                    ),
                percentError = StatisticUserDiscount.DataState.PercentError.NO_ERROR,
            )
        }
    }

    private fun handleSendClick(dataState: StatisticUserDiscount.DataState) {
        if (dataState.isLoading) {
            return
        }

        val percentDiscount =
            dataState.percentField.value
                .trim()
                .toIntOrNull()
        if (percentDiscount == null) {
            setPercentError(StatisticUserDiscount.DataState.PercentError.INVALID_PERCENT)
            return
        }

        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(
                        isLoading = true,
                        percentField = percentField.copy(isError = false),
                        percentError = StatisticUserDiscount.DataState.PercentError.NO_ERROR,
                    )
                }
                val updatedClientUserSettings =
                    updateClientUserDiscountUseCase(
                        phoneNumber = dataState.phoneNumber,
                        percentDiscount = percentDiscount,
                    )
                setState {
                    copy(isLoading = false)
                }
                sendEvent {
                    StatisticUserDiscount.Event.ShowSavedMessage(
                        personalDiscountPercent = updatedClientUserSettings.personalDiscountPercent,
                    )
                }
            },
            onError = { throwable ->
                setPercentError(
                    when (throwable) {
                        is PercentDiscountException -> StatisticUserDiscount.DataState.PercentError.INVALID_PERCENT
                        is ClientPhoneNumberException -> StatisticUserDiscount.DataState.PercentError.INVALID_PHONE
                        else -> StatisticUserDiscount.DataState.PercentError.SOMETHING_WENT_WRONG
                    },
                )
            },
        )
    }

    private fun setPercentError(percentError: StatisticUserDiscount.DataState.PercentError) {
        setState {
            copy(
                isLoading = false,
                percentField = percentField.copy(isError = true),
                percentError = percentError,
            )
        }
    }
}
