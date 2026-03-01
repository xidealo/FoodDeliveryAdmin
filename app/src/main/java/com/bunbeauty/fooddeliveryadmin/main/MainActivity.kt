package com.bunbeauty.fooddeliveryadmin.main

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.FloatingWindow
import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.setContentWithTheme
import com.bunbeauty.presentation.designsystem.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentContainerBinding
import com.bunbeauty.fooddeliveryadmin.databinding.LayoutComposeBinding
import com.bunbeauty.presentation.designsystem.compose.AdminSnackbarVisuals
import com.bunbeauty.presentation.viewmodel.main.Main
import com.bunbeauty.presentation.viewmodel.main.MainViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity :
    AppCompatActivity(R.layout.layout_compose),
    MessageHost {
    val viewModel: MainViewModel by viewModel()

    private val viewBinding: LayoutComposeBinding by viewBinding(LayoutComposeBinding::bind)

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

        viewBinding.root.setContentWithTheme {
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

    private fun fragmentContainerFactory(
        inflater: LayoutInflater,
        parent: ViewGroup,
        attachToParent: Boolean,
    ): FragmentContainerBinding =
        FragmentContainerBinding.inflate(inflater, parent, attachToParent).also {
            setupNavigationListener()
        }

    private fun setupNavigationListener() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.containerFcv) as NavHostFragment
        val navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { controller, destination, _ ->
            if (destination !is FloatingWindow) {
                val navigationBarItem =
                    when (destination.id) {
                        R.id.ordersFragment -> Main.NavigationBarItem.ORDERS
                        R.id.menuFragment -> Main.NavigationBarItem.MENU
                        R.id.profileFragment -> Main.NavigationBarItem.PROFILE
                        else -> null
                    }
                viewModel.onAction(Main.Action.UpdateNavDestination(navigationBarItem, controller))
            }
        }
    }
}
