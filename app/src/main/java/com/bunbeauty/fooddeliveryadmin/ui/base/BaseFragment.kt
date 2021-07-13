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
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.viewbinding.ViewBinding
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.Router
import com.bunbeauty.fooddeliveryadmin.extensions.startedLaunch
import com.bunbeauty.presentation.ErrorEvent
import com.bunbeauty.presentation.view_model.BaseViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.flow.onEach
import java.lang.reflect.ParameterizedType
import javax.inject.Inject

abstract class BaseFragment<B : ViewBinding> : Fragment() {

    private var mutableBinding: B? = null
    protected val binding
        get() = mutableBinding!!
    protected val textInputMap = HashMap<String, TextInputLayout>()
    protected abstract val viewModel: BaseViewModel

    @Inject
    lateinit var router: Router

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
        mutableBinding = null
    }

    protected fun <T> subscribe(liveData: LiveData<T>, observer: (T) -> Unit) {
        liveData.observe(viewLifecycleOwner, observer::invoke)
    }

    protected fun showError(errorMessage: String) {
        showSnackbar(errorMessage, R.color.lightTextColor, R.color.errorColor)
    }

    private fun showSnackbar(errorMessage: String, textColorId: Int, backgroundColorId: Int) {
        val snack = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_SHORT)
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