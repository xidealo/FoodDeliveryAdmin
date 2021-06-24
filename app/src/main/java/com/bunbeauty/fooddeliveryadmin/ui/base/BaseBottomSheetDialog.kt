package com.bunbeauty.fooddeliveryadmin.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.presentation.BaseViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.lang.reflect.ParameterizedType

abstract class BaseBottomSheetDialog<B : ViewDataBinding, VM : BaseViewModel> :
    BottomSheetDialogFragment() {

    private var _viewDataBinding: B? = null
    protected val viewDataBinding get() = _viewDataBinding!!
    protected abstract val viewModel: VM

    override fun onAttach(context: Context) {
        super.onAttach(context)

        setStyle(STYLE_NORMAL, R.style.BottomSheetTheme)

        //inject((requireActivity() as MainActivity).activityComponent)
    }

    //abstract fun inject(activityComponent: ActivityComponent)

    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val viewBindingClass = getViewBindingClass()
        val inflateMethod = viewBindingClass.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java,
        )
        _viewDataBinding = inflateMethod.invoke(viewBindingClass, inflater, container, false) as B

        return viewDataBinding.root
    }

    @Suppress("UNCHECKED_CAST")
    private fun getViewModelClass() =
        (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<VM>

    @Suppress("UNCHECKED_CAST")
    private fun getViewBindingClass() =
        (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<B>

    override fun onDestroyView() {
        super.onDestroyView()
        _viewDataBinding = null
    }

}