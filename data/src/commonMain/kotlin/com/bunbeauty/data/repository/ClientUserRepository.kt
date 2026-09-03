package com.bunbeauty.data.repository

import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.mapper.clientuser.ClientUserSettingsMapper
import com.bunbeauty.data.mapper.clientuser.ClientUserStatisticMapper
import com.bunbeauty.data.model.server.clientuser.PatchClientUserDiscountServer
import com.bunbeauty.data.model.server.clientuser.PatchClientUserProblematicServer
import com.bunbeauty.data.model.server.clientuser.PostClientPushServer
import com.bunbeauty.domain.feature.clientuser.model.ClientUserSettings
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

    override suspend fun getClientUserListByQuery(
        token: String,
        query: String,
        limit: Int,
        offset: Int,
    ): ClientUserSettingsList =
        when (
            val result =
                foodDeliveryApi.getClientUserSearch(
                    token = token,
                    query = query,
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
                throw Exception("client user search load error")
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

    override suspend fun updateClientUserProblematic(
        token: String,
        clientUserUuid: String,
        isProblematic: Boolean,
    ): ClientUserSettings =
        when (
            val result =
                foodDeliveryApi.patchClientUserProblematic(
                    token = token,
                    clientUserUuid = clientUserUuid,
                    patch =
                        PatchClientUserProblematicServer(
                            isProblematic = isProblematic,
                        ),
                )
        ) {
            is ApiResult.Success -> {
                clientUserSettingsMapper.map(result.data)
            }

            is ApiResult.Error -> {
                throw Exception("client user update error")
            }
        }

    override suspend fun updateClientUserDiscount(
        token: String,
        phoneNumber: String,
        percentDiscount: Int,
    ): ClientUserSettings =
        when (
            val result =
                foodDeliveryApi.patchClientUserDiscount(
                    token = token,
                    phoneNumber = phoneNumber,
                    patch =
                        PatchClientUserDiscountServer(
                            percentDiscount = percentDiscount,
                        ),
                )
        ) {
            is ApiResult.Success -> {
                clientUserSettingsMapper.map(result.data)
            }

            is ApiResult.Error -> {
                throw Exception("client user discount update error")
            }
        }

    override suspend fun sendClientPush(
        token: String,
        phoneNumber: String,
        title: String,
        body: String,
    ) {
        when (
            val result =
                foodDeliveryApi.postClientPush(
                    token = token,
                    phoneNumber = phoneNumber,
                    body =
                        PostClientPushServer(
                            title = title,
                            body = body,
                        ),
                )
        ) {
            is ApiResult.Success -> Unit
            is ApiResult.Error -> {
                throw Exception("client push send error")
            }
        }
    }
}
