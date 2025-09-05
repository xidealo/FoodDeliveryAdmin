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
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.AdditionGroupForMenuProduct
import com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.AdditionGroupForMenuProductViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class AdditionGroupForMenuProductFragment :
    BaseComposeFragment<AdditionGroupForMenuProduct.DataState, AdditionGroupForMenuProductViewState, AdditionGroupForMenuProduct.Action, AdditionGroupForMenuProduct.Event>() {

    override val viewModel: AdditionGroupForMenuProductViewModel by viewModel()
    private val additionGroupForMenuProductFragmentArgs: AdditionGroupForMenuProductFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onAction(
            AdditionGroupForMenuProduct.Action.Init(
                menuProductUuid = additionGroupForMenuProductFragmentArgs.menuProductUuid
            )
        )
    }

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
            title = stringResource(id = R.string.title_addition_group_for_menu_product),
            backActionClick = {
                onAction(AdditionGroupForMenuProduct.Action.OnBackClick)
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
        additionGroup: AdditionGroupForMenuProductViewState.AdditionGroupWithAdditions,
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
    override fun mapState(state: AdditionGroupForMenuProduct.DataState): AdditionGroupForMenuProductViewState {
        return AdditionGroupForMenuProductViewState(
            additionGroupWithAdditionsList = state.additionGroupList.map { additionGroupWithAdditions ->
                AdditionGroupForMenuProductViewState.AdditionGroupWithAdditions(
                    uuid = additionGroupWithAdditions.additionGroup.uuid,
                    name = additionGroupWithAdditions.additionGroup.name,
                    additionNameList = additionGroupWithAdditions.additionList
                        .takeIf { list ->
                            list.isNotEmpty()
                        }
                        ?.joinToString(separator = " ${Constants.BULLET_SYMBOL} ") { selectableCategory ->
                            selectableCategory.name
                        }
                )
            }
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
                uuid = "12321",
                name = "Вкусняшки",
                additionNameList = "Оленина Сопли Вопли"
            ),
            AdditionGroupForMenuProductViewState.AdditionGroupWithAdditions(
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
                state = additionGroupForMenuProductViewState,
                onAction = {}
            )
        }
    }
}
