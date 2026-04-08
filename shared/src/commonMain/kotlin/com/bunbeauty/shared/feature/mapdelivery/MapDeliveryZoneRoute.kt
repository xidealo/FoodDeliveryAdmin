package com.bunbeauty.shared.feature.mapdelivery

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bunbeauty.shared.designsystem.compose.AdminScaffold
import com.bunbeauty.shared.designsystem.compose.element.bottomsheet.AdminModalBottomSheet
import com.bunbeauty.shared.designsystem.compose.element.button.LoadingButton
import com.bunbeauty.shared.designsystem.compose.element.topbar.AdminHorizontalDivider
import com.bunbeauty.shared.designsystem.compose.theme.AdminTheme
import com.bunbeauty.shared.navigation.NavStateHandleParameters.UPDATED_DELIVERY_ZONE_UUID
import common.Constants.RUBLE_CURRENCY
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.action_bottom_sheet_edit
import fooddeliveryadmin.shared.generated.resources.error_bottom_sheet_free_orders_cost
import fooddeliveryadmin.shared.generated.resources.error_bottom_sheet_min_orders_cost
import fooddeliveryadmin.shared.generated.resources.title_bottom_sheet_free_orders_cost
import fooddeliveryadmin.shared.generated.resources.title_bottom_sheet_map_delivery_area
import fooddeliveryadmin.shared.generated.resources.title_bottom_sheet_min_orders_cost
import fooddeliveryadmin.shared.generated.resources.title_bottom_sheet_orders_cost
import fooddeliveryadmin.shared.generated.resources.title_map_delivery_area
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
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

private const val BASE_STYLE_URL = "https://tiles.openfreemap.org/styles/liberty"
private const val POLYGON_LAYER_PREFIX = "polygon-layer-"
private const val MAP_ZOOM = 10.5

@Composable
fun MapDeliveryZoneRouteScreen(
    viewModel: MapDeliveryZoneViewModel = koinViewModel(),
    savedStateHandle: SavedStateHandle,
    goBack: () -> Unit,
    goToEditDeliveryZoneInfo: (String) -> Unit,
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { event: MapDeliveryZone.Action ->
                viewModel.onAction(event)
            }
        }

    val effects by viewModel.events.collectAsStateWithLifecycle()
    val consumeEffects =
        remember {
            {
                viewModel.consumeEvents(effects)
            }
        }

    LaunchedEffect(Unit) {
        onAction(MapDeliveryZone.Action.LoadAllData)
    }

    LaunchedEffect(Unit) {
        savedStateHandle
            .getStateFlow<String?>(
                key = UPDATED_DELIVERY_ZONE_UUID,
                initialValue = null,
            ).collect { updatedZoneUuid ->
                if (updatedZoneUuid != null) {
                    onAction(MapDeliveryZone.Action.UpdateDeliveryZone(updatedZoneUuid))
                    savedStateHandle.remove<String>(UPDATED_DELIVERY_ZONE_UUID)
                }
            }
    }

    MapDeliveryZoneEffect(
        effects = effects,
        consumeEffects = consumeEffects,
        goBack = goBack,
        goToEditDeliveryZoneInfo = goToEditDeliveryZoneInfo,
    )

    MapScreen(
        state = viewState.toViewState(),
        onAction = onAction,
        isZoneBottomSheetVisible = viewState.isZoneBottomSheetVisible,
        onCloseBottomSheet = {
            onAction(MapDeliveryZone.Action.OnCloseBottomSheetDeliveryZoneClicked)
        },
    )
}

@Composable
private fun MapDeliveryZoneEffect(
    effects: List<MapDeliveryZone.Event>,
    goBack: () -> Unit,
    consumeEffects: () -> Unit,
    goToEditDeliveryZoneInfo: (String) -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                is MapDeliveryZone.Event.Back -> {
                    goBack()
                }

                is MapDeliveryZone.Event.EditInfoDeliveryZoneEvent -> {
                    goToEditDeliveryZoneInfo(effect.zoneUuid)
                }
            }
        }
        consumeEffects()
    }
}

@Composable
private fun MapScreen(
    state: MapDeliveryZoneViewState,
    onAction: (MapDeliveryZone.Action) -> Unit,
    isZoneBottomSheetVisible: Boolean,
    onCloseBottomSheet: () -> Unit,
) {
    AdminScaffold(
        title = stringResource(Res.string.title_map_delivery_area),
        backActionClick = { onAction(MapDeliveryZone.Action.OnBackClick) },
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            when (state.state) {
                MapDeliveryZoneViewState.State.Loading, MapDeliveryZoneViewState.State.Error -> {
                    // Map will show loading/error state through camera
                }

                is MapDeliveryZoneViewState.State.Success -> {
                    SimpleMapScreen(
                        state = state.state,
                        onAction = onAction,
                    )

                    state.state.selectedZoneData?.let { zoneData ->
                        DeliveryZoneBottomSheet(
                            zoneData = zoneData,
                            loadingMap = state.state.loadingMap,
                            isShown = isZoneBottomSheetVisible,
                            onDismissRequest = onCloseBottomSheet,
                            onEditClick = { zoneUuid ->
                                onAction(
                                    MapDeliveryZone.Action.OnEditInfoDeliveryZone(
                                        zoneUuid = zoneUuid,
                                    ),
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}

@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
@Composable
private fun SimpleMapScreen(
    state: MapDeliveryZoneViewState.State.Success,
    onAction: (MapDeliveryZone.Action) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        val colors = generatePolygonColors(state.listDeliveryAreaZone.size)

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
            baseStyle = BaseStyle.Uri(uri = BASE_STYLE_URL),
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
                    id = "${POLYGON_LAYER_PREFIX}${zone.uuid}",
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
    zoneData: MapDeliveryZoneViewState.ZoneData,
    loadingMap: Boolean,
    isShown: Boolean,
    onDismissRequest: () -> Unit,
    onEditClick: (String) -> Unit,
) {
    AdminModalBottomSheet(
        title = stringResource(Res.string.title_bottom_sheet_map_delivery_area, zoneData.nameZona),
        isShown = isShown,
        onDismissRequest = onDismissRequest,
        content = {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth(),
            ) {
                InfoRow(
                    title = stringResource(Res.string.title_bottom_sheet_min_orders_cost),
                    value =
                        zoneData.minOrderCost?.let { minOrder -> "$minOrder $RUBLE_CURRENCY" }
                            ?: stringResource(Res.string.error_bottom_sheet_min_orders_cost),
                )

                InfoRow(
                    title = stringResource(Res.string.title_bottom_sheet_orders_cost),
                    value = "${zoneData.normalDeliveryCost} $RUBLE_CURRENCY",
                    modifier = Modifier.padding(top = 16.dp),
                )

                InfoRow(
                    title = stringResource(Res.string.title_bottom_sheet_free_orders_cost),
                    value =
                        zoneData.forLowDeliveryCost?.let { cost -> "$cost $RUBLE_CURRENCY" }
                            ?: stringResource(Res.string.error_bottom_sheet_free_orders_cost),
                    modifier = Modifier.padding(top = 16.dp),
                )

                LoadingButton(
                    modifier = Modifier.padding(top = 16.dp),
                    text = stringResource(Res.string.action_bottom_sheet_edit),
                    isLoading = loadingMap,
                    onClick = {
                        onEditClick(zoneData.uuid)
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
            style = AdminTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
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
