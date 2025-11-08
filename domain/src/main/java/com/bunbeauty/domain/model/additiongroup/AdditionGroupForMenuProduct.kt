package com.bunbeauty.domain.model.additiongroup

data class AdditionGroupForMenuProduct(
    val uuid: String,
    val name: String,
    val additionNameList: String?,
    val priority: Int
) {
    companion object {
        val mock = AdditionGroupForMenuProduct(
            uuid = "",
            name = "",
            priority = 0,
            additionNameList = ""
        )
    }
}
