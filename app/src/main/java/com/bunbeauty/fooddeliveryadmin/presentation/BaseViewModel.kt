package com.bunbeauty.fooddeliveryadmin.presentation

import androidx.lifecycle.ViewModel
import androidx.navigation.NavArgs
import com.bunbeauty.fooddeliveryadmin.Router
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {

    @Inject
    lateinit var router: Router

    fun goBack() {
        router.navigateUp()
    }
}