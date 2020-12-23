package com.bunbeauty.fooddeleveryadmin.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bunbeauty.fooddeleveryadmin.FoodDeliveryAdminApplication
import com.bunbeauty.fooddeleveryadmin.R
import com.bunbeauty.fooddeleveryadmin.databinding.ActivityMainBinding
import com.bunbeauty.fooddeleveryadmin.view_model.MainViewModel
import com.bunbeauty.fooddeleveryadmin.view_model.base.ViewModelFactory
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    lateinit var viewDataBinding: ActivityMainBinding
    lateinit var viewModel: MainViewModel

    @Inject
    lateinit var modelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as FoodDeliveryAdminApplication).appComponent
            .getViewModelComponent()
            .create(this)
            .inject(this)

        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.executePendingBindings()

        viewModel = ViewModelProvider(this, modelFactory).get(MainViewModel::class.java)
    }
}