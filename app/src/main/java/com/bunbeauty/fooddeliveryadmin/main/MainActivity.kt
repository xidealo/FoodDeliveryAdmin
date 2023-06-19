package com.bunbeauty.fooddeliveryadmin.main

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.app.NotificationManagerCompat
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
import com.bunbeauty.presentation.view_model.main.AdminMessageType
import com.bunbeauty.presentation.view_model.main.AdminNavigationBarItem
import com.bunbeauty.presentation.view_model.main.MainUiState
import com.bunbeauty.presentation.view_model.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.layout_compose), MessageHost {

    val viewModel: MainViewModel by viewModels()

    private val viewBinding: LayoutComposeBinding by viewBinding(LayoutComposeBinding::bind)

    private val requestPermissionLauncher by lazy {
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        viewBinding.root.setContentWithTheme {
            val mainState by viewModel.mainUiState.collectAsStateWithLifecycle()
            val snackbarHostState = remember { SnackbarHostState() }

            LaunchedEffect(mainState.eventList) {
                handleEventList(
                    eventList = mainState.eventList,
                    snackbarHostState = snackbarHostState,
                )
            }
            MainScreen(
                mainUiState = mainState,
                snackbarHostState = snackbarHostState
            )
        }

        checkNotificationPermission()
    }

    override fun showInfoMessage(text: String) {
        viewModel.showInfoMessage(text)
    }

    override fun showErrorMessage(text: String) {
        viewModel.showErrorMessage(text)
    }

    @Composable
    private fun MainScreen(mainUiState: MainUiState, snackbarHostState: SnackbarHostState) {
        Scaffold(
            snackbarHost = {
                AdminSnackbarHost(snackbarHostState)
            },
            bottomBar = {
                AdminNavigationBar(options = mainUiState.navigationBarOptions)
            }
        ) { padding ->
            Column(modifier = Modifier.padding(padding)) {
                ConnectionErrorMessage(visible = mainUiState.connectionLost)
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
            exit = fadeOut(tween(300))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AdminTheme.colors.mainColors.error)
                    .padding(8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = resources.getString(R.string.error_common_no_internet),
                    style = AdminTheme.typography.bodyMedium,
                    color = AdminTheme.colors.mainColors.onError
                )
            }
        }
    }

    @Composable
    private fun AdminSnackbarHost(snackbarHostState: SnackbarHostState) {
        SnackbarHost(hostState = snackbarHostState) { snackbarData ->
            (snackbarData.visuals as? AdminSnackbarVisuals)?.let { visuals ->
                val containerColor = when (visuals.adminMessage.type) {
                    AdminMessageType.INFO -> AdminTheme.colors.mainColors.primary
                    AdminMessageType.ERROR -> AdminTheme.colors.mainColors.error
                }
                val contentColor = when (visuals.adminMessage.type) {
                    AdminMessageType.INFO -> AdminTheme.colors.mainColors.onPrimary
                    AdminMessageType.ERROR -> AdminTheme.colors.mainColors.onError
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
        eventList: List<MainUiState.Event>,
        snackbarHostState: SnackbarHostState
    ) {
        eventList.forEach { event ->
            when (event) {
                is MainUiState.Event.ShowMessageEvent -> {
                    lifecycleScope.launch {
                        snackbarHostState.showSnackbar(
                            AdminSnackbarVisuals(event.message)
                        )
                    }
                }
            }
        }

        viewModel.consumeEventList(eventList)
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
        attachToParent: Boolean
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
                val navigationBarItem = when (destination.id) {
                    R.id.ordersFragment -> AdminNavigationBarItem.ORDERS
                    R.id.statisticFragment -> AdminNavigationBarItem.STATISTIC
                    R.id.menuFragment -> AdminNavigationBarItem.MENU
                    R.id.settingsFragment -> AdminNavigationBarItem.SETTINGS
                    else -> null
                }
                viewModel.onNavDestinationUpdated(navigationBarItem, controller)
            }
        }
    }
}
