package com.bunbeauty.presentation.extension

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle

inline fun <reified T> SavedStateHandle.navArg(key: String): Lazy<T> = lazy {
    when {
        Parcelable::class.java.isAssignableFrom(T::class.java) -> {
            get<Parcelable>(key) as T
        }
        T::class.java == Boolean::class.java -> {
            get<Boolean>(key) as T
        }
        T::class.java == String::class.java -> {
            get<String>(key) as T
        }
        else -> error("Argument not found")
    }
}
