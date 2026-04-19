package com.bunbeauty.shared.feature

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bunbeauty.shared.designsystem.compose.LocalBottomBarPadding
import com.bunbeauty.shared.designsystem.compose.AdminSnackbarVisuals
import com.bunbeauty.shared.designsystem.compose.bottombar.AdminNavigationBar
import com.bunbeauty.shared.designsystem.compose.theme.AdminTheme
import com.bunbeauty.shared.feature.menu.navigation.MenuScreenDestination
import com.bunbeauty.shared.feature.orderlist.navigation.OrderListScreenNavigation
import com.bunbeauty.shared.feature.profile.navigation.ProfileScreenDestination
import com.bunbeauty.shared.navigation.FoodDeliveryNavHost
import com.bunbeauty.shared.viewmodel.main.Main
import com.bunbeauty.shared.viewmodel.main.MainViewModel
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.error_common_no_internet
import fooddeliveryadmin.shared.generated.resources.msg_common_non_working_day
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(
    modifier: Modifier,
    viewModel: MainViewModel = koinViewModel()) {
    val mainState by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var snackbarPaddingBottom by remember {
        mutableStateOf(0.dp)
    }

    HandleSnackbarMessages(
        snackbarMessages = viewModel.snackbarMessages,
        snackbarHostState = snackbarHostState,
        onShowMessage = { message ->
            snackbarPaddingBottom = message.paddingBottom ?: 0.dp
        },
    )
    val navController = rememberNavController()

    HandleNavigation(
        onAction = viewModel::onAction,
        navHostController = navController,
    )

    val localBottomBarPadding =
        with(LocalDensity.current) {
            WindowInsets.navigationBars.getBottom(this).toDp()
        }

    CompositionLocalProvider(LocalBottomBarPadding provides localBottomBarPadding) {
        Scaffold(
            modifier =
                modifier
                    .imePadding(),
            snackbarHost = {
                AdminSnackbarHost(
                    snackbarHostState = snackbarHostState,
                    paddingBottom = snackbarPaddingBottom,
                )
            },
            bottomBar = {
                AdminNavigationBar(
                    options = mainState.navigationBarOptions,
                    navHostController = navController,
                )
            },
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize(),
            ) {
                ConnectionErrorMessage(visible = mainState.connectionLost)
                NonWorkingDayWarningMessage(visible = mainState.nonWorkingDay)
                Box(modifier = Modifier.weight(1f)) {
                    FoodDeliveryNavHost(
                        showInfoMessage = { text: String, paddingBottom: Dp ->
                            viewModel.onAction(Main.Action.ShowInfoMessage(text, paddingBottom))
                        },
                        showErrorMessage = { text ->
                            viewModel.onAction(Main.Action.ShowErrorMessage(text))
                        },
                        navController = navController,
                    )
                }
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
                text = stringResource(Res.string.error_common_no_internet),
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
                text = stringResource(Res.string.msg_common_non_working_day),
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

@Composable
private fun HandleSnackbarMessages(
    snackbarMessages: Flow<Main.Message>,
    snackbarHostState: SnackbarHostState,
    onShowMessage: (Main.Message) -> Unit,
) {
    LaunchedEffect(Unit) {
        snackbarMessages.collectLatest { message ->
            onShowMessage(message)
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(
                visuals =
                    AdminSnackbarVisuals(
                        adminMessage = message,
                    ),
            )
        }
    }
}

@Composable
private fun HandleNavigation(
    onAction: (Main.Action) -> Unit,
    navHostController: NavHostController,
) {
    LaunchedEffect(Unit) {
        navHostController.currentBackStackEntryFlow.collect { navBackStackEntry ->
            when {
                navBackStackEntry.destination.hasRoute(
                    route = OrderListScreenNavigation::class.qualifiedName.orEmpty(),
                    arguments = null,
                ) -> {
                    onAction(
                        Main.Action.UpdateNavDestination(
                            Main.NavigationBarItem.ORDERS,
                        ),
                    )
                }

                navBackStackEntry.destination.hasRoute(
                    route = MenuScreenDestination::class.qualifiedName.orEmpty(),
                    arguments = null,
                ) -> {
                    onAction(
                        Main.Action.UpdateNavDestination(
                            Main.NavigationBarItem.MENU,
                        ),
                    )
                }

                navBackStackEntry.destination.hasRoute(
                    route = ProfileScreenDestination::class.qualifiedName.orEmpty(),
                    arguments = null,
                ) -> {
                    onAction(
                        Main.Action.UpdateNavDestination(
                            Main.NavigationBarItem.PROFILE,
                        ),
                    )
                }

                else ->
                    onAction(
                        Main.Action.UpdateNavDestination(
                            null,
                        ),
                    )
            }
        }
    }
}
