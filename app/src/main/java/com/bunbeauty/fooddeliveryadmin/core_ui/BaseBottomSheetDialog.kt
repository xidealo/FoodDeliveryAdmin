package com.bunbeauty.fooddeliveryadmin.core_ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.Router
import com.bunbeauty.fooddeliveryadmin.util.startedLaunch
import com.bunbeauty.presentation.navigation_event.NavigationEvent
import com.bunbeauty.presentation.view_model.BaseViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.onEach
import java.lang.reflect.ParameterizedType
import javax.inject.Inject

abstract class BaseBottomSheetDialog<B : ViewBinding> : BottomSheetDialogFragment() {

    @Inject
    lateinit var router: Router

    private var mutableBinding: B? = null
    protected val binding
        get() = checkNotNull(mutableBinding)
    protected abstract val viewModel: BaseViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)

        setStyle(STYLE_NORMAL, R.style.BottomSheetTheme)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        mutableBinding = createBindingInstance(inflater, container)
        return mutableBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (dialog as? BottomSheetDialog)?.behavior?.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
        }
        viewModel.navigation.onEach { navigationEvent ->
            if (navigationEvent is NavigationEvent.Back) {
                router.navigateUp()
            }
        }.startedLaunch(viewLifecycleOwner)
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
}