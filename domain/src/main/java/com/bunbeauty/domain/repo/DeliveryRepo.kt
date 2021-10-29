package com.bunbeauty.domain.repo

interface DeliveryRepo {
    suspend fun refreshDelivery(token: String, companyUuid: String)
}