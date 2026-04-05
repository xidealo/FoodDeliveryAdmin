package com.bunbeauty.presentation.designsystem.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.bunbeauty.presentation.designsystem.compose.element.topbar.AdminTopBar
import com.bunbeauty.presentation.designsystem.compose.element.topbar.AdminTopBarAction
import com.bunbeauty.presentation.designsystem.compose.theme.AdminTheme

// TODO ADD PULL TO REFRESH
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScaffold(
    title: String? = null,
    backActionClick: (() -> Unit)? = null,
    topActions: List<AdminTopBarAction> = emptyList(),
    backgroundColor: Color = AdminTheme.colors.main.background,
    actionButton: @Composable () -> Unit = {},
    pullRefreshEnabled: Boolean = false,
    refreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    floatingActionButtonPosition: Alignment = Alignment.BottomCenter,
    content: (@Composable () -> Unit),
) {
    val appBarState = rememberTopAppBarState()
    val behavior = TopAppBarDefaults.pinnedScrollBehavior(appBarState)
    val scrollBehavior = remember { behavior }
    val pullToRefreshState = rememberPullToRefreshState()

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .background(backgroundColor),
    ) {
        Column(
            Modifier
                .fillMaxSize(),
        ) {
            AdminTopBar(
                title = title,
                backActionClick = backActionClick,
                scrollBehavior = scrollBehavior,
                actions = topActions,
            )
            if (pullRefreshEnabled) {
                PullToRefreshBox(
                    isRefreshing = refreshing,
                    onRefresh = onRefresh,
                    state = pullToRefreshState,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    indicator = {
                        PullToRefreshDefaults.Indicator(
                            modifier = Modifier.align(Alignment.TopCenter),
                            isRefreshing = refreshing,
                            state = pullToRefreshState,
                        )
                    },
                ) {
                    content()
                }
            } else {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .weight(1f),
                ) {
                    content()
                }
            }
        }

        Box(
            modifier =
                Modifier
                    .align(floatingActionButtonPosition)
                    .padding(
                        bottom = 20.dp,
                        end =
                            if (floatingActionButtonPosition == Alignment.BottomEnd) {
                                16.dp
                            } else {
                                0.dp
                            },
                    ),
        ) {
            actionButton()
        }
    }
}
