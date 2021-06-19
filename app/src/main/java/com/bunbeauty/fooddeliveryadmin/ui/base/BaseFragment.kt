package com.bunbeauty.fooddeliveryadmin.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.Router
import com.bunbeauty.fooddeliveryadmin.di.components.ActivityComponent
import com.bunbeauty.fooddeliveryadmin.presentation.BaseViewModel
import com.bunbeauty.fooddeliveryadmin.presentation.ViewModelFactory
import com.bunbeauty.fooddeliveryadmin.ui.MainActivity
import com.google.android.material.snackbar.Snackbar
import java.lang.reflect.ParameterizedType
import javax.inject.Inject

abstract class BaseFragment<B : ViewDataBinding, VM : BaseViewModel> : Fragment(),
    LifecycleObserver {

    private var _viewDataBinding: B? = null
    protected val viewDataBinding get() = _viewDataBinding!!
    protected lateinit var viewModel: VM

    @Inject
    protected lateinit var router: Router

    @Inject
    lateinit var factory: ViewModelFactory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inject((requireActivity() as MainActivity).activityComponent)
        viewModel = ViewModelProvider(this, factory).get(getViewModelClass())
    }

    abstract fun inject(activityComponent: ActivityComponent)

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
        return _viewDataBinding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewDataBinding = null
    }

    protected fun <T> subscribe(liveData: LiveData<T>, observer: (T) -> Unit) {
        liveData.observe(viewLifecycleOwner, observer::invoke)
    }

    protected fun showError(errorMessage: String) {
        val snack = Snackbar.make(viewDataBinding.root, errorMessage, Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.errorColor))
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.lightTextColor))
            .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.lightTextColor))
        snack.view.findViewById<TextView>(R.id.snackbar_text).textAlignment =
            View.TEXT_ALIGNMENT_CENTER
        snack.show()
    }

    protected fun showMessage(errorMessage: String) {
        val snack = Snackbar.make(viewDataBinding.root, errorMessage, Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.lightTextColor))
            .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.lightTextColor))
        snack.view.findViewById<TextView>(R.id.snackbar_text).textAlignment =
            View.TEXT_ALIGNMENT_CENTER
        snack.show()
    }

    @Suppress("UNCHECKED_CAST")
    private fun getViewModelClass() =
        (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<VM>

    @Suppress("UNCHECKED_CAST")
    private fun getViewBindingClass() =
        (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<B>

}