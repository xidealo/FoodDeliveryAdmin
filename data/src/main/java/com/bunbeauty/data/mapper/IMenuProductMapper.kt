package com.bunbeauty.data.mapper

import com.bunbeauty.domain.model.MenuProduct
import com.bunbeauty.domain.model.firebase.MenuProductFirebase

interface IMenuProductMapper: Mapper<MenuProductFirebase, MenuProduct> {
}