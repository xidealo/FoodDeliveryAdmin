package com.bunbeauty.presentation.feature.mapdelivery.editinfodeliveryzone

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.exception.ZoneNameException
import com.bunbeauty.domain.feature.mapzonedelivery.editinfodeliveryzone.GetZoneUseCase
import com.bunbeauty.domain.feature.mapzonedelivery.editinfodeliveryzone.SaveInfoZoneUseCase
import com.bunbeauty.domain.model.cafe.UpdateInfoDeliveryZone
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.feature.menulist.common.TextFieldData
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import kotlin.String
import kotlin.text.orEmpty

private const val ZONE_UUID = "zoneUuid"

class EditInfoDeliveryZoneViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getZoneUseCase: GetZoneUseCase,
    private val saveInfoZoneUseCase: SaveInfoZoneUseCase,
) : BaseStateViewModel<EditInfoDeliveryZone.DataState, EditInfoDeliveryZone.Action, EditInfoDeliveryZone.Event>(
        initState =
            EditInfoDeliveryZone.DataState(
                state = EditInfoDeliveryZone.DataState.State.LOADING,
                uuid = "",
                isLoading = false,
                nameZona = TextFieldData.empty,
                hasEditNameError = false,
                minOrderCost = TextFieldData.empty,
                normalDeliveryCost = TextFieldData.empty,
                forLowDeliveryCost = TextFieldData.empty,
            ),
    ) {
    override fun reduce(
        action: EditInfoDeliveryZone.Action,
        dataState: EditInfoDeliveryZone.DataState,
    ) {
        when (action) {
            EditInfoDeliveryZone.Action.OnBackClick -> backClick()
            EditInfoDeliveryZone.Action.InitZone -> loadData()
            EditInfoDeliveryZone.Action.SaveDeliveryZone -> saveInfoDeliveryZone()

            is EditInfoDeliveryZone.Action.EditNameDeliveryZone ->
                editNameZona(
                    nameEditZona = action.nameDeliveryZone,
                )

            is EditInfoDeliveryZone.Action.EditMinOrderCostDeliveryZone ->
                editMinOrderCost(
                    minOrder = action.minOrderCost,
                )

            is EditInfoDeliveryZone.Action.EditForLowDeliveryCostDeliveryZone ->
                editForLowDeliveryCost(
                    freeCast = action.forLowDeliveryCost,
                )

            is EditInfoDeliveryZone.Action.EditNormalDeliveryCostDeliveryZone ->
                editNormalDeliveryCost(
                    normalCast = action.normalDeliveryCost,
                )
        }
    }

    private fun loadData() {
        viewModelScope.launchSafe(
            block = {
                setState {
                    val zoneUuidNavigation =
                        savedStateHandle.get<String>(ZONE_UUID).orEmpty()
                    val zone = getZoneUseCase(zoneUuid = zoneUuidNavigation)
                    copy(
                        state = EditInfoDeliveryZone.DataState.State.SUCCESS,
                        uuid = zoneUuidNavigation,
                        nameZona =
                            TextFieldData(
                                value = zone.nameZone,
                                isError = false,
                            ),
                        minOrderCost =
                            TextFieldData(
                                value = zone.minOrderCost.toString(),
                                isError = false,
                            ),
                        normalDeliveryCost =
                            TextFieldData(
                                value = zone.normalDeliveryCost.toString(),
                                isError = false,
                            ),
                        forLowDeliveryCost =
                            TextFieldData(
                                value = zone.forLowDeliveryCost.toString(),
                                isError = false,
                            ),
                    )
                }
            },
            onError = {
                setState {
                    copy(state = EditInfoDeliveryZone.DataState.State.ERROR)
                }
            },
        )
    }

    private fun saveInfoDeliveryZone() {
        viewModelScope.launchSafe(
            block = {
                saveInfoZoneUseCase(
                    uuidZone = state.value.uuid,
                    updateInfoZone =
                        state.value.run {
                            UpdateInfoDeliveryZone(
                                name = nameZona.value,
                                minOrderCost = minOrderCost.value.toInt(),
                                normalDeliveryCost = normalDeliveryCost.value.toInt(),
                                forLowDeliveryCost = forLowDeliveryCost.value.toInt(),
                            )
                        },
                )
                sendEvent { dataState ->
                    EditInfoDeliveryZone.Event.SaveInfoZoneSuccess(
                        zoneName = dataState.nameZona.value,
                    )
                }
            },
            onError = { throwable ->
                setState {
                    when (throwable) {
                        is ZoneNameException -> {
                            copy(
                                hasEditNameError = true,
                                isLoading = false,
                            )
                        }

                        else -> copy(isLoading = false)
                    }
                }
            },
        )
    }

    private fun backClick() {
        sendEvent { EditInfoDeliveryZone.Event.Back }
    }

    private fun editNameZona(nameEditZona: String) {
        setState {
            copy(
                nameZona =
                    nameZona.copy(
                        value = nameEditZona,
                        isError = false,
                    ),
            )
        }
    }

    private fun editMinOrderCost(minOrder: String) {
        setState {
            copy(
                minOrderCost =
                    minOrderCost.copy(
                        value = minOrder,
                        isError = false,
                    ),
            )
        }
    }

    private fun editNormalDeliveryCost(normalCast: String) {
        setState {
            copy(
                normalDeliveryCost =
                    normalDeliveryCost.copy(
                        value = normalCast,
                        isError = false,
                    ),
            )
        }
    }

    private fun editForLowDeliveryCost(freeCast: String) {
        setState {
            copy(
                forLowDeliveryCost =
                    forLowDeliveryCost.copy(
                        value = freeCast,
                        isError = false,
                    ),
            )
        }
    }
}
