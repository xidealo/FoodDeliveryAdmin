package com.bunbeauty.fooddeliveryadmin.ui.main

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
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

    fun createNotification(){
        val notificationId = 3
        //создание notification
        val builder =
            NotificationCompat.Builder(
                this,
                "1"
            )
                //.setSmallIcon(R.drawable.bun_beauty_icon)
                //.setLargeIcon(getBitmapFromURL(photoLink))
                .setContentTitle("Новый заказ!")
                //.setContentText("На вас подписался ${name.firstCapitalSymbol()} ${surname.firstCapitalSymbol()}")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        val notificationManager = NotificationManagerCompat.from(this)
        val notification = builder.build()
        notification.flags = Notification.FLAG_AUTO_CANCEL
        notificationManager.notify(notificationId, notification)
    }
}