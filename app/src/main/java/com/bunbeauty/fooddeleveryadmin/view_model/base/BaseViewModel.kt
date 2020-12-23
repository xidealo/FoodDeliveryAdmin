package com.bunbeauty.fooddeleveryadmin.view_model.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel<N> : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext = Job()
    abstract var navigator: WeakReference<N>?

}