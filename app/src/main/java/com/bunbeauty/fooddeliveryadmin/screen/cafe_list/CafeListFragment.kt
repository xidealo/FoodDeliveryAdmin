package com.bunbeauty.fooddeliveryadmin.screen.cafe_list

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.bunbeauty.domain.model.cafe.CafeWithWorkingHours
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.screen.ErrorScreen
import com.bunbeauty.fooddeliveryadmin.compose.screen.LoadingScreen
import com.bunbeauty.fooddeliveryadmin.compose.setContentWithTheme
import com.bunbeauty.fooddeliveryadmin.core_ui.BaseFragment
import com.bunbeauty.fooddeliveryadmin.databinding.LayoutComposeBinding
import com.bunbeauty.fooddeliveryadmin.screen.cafe_list.item.CafeItem
import com.bunbeauty.fooddeliveryadmin.screen.cafe_list.item.CafeUiItem
import com.bunbeauty.presentation.feature.cafe_list.CafeListUiState
import com.bunbeauty.presentation.feature.cafe_list.CafeListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CafeListFragment : BaseFragment<LayoutComposeBinding>() {

    @Inject
    lateinit var cafeItemMapper: CafeItemMapper

    override val viewModel: CafeListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.updateCafeList()

        binding.root.setContentWithTheme {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            CafeListScreen(
                uiState = uiState,
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
        onRetryClicked: () -> Unit,
        onCafeClicked: (CafeUiItem) -> Unit,
    ) {
        AdminScaffold(
            title = stringResource(R.string.title_cafe_list),
            backActionClick = { findNavController().popBackStack() }
        ) {
            when (val state = uiState.state) {
                CafeListUiState.State.Loading -> LoadingScreen()
                CafeListUiState.State.Error -> ErrorScreen(
                    mainTextId = R.string.title_common_can_not_load_data,
                    extraTextId = R.string.msg_common_check_connection_and_retry,
                    onClick = onRetryClicked
                )

                is CafeListUiState.State.Success -> {
                    CafeListSuccessScreen(
                        cafeList = state.cafeList,
                        onCafeClicked = onCafeClicked,
                    )
                }
            }
        }
    }

    @Composable
    private fun CafeListSuccessScreen(
        cafeList: List<CafeWithWorkingHours>,
        onCafeClicked: (CafeUiItem) -> Unit,
    ) {
        val cafeItems = remember(cafeList) {
            cafeList.map(cafeItemMapper::map)
        }
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(cafeItems) { cafeUiItem ->
                CafeItem(
                    cafeItem = cafeUiItem,
                    onClick = onCafeClicked
                )
            }
        }
    }

}