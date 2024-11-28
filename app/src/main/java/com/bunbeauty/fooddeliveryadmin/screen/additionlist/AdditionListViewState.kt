package com.bunbeauty.fooddeliveryadmin.screen.additionlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.key
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.bunbeauty.domain.model.addition.Addition
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.presentation.feature.additionlist.AdditionList
import com.bunbeauty.presentation.viewmodel.base.BaseViewState
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class AdditionListViewState(
    val visibleAdditionItems: ImmutableList<AdditionFeedViewItem>,
    val hiddenAdditionItems: ImmutableList<AdditionFeedViewItem>,
    val isRefreshing: Boolean,
    val isLoading: Boolean,
    val hasError: Boolean
) : BaseViewState {

    @Immutable
    sealed interface AdditionFeedViewItem {
        val key: String

        @Immutable
        data class Title(
            override val key: String,
            val title: String
        ) : AdditionFeedViewItem

        @Immutable
        data class AdditionItem(
            override val key: String,
            val addition: AdditionViewItem
        ) : AdditionFeedViewItem
    }

    @Immutable
    data class AdditionViewItem(
        val uuid: String,
        val name: String,
        val photoLink: String,
        val iconColor: Color,
        val isVisible: Boolean
    )
}

@Composable
fun AdditionList.DataState.AdditionFeedItem.toItem(): AdditionListViewState.AdditionFeedViewItem {
    return when (this) {
        is AdditionList.DataState.AdditionFeedItem.AdditionItem ->
            AdditionListViewState
                .AdditionFeedViewItem
                .AdditionItem(
                    key = addition.uuid,
                    addition = addition.toItem()
                )

        is AdditionList.DataState.AdditionFeedItem.Title ->
            AdditionListViewState
                .AdditionFeedViewItem
                .Title(
                    key = key,
                    title = title ?: stringResource(R.string.title_addition_other_additions)
                )
    }
}

@Composable
fun Addition.toItem(): AdditionListViewState.AdditionViewItem {
    return AdditionListViewState.AdditionViewItem(
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
