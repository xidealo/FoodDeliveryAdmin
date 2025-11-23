package com.bunbeauty.fooddeliveryadmin.screen.menulist.additiongroupformenuproduct

import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bunbeauty.domain.model.additiongroup.AdditionGroupForMenuProduct
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.DragDropList
import com.bunbeauty.fooddeliveryadmin.compose.element.button.FloatingButton
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCardDefaults.noCornerCardShape
import com.bunbeauty.fooddeliveryadmin.compose.element.topbar.AdminHorizontalDivider
import com.bunbeauty.fooddeliveryadmin.compose.element.topbar.AdminTopBarAction
import com.bunbeauty.fooddeliveryadmin.compose.screen.ErrorScreen
import com.bunbeauty.fooddeliveryadmin.compose.screen.LoadingScreen
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.fooddeliveryadmin.navigation.navigateSafe
import com.bunbeauty.fooddeliveryadmin.screen.menulist.additiongroupformenuproduct.createadditiongroupformenuproduct.CreateAdditionGroupForMenuProductFragment.Companion.CREATE_ADDITION_GROUP
import com.bunbeauty.fooddeliveryadmin.screen.menulist.additiongroupformenuproduct.editadditiongroupformenuproduct.EditAdditionGroupForMenuProductFragment.Companion.EDIT_ADDITION_GROUP
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.AdditionGroupForMenuProductList
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.AdditionGroupForMenuProductListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class AdditionGroupForMenuProductListFragment :
    BaseComposeFragment<
        AdditionGroupForMenuProductList.DataState,
        AdditionGroupForMenuProductListViewState,
        AdditionGroupForMenuProductList.Action,
        AdditionGroupForMenuProductList.Event,
    >() {
    override val viewModel: AdditionGroupForMenuProductListViewModel by viewModel()
    private val additionGroupForMenuProductFragmentArgs:
        AdditionGroupForMenuProductListFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.onAction(
            AdditionGroupForMenuProductList.Action.Init(
                menuProductUuid = additionGroupForMenuProductFragmentArgs.menuProductUuid,
            ),
        )

        setFragmentResultListener(EDIT_ADDITION_GROUP) { _, bundle ->
            viewModel.onAction(
                AdditionGroupForMenuProductList.Action.Init(
                    menuProductUuid = additionGroupForMenuProductFragmentArgs.menuProductUuid,
                ),
            )
        }

        setFragmentResultListener(CREATE_ADDITION_GROUP) { _, bundle ->
            viewModel.onAction(
                AdditionGroupForMenuProductList.Action.Init(
                    menuProductUuid = additionGroupForMenuProductFragmentArgs.menuProductUuid,
                ),
            )
        }
    }

    @Composable
    override fun Screen(
        state: AdditionGroupForMenuProductListViewState,
        onAction: (AdditionGroupForMenuProductList.Action) -> Unit,
    ) {
        AdditionGroupForMenuProductScreen(
            state = state,
            onAction = onAction,
            menuProductUuid = additionGroupForMenuProductFragmentArgs.menuProductUuid,
        )
    }

    @Composable
    fun AdditionGroupForMenuProductScreen(
        state: AdditionGroupForMenuProductListViewState,
        menuProductUuid: String,
        onAction: (AdditionGroupForMenuProductList.Action) -> Unit,
    ) {
        AdminScaffold(
            title =
                when (state.state) {
                    is AdditionGroupForMenuProductListViewState.State.Success ->
                        stringResource(
                            id = R.string.title_addition_group_for_menu_product,
                        )

                    is AdditionGroupForMenuProductListViewState.State.SuccessDragDrop ->
                        stringResource(
                            id = R.string.title_edit_priority,
                        )

                    AdditionGroupForMenuProductListViewState.State.Error,
                    AdditionGroupForMenuProductListViewState.State.Loading,
                    -> null
                },
            pullRefreshEnabled = true,
            refreshing = state.isRefreshing,
            onRefresh = {
                onAction(
                    AdditionGroupForMenuProductList.Action.RefreshData(
                        menuProductUuid = additionGroupForMenuProductFragmentArgs.menuProductUuid,
                    ),
                )
            },
            backActionClick = {
                when (state.state) {
                    AdditionGroupForMenuProductListViewState.State.Error -> Unit
                    AdditionGroupForMenuProductListViewState.State.Loading -> Unit
                    is AdditionGroupForMenuProductListViewState.State.Success ->
                        onAction(
                            AdditionGroupForMenuProductList.Action.OnBackClick,
                        )

                    is AdditionGroupForMenuProductListViewState.State.SuccessDragDrop ->
                        onAction(
                            AdditionGroupForMenuProductList.Action.OnCancelClicked,
                        )
                }
            },
            backgroundColor = AdminTheme.colors.main.surface,
            topActions =
                when (state.state) {
                    is AdditionGroupForMenuProductListViewState.State.Success ->
                        listOf(
                            AdminTopBarAction(
                                iconId = R.drawable.ic_edit,
                                color = AdminTheme.colors.main.primary,
                                onClick = {
                                    onAction(AdditionGroupForMenuProductList.Action.OnPriorityEditClicked)
                                },
                            ),
                        )

                    is AdditionGroupForMenuProductListViewState.State.SuccessDragDrop ->
                        listOf(
                            AdminTopBarAction(
                                iconId = R.drawable.ic_check,
                                color = AdminTheme.colors.main.primary,
                                onClick = {
                                    onAction(
                                        AdditionGroupForMenuProductList.Action.OnSaveEditPriorityClick(
                                            updateAdditionGroupForMenuProductList = state.state.additionGroupWithAdditionsList,
                                        ),
                                    )
                                },
                            ),
                        )

                    AdditionGroupForMenuProductListViewState.State.Error,
                    AdditionGroupForMenuProductListViewState.State.Loading,
                    -> emptyList()
                },
            actionButton = {
                when (state.state) {
                    is AdditionGroupForMenuProductListViewState.State.Success -> {
                        FloatingButton(
                            iconId = R.drawable.ic_plus,
                            textStringId = R.string.action_addition_group_for_menu_product_addition_add,
                            onClick = {
                                onAction(AdditionGroupForMenuProductList.Action.OnCreateClick)
                            },
                        )
                    }

                    AdditionGroupForMenuProductListViewState.State.Error,
                    AdditionGroupForMenuProductListViewState.State.Loading,
                    is AdditionGroupForMenuProductListViewState.State.SuccessDragDrop,
                    -> Unit
                }
            },
            actionButtonPosition = FabPosition.End,
        ) {
            when (state.state) {
                is AdditionGroupForMenuProductListViewState.State.Loading -> LoadingScreen()
                is AdditionGroupForMenuProductListViewState.State.Error -> {
                    ErrorScreen(
                        mainTextId = R.string.title_common_can_not_load_data,
                        extraTextId = R.string.msg_common_check_connection_and_retry,
                        onClick = {
                            onAction(
                                AdditionGroupForMenuProductList.Action.Init(
                                    menuProductUuid = menuProductUuid,
                                ),
                            )
                        },
                    )
                }

                is AdditionGroupForMenuProductListViewState.State.Success -> {
                    AdditionGroupForMenuProductScreenSuccess(
                        state = state.state,
                        onAction = onAction,
                    )
                }

                is AdditionGroupForMenuProductListViewState.State.SuccessDragDrop -> {
                    AdditionGroupForMenuProductScreenSuccessDragDrop(
                        state = state.state,
                        onAction = onAction,
                    )
                }
            }
        }
    }

    @Composable
    private fun AdditionGroupForMenuProductScreenSuccess(
        state: AdditionGroupForMenuProductListViewState.State.Success,
        onAction: (AdditionGroupForMenuProductList.Action) -> Unit,
    ) {
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize(),
        ) {
            items(
                items = state.additionGroupWithAdditionsList,
                key = { additionGroupWithAddition -> additionGroupWithAddition.uuid },
            ) { additionGroup ->
                Column {
                    AdditionGroupItemView(
                        additionGroup = additionGroup,
                        onClick = {
                            onAction(
                                AdditionGroupForMenuProductList.Action.OnAdditionGroupClick(
                                    uuid = additionGroup.uuid,
                                ),
                            )
                        },
                        isClickable = true,
                    )
                    AdminHorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }
            }
        }
    }

    @Composable
    private fun AdditionGroupItemView(
        modifier: Modifier = Modifier,
        additionGroup: AdditionGroupForMenuProduct,
        onClick: () -> Unit,
        isClickable: Boolean,
    ) {
        AdminCard(
            modifier = modifier.fillMaxWidth(),
            onClick = onClick,
            clickable = isClickable,
            shape = noCornerCardShape,
            elevated = false,
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(
                            horizontal = 16.dp,
                            vertical = 12.dp,
                        ).fillMaxWidth(),
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = additionGroup.name,
                    style = AdminTheme.typography.bodyLarge,
                    color = AdminTheme.colors.main.onSurface,
                )
                additionGroup.additionNameList?.let { additionNameList ->
                    Text(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                        text = additionNameList,
                        style = AdminTheme.typography.bodySmall,
                        color = AdminTheme.colors.main.onSurfaceVariant,
                    )
                }
            }
        }
    }

    @Composable
    private fun AdditionGroupForMenuProductScreenSuccessDragDrop(
        state: AdditionGroupForMenuProductListViewState.State.SuccessDragDrop,
        onAction: (AdditionGroupForMenuProductList.Action) -> Unit,
    ) {
        DragDropList(
            items = state.additionGroupWithAdditionsList,
            itemKey = { it.uuid },
            onMove = { fromIndex, toIndex ->
                onAction(
                    AdditionGroupForMenuProductList.Action.MoveSelectedItem(
                        fromIndex = fromIndex,
                        toIndex = toIndex,
                    ),
                )
            },
            itemLabel = { it.name },
        )
    }

    @Composable
    override fun mapState(state: AdditionGroupForMenuProductList.DataState): AdditionGroupForMenuProductListViewState =
        AdditionGroupForMenuProductListViewState(
            state =
                when (state.state) {
                    AdditionGroupForMenuProductList.DataState.State.LOADING ->
                        AdditionGroupForMenuProductListViewState.State.Loading

                    AdditionGroupForMenuProductList.DataState.State.ERROR ->
                        AdditionGroupForMenuProductListViewState.State.Error

                    AdditionGroupForMenuProductList.DataState.State.SUCCESS_DRAG_DROP ->
                        AdditionGroupForMenuProductListViewState.State.SuccessDragDrop(
                            additionGroupWithAdditionsList =
                                state.additionGroupList.map { additionGroupForMenuProduct ->
                                    AdditionGroupForMenuProduct(
                                        uuid = additionGroupForMenuProduct.uuid,
                                        name = additionGroupForMenuProduct.name,
                                        additionNameList = additionGroupForMenuProduct.additionNameList,
                                        priority = additionGroupForMenuProduct.priority,
                                    )
                                },
                        )

                    AdditionGroupForMenuProductList.DataState.State.SUCCESS ->
                        AdditionGroupForMenuProductListViewState.State.Success(
                            additionGroupWithAdditionsList =
                                state.additionGroupList.map { additionGroupForMenuProduct ->
                                    AdditionGroupForMenuProduct(
                                        uuid = additionGroupForMenuProduct.uuid,
                                        name = additionGroupForMenuProduct.name,
                                        additionNameList = additionGroupForMenuProduct.additionNameList,
                                        priority = additionGroupForMenuProduct.priority,
                                    )
                                },
                        )
                },
            isRefreshing = state.isRefreshing,
        )

    override fun handleEvent(event: AdditionGroupForMenuProductList.Event) {
        when (event) {
            AdditionGroupForMenuProductList.Event.Back -> findNavController().popBackStack()

            is AdditionGroupForMenuProductList.Event.OnAdditionGroupClicked -> {
                findNavController().navigateSafe(
                    AdditionGroupForMenuProductListFragmentDirections
                        .toEditAdditionGroupForMenuProductFragment(
                            menuProductUuid = additionGroupForMenuProductFragmentArgs.menuProductUuid,
                            additionGroupForMenuUuid = event.additionGroupUuid,
                        ),
                )
            }

            AdditionGroupForMenuProductList.Event.OnCreateClicked -> {
                findNavController().navigateSafe(
                    AdditionGroupForMenuProductListFragmentDirections
                        .toCreateAdditionGroupForMenuProductFragment(
                            menuProductUuid = additionGroupForMenuProductFragmentArgs.menuProductUuid,
                        ),
                )
            }
        }
    }

    val additionGroupForMenuProductListViewState =
        AdditionGroupForMenuProductListViewState(
            state =
                AdditionGroupForMenuProductListViewState.State.Success(
                    additionGroupWithAdditionsList =
                        listOf(
                            AdditionGroupForMenuProduct(
                                uuid = "12321",
                                name = "Вкусняшки",
                                additionNameList = "Оленина Сопли Вопли",
                                priority = 1,
                            ),
                            AdditionGroupForMenuProduct(
                                uuid = "1232112",
                                name = "Не Вкусняшки",
                                additionNameList = "Жижи Топли Нопли",
                                priority = 2,
                            ),
                        ),
                ),
            isRefreshing = false,
        )

    @Composable
    @Preview
    fun AdditionGroupForMenuProductScreenPreview() {
        AdminTheme {
            AdditionGroupForMenuProductScreen(
                state = additionGroupForMenuProductListViewState,
                onAction = {},
                menuProductUuid = "",
            )
        }
    }
}
