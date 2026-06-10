package com.bunbeauty.domain.feature.menu.editmenuproduct.exception

class MenuProductNotUpdatedException(
    val serverDetail: String? = null,
) : Exception(serverDetail)
