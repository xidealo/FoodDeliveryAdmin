package com.bunbeauty.fooddeliveryadmin

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Router @Inject constructor() {

    private var activity: WeakReference<AppCompatActivity>? = null

    @IdRes
    private var navHostId: Int? = null

    fun attach(activity: AppCompatActivity, @IdRes navHostId: Int) {
        this.activity = WeakReference(activity)
        this.navHostId = navHostId
    }

    fun navigateUp() {
        if (navHostId == null) return

        activity?.let { activity ->
            hideKeyboard(activity.get())
            activity.get()?.findNavController(navHostId!!)?.navigateUp()
        }
    }

    fun navigate(navDirections: NavDirections) {
        val navHostId = navHostId ?: return

        try {
            hideKeyboard(activity?.get())
            activity?.get()?.findNavController(navHostId)?.navigate(navDirections)
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    fun navigate(navDirectionsId: Int, args: Bundle?) {
        val navHostId = navHostId ?: return

        try {
            hideKeyboard(activity?.get())
            activity?.get()?.findNavController(navHostId)?.navigate(navDirectionsId, args)
        } catch (exception: Exception) {
            Log.d("test1", "navigate " + exception.message)
            exception.printStackTrace()
        }
    }

    fun detach() {
        activity = null
        navHostId = null
    }

    private fun hideKeyboard(activity: AppCompatActivity?) {
        activity?.currentFocus?.also { view ->
            val inputMethodManager =
                activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
            inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}