package gr323.shalaev.weather.screens.graph_screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import gr323.shalaev.weather.data.models.DailyUI
import gr323.shalaev.weather.navigation.Screens
import kotlinx.coroutines.flow.asStateFlow
import org.jetbrains.compose.resources.stringResource
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.ceil
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GraphScreen(cityId: Int, cityName: String){
    val viewModel: GraphViewModel = viewModel()
    val state by viewModel.stateFlow.collectAsState()
    var sliderValue by remember { mutableFloatStateOf(20f) }
    var openStartDialog by remember {
        mutableStateOf(false)
    }
    var openEndDialog by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(viewModel) {
        viewModel.getMeasurementTimeRange(cityId, cityName)
    }

//    ChangeColorDialog(
//
//    )

    if (state.openChangeColorDialog){
        ChangeColorDialog(
            currentColor = state.currentColor,
            selectedColors = state.selectedGraphColor,
            onDismissRequest = {
                viewModel.changeDialogState(false)
            },
            onChangeColor = { colorName ->
                viewModel.changeCurrentColor(colorName)
                viewModel.changeDialogState(false)
            }
        )
    }

    Column(
        modifier = Modifier
            .verticalScroll(
                rememberScrollState()
            )
            .fillMaxSize()
            .animateContentSize()
    ) {

        Spacer(modifier = Modifier.size(14.dp))
        Text(text = cityName, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontSize = 22.sp, fontWeight = Bold)
        Spacer(modifier = Modifier.size(14.dp))
        HorizontalDivider(color = Color.Gray.copy(alpha = 0.3f), thickness = 2.dp)

        if (state.daily.isNotEmpty()) {
            if (state.points != 0) {
                var sliderStartValue by remember { mutableFloatStateOf(0f) }
                var sliderEndValue by remember { mutableFloatStateOf((state.daily.size - 1).toFloat()) }
                val startDatePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = state.dateStart.time
                )

                val endDatePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = state.dateEnd.time
                )
                var sliderState by remember {
                    mutableStateOf(
                        SliderState(
                            value = state.points.toFloat(),
                            valueRange = 1f..state.daily.size.toFloat(),
                            onValueChangeFinished = {
                            },
                            steps = state.daily.size - 1
                        )
                    )
                }

                var sliderStartState by remember {
                    mutableStateOf(
                        SliderState(
                            value = 0f,
                            valueRange = 0f..state.daily.size.toFloat(),
                            onValueChangeFinished = {
                            },
                            steps = state.daily.size - 1
                        )
                    )
                }

                var sliderEndState by remember {
                    mutableStateOf(
                        SliderState(
                            value = state.daily.size.toFloat(),
                            valueRange = 0f..state.daily.size.toFloat(),
                            onValueChangeFinished = {
                            },
                            steps = state.daily.size - 1
                        )
                    )
                }


                LaunchedEffect(sliderState.value) {
                    sliderValue = sliderState.value
                    viewModel.changePoints(sliderValue.roundToInt())
                }

                LaunchedEffect(sliderStartState.value) {
                    sliderStartValue = sliderStartState.value
                    state.daily.getOrNull(sliderStartState.value.roundToInt())?.let {
                        startDatePickerState.selectedDateMillis = it.ts.time
                        viewModel.changeDate(startDate = it.ts)
                    }
                }

                LaunchedEffect(sliderEndState.value) {
                    sliderEndValue = sliderEndState.value
                    state.daily.getOrNull(sliderEndState.value.roundToInt())?.let {
                        endDatePickerState.selectedDateMillis = it.ts.time
                        viewModel.changeDate(endDate = it.ts)
                    }
                }
                val selectedStep = sliderValue.roundToInt()
                Spacer(modifier = Modifier.size(20.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    val startMutableInteractionSource = remember {
                        MutableInteractionSource()
                    }
                    val endMutableInteractionSource = remember {
                        MutableInteractionSource()
                    }
                    OutlinedTextField(
                        modifier = Modifier
                            .weight(1f),
                        value = state.dateStart.toUI(),
                        onValueChange = {},
                        readOnly = true,
                        maxLines = 1,
                        singleLine = true,
                        leadingIcon = {
                            IconButton(onClick = { openStartDialog = true }) {
                                androidx.compose.material3.Icon(
                                    Icons.Default.DateRange,
                                    contentDescription = "",
                                    modifier = Modifier.size(16.dp),
                                )
                            }
                        },
                        interactionSource = startMutableInteractionSource
                            .also { interactionSource ->
                                LaunchedEffect(interactionSource) {
                                    interactionSource.interactions.collect {
                                        if (it is PressInteraction.Release) {
                                            openStartDialog = true
                                        }
                                    }
                                }
                            },
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                openEndDialog = true
                            },
                        value = state.dateEnd.toUI(),
                        onValueChange = {},
                        readOnly = true,
                        maxLines = 1,
                        singleLine = true,
                        leadingIcon = {
                            IconButton(onClick = { openEndDialog = true }) {
                                androidx.compose.material3.Icon(
                                    Icons.Default.DateRange,
                                    contentDescription = "",
                                    modifier = Modifier.size(16.dp),
                                )
                            }
                        },
                        interactionSource = endMutableInteractionSource
                            .also { interactionSource ->
                                LaunchedEffect(interactionSource) {
                                    interactionSource.interactions.collect {
                                        if (it is PressInteraction.Release) {
                                            openEndDialog = true
                                        }
                                    }
                                }
                            },
                    )
                }
                Spacer(modifier = Modifier.size(20.dp))
                if (!state.loading) {
                    TemperatureGraph(
                        colorSchemeName = state.currentColor,
                        modifier = Modifier.clickable {
                            viewModel.changeDialogState(true)
                        },
                        colors = state.selectedGraphColor,
                        datesByPoints = state.datesByPoints.take(sliderValue.toInt()),
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(80.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.size(20.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Начало",
                        fontSize = 20.sp
                    )
                    Slider(
                        state = sliderStartState,
                        modifier = Modifier
                            .weight(1f),
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Конец",
                        fontSize = 20.sp
                    )
                    Slider(
                        state = sliderEndState,
                        modifier = Modifier
                            .weight(1f)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Точки: $selectedStep",
                        fontSize = 20.sp
                    )
                    Slider(
                        state = sliderState,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (openStartDialog) {
                    val confirmEnabled = remember {
                        derivedStateOf { startDatePickerState.selectedDateMillis != null }
                    }
                    DatePickerDialog(
                        onDismissRequest = {
                            openStartDialog = false
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    viewModel.changeDate(
                                        startDate = Date(
                                            startDatePickerState.selectedDateMillis.orEmpty()
                                                .toStartOfDay()
                                        )
                                    )
                                    val date = Date(
                                        startDatePickerState.selectedDateMillis.orEmpty()
                                            .toStartOfDay()
                                    )
                                    println("date - $date")
                                    println(
                                        "state.daily.firstOrNull { it.ts.time == startDatePickerState.selectedDateMillis } - ${
                                            state.daily.firstOrNull {
                                                it.ts.time == startDatePickerState.selectedDateMillis.orEmpty()
                                                    .toStartOfDay()
                                            }
                                        }"
                                    )
                                    state.daily.firstOrNull {
                                        it.ts.time == startDatePickerState.selectedDateMillis.orEmpty()
                                            .toStartOfDay()
                                    }
                                        ?.let { day ->
                                            sliderStartState.value =
                                                state.daily.indexOf(day).toFloat()
                                        }
                                    openStartDialog = false
                                },
                                enabled = confirmEnabled.value
                            ) {
                                Text("OK")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    openStartDialog = false
                                }
                            ) {
                                Text("Cancel")
                            }
                        }
                    ) {
                        DatePicker(state = startDatePickerState)
                    }
                }

                if (openEndDialog) {
                    val confirmEnabled = remember {
                        derivedStateOf { endDatePickerState.selectedDateMillis != null }
                    }
                    DatePickerDialog(
                        onDismissRequest = {
                            openEndDialog = false
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    viewModel.changeDate(
                                        endDate = Date(
                                            endDatePickerState.selectedDateMillis.orEmpty()
                                                .toStartOfDay()
                                        )
                                    )
                                    state.daily.firstOrNull {
                                        it.ts.time == endDatePickerState.selectedDateMillis.orEmpty()
                                            .toStartOfDay()
                                    }
                                        ?.let { day ->
                                            sliderEndState.value =
                                                state.daily.indexOf(day).toFloat()
                                        }
                                    openEndDialog = false

                                },
                                enabled = confirmEnabled.value
                            ) {
                                Text("OK")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    openEndDialog = false
                                }
                            ) {
                                Text("Cancel")
                            }
                        }
                    ) {
                        DatePicker(state = endDatePickerState)
                    }
                }
            } else {
                Spacer(modifier = Modifier.size(40.dp))
                Box(modifier = Modifier, contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(80.dp)
                    )
                }
            }
        } else {
            Spacer(modifier = Modifier.size(40.dp))
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(80.dp)
                )
            }
        }
    }
}

