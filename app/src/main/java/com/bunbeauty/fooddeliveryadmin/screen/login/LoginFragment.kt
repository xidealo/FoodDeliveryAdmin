package com.bunbeauty.fooddeliveryadmin.screen.login

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.coreui.BaseFragment
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentLoginBinding
import com.bunbeauty.fooddeliveryadmin.main.MessageHost
import com.bunbeauty.fooddeliveryadmin.navigation.navigateSafe
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    override val viewModel: LoginViewModel by viewModel()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            fragmentLoginBtnLogin.setOnClickListener {
                viewModel.login(
                    fragmentLoginEtLogin.text.toString(),
                    fragmentLoginEtPassword.text.toString(),
                )
            }
        }

        viewModel.loginViewState.collectWithLifecycle { state ->
            binding.fragmentLoginPbLoading.isVisible = state.isLoading
            binding.fragmentLoginGroupLogin.isVisible = !state.isLoading

            handleEvents(state.eventList)
        }
    }

    private fun handleEvents(eventList: List<LoginViewState.Event>) {
        eventList.forEach { event ->
            when (event) {
                LoginViewState.Event.OpenOrderListEvent -> {
                    findNavController().navigateSafe(LoginFragmentDirections.toOrdersFragment())
                }
                LoginViewState.Event.ShowWrongCredentialError -> {
                    (activity as? MessageHost)?.showErrorMessage(
                        resources.getString(R.string.error_login_wrong_data),
                    )
                }
                LoginViewState.Event.ShowConnectionError -> {
                    (activity as? MessageHost)?.showErrorMessage(
                        resources.getString(R.string.msg_common_check_connection_and_retry),
                    )
                }
            }
        }
        viewModel.consumeEvents(eventList)
    }
}
