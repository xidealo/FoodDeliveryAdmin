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
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentContainerBinding
import com.bunbeauty.fooddeliveryadmin.databinding.LayoutComposeBinding
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
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
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

    @Composable
    private fun MainScreen(
        mainState: Main.ViewDataState,
        snackbarHostState: SnackbarHostState,
    ) {
        Scaffold(
            modifier = Modifier.navigationBarsPadding(),
            snackbarHost = {
                AdminSnackbarHost(
                    snackbarHostState = snackbarHostState,
                    paddingBottom = AdminTheme.dimensions.snackBarPadding,
                )
            },
            bottomBar = {
                AdminNavigationBar(options = mainState.navigationBarOptions)
            },
        ) { padding ->
            Column(
                modifier =
                    Modifier
                        .padding(padding)
                        .imePadding(),
            ) {
                ConnectionErrorMessage(visible = mainState.connectionLost)
                NonWorkingDayWarningMessage(visible = mainState.nonWorkingDay)
                Box(modifier = Modifier.weight(1f)) {
                    AndroidViewBinding(factory = ::fragmentContainerFactory)
                }
            }
        }
    }

    @Composable
    private fun ConnectionErrorMessage(visible: Boolean) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(300)),
            exit = fadeOut(tween(300)),
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(AdminTheme.colors.status.negative)
                        .padding(8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = resources.getString(R.string.error_common_no_internet),
                    style = AdminTheme.typography.bodyMedium,
                    color = AdminTheme.colors.status.onStatus,
                )
            }
        }
    }

    @Composable
    private fun NonWorkingDayWarningMessage(visible: Boolean) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(300)),
            exit = fadeOut(tween(300)),
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(AdminTheme.colors.status.warning)
                        .padding(8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = resources.getString(R.string.msg_common_non_working_day),
                    style = AdminTheme.typography.bodyMedium,
                    color = AdminTheme.colors.status.onStatus,
                )
            }
        }
    }

    @Composable
    private fun AdminSnackbarHost(
        snackbarHostState: SnackbarHostState,
        paddingBottom: Dp,
    ) {
        SnackbarHost(
            hostState = snackbarHostState,
            modifier =
                Modifier
                    .padding(bottom = paddingBottom),
        ) { snackbarData ->
            (snackbarData.visuals as? AdminSnackbarVisuals)?.let { visuals ->
                val containerColor =
                    when (visuals.adminMessage.type) {
                        Main.Message.Type.INFO -> AdminTheme.colors.main.primary
                        Main.Message.Type.ERROR -> AdminTheme.colors.main.error
                    }
                val contentColor =
                    when (visuals.adminMessage.type) {
                        Main.Message.Type.INFO -> AdminTheme.colors.main.onPrimary
                        Main.Message.Type.ERROR -> AdminTheme.colors.main.onError
                    }
                Snackbar(
                    snackbarData = snackbarData,
                    containerColor = containerColor,
                    contentColor = contentColor,
                )
            }
        }
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
