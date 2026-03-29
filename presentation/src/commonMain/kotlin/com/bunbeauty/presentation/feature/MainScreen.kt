package com.bunbeauty.presentation.feature

import androidx.compose.ui.unit.Dp
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bunbeauty.presentation.designsystem.compose.AdminSnackbarVisuals
import com.bunbeauty.presentation.designsystem.compose.bottombar.AdminNavigationBar
import com.bunbeauty.presentation.designsystem.compose.theme.AdminTheme
import com.bunbeauty.presentation.feature.menu.navigation.MenuScreenDestination
import com.bunbeauty.presentation.feature.orderlist.navigation.OrderListScreenNavigation
import com.bunbeauty.presentation.feature.profile.navigation.ProfileScreenDestination
import com.bunbeauty.presentation.navigation.FoodDeliveryNavHost
import com.bunbeauty.presentation.viewmodel.main.Main
import com.bunbeauty.presentation.viewmodel.main.MainViewModel
import fooddeliveryadmin.presentation.generated.resources.Res
import fooddeliveryadmin.presentation.generated.resources.error_common_no_internet
import fooddeliveryadmin.presentation.generated.resources.msg_common_non_working_day
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(viewModel: MainViewModel = koinViewModel()) {
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

    Scaffold(
        modifier =
            Modifier
                .navigationBarsPadding()
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
