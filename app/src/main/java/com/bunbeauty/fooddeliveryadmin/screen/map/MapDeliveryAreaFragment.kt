package com.bunbeauty.fooddeliveryadmin.screen.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
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
import org.maplibre.spatialk.geojson.FeatureId
import org.maplibre.spatialk.geojson.Geometry
import org.maplibre.spatialk.geojson.Polygon
import org.maplibre.spatialk.geojson.Position
import kotlin.getValue
import kotlin.random.Random

//удалить вью стейт
class MapDeliveryAreaFragment :
    BaseComposeFragment<MapDeliveryArea.DataState, MapDeliveryAreaViewState, MapDeliveryArea.Action, MapDeliveryArea.Event>() {
    override val viewModel: MapDeliveryAreaViewModel by viewModel()

    @Composable
    override fun mapState(state: MapDeliveryArea.DataState): MapDeliveryAreaViewState =
        MapDeliveryAreaViewState(
            //  isLoading = state.isLoading,
            listPolygons = state.listPolygons,
            positionCafe = state.positionCafe,
        )

    override fun handleEvent(event: MapDeliveryArea.Event) {
        when (event) {
            is MapDeliveryArea.Event.Back -> {
                findNavController().navigateUp()
            }
        }
    }

    @Composable
    override fun Screen(
        state: MapDeliveryAreaViewState,
        onAction: (MapDeliveryArea.Action) -> Unit,
    ) {
        LaunchedEffect(Unit) {
            onAction(MapDeliveryArea.Action.LoadAllData)
        }
        MapScreen(state = state, onAction = onAction)
    }

    @Composable
    fun MapScreen(
        state: MapDeliveryAreaViewState,
        onAction: (MapDeliveryArea.Action) -> Unit,
    ) {
        AdminScaffold(
            title = "Зона обслуживания",
            backActionClick = { onAction(MapDeliveryArea.Action.OnBackClick) },
        ) {
            SimpleMapScreen(state)
        }
    }
}

@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
@Composable
fun SimpleMapScreen(state: MapDeliveryAreaViewState) {
    Box(modifier = Modifier.fillMaxSize()) {
        val colors = state.listPolygons.indices.map { generateRandomColor() }
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
                                    properties = buildJsonObject { },
                                    id = JsonPrimitive("$index")
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
                    onClick = object : FeaturesClickHandler {
                        override fun invoke(p1: List<Feature<Geometry, JsonObject?>>): ClickResult {
                            println(p1)
                            return ClickResult.Consume
                        }
                    }
                )
            }
        }
    }
}

private fun generateRandomColor(): Color =
    Color(
        red = Random.nextFloat(),
        green = Random.nextFloat(),
        blue = Random.nextFloat(),
        alpha = 1f,
    )
