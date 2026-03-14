package com.bunbeauty.fooddeliveryadmin.main

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.presentation.designsystem.compose.AdminSnackbarVisuals
import com.bunbeauty.presentation.feature.MainScreen
import com.bunbeauty.presentation.viewmodel.main.Main
import com.bunbeauty.presentation.viewmodel.main.MainViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity :
    AppCompatActivity(R.layout.layout_compose),
    MessageHost {
    val viewModel: MainViewModel by viewModel()

    private val requestPermissionLauncher by lazy {
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
        } else {
            WindowCompat.setDecorFitsSystemWindows(window, true)
        }
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        setContent {
            val mainState by viewModel.state.collectAsStateWithLifecycle()
            val snackbarHostState = remember { SnackbarHostState() }

            MainScreen(
                mainState = mainState,
                snackbarHostState = snackbarHostState,
            )

            val events by viewModel.events.collectAsStateWithLifecycle()
            LaunchedEffect(events) {
                handleEventList(
                    events = events,
                    snackbarHostState = snackbarHostState,
                )
            }
        }

        checkNotificationPermission()
    }

    override fun showInfoMessage(text: String) {
        viewModel.onAction(Main.Action.ShowInfoMessage(text))
    }

    override fun showErrorMessage(text: String) {
        viewModel.onAction(Main.Action.ShowErrorMessage(text))
    }

    private fun handleEventList(
        events: List<Main.Event>,
        snackbarHostState: SnackbarHostState,
    ) {
        events.forEach { event ->
            when (event) {
                is Main.Event.ShowMessageEvent -> {
                    lifecycleScope.launch {
                        snackbarHostState.showSnackbar(
                            AdminSnackbarVisuals(event.message),
                        )
                    }
                }
            }
        }

        viewModel.consumeEvents(events)
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
