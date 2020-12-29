package com.bunbeauty.fooddeliveryadmin.ui.log_in

import android.os.Bundle
import com.bunbeauty.fooddeliveryadmin.BR
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentLoginBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ViewModelComponent
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.bunbeauty.fooddeliveryadmin.view_model.LoginViewModel
import java.lang.ref.WeakReference

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>(), LoginNavigator {

    override var viewModelVariable: Int = BR.viewModel
    override var layoutId: Int = R.layout.fragment_login
    override var viewModelClass = LoginViewModel::class.java

    override fun inject(viewModelComponent: ViewModelComponent) {
        viewModelComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigator = WeakReference(this)
    }

    override fun login() {
        if (!viewModel.isCorrectUsername(viewDataBinding.fragmentLoginEtLogin.text.toString())) {
            viewDataBinding.fragmentLoginEtLogin.error =
                resources.getString(R.string.enter_user_name_error)
            viewDataBinding.fragmentLoginBtnLogin.hideLoading()
            return
        }

        if (!viewModel.isCorrectPassword(viewDataBinding.fragmentLoginEtPassword.text.toString())) {
            viewDataBinding.fragmentLoginEtPassword.error =
                resources.getString(R.string.enter_user_password_error)
            viewDataBinding.fragmentLoginBtnLogin.hideLoading()
            return
        }

        viewModel.login(
            viewDataBinding.fragmentLoginEtLogin.text.toString(),
            viewDataBinding.fragmentLoginEtPassword.text.toString()
        )
    }

}