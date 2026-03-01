package com.bunbeauty.presentation.feature

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bunbeauty.presentation.designsystem.compose.AdminSnackbarVisuals
import com.bunbeauty.presentation.designsystem.compose.bottombar.AdminNavigationBar
import com.bunbeauty.presentation.designsystem.compose.theme.AdminTheme
import com.bunbeauty.presentation.navigation.FoodDeliveryNavHost
import com.bunbeauty.presentation.viewmodel.main.Main
import fooddeliveryadmin.presentation.generated.resources.Res
import fooddeliveryadmin.presentation.generated.resources.error_common_no_internet
import fooddeliveryadmin.presentation.generated.resources.msg_common_non_working_day
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainScreen(
    mainState: Main.ViewDataState,
    snackbarHostState: SnackbarHostState,
) {
    Scaffold(
        modifier = Modifier
            .navigationBarsPadding()
            .imePadding(),
        snackbarHost = {
            AdminSnackbarHost(
                snackbarHostState = snackbarHostState,
                paddingBottom = AdminTheme.dimensions.snackBarPadding,
            )
        },
        bottomBar = {
            AdminNavigationBar(
                options = mainState.navigationBarOptions,
                goToOrderList = {},
                goToMenu = {},
                goToProfile = {},
            )
        },
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .padding(padding),
        ) {
            ConnectionErrorMessage(visible = mainState.connectionLost)
            NonWorkingDayWarningMessage(visible = mainState.nonWorkingDay)
            Box(modifier = Modifier.weight(1f)) {
                FoodDeliveryNavHost(
                    showInfoMessage = { string: String, i: Int ->

                    },
                    showErrorMessage = {

                    },
                    goToOrderList = {},
                    goToMenu = {},
                    goToProfile = {},
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
