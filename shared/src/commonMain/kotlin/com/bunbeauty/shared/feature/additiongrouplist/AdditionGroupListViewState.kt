package com.bunbeauty.shared.feature.additiongrouplist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.bunbeauty.domain.model.additiongroup.AdditionGroup
import com.bunbeauty.shared.designsystem.compose.theme.AdminTheme
import com.bunbeauty.shared.viewmodel.base.BaseViewState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Immutable
data class AdditionGroupListViewState(
    val visibleAdditionItems: ImmutableList<AdditionGroupItem>,
    val hiddenAdditionItems: ImmutableList<AdditionGroupItem>,
    val isRefreshing: Boolean,
    val isLoading: Boolean,
    val isSearchEnabled: Boolean,
    val searchQuery: String,
    val searchResultList: ImmutableList<AdditionGroupItem>?,
) : BaseViewState {
    @Immutable
    data class AdditionGroupItem(
        val uuid: String,
        val name: String,
        val iconColor: Color,
        val isVisible: Boolean,
    )
}

@Composable
internal fun AdditionGroupList.DataState.toViewState(): AdditionGroupListViewState =
    AdditionGroupListViewState(
        visibleAdditionItems =
            visibleAdditionGroups
                .map { additionGroup ->
                    additionGroup.toItem()
                }.toImmutableList(),
        hiddenAdditionItems =
            hiddenAdditionGroups
                .map { additionGroup ->
                    additionGroup.toItem()
                }.toImmutableList(),
        isRefreshing = isRefreshing,
        isLoading = isLoading,
        isSearchEnabled = isSearchEnabled,
        searchQuery = searchQuery,
        searchResultList = getSearchResultList(),
    )

@Composable
private fun AdditionGroupList.DataState.getSearchResultList(): ImmutableList<AdditionGroupListViewState.AdditionGroupItem>? {
    val normalizedSearchQuery = searchQuery.trim()
    if (!isSearchEnabled || normalizedSearchQuery.isEmpty()) {
        return null
    }

    return (visibleAdditionGroups + hiddenAdditionGroups)
        .filter { additionGroup ->
            additionGroup.name.contains(normalizedSearchQuery, ignoreCase = true)
        }.map { additionGroup ->
            additionGroup.toItem()
        }.toImmutableList()
}

@Composable
private fun AdditionGroup.toItem(): AdditionGroupListViewState.AdditionGroupItem =
    AdditionGroupListViewState.AdditionGroupItem(
        name = name,
        uuid = uuid,
        iconColor =
            if (isVisible) {
                AdminTheme.colors.main.primary
            } else {
                AdminTheme.colors.main.onSurfaceVariant
            },
        isVisible = isVisible,
    )
