package com.bunbeauty.fooddeliveryadmin.screen.additiongrouplist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.bunbeauty.domain.model.additiongroup.AdditionGroup
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.presentation.viewmodel.base.BaseViewState
import kotlinx.collections.immutable.ImmutableList

@Stable
data class AdditionGroupListViewState(
    val visibleAdditionItems: ImmutableList<AdditionGroupItem>,
    val hiddenAdditionItems: ImmutableList<AdditionGroupItem>,
    val isRefreshing: Boolean,
    val isLoading: Boolean
) : BaseViewState {
    @Stable
    data class AdditionGroupItem(
        val uuid: String,
        val name: String,
        val iconColor: Color,
        val isVisible: Boolean
    )
}

@Composable
fun AdditionGroup.toItem(): AdditionGroupListViewState.AdditionGroupItem {
    return AdditionGroupListViewState.AdditionGroupItem(
        name = name,
        uuid = uuid,
        iconColor = if (isVisible) {
            AdminTheme.colors.main.primary
        } else {
            AdminTheme.colors.main.onSurfaceVariant
        },
        isVisible = isVisible
    )
}
