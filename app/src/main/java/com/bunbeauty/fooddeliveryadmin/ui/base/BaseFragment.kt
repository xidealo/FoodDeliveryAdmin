package com.bunbeauty.fooddeliveryadmin.ui.base

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
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.extensions.startedLaunch
import com.bunbeauty.fooddeliveryadmin.presentation.BaseViewModel
import com.bunbeauty.fooddeliveryadmin.ui.ErrorEvent
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.flow.onEach
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<B : ViewDataBinding> : Fragment() {

    private var _viewDataBinding: B? = null
    protected val viewDataBinding
        get() = checkNotNull(_viewDataBinding)
    protected val textInputMap = HashMap<String, TextInputLayout>()
    protected abstract val viewModel: BaseViewModel

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.message.onEach { message ->
            showSnackbar(message, R.color.lightTextColor, R.color.colorPrimary)
        }.startedLaunch(viewLifecycleOwner)
        viewModel.error.onEach { error ->
            textInputMap.values.forEach { textInput ->
                textInput.error = null
                textInput.clearFocus()
            }
             when (error) {
                 is ErrorEvent.MessageError -> {
                     showError(error.message)
                 }
                 is ErrorEvent.FieldError -> {
                     textInputMap[error.key]?.error = error.message
                     textInputMap[error.key]?.requestFocus()
                 }
             }
        }.startedLaunch(viewLifecycleOwner)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewDataBinding = null
    }

    protected fun <T> subscribe(liveData: LiveData<T>, observer: (T) -> Unit) {
        liveData.observe(viewLifecycleOwner, observer::invoke)
    }

    protected fun showError(errorMessage: String) {
        showSnackbar(errorMessage, R.color.lightTextColor, R.color.errorColor)
    }

    private fun showSnackbar(errorMessage: String, textColorId: Int, backgroundColorId: Int) {
        val snack = Snackbar.make(viewDataBinding.root, errorMessage, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(ContextCompat.getColor(requireContext(), backgroundColorId))
            .setTextColor(ContextCompat.getColor(requireContext(), textColorId))
            .setActionTextColor(ContextCompat.getColor(requireContext(), textColorId))
        val layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
            gravity = Gravity.TOP
            setMargins(16, 16, 16, 0)
        }
        with(snack) {
            view.layoutParams = layoutParams
            view.findViewById<TextView>(R.id.snackbar_text).textAlignment = TEXT_ALIGNMENT_CENTER
            show()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun getViewBindingClass() =
        (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<B>

}