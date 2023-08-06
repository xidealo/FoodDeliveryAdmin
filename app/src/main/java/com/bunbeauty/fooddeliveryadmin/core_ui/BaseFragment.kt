package com.bunbeauty.fooddeliveryadmin.core_ui

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.TEXT_ALIGNMENT_CENTER
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.util.startedLaunch
import com.bunbeauty.presentation.view_model.BaseViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<B : ViewBinding> : Fragment() {

    private var mutableBinding: B? = null
    protected val binding
        get() = checkNotNull(mutableBinding)
    protected abstract val viewModel: BaseViewModel

    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mutableBinding = createBindingInstance(inflater, container)
        return mutableBinding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mutableBinding = null
    }

    @Suppress("UNCHECKED_CAST")
    protected open fun createBindingInstance(inflater: LayoutInflater, container: ViewGroup?): B {
        val viewBindingClass =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<B>
        val method = viewBindingClass.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )

        return method.invoke(null, inflater, container, false) as B
    }

    fun <T> Flow<T>.collectWithLifecycle(
        lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
        block: (T) -> Unit
    ) {
        lifecycleScope.launch {
            flowWithLifecycle(lifecycle, lifecycleState).collect {
                block(it)
            }
        }
    }
}
