package com.bunbeauty.fooddeliveryadmin.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.zIndex
import com.bunbeauty.fooddeliveryadmin.compose.element.topbar.AdminTopBar
import com.bunbeauty.fooddeliveryadmin.compose.element.topbar.AdminTopBarAction
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class,
)
@Composable
fun AdminScaffold(
    title: String? = null,
    backActionClick: (() -> Unit)? = null,
    topActions: List<AdminTopBarAction> = emptyList(),
    backgroundColor: Color = AdminTheme.colors.main.background,
    actionButton: @Composable () -> Unit = {},
    actionButtonPosition: FabPosition = FabPosition.Center,
    pullRefreshEnabled: Boolean = false,
    refreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    content: (@Composable () -> Unit),
) {
    val appBarState = rememberTopAppBarState()
    val behavior = TopAppBarDefaults.pinnedScrollBehavior(appBarState)
    val scrollBehavior = remember { behavior }
    val pullRefreshState =
        rememberPullRefreshState(
            refreshing = refreshing,
            onRefresh = onRefresh,
        )
    Scaffold(
        modifier =
            Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .pullRefresh(
                    state = pullRefreshState,
                    enabled = pullRefreshEnabled,
                ),
        topBar = {
            AdminTopBar(
                title = title,
                backActionClick = backActionClick,
                scrollBehavior = scrollBehavior,
                actions = topActions,
            )
        },
        containerColor = AdminTheme.colors.main.background,
        floatingActionButton = actionButton,
        floatingActionButtonPosition = actionButtonPosition,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        Box(
            modifier =
                Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .background(backgroundColor),
        ) {
            content()
            PullRefreshIndicator(
                refreshing = refreshing,
                state = pullRefreshState,
                modifier =
                    Modifier
                        .align(Alignment.TopCenter)
                        .zIndex(1F),
                backgroundColor = AdminTheme.colors.main.surface,
                contentColor = AdminTheme.colors.main.primary,
            )
        }
    }
}
