package com.bunbeauty.fooddeliveryadmin.screen.additionlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.bunbeauty.domain.model.addition.Addition
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.presentation.viewmodel.base.BaseViewState
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class AdditionListViewState(
    val visibleAdditionItems: ImmutableList<AdditionItem>,
    val hiddenAdditionItems: ImmutableList<AdditionItem>,
    val isRefreshing: Boolean,
    val isLoading: Boolean,
    val hasError: Boolean
) : BaseViewState {
    @Immutable
    data class AdditionItem(
        val uuid: String,
        val name: String,
        val photoLink: String,
        val iconColor: Color,
        val isVisible: Boolean
    )
}

@Composable
fun Addition.toItem(): AdditionListViewState.AdditionItem {
    return AdditionListViewState.AdditionItem(
        name = name,
        photoLink = photoLink,
        uuid = uuid,
        iconColor = if (isVisible) {
            AdminTheme.colors.main.primary
        } else {
            AdminTheme.colors.main.onSurfaceVariant
        },
        isVisible = isVisible
    )
}
