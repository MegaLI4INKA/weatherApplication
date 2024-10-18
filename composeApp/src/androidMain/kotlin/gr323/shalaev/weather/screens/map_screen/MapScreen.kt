package gr323.shalaev.weather.screens.map_screen

import android.graphics.Point
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import gr323.shalaev.weather.data.models.CoastlineUi
import gr323.shalaev.weather.data.models.CountryUi
import gr323.shalaev.weather.data.models.RegionUi


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(){

    val viewModel: MapViewModel = viewModel()
    val state by viewModel.stateFlow.collectAsState()

    LaunchedEffect(Unit){
        viewModel.loadData()
    }

    if (state.coastline.isNotEmpty()){
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            Column {
                Spacer(modifier = Modifier.size(14.dp))
                Text(text = "MAP", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontSize = 22.sp, fontWeight = Bold)
                Spacer(modifier = Modifier.size(14.dp))
                HorizontalDivider(color = Color.Gray.copy(alpha = 0.3f), thickness = 2.dp)
                Spacer(modifier = Modifier.size(14.dp))
                WorldMap(state.coastline, selectedCityLatitude = 30.0, selectedCityLongitude = 59.0)
                Column(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Region:", fontSize = 18.sp)
                        ExposedDropdownMenuBox(
                            modifier = Modifier.padding(start = 12.dp),
                            expanded = state.showRegionMenu,
                            onExpandedChange = {
                                viewModel.changeRegionMenuState(it)
                            }
                        ) {
                            OutlinedTextField(
                                modifier = Modifier
                                    .menuAnchor(),
                                value = state.selectedRegion.region,
                                onValueChange = {
                                },
                                label = { Text("Select Region") },
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = state.showRegionMenu
                                    )
                                },
                                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                            )

                            ExposedDropdownMenu(
                                expanded = state.showRegionMenu,
                                onDismissRequest = {
                                    viewModel.changeRegionMenuState(false)
                                }
                            ) {
                                state.regions.forEach { region ->
                                    DropdownMenuItem(
                                        text = { Text(region.region) },
                                        onClick = {
                                            viewModel.changeSelectedRegion(region.identifier)
                                            viewModel.changeRegionMenuState(false)
                                        }
                                    )
                                }
                            }
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Country:", fontSize = 18.sp)
                        ExposedDropdownMenuBox(
                            modifier = Modifier.padding(start = 12.dp),
                            expanded = state.showCountryMenu,
                            onExpandedChange = {
                                viewModel.changeCountryMenuState(it)
                            }
                        ) {
                            OutlinedTextField(
                                enabled = state.selectedRegion != RegionUi.Default,
                                modifier = Modifier
                                    .menuAnchor(),
                                value = state.selectedCountry.description,
                                onValueChange = {
                                },
                                label = { Text("Select Region") },
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = state.showCountryMenu
                                    )
                                },
                                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                            )

                            ExposedDropdownMenu(
                                expanded = state.showCountryMenu,
                                onDismissRequest = {
                                    viewModel.changeCountryMenuState(false)
                                }
                            ) {
                                state.countries.forEach { country ->
                                    DropdownMenuItem(
                                        text = { Text(country.description) },
                                        onClick = {
                                            viewModel.changeSelectedCountry(country.identifier)
                                            viewModel.changeCountryMenuState(false)
                                        }
                                    )
                                }
                            }
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "City:", fontSize = 18.sp)
                        ExposedDropdownMenuBox(
                            modifier = Modifier.padding(start = 12.dp),
                            expanded = state.showCityMenu,
                            onExpandedChange = {
                                viewModel.changeCityMenuState(it)
                            }
                        ) {
                            OutlinedTextField(
                                enabled = state.selectedCountry != CountryUi.Default,
                                modifier = Modifier
                                    .menuAnchor(),
                                value = state.selectedCity.description,
                                onValueChange = {
                                },
                                label = { Text("Select Region") },
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = state.showCityMenu
                                    )
                                },
                                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                            )

                            ExposedDropdownMenu(
                                expanded = state.showCityMenu,
                                onDismissRequest = {
                                    viewModel.changeCityMenuState(false)
                                }
                            ) {
                                state.cities.forEach { city ->
                                    DropdownMenuItem(
                                        text = { Text(city.description) },
                                        onClick = {
                                            viewModel.changeSelectedCity(city.identifier)
                                            viewModel.changeCityMenuState(false)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WorldMap(
    points: List<CoastlineUi>,
    selectedCityLatitude: Double?, // Добавляем координаты города
    selectedCityLongitude: Double?
) {
    Box(
        modifier = Modifier.padding(6.dp)
    ) {
        Canvas(modifier = Modifier.aspectRatio(8f / 5)) {
            // Проверяем, есть ли точки для отрисовки
            if (points.isNotEmpty()) {
                // Определяем границы карты
                val minLatitude = points.minOf { it.latitude }
                val maxLatitude = points.maxOf { it.latitude }
                val minLongitude = points.minOf { it.longitude }
                val maxLongitude = points.maxOf { it.longitude }

                // Начинаем рисовать береговую линию
                for (i in 0 until points.size - 1) {
                    val startPoint = points[i]
                    val endPoint = points[i + 1]

                    // Преобразуем координаты в пиксели
                    val startX = latitudeToX(startPoint.latitude, minLatitude, maxLatitude, size.width)
                    val startY = longitudeToY(startPoint.longitude, minLongitude, maxLongitude, size.height)
                    val endX = latitudeToX(endPoint.latitude, minLatitude, maxLatitude, size.width)
                    val endY = longitudeToY(endPoint.longitude, minLongitude, maxLongitude, size.height)

                    val distance = Math.hypot((startX - endX).toDouble(), (startY - endY).toDouble())
                    val threshold = 30f

                    if (distance > threshold) {
                        continue
                    }

                    drawLine(
                        color = Color.Black,
                        start = Offset(startX, startY),
                        end = Offset(endX, endY),
                        strokeWidth = 1.dp.toPx()
                    )
                }

                // Отображаем крестик для выбранного города
                if (selectedCityLatitude != null && selectedCityLongitude != null) {
                    val cityX = latitudeToX(selectedCityLatitude, minLatitude, maxLatitude, size.width)
                    val cityY = longitudeToY(selectedCityLongitude, minLongitude, maxLongitude, size.height)

                    // Рисуем крестик
                    val crossSize = 10.dp.toPx()
                    drawLine(
                        color = Color.Red,
                        start = Offset(cityX - crossSize, cityY - crossSize),
                        end = Offset(cityX + crossSize, cityY + crossSize),
                        strokeWidth = 2.dp.toPx()
                    )
                    drawLine(
                        color = Color.Red,
                        start = Offset(cityX - crossSize, cityY + crossSize),
                        end = Offset(cityX + crossSize, cityY - crossSize),
                        strokeWidth = 2.dp.toPx()
                    )
                }
            }
        }
    }
}


private fun latitudeToX(latitude: Double, minLatitude: Double, maxLatitude: Double, width: Float): Float {
    val normalizedLatitude = (latitude - minLatitude) / (maxLatitude - minLatitude)
    return (normalizedLatitude * width).toFloat()
}

private fun longitudeToY(longitude: Double, minLongitude: Double, maxLongitude: Double, height: Float): Float {
    val normalizedLongitude = (longitude - minLongitude) / (maxLongitude - minLongitude)
    return ((1 - normalizedLongitude) * height).toFloat() // Инвертируем Y
}