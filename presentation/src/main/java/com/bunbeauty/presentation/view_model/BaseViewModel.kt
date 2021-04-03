package com.bunbeauty.presentation.view_model

import androidx.lifecycle.ViewModel
import com.bunbeauty.common.utils.IDataStoreHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import java.lang.ref.WeakReference
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel(), CoroutineScope {
    override val coroutineContext: CoroutineContext = Job()
}