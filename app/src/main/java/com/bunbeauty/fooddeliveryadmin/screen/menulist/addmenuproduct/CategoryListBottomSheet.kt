package com.bunbeauty.fooddeliveryadmin.screen.menulist.addmenuproduct

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.element.SelectableItemView
import com.bunbeauty.fooddeliveryadmin.compose.element.bottomsheet.AdminBottomSheetContent
import com.bunbeauty.fooddeliveryadmin.compose.element.bottomsheet.AdminModalBottomSheet
import com.bunbeauty.presentation.feature.menulist.addmenuproduct.AddMenuProduct

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryListBottomSheet(
    show: Boolean,
    onAction: (AddMenuProduct.Action) -> Unit,
    selectableCategories: List<AddMenuProductViewState.CategoryItem>,
    modifier: Modifier = Modifier
) {
    if (show) {
        AdminModalBottomSheet(
            modifier = modifier,
            onDismissRequest = {
                onAction(AddMenuProduct.Action.OnHideCategoriesClick)
            }
        ) {
            CategoryListContent(
                selectableCategories = selectableCategories,
                onAction = onAction
            )
        }
    }
}

@Composable
private fun ColumnScope.CategoryListContent(
    selectableCategories: List<AddMenuProductViewState.CategoryItem>,
    onAction: (AddMenuProduct.Action) -> Unit
) {
    AdminBottomSheetContent(titleResId = R.string.title_categories) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp),
            verticalArrangement = Arrangement.Absolute.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(
                items = selectableCategories,
                key = { question -> question.uuid }
            ) { selectableCategory ->
                SelectableItemView(
                    title = selectableCategory.name,
                    onClick = {
                        onAction(
                            AddMenuProduct.Action.OnCategoryClick(
                                uuid = selectableCategory.uuid,
                                selected = selectableCategory.selected
                            )
                        )
                    },
                    elevated = false,
                    isSelected = selectableCategory.selected,
                    isClickable = true
                )
            }
        }
    }
}
