import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.screen.ErrorScreen
import com.bunbeauty.fooddeliveryadmin.compose.screen.LoadingScreen
import com.bunbeauty.presentation.feature.category.CategoriesState
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.fooddeliveryadmin.screen.category.CategoriesViewState
import com.bunbeauty.presentation.feature.category.CategoriesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CategoriesListFragment() :
    BaseComposeFragment<CategoriesState.DataState, CategoriesViewState, CategoriesState.Action, CategoriesState.Event>() {

    override val viewModel: CategoriesViewModel by viewModel()

    @Composable
    override fun Screen(
        state: CategoriesViewState,
        onAction: (CategoriesState.Action) -> Unit
    ) {
        CategoriesScreen(
            state = state,
            onAction = onAction
        )
    }

    @Composable
    private fun CategoriesScreen(
        state: CategoriesViewState,
        onAction: (CategoriesState.Action) -> Unit
    ) {
        AdminScaffold(
            title = stringResource(R.string.title_settings),
            backActionClick = {
                onAction(CategoriesState.Action.OnBackClicked)
            }
        ) {
            when (state.state) {
                CategoriesViewState.State.Loading -> {
                    LoadingScreen()
                }

                CategoriesViewState.State.Error -> {
                    ErrorScreen(
                        mainTextId = R.string.title_common_can_not_load_data,
                        extraTextId = R.string.msg_common_check_connection_and_retry,
                        onClick = {
                            onAction(CategoriesState.Action.Init)
                        }
                    )
                }

                is CategoriesViewState.State.Success -> {

                }
            }
        }
    }

    override fun handleEvent(event: CategoriesState.Event) {}

    @Composable
    override fun mapState(state: CategoriesState.DataState): CategoriesViewState {
        TODO("Not yet implemented")
    }
}
