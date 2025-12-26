package com.bunbeauty.fooddeliveryadmin.screen.mapdelivery

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.fragment.findNavController
import com.bunbeauty.common.Constants.RUBLE_CURRENCY
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.bottomsheet.AdminModalBottomSheet
import com.bunbeauty.fooddeliveryadmin.compose.element.button.LoadingButton
import com.bunbeauty.fooddeliveryadmin.compose.element.topbar.AdminHorizontalDivider
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.Colors
import com.bunbeauty.fooddeliveryadmin.compose.theme.medium
import com.bunbeauty.fooddeliveryadmin.coreui.SingleStateComposeFragment
import com.bunbeauty.fooddeliveryadmin.navigation.navigateSafe
import com.bunbeauty.presentation.feature.mapdelivery.MapDeliveryArea
import com.bunbeauty.presentation.feature.mapdelivery.MapDeliveryAreaViewModel
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.layers.FillLayer
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.compose.style.BaseStyle
import org.maplibre.compose.util.ClickResult
import org.maplibre.compose.util.FeaturesClickHandler
import org.maplibre.spatialk.geojson.Feature
import org.maplibre.spatialk.geojson.FeatureCollection
import org.maplibre.spatialk.geojson.Geometry
import org.maplibre.spatialk.geojson.Polygon
import org.maplibre.spatialk.geojson.Position
import kotlin.getValue

object MapConstants {
    const val BASE_STYLE_URL = "https://tiles.openfreemap.org/styles/liberty"
    const val POLYGON_LAYER_PREFIX = "polygon-layer-"
}

object MapColors {
    val polygonColors =
        listOf(
            Colors.Red,
            Colors.Purple,
            Colors.LightOrange,
            Colors.Blue1,
            Colors.Green,
        )

    fun generatePolygonColor(index: Int): Color = polygonColors[index % polygonColors.size]
}

class MapDeliveryAreaFragment : SingleStateComposeFragment<MapDeliveryArea.DataState, MapDeliveryArea.Action, MapDeliveryArea.Event>() {
    override val viewModel: MapDeliveryAreaViewModel by viewModel()

    @Composable
    override fun Screen(
        state: MapDeliveryArea.DataState,
        onAction: (MapDeliveryArea.Action) -> Unit,
    ) {
        LaunchedEffect(Unit) {
            onAction(MapDeliveryArea.Action.LoadAllData)
        }
        MapScreen(state = state, onAction = onAction)
    }

    override fun handleEvent(event: MapDeliveryArea.Event) {
        when (event) {
            is MapDeliveryArea.Event.Back -> {
                findNavController().navigateUp()
            }

            is MapDeliveryArea.Event.EditInfoDeliveryZoneEvent -> {
                findNavController().navigateSafe(
                    directions =
                        MapDeliveryAreaFragmentDirections.toEditDeliveryZoneFragment(
                            event.zoneUuid,
                        ),
                )
            }
        }
    }

    @Composable
    fun MapScreen(
        state: MapDeliveryArea.DataState,
        onAction: (MapDeliveryArea.Action) -> Unit,
    ) {
        AdminScaffold(
            title = stringResource(R.string.title_map_delivery_area),
            backActionClick = { onAction(MapDeliveryArea.Action.OnBackClick) },
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                SimpleMapScreen(state, onAction)

                state.selectedZoneIndex?.let { zoneIndex ->
                    val zoneData = state.listDeliveryAreaZone.getOrNull(zoneIndex)
                    if (state.isZoneBottomSheetVisible && zoneData != null) {
                        DeliveryZoneBottomSheet(
                            zoneData = zoneData,
                            zoneState = state,
                            onClose = {
                                onAction(MapDeliveryArea.Action.OnCloseBottomSheetDeliveryZoneClicked)
                            },
                            onAction = onAction,
                        )
                    }
                }
            }
        }
    }
}

@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
@Composable
fun SimpleMapScreen(
    state: MapDeliveryArea.DataState,
    onAction: (MapDeliveryArea.Action) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        val colors =
            remember(state.listPolygons.size) {
                state.listPolygons.indices.map { MapColors.generatePolygonColor(it) }
            }

        val cameraState =
            rememberCameraState(
                firstPosition =
                    CameraPosition(
                        target = Position(0.0, 0.0),
                        zoom = 10.5,
                    ),
            )
        LaunchedEffect(state.positionCafe) {
            state.positionCafe?.let { cafePosition ->
                cameraState.position =
                    CameraPosition(
                        target = cafePosition,
                        zoom = 10.5,
                    )
            }
        }
        MaplibreMap(
            baseStyle = BaseStyle.Uri(uri = MapConstants.BASE_STYLE_URL),
            cameraState = cameraState,
        ) {
            state.listPolygons.forEachIndexed { index, coordinates ->
                val featureCollection =
                    FeatureCollection(
                        features =
                            listOf(
                                Feature(
                                    geometry = Polygon(listOf(coordinates)),
                                    properties =
                                        buildJsonObject {
                                        },
                                    id = JsonPrimitive("$index"),
                                ),
                            ),
                    )
                val source =
                    rememberGeoJsonSource(
                        data = GeoJsonData.Features(featureCollection),
                    )

                FillLayer(
                    id = "${MapConstants.POLYGON_LAYER_PREFIX}$index",
                    source = source,
                    color = const(colors[index]),
                    opacity = const(0.5f),
                    onClick =
                        object : FeaturesClickHandler {
                            override fun invoke(features: List<Feature<Geometry, JsonObject?>>): ClickResult {
                                features.firstOrNull()?.let { feature ->
                                    val zoneIndex = feature.id?.content?.toIntOrNull()
                                    zoneIndex?.let { index ->
                                        onAction(
                                            MapDeliveryArea.Action.OnDeliveryZoneClicked(
                                                zoneIndex = index,
                                            ),
                                        )
                                    }
                                }
                                return ClickResult.Consume
                            }
                        },
                )
            }
        }
    }
}

@Composable
private fun DeliveryZoneBottomSheet(
    zoneState: MapDeliveryArea.DataState,
    zoneData: MapDeliveryArea.DataState.ZoneData,
    onClose: () -> Unit,
    onAction: (MapDeliveryArea.Action) -> Unit,
) {
    AdminModalBottomSheet(
        title = stringResource(R.string.title_bottom_sheet_map_delivery_area, zoneData.nameZona),
        isShown = zoneState.showBottomSheet,
        onDismissRequest = onClose,
        content = {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth(),
            ) {
                InfoRow(
                    title = stringResource(R.string.title_bottom_sheet_min_orders_cost),
                    value =
                        zoneData.minOrderCost?.let { minOrder -> "$minOrder $RUBLE_CURRENCY" }
                            ?: stringResource(R.string.error_bottom_sheet_min_orders_cost),
                )

                InfoRow(
                    title = stringResource(R.string.title_bottom_sheet_orders_cost),
                    value = "${zoneData.normalDeliveryCost} $RUBLE_CURRENCY",
                    modifier = Modifier.padding(top = 16.dp),
                )

                InfoRow(
                    title = stringResource(R.string.title_bottom_sheet_free_orders_cost),
                    value =
                        zoneData.forLowDeliveryCost?.let { cost -> "$cost $RUBLE_CURRENCY" }
                            ?: stringResource(R.string.error_bottom_sheet_free_orders_cost),
                    modifier = Modifier.padding(top = 16.dp),
                )

                LoadingButton(
                    modifier = Modifier.padding(top = 16.dp),
                    text = stringResource(R.string.action_bottom_sheet_edit),
                    isLoading = zoneState.loadingMap,
                    onClick = {
                        onAction(
                            MapDeliveryArea.Action.OnEditInfoDeliveryZone(
                                zoneUuid = zoneData.uuid,
                            ),
                        )
                    },
                )
            }
        },
    )
}

@Composable
private fun InfoRow(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            style = AdminTheme.typography.labelMedium.medium,
            color = AdminTheme.colors.main.onSurfaceVariant,
        )
        Text(
            text = value,
            fontSize = 16.sp,
            style = AdminTheme.typography.bodyMedium,
        )
        AdminHorizontalDivider(Modifier.padding(top = 8.dp))
    }
}
