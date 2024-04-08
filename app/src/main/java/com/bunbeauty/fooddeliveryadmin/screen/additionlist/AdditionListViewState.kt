package com.bunbeauty.fooddeliveryadmin.screen.additionlist

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.bunbeauty.domain.model.addition.Addition
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.presentation.viewmodel.base.BaseViewState
import kotlinx.collections.immutable.ImmutableList

@Stable
data class AdditionListViewState(
    val visibleAdditionItems: ImmutableList<AdditionItem>,
    val hiddenAdditionItems: ImmutableList<AdditionItem>,
    val isRefreshing: Boolean,
    val isLoading: Boolean
) : BaseViewState {
    @Stable
    data class AdditionItem(
        val uuid: String,
        val name: String,
        val photoLink: String,
        @DrawableRes val icon: Int,
        val iconColor: Color
    )
}

@Composable
fun Addition.toItem(): AdditionListViewState.AdditionItem {
    return AdditionListViewState.AdditionItem(
        name = name,
        photoLink = photoLink,
        uuid = uuid,
        icon = R.drawable.ic_visible,
        iconColor = if (isVisible) {
            AdminTheme.colors.main.primary
        } else {
            AdminTheme.colors.main.onSurfaceVariant
        }
    )
}
