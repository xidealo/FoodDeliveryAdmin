package com.bunbeauty.fooddeliveryadmin.screen.menulist.additiongroupformenuproduct

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bunbeauty.common.Constants
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCardDefaults.noCornerCardShape
import com.bunbeauty.fooddeliveryadmin.compose.element.topbar.AdminHorizontalDivider
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.fooddeliveryadmin.navigation.navigateSafe
import com.bunbeauty.fooddeliveryadmin.screen.additionlist.AdditionListFragmentDirections
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.AdditionGroupForMenuProductList
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.AdditionGroupForMenuProductList.DataState.AdditionGroupForMenuProduct
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.AdditionGroupForMenuProductListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class AdditionGroupForMenuProductListFragment :
    BaseComposeFragment<AdditionGroupForMenuProductList.DataState, AdditionGroupForMenuProductListViewState, AdditionGroupForMenuProductList.Action, AdditionGroupForMenuProductList.Event>() {

    override val viewModel: AdditionGroupForMenuProductListViewModel by viewModel()
    private val additionGroupForMenuProductFragmentArgs: AdditionGroupForMenuProductListFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onAction(
            AdditionGroupForMenuProductList.Action.Init(
                menuProductUuid = additionGroupForMenuProductFragmentArgs.menuProductUuid
            )
        )
    }

    @Composable
    override fun Screen(
        state: AdditionGroupForMenuProductListViewState,
        onAction: (AdditionGroupForMenuProductList.Action) -> Unit
    ) {
        AdditionGroupForMenuProductScreen(state = state, onAction = onAction)
    }

    @Composable
    fun AdditionGroupForMenuProductScreen(
        state: AdditionGroupForMenuProductListViewState,
        onAction: (AdditionGroupForMenuProductList.Action) -> Unit
    ) {
        AdminScaffold(
            title = stringResource(id = R.string.title_addition_group_for_menu_product),
            backActionClick = {
                onAction(AdditionGroupForMenuProductList.Action.OnBackClick)
            },
            backgroundColor = AdminTheme.colors.main.surface
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(
                    items = state.additionGroupWithAdditionsList,
                    key = { additionGroupWithAddition -> additionGroupWithAddition.uuid }
                ) { additionGroup ->
                    Column {
                        AdditionGroupItemView(
                            additionGroup = additionGroup,
                            onClick = {
                                onAction(
                                    AdditionGroupForMenuProductList.Action.OnAdditionGroupClick(
                                        uuid = additionGroup.uuid
                                    )
                                )
                            },
                            isClickable = true
                        )
                        AdminHorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun AdditionGroupItemView(
        modifier: Modifier = Modifier,
        additionGroup: AdditionGroupForMenuProductListViewState.AdditionGroupWithAdditions,
        onClick: () -> Unit,
        isClickable: Boolean
    ) {
        AdminCard(
            modifier = modifier.fillMaxWidth(),
            onClick = onClick,
            clickable = isClickable,
            shape = noCornerCardShape,
            elevated = false
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    )
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = additionGroup.name,
                    style = AdminTheme.typography.bodyLarge,
                    color = AdminTheme.colors.main.onSurface
                )
                additionGroup.additionNameList?.let {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        text = additionGroup.additionNameList,
                        style = AdminTheme.typography.bodySmall,
                        color = AdminTheme.colors.main.onSurfaceVariant
                    )
                }
            }
        }
    }

    @Composable
    override fun mapState(state: AdditionGroupForMenuProductList.DataState): AdditionGroupForMenuProductListViewState {
        return AdditionGroupForMenuProductListViewState(
            additionGroupWithAdditionsList = state.additionGroupList.map { additionGroupForMenuProduct ->
                AdditionGroupForMenuProductListViewState.AdditionGroupWithAdditions(
                    uuid = additionGroupForMenuProduct.uuid,
                    name = additionGroupForMenuProduct.name,
                    additionNameList = additionGroupForMenuProduct.additionNameList
                )
            }
        )
    }

    override fun handleEvent(event: AdditionGroupForMenuProductList.Event) {
        when (event) {
            AdditionGroupForMenuProductList.Event.Back -> findNavController().popBackStack()

            is AdditionGroupForMenuProductList.Event.OnAdditionGroupClick -> {
                findNavController().navigateSafe(
                    AdditionGroupForMenuProductListFragmentDirections
                        .toEditAdditionGroupForMenuProductFragment(
                            menuProductUuid = additionGroupForMenuProductFragmentArgs.menuProductUuid,
                            additionGroupForMenuUuid = event.additionGroupUuid
                        )
                )
            }
        }
    }

    val additionGroupForMenuProductListViewState = AdditionGroupForMenuProductListViewState(
        additionGroupWithAdditionsList = listOf(
            AdditionGroupForMenuProductListViewState.AdditionGroupWithAdditions(
                uuid = "12321",
                name = "Вкусняшки",
                additionNameList = "Оленина Сопли Вопли"
            ),
            AdditionGroupForMenuProductListViewState.AdditionGroupWithAdditions(
                uuid = "1232112",
                name = "Не Вкусняшки",
                additionNameList = "Жижи Топли Нопли"
            )
        )
    )

    @Composable
    @Preview
    fun AdditionGroupForMenuProductScreenPreview() {
        AdminTheme {
            AdditionGroupForMenuProductScreen(
                state = additionGroupForMenuProductListViewState,
                onAction = {}
            )
        }
    }
}
