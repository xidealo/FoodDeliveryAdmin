package com.bunbeauty.fooddeliveryadmin.extensions

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import androidx.navigation.NavArgsLazy

inline fun <reified Args : NavArgs> SavedStateHandle.navArgs() = NavArgsLazy(Args::class) {
    val bundle = Bundle()
    keys().forEach {
        val value = get<Any>(it)
        when (value) {
            is Parcelable -> {
                bundle.putParcelable(it, value)
            }
            is Boolean -> {
                bundle.putBoolean(it, value)
            }
            is String -> {
                bundle.putString(it, value)
            }
            is Array<*> -> {
                if (value.isArrayOf<Parcelable>()) {
                    bundle.putParcelableArray(it, value as Array<out Parcelable>)
                }
            }
        }
    }
    bundle
}