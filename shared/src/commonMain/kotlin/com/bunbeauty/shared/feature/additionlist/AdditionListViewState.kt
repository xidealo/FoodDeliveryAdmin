package com.bunbeauty.shared.feature.additionlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.bunbeauty.domain.model.addition.Addition
import com.bunbeauty.shared.designsystem.compose.theme.AdminTheme
import com.bunbeauty.shared.viewmodel.base.BaseViewState
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.title_addition_other_additions
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.stringResource

@Immutable
data class AdditionListViewState(
    val visibleAdditionItems: ImmutableList<AdditionFeedViewItem>,
    val hiddenAdditionItems: ImmutableList<AdditionFeedViewItem>,
    val isRefreshing: Boolean,
    val isLoading: Boolean,
    val hasError: Boolean,
    val isSearchEnabled: Boolean,
    val searchQuery: String,
    val searchResultList: ImmutableList<AdditionFeedViewItem>?,
) : BaseViewState {
    @Immutable
    sealed interface AdditionFeedViewItem {
        val key: String

        @Immutable
        data class Title(
            override val key: String,
            val title: String,
        ) : AdditionFeedViewItem

        @Immutable
        data class AdditionItem(
            override val key: String,
            val addition: AdditionViewItem,
        ) : AdditionFeedViewItem
    }

    @Immutable
    data class AdditionViewItem(
        val uuid: String,
        val name: String,
        val photoLink: String,
        val iconColor: Color,
        val isVisible: Boolean,
    )
}

@Composable
internal fun AdditionList.DataState.toViewState(): AdditionListViewState =
    AdditionListViewState(
        visibleAdditionItems =
            visibleAdditions
                .map { additionFeedItem ->
                    additionFeedItem.toItem()
                }.toImmutableList(),
        hiddenAdditionItems =
            hiddenAdditions
                .map { additionFeedItem ->
                    additionFeedItem.toItem()
                }.toImmutableList(),
        isRefreshing = isRefreshing,
        isLoading = isLoading,
        hasError = hasError,
        isSearchEnabled = isSearchEnabled,
        searchQuery = searchQuery,
        searchResultList = getSearchResultList(),
    )

@Composable
private fun AdditionList.DataState.getSearchResultList(): ImmutableList<AdditionListViewState.AdditionFeedViewItem>? {
    val normalizedSearchQuery = searchQuery.trim()
    if (!isSearchEnabled || normalizedSearchQuery.isEmpty()) {
        return null
    }

    return (visibleAdditions + hiddenAdditions)
        .filterIsInstance<AdditionList.DataState.AdditionFeedItem.AdditionItem>()
        .filter { additionFeedItem ->
            additionFeedItem.addition.name.contains(normalizedSearchQuery, ignoreCase = true)
        }.map { additionFeedItem ->
            additionFeedItem.toItem()
        }.toImmutableList()
}

@Composable
private fun AdditionList.DataState.AdditionFeedItem.toItem(): AdditionListViewState.AdditionFeedViewItem =
    when (this) {
        is AdditionList.DataState.AdditionFeedItem.AdditionItem ->
            AdditionListViewState
                .AdditionFeedViewItem
                .AdditionItem(
                    key = addition.uuid,
                    addition = addition.toItem(),
                )

        is AdditionList.DataState.AdditionFeedItem.Title ->
            AdditionListViewState
                .AdditionFeedViewItem
                .Title(
                    key = key,
                    title = title ?: stringResource(Res.string.title_addition_other_additions),
                )
    }

@Composable
private fun Addition.toItem(): AdditionListViewState.AdditionViewItem =
    AdditionListViewState.AdditionViewItem(
        name = name,
        photoLink = photoLink,
        uuid = uuid,
        iconColor =
            if (isVisible) {
                AdminTheme.colors.main.primary
            } else {
                AdminTheme.colors.main.onSurfaceVariant
            },
        isVisible = isVisible,
    )
