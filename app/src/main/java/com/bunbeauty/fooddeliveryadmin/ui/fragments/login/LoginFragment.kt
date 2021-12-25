package com.bunbeauty.fooddeliveryadmin.ui.fragments.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentLoginBinding
import com.bunbeauty.fooddeliveryadmin.extensions.gone
import com.bunbeauty.fooddeliveryadmin.extensions.startedLaunch
import com.bunbeauty.fooddeliveryadmin.extensions.visible
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.bunbeauty.presentation.navigation_event.LoginNavigationEvent
import com.bunbeauty.presentation.view_model.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            fragmentLoginBtnLogin.setOnClickListener {
                viewModel.login(
                    fragmentLoginEtLogin.text.toString(),
                    fragmentLoginEtPassword.text.toString()
                )
            }
        }

        viewModel.run {
            isLoading.onEach { isLoading ->
                if (isLoading) {
                    binding.fragmentLoginPbLoading.visible()
                    binding.fragmentLoginGroupLogin.gone()
                } else {
                    binding.fragmentLoginPbLoading.gone()
                    binding.fragmentLoginGroupLogin.visible()
                }
            }.startedLaunch(viewLifecycleOwner)

            navigation.onEach { navigationEvent ->
                when (navigationEvent) {
                    is LoginNavigationEvent.ToOrders -> router.navigate(LoginFragmentDirections.toOrdersFragment())
                    else -> Unit
                }
            }.startedLaunch(viewLifecycleOwner)
        }
    }
}