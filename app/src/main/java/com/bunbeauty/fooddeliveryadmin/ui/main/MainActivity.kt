package com.bunbeauty.fooddeliveryadmin.ui.main

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bunbeauty.fooddeliveryadmin.FoodDeliveryAdminApplication
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.ActivityMainBinding
import com.bunbeauty.fooddeliveryadmin.view_model.MainViewModel
import com.bunbeauty.fooddeliveryadmin.view_model.base.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    lateinit var viewDataBinding: ActivityMainBinding
    lateinit var viewModel: MainViewModel

    @Inject
    lateinit var modelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as FoodDeliveryAdminApplication).appComponent
            .getViewModelComponent()
            .create(this)
            .inject(this)

        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.executePendingBindings()

        viewModel = ViewModelProvider(this, modelFactory).get(MainViewModel::class.java)
    }

    fun showMessage(message: String) {
        val snack = Snackbar.make(viewDataBinding.root, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(ContextCompat.getColor(this, R.color.colorPrimary))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .setActionTextColor(ContextCompat.getColor(this, R.color.white))
        snack.view.findViewById<TextView>(R.id.snackbar_text).textAlignment =
            View.TEXT_ALIGNMENT_CENTER
        snack.show()
    }

    fun showError(messageError: String) {
        val snack = Snackbar.make(viewDataBinding.root, messageError, Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(this, R.color.errorColor))
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .setActionTextColor(ContextCompat.getColor(this, R.color.white))
        snack.view.findViewById<TextView>(R.id.snackbar_text).textAlignment =
            View.TEXT_ALIGNMENT_CENTER
        snack.show()
    }

    fun createNotification(title: String, text: String) {
        val notificationId = 3
        val builder =
            NotificationCompat.Builder(
                this,
                "1"
            ).setSmallIcon(R.drawable.ic_done)
                //.setLargeIcon(getBitmapFromURL(photoLink))
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }
        //notification.flags = Notification.FLAG_AUTO_CANCEL
    }
}