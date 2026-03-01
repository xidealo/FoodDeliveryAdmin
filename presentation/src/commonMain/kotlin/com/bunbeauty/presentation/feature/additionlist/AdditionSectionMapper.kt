package com.bunbeauty.presentation.feature.additionlist

import com.bunbeauty.domain.feature.additionlist.GroupedAdditionList

fun GroupedAdditionList.toAdditionFeedItemList(): List<AdditionList.DataState.AdditionFeedItem> =
    buildList {
        add(toMenuTitleItem())
        addAll(toAdditionItemList())
    }

private fun GroupedAdditionList.toMenuTitleItem(): AdditionList.DataState.AdditionFeedItem.Title =
    AdditionList.DataState.AdditionFeedItem.Title(
        title = title,
        key = uuid,
    )

private fun GroupedAdditionList.toAdditionItemList(): List<AdditionList.DataState.AdditionFeedItem.AdditionItem> =
    additionList.map { addition ->
        AdditionList.DataState.AdditionFeedItem.AdditionItem(addition = addition)
    }