@Composable
fun TemperatureGraph(
    modifier: Modifier = Modifier,
    datesByPoints: List<DailyUI>,
    colors: Map<String, Pair<Color, Color>>,
    colorSchemeName: String = "GnBu",
) {
    val minTemp = datesByPoints.minOfOrNull { it.temperature }?.let {
        it - (it % 5) - 5
    } ?: 0.0

    val maxTemp = datesByPoints.maxOfOrNull { it.temperature }?.let {
        it + (5 - it % 5)
    } ?: 0.0

    val dateFormat = SimpleDateFormat("yyyy")
    val steps = ceil(((maxTemp - minTemp) / 5)).toInt()
    val canvasHeight = 300.dp
    val visibleLabelsCount = 10
    val yAxisWidth = 40.dp

    val colorPair = colors[colorSchemeName] ?: Pair(Color.Black, Color.Red)


    Column {
        Canvas(
            modifier = modifier
                .padding(start = yAxisWidth)
                .fillMaxWidth()
                .height(20.dp)
        ) {
            val xStep = size.width / datesByPoints.size
            for (index in datesByPoints.indices) {
                val temperature = datesByPoints[index].temperature
                val tempRatio = ((temperature - minTemp) / (maxTemp - minTemp)).toFloat()
                val color = lerp(colorPair.first, colorPair.second, tempRatio)
                drawRect(
                    color = color,
                    topLeft = Offset(xStep * index, 0f),
                    size = Size(xStep, size.height)
                )
            }
        }

        Row {
            Canvas(
                modifier = Modifier
                    .height(canvasHeight)
                    .width(yAxisWidth)
            ) {
                val yStep = size.height / steps
                for (i in 0..steps) {
                    val temp = minTemp + i * 5
                    val yOffset = size.height - i * yStep
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            "${temp}°C",
                            5f,
                            yOffset,
                            android.graphics.Paint().apply {
                                color = android.graphics.Color.BLACK
                                textSize = 30f
                            }
                        )
                    }
                }
            }

            Canvas(
                modifier = Modifier
                    .weight(1f)
                    .height(canvasHeight)
            ) {
                val temperatureRange = maxTemp - minTemp
                val xStep = size.width / datesByPoints.size
                val path = Path()
                val startY = (size.height * (1 - (datesByPoints[0].temperature - minTemp) / temperatureRange)).toFloat()
                path.moveTo(0f, startY)

                val yStep = size.height / steps
                for (i in 0..steps) {
                    val yOffset = size.height - i * yStep
                    drawLine(
                        color = Color.LightGray,
                        start = Offset(0f, yOffset),
                        end = Offset(size.width, yOffset),
                        strokeWidth = 1f
                    )
                }

                for (index in 1 until datesByPoints.size) {
                    val currentPoint = datesByPoints[index]
                    val prevPoint = datesByPoints[index - 1]
                    val prevX = (index - 1) * xStep
                    val currentX = index * xStep
                    val prevY = size.height * (1 - (prevPoint.temperature - minTemp) / temperatureRange.toFloat())
                    val currentY = size.height * (1 - (currentPoint.temperature - minTemp) / temperatureRange.toFloat())
                    val controlX = (prevX + currentX) / 2
                    val controlY = (prevY + currentY) / 2
                    path.quadraticBezierTo(controlX, controlY.toFloat(), currentX, currentY.toFloat())
                }

                drawPath(
                    path = path,
                    color = Color.Black,
                    style = Stroke(width = 4f)
                )

                for (i in 0 until visibleLabelsCount) {
                    val yearPosition = datesByPoints.minOf { it.ts.time } +
                            (datesByPoints.maxOf { it.ts.time } - datesByPoints.minOf { it.ts.time }) * i / (visibleLabelsCount - 1)
                    val xPosition = (size.width * i / (visibleLabelsCount - 1)).toFloat()
                    val yearLabel = dateFormat.format(Date(yearPosition))

                    drawLine(
                        color = Color.LightGray,
                        start = Offset(xPosition, 0f),
                        end = Offset(xPosition, size.height),
                        strokeWidth = 1f
                    )

                    drawContext.canvas.nativeCanvas.apply {
                        save()
                        rotate(90f, xPosition, size.height + 30f)
                        drawText(
                            yearLabel,
                            xPosition,
                            size.height + 30f,
                            android.graphics.Paint().apply {
                                color = android.graphics.Color.BLACK
                                textSize = 24f
                                textAlign = android.graphics.Paint.Align.CENTER
                            }
                        )
                        restore()
                    }
                }
            }
        }
    }
}


fun Date.toUI(): String {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy")
    return dateFormat.format(this)
}

fun Long.toStartOfDay(): Long {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = this@toStartOfDay
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    return calendar.timeInMillis
}

fun Long?.orEmpty(): Long = this ?: 0


@Composable
fun ChangeColorDialog(
    currentColor: String,
    selectedColors: Map<String, Pair<Color, Color>>,
    onChangeColor: (String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color.White)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(18.dp)
            ) {
                selectedColors.forEach { (name, colors) ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(33.dp)
                            .padding(vertical = 8.dp)
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(colors.first, colors.second)
                                )
                            )
                            .clickable {
                                onChangeColor(name)
                            }
                            .border(
                                1.dp,
                                if (currentColor == name){colors.second} else{Color.Transparent}
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = name,
                            color = Color.White,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

            }
        }
    }
}
