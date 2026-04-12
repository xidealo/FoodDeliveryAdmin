package com.bunbeauty.data.mapper

interface Mapper<F, S> {
    fun from(model: F): S

    fun to(model: S): F
}
