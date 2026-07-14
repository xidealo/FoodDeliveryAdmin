package com.bunbeauty.data.repository

import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.mapper.clientuser.ClientUserSettingsMapper
import com.bunbeauty.data.mapper.clientuser.ClientUserStatisticMapper
import com.bunbeauty.domain.feature.clientuser.model.ClientUserSettingsList
import com.bunbeauty.domain.feature.clientuser.model.ClientUserStatistic
import com.bunbeauty.domain.repo.ClientUserRepo
import common.ApiResult

class ClientUserRepository(
    private val foodDeliveryApi: FoodDeliveryApi,
    private val clientUserSettingsMapper: ClientUserSettingsMapper,
    private val clientUserStatisticMapper: ClientUserStatisticMapper,
) : ClientUserRepo {
    override suspend fun getClientUserList(
        token: String,
        limit: Int,
        offset: Int,
    ): ClientUserSettingsList =
        when (
            val result =
                foodDeliveryApi.getClientUserList(
                    token = token,
                    limit = limit,
                    offset = offset,
                )
        ) {
            is ApiResult.Success -> {
                ClientUserSettingsList(
                    count = result.data.count,
                    results = result.data.results.map(clientUserSettingsMapper::map),
                )
            }

            is ApiResult.Error -> {
                throw Exception("client user list load error")
            }
        }

    override suspend fun getClientUserStatistic(
        token: String,
        clientUserUuid: String,
    ): ClientUserStatistic =
        when (
            val result =
                foodDeliveryApi.getClientUserStatistic(
                    token = token,
                    clientUserUuid = clientUserUuid,
                )
        ) {
            is ApiResult.Success -> {
                clientUserStatisticMapper.map(result.data)
            }

            is ApiResult.Error -> {
                throw Exception("client user statistic load error")
            }
        }
}
