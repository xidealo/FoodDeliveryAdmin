package com.bunbeauty.fooddeliveryadmin.screen.map

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.bottomsheet.AdminModalBottomSheet
import com.bunbeauty.fooddeliveryadmin.compose.element.topbar.AdminHorizontalDivider
import com.bunbeauty.fooddeliveryadmin.coreui.SingleStateComposeFragment
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
import kotlin.random.Random

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
        }
    }

    @Composable
    fun MapScreen(
        state: MapDeliveryArea.DataState,
        onAction: (MapDeliveryArea.Action) -> Unit,
    ) {
        AdminScaffold(
            title = "Зона обслуживания",
            backActionClick = { onAction(MapDeliveryArea.Action.OnBackClick) },
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                SimpleMapScreen(state, onAction)

                if (state.isZoneBottomSheetVisible && state.selectedZoneIndex != null) {
                    val zoneIndex = state.selectedZoneIndex!!
                    val zoneData = state.listDeliveryAreaZone.getOrNull(zoneIndex)
                    zoneData?.let {
                        DeliveryZoneBottomSheet(
                            zoneData = it,
                            onClose = {
                                onAction(MapDeliveryArea.Action.OnCloseBottomSheetDeliveryZoneClicked)
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
fun SimpleMapScreen(
    state: MapDeliveryArea.DataState,
    onAction: (MapDeliveryArea.Action) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        val colors =
            remember(state.listPolygons.size) {
                state.listPolygons.indices.map { generateRandomColor() }
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
            baseStyle = BaseStyle.Uri("https://tiles.openfreemap.org/styles/liberty"),
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
                    id = "polygon-layer-$index",
                    source = source,
                    color = const(colors[index]),
                    opacity = const(0.5f),
                    onClick =
                        object : FeaturesClickHandler {
                            override fun invoke(features: List<Feature<Geometry, JsonObject?>>): ClickResult {
                                features.firstOrNull()?.let { feature ->
                                    val zoneIndex = feature.id?.content?.toIntOrNull()
                                    zoneIndex?.let { index ->
                                        onAction(MapDeliveryArea.Action.OnDeliveryZoneClicked(zoneIndex = index))
                                    }
                                }
                                return ClickResult.Consume
                            }
                        }
                )
            }
        }
    }
}

@Composable
private fun DeliveryZoneBottomSheet(
    zoneData: MapDeliveryArea.DataState.ZoneData,
    onClose: () -> Unit,
) {
    AdminModalBottomSheet(
        title = "Зона доставки",
        isShown = true,
        onDismissRequest = onClose,
        content = {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
            ) {
                InfoRow(
                    title = "Минимальная стоимость заказа:",
                    value = zoneData.minOrderCost?.let { "$it ₽" } ?: "Не установлена",
                )

                AdminHorizontalDivider(
                    modifier =
                        Modifier.padding(
                            horizontal = 16.dp,
                        ),
                )

                InfoRow(
                    title = "Стоимость доставки:",
                    value = "${zoneData.normalDeliveryCost} ₽",
                )

                AdminHorizontalDivider(
                    modifier =
                        Modifier.padding(
                            horizontal = 16.dp,
                        ),
                )

                InfoRow(
                    title = "Бесплатная доставка от:",
                    value = zoneData.forLowDeliveryCost?.let { "$it ₽" } ?: "Не установлена",
                )
            }
        },
    )
}

@Composable
private fun InfoRow(
    title: String,
    value: String,
) {
    Column {
        Text(
            text = title,
            fontSize = 14.sp,
        )
        Text(
            text = value,
            fontSize = 16.sp,
        )
    }
}

private fun generateRandomColor(): Color =
    Color(
        red = Random.nextFloat(),
        green = Random.nextFloat(),
        blue = Random.nextFloat(),
        alpha = 1f,
    )
