package com.bunbeauty.fooddeliveryadmin.screen.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentLoginBinding
import com.bunbeauty.fooddeliveryadmin.util.gone
import com.bunbeauty.fooddeliveryadmin.util.startedLaunch
import com.bunbeauty.fooddeliveryadmin.util.visible
import com.bunbeauty.fooddeliveryadmin.core_ui.BaseFragment
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
        }
    }
}