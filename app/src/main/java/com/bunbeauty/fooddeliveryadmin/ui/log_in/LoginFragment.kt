package com.bunbeauty.fooddeliveryadmin.ui.log_in

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.BR
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentLoginBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ViewModelComponent
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.bunbeauty.fooddeliveryadmin.ui.main.MainActivity
import com.bunbeauty.fooddeliveryadmin.view_model.LoginViewModel
import java.lang.ref.WeakReference
import java.util.*

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>(), LoginNavigator {

    override var viewModelVariable: Int = BR.viewModel
    override var layoutId: Int = R.layout.fragment_login
    override var viewModelClass = LoginViewModel::class.java

    override fun inject(viewModelComponent: ViewModelComponent) {
        viewModelComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.navigator = WeakReference(this)
        super.onViewCreated(view, savedInstanceState)
        viewModel.tokenLiveData.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                viewModel.isLoading.set(false)
            } else {
                viewModel.updateToken()
                goToOrders()
            }
        }
    }

    override fun login() {
        if (!viewModel.isCorrectUsername(viewDataBinding.fragmentLoginEtLogin.text.toString())) {
            viewDataBinding.fragmentLoginEtLogin.error =
                resources.getString(R.string.error_login_enter_user_name)
            return
        }

        if (!viewModel.isCorrectPassword(viewDataBinding.fragmentLoginEtPassword.text.toString())) {
            viewDataBinding.fragmentLoginEtPassword.error =
                resources.getString(R.string.error_login_enter_user_password)
            return
        }

        viewModel.isLoading.set(true)
        viewModel.login(
            viewDataBinding.fragmentLoginEtLogin.text.toString().toLowerCase(Locale.ROOT).trim(),
            viewDataBinding.fragmentLoginEtPassword.text.toString().toLowerCase(Locale.ROOT).trim()
        ).observe(viewLifecycleOwner) {
            if (!it) {
                viewModel.isLoading.set(false)
                (activity as MainActivity).showError(resources.getString(R.string.error_login_authorization))
            }
        }
    }

    private fun goToOrders() {
        findNavController().navigate(LoginFragmentDirections.
        toMainFragment())
    }
}