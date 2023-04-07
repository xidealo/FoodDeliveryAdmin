package com.bunbeauty.fooddeliveryadmin.screen.login

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.core_ui.BaseFragment
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            fragmentLoginBtnLogin.setOnClickListener {
                viewModel.login(
                    fragmentLoginEtLogin.text.toString(),
                    fragmentLoginEtPassword.text.toString()
                )
            }
        }

        viewModel.loginViewState.collectWithLifecycle { state ->
            binding.fragmentLoginPbLoading.isVisible = state.isLoading
            binding.fragmentLoginGroupLogin.isVisible = !state.isLoading
            state.appVersion?.let { version ->
                binding.versionTextView.text =
                    resources.getString(R.string.msg_login_version, version)
            }
            handleEvents(state.eventList)
        }
    }

    private fun handleEvents(eventList: List<LoginViewState.Event>) {
        eventList.forEach { event ->
            when (event) {
                LoginViewState.Event.OpenOrderListEvent -> {
                    findNavController().navigate(LoginFragmentDirections.toOrdersFragment())
                }
                LoginViewState.Event.ShowWrongCredentialError -> {
                    showSnackbar(
                        resources.getString(R.string.error_login_wrong_data),
                        R.color.lightTextColor,
                        R.color.errorColor
                    )
                }
                LoginViewState.Event.ShowConnectionError -> {
                    showSnackbar(
                        resources.getString(R.string.msg_check_connection_and_retry),
                        R.color.lightTextColor,
                        R.color.errorColor
                    )
                }
            }
        }
        viewModel.consumeEvents(eventList)
    }
}
