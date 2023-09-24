package com.bunbeauty.fooddeliveryadmin.screen.cafelist

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.bunbeauty.domain.model.cafe.CafeStatus
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.screen.ErrorScreen
import com.bunbeauty.fooddeliveryadmin.compose.screen.LoadingScreen
import com.bunbeauty.fooddeliveryadmin.compose.setContentWithTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.BaseFragment
import com.bunbeauty.fooddeliveryadmin.databinding.LayoutComposeBinding
import com.bunbeauty.fooddeliveryadmin.screen.cafelist.item.CafeItem
import com.bunbeauty.fooddeliveryadmin.screen.cafelist.item.CafeUiItem
import com.bunbeauty.presentation.feature.cafelist.CafeListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CafeListFragment : BaseFragment<LayoutComposeBinding>() {

    @Inject
    lateinit var cafeStateMapper: CafeStateMapper

    override val viewModel: CafeListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.updateCafeList()

        binding.root.setContentWithTheme {
            val dataState by viewModel.dataState.collectAsStateWithLifecycle()
            val uiState = cafeStateMapper.map(dataState)
            CafeListScreen(
                uiState = uiState,
                backActionClick = findNavController()::popBackStack,
                onRetryClicked = viewModel::updateCafeList,
                onCafeClicked = {
                    // TODO go to edit cafe
                }
            )
        }
    }

    @Composable
    private fun CafeListScreen(
        uiState: CafeListUiState,
        backActionClick: () -> Unit,
        onRetryClicked: () -> Unit,
        onCafeClicked: (CafeUiItem) -> Unit,
    ) {
        AdminScaffold(
            title = stringResource(R.string.title_cafe_list),
            backActionClick = backActionClick
        ) {
            when (uiState) {
                CafeListUiState.Loading -> LoadingScreen()
                CafeListUiState.Error -> ErrorScreen(
                    mainTextId = R.string.title_common_can_not_load_data,
                    extraTextId = R.string.msg_common_check_connection_and_retry,
                    onClick = onRetryClicked
                )

                is CafeListUiState.Success -> {
                    CafeListSuccessScreen(
                        cafeItemList = uiState.cafeItemList,
                        onCafeClicked = onCafeClicked
                    )
                }
            }
        }
    }

    @Composable
    private fun CafeListSuccessScreen(
        cafeItemList: List<CafeUiItem>,
        onCafeClicked: (CafeUiItem) -> Unit,
    ) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(cafeItemList) { cafeItem ->
                CafeItem(
                    cafeItem = cafeItem,
                    onClick = onCafeClicked
                )
            }
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    private fun CafeListScreenPreview() {
        AdminTheme {
            CafeListScreen(
                uiState = CafeListUiState.Success(
                    listOf(
                        CafeUiItem(
                            uuid = "",
                            address = "улица Чапаева, д. 22а",
                            workingHours = "10:00-22:00",
                            cafeStatusText = "Открыто",
                            cafeStatus = CafeStatus.Open,
                        ),
                        CafeUiItem(
                            uuid = "",
                            address = "улица Чапаева, д. 22а",
                            workingHours = "10:00-20:00",
                            cafeStatusText = "Закрыто",
                            cafeStatus = CafeStatus.Closed,
                        ),
                        CafeUiItem(
                            uuid = "",
                            address = "улица Чапаева, д. 22а",
                            workingHours = "10:00-21:00",
                            cafeStatusText = "Открыто. Закроется через 30 мин",
                            cafeStatus = CafeStatus.CloseSoon(30),
                        ),
                    )
                ),
                backActionClick = {},
                onRetryClicked = {},
                onCafeClicked = {},
            )
        }
    }
}
