package com.bunbeauty.fooddeliveryadmin.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentLoginBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ViewModelComponent
import com.bunbeauty.fooddeliveryadmin.ui.base.BarsFragment
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.bunbeauty.fooddeliveryadmin.ui.main.MainActivity
import com.bunbeauty.presentation.view_model.LoginViewModel
import kotlinx.coroutines.flow.onEach
import java.util.*

class LoginFragment : BarsFragment<FragmentLoginBinding>() {

    override var layoutId = R.layout.fragment_login
    override val viewModel: LoginViewModel by viewModels { modelFactory }

    override val isBottomBarVisible: Boolean = false

    override fun inject(viewModelComponent: ViewModelComponent) {
        viewModelComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.tokenLiveData.observe(viewLifecycleOwner) { token ->
            if (token.isEmpty()) {
                viewModel.isLoading.set(false)
                viewModel.turnOffNotification()
            } else {
                viewModel.turnOnNotification()
                goToOrders()
            }
        }
        viewDataBinding.fragmentLoginBtnLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {
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
        ).onEach {
            if (!it) {
                viewModel.isLoading.set(false)
                (activity as MainActivity).showError(resources.getString(R.string.error_login_authorization))
            }
        }.launchWhenStarted(lifecycleScope)
    }

    private fun goToOrders() {
        findNavController().navigate(
            LoginFragmentDirections.toOrdersFragment()
        )
    }
}