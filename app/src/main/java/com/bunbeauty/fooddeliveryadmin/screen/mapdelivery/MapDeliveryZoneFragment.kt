package com.bunbeauty.fooddeliveryadmin.screen.mapdelivery

import android.os.Bundle
import android.view.View
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
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
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
import com.bunbeauty.fooddeliveryadmin.screen.mapdelivery.MapConstants.MAP_ZOOM
import com.bunbeauty.presentation.feature.mapdelivery.MapDeliveryZone
import com.bunbeauty.presentation.feature.mapdelivery.MapDeliveryZoneViewModel
import common.Constants.RUBLE_CURRENCY
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

object MapConstants {
    const val BASE_STYLE_URL = "https://tiles.openfreemap.org/styles/liberty"
    const val POLYGON_LAYER_PREFIX = "polygon-layer-"
    const val MAP_ZOOM = 10.5
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

class MapDeliveryZoneFragment : SingleStateComposeFragment<MapDeliveryZone.DataState, MapDeliveryZone.Action, MapDeliveryZone.Event>() {
    override val viewModel: MapDeliveryZoneViewModel by viewModel()

    companion object {
        const val SELECT_ZONE_KEY = "SELECT_ZONE_KEY"
        const val UUID_ZONE_KEY = "UUID_ZONE_KEY"
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResultListener(SELECT_ZONE_KEY) { _, bundle ->
            viewModel.onAction(
                MapDeliveryZone.Action.UpdateDeliveryZone(
                    zoneUuid = bundle.getString(UUID_ZONE_KEY).orEmpty(),
                ),
            )
        }
    }

    @Composable
    override fun Screen(
        state: MapDeliveryZone.DataState,
        onAction: (MapDeliveryZone.Action) -> Unit,
    ) {
        LaunchedEffect(Unit) {
            onAction(MapDeliveryZone.Action.LoadAllData)
        }
        MapScreen(state = state, onAction = onAction)
    }

    override fun handleEvent(event: MapDeliveryZone.Event) {
        when (event) {
            is MapDeliveryZone.Event.Back -> {
                findNavController().navigateUp()
            }

            is MapDeliveryZone.Event.EditInfoDeliveryZoneEvent -> {
                findNavController().navigateSafe(
                    directions =
                        MapDeliveryZoneFragmentDirections.toEditDeliveryZoneFragment(
                            event.zoneUuid,
                        ),
                )
            }
        }
    }

    @Composable
    fun MapScreen(
        state: MapDeliveryZone.DataState,
        onAction: (MapDeliveryZone.Action) -> Unit,
    ) {
        AdminScaffold(
            title = stringResource(R.string.title_map_delivery_area),
            backActionClick = { onAction(MapDeliveryZone.Action.OnBackClick) },
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                SimpleMapScreen(state, onAction)

                state.selectedZoneData?.let { zoneData ->
                    DeliveryZoneBottomSheet(
                        zoneData = zoneData,
                        zoneState = state,
                        onClose = {
                            onAction(MapDeliveryZone.Action.OnCloseBottomSheetDeliveryZoneClicked)
                        },
                        onAction = onAction,
                    )
                }
            }
        }
    }
}

@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
@Composable
fun SimpleMapScreen(
    state: MapDeliveryZone.DataState,
    onAction: (MapDeliveryZone.Action) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        val colors =
            remember(state.listDeliveryAreaZone.size) {
                state.listDeliveryAreaZone.indices.map { MapColors.generatePolygonColor(it) }
            }

        val cameraState =
            rememberCameraState(
                firstPosition =
                    CameraPosition(
                        target = Position(longitude = 0.0, latitude = 0.0),
                        zoom = MAP_ZOOM,
                    ),
            )

        LaunchedEffect(state.positionCafe) {
            state.positionCafe?.let { cafePosition ->
                cameraState.position =
                    CameraPosition(
                        target = cafePosition,
                        zoom = MAP_ZOOM,
                    )
            }
        }

        MaplibreMap(
            baseStyle = BaseStyle.Uri(uri = MapConstants.BASE_STYLE_URL),
            cameraState = cameraState,
        ) {
            state.listDeliveryAreaZone.forEachIndexed { index, zone ->
                val featureCollection =
                    FeatureCollection(
                        features =
                            listOf(
                                Feature(
                                    geometry = Polygon(zone.deliveryZonePoint),
                                    properties = buildJsonObject {},
                                    id = JsonPrimitive(zone.uuid),
                                ),
                            ),
                    )
                val source =
                    rememberGeoJsonSource(
                        data = GeoJsonData.Features(featureCollection),
                    )

                FillLayer(
                    id = "${MapConstants.POLYGON_LAYER_PREFIX}${zone.uuid}",
                    source = source,
                    color = const(colors[index]),
                    opacity = const(0.5f),
                    onClick =
                        object : FeaturesClickHandler {
                            override fun invoke(features: List<Feature<Geometry, JsonObject?>>): ClickResult {
                                features.firstOrNull()?.let { feature ->
                                    val zoneUuid = feature.id?.content
                                    zoneUuid?.let { zoneUuid ->
                                        onAction(
                                            MapDeliveryZone.Action.OnDeliveryZoneClicked(
                                                zoneUuid = zoneUuid,
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
    zoneState: MapDeliveryZone.DataState,
    zoneData: MapDeliveryZone.DataState.ZoneData,
    onClose: () -> Unit,
    onAction: (MapDeliveryZone.Action) -> Unit,
) {
    AdminModalBottomSheet(
        title = stringResource(R.string.title_bottom_sheet_map_delivery_area, zoneData.nameZona),
        isShown = zoneState.isZoneBottomSheetVisible,
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
                            MapDeliveryZone.Action.OnEditInfoDeliveryZone(
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
