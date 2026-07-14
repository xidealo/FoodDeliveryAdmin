package com.bunbeauty.data.repository

import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.mapper.clientuser.ClientUserSettingsMapper
import com.bunbeauty.domain.feature.clientuser.model.ClientUserSettingsList
import com.bunbeauty.domain.repo.ClientUserRepo
import common.ApiResult

class ClientUserRepository(
    private val foodDeliveryApi: FoodDeliveryApi,
    private val clientUserSettingsMapper: ClientUserSettingsMapper,
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
}
