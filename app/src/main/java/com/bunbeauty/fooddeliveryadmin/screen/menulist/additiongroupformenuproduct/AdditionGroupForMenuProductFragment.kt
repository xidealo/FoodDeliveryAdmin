package com.bunbeauty.fooddeliveryadmin.screen.menulist.additiongroupformenuproduct

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.AdditionGroupForMenuProduct
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.AdditionGroupForMenuProductViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.bunbeauty.fooddeliveryadmin.R

class AdditionGroupForMenuProductFragment :
    BaseComposeFragment<AdditionGroupForMenuProduct.DataState, AdditionGroupForMenuProductViewState, AdditionGroupForMenuProduct.Action, AdditionGroupForMenuProduct.Event>() {

    override val viewModel: AdditionGroupForMenuProductViewModel by viewModel()

    @Composable
    override fun Screen(
        state: AdditionGroupForMenuProductViewState,
        onAction: (AdditionGroupForMenuProduct.Action) -> Unit
    ) {
        AdditionGroupForMenuProductScreen(state = state, onAction = onAction)
    }

    @Composable
    fun AdditionGroupForMenuProductScreen(
        state: AdditionGroupForMenuProductViewState,
        onAction: (AdditionGroupForMenuProduct.Action) -> Unit
    ) {
        AdminScaffold(
            title = stringResource(R.string.title_addition_group_for_menu_product),
            backActionClick = {
                onAction(AdditionGroupForMenuProduct.Action.OnBackClick)
            }
        ) {
            Column {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.Absolute.spacedBy(8.dp),
                    contentPadding = PaddingValues(
                        top = 16.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = AdminTheme.dimensions.scrollScreenBottomSpace
                    )
                ) {
                    items(
                        items = state.additionGroupWithAdditionsList,
                        key = { additionGroupWithAddition -> additionGroupWithAddition.uuid }
                    ) { selectableCategory ->
//                        SelectableItemView(
//                            selectableItem = SelectableItem(
//                                uuid = selectableCategory.uuid,
//                                title = selectableCategory.name,
//                                isSelected = selectableCategory.selected
//                            ),
//                            onClick = {
//                                onAction(
//                                    AdditionGroupForMenuProduct.Action.OnAdditionGroupClick(
//                                        uuid = selectableCategory.uuid,
//                                        selected = selectableCategory.selected
//                                    )
//                                )
//                            },
//                            elevated = false,
//                            isClickable = true
//                        )
                    }
                }
            }
        }
    }

    @Composable
    override fun mapState(state: AdditionGroupForMenuProduct.DataState): AdditionGroupForMenuProductViewState {
        return AdditionGroupForMenuProductViewState(
            additionGroupWithAdditionsList = emptyList()
        )
    }

    override fun handleEvent(event: AdditionGroupForMenuProduct.Event) {
        when (event) {
            AdditionGroupForMenuProduct.Event.Back -> {
                findNavController().popBackStack()
            }

            is AdditionGroupForMenuProduct.Event.OnAdditionGroupClick -> TODO()
        }
    }

    val additionGroupForMenuProductViewState = AdditionGroupForMenuProductViewState(
        additionGroupWithAdditionsList = listOf(
            AdditionGroupForMenuProductViewState.AdditionGroupWithAdditions(
                uuid = "",
                name = "",
                additionNameList = "",
            )
        )
    )

    @Composable
    @Preview
    fun AdditionGroupForMenuProductScreenPreview() {
        AdminTheme {
            AdditionGroupForMenuProductScreen(
                state = additionGroupForMenuProductViewState,
                onAction = {}
            )
        }
    }
}