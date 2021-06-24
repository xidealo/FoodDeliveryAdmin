package com.bunbeauty.fooddeliveryadmin.extensions

import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import androidx.navigation.NavArgsLazy

inline fun <reified Args : NavArgs> SavedStateHandle.navArgs() = NavArgsLazy(Args::class) {
    val bundle = Bundle()
    keys().forEach {
        val value = get<Any>(it)
        when {
            value is Parcelable -> {
                bundle.putParcelable(it, value)
            }
            value is Boolean -> {
                bundle.putBoolean(it, value)
            }
        }
    }
    bundle
}