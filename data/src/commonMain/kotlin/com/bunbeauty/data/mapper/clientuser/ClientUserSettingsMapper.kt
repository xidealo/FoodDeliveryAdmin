package com.bunbeauty.data.mapper.clientuser

import com.bunbeauty.data.model.server.clientuser.ClientUserSettingsServer
import com.bunbeauty.domain.feature.clientuser.model.ClientUserSettings

class ClientUserSettingsMapper {
    fun map(clientUserSettingsServer: ClientUserSettingsServer): ClientUserSettings =
        ClientUserSettings(
            uuid = clientUserSettingsServer.uuid,
            phoneNumber = clientUserSettingsServer.phoneNumber,
            email = clientUserSettingsServer.email,
            isActive = clientUserSettingsServer.isActive,
            isProblematic = clientUserSettingsServer.isProblematic,
        )
}
