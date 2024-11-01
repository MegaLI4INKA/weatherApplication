package gr323.shalaev.weather.screens.graph_screen

import android.util.Log
import androidx.lifecycle.viewModelScope
import gr323.shalaev.weather.data.api.WeatherApi
import gr323.shalaev.weather.data.api.handleApiResponse
import gr323.shalaev.weather.data.models.DailyUI
import gr323.shalaev.weather.data.models.MeasurementTimeRangeUI
import gr323.shalaev.weather.data.models.formatDateToISOString
import gr323.shalaev.weather.data.models.toUI
import gr323.shalaev.weather.data.models.toUi
import gr323.shalaev.weather.di.ApiModule
import gr323.shalaev.weather.navigation.Screens
import gr323.shalaev.weather.screens.BaseScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class GraphViewModel() : BaseScreenViewModel<GraphState>(GraphState.InitState) {
    private val weatherApi: WeatherApi = ApiModule.provideApi()
    var jobChangePoint: Job? = null
    fun changePoints(points: Int) {
        jobChangePoint?.cancel()
        jobChangePoint =  CoroutineScope(Dispatchers.IO).launch {
            delay(300L)
            reduce {
                state.copy(
                    points = points
                )
            }
            changeDateRange(points)
        }
    }
    var jobChangeDate: Job? = null
    fun changeDate(startDate: Date = state.dateStart, endDate: Date = state.dateEnd, after: () -> Unit= {}) {
        println("endDate - $endDate")
        jobChangeDate?.cancel()
        jobChangeDate=  CoroutineScope(Dispatchers.IO).launch {
            delay(200L)
            reduce {
                state.copy(
                    dateStart = startDate,
                    dateEnd = endDate
                )
            }
            after()
            changeDateRange(startDate = startDate, endDate = endDate,)
        }
    }

    var jobChangeRange: Job? = null
    fun changeDateRange(points: Int = state.points, endDate: Date = state.dateEnd, startDate: Date = state.dateStart) {
        println("changeDateRange")
        jobChangeRange?.cancel()
        jobChangeRange =  CoroutineScope(Dispatchers.IO).launch {
            reduce { state.copy(loading = true) }
            delay(300L)
            val dates = generateEqualDates(
                allDays = state.daily,
                dateFrom = startDate,
                dateTo = endDate,
                points = points
            )
            println("dates - $dates")
            reduce {
                state.copy(
                    datesByPoints = dates, loading = false
                )
            }
        }
    }

    fun getMeasurementTimeRange(city: Int, cityName: String){
        viewModelScope.launch {
            handleApiResponse(
                call = {
                    weatherApi.getMeasurementTimeRange(city)
                },
                onSuccess = { response ->
                    val measurementTimeRange = response.firstOrNull()?.toUI()
                        ?: MeasurementTimeRangeUI.Default
                    reduce {
                        state.copy(
                            measurementTimeRange = measurementTimeRange,
                            dateStart = measurementTimeRange.tsMin,
                            dateEnd = measurementTimeRange.tsMax
                        )
                    }

                    getDailyTemperatures(
                        city
                    )
                }
            )
        }
    }

    fun getDailyTemperatures(city: Int){
        viewModelScope.launch {
            handleApiResponse(
                call = {
                    weatherApi.getDailyTemperatures(
                        city,
                        state.measurementTimeRange.tsMin.formatDateToISOString(),
                        state.measurementTimeRange.tsMax.formatDateToISOString()
                    )
                },
                onSuccess = { response ->
                    reduce {
                        state.copy(
                            daily = response.map { it.toUI() },
                        )
                    }
                    changeDateRange()
                }
            )
        }
    }

}

fun generateEqualDates(
    allDays: List<DailyUI>,
    dateFrom: Date,
    dateTo: Date,
    points: Int
): List<DailyUI> {
    val dates = mutableListOf<DailyUI>()

    val calendarFrom = Calendar.getInstance().apply {
        time = dateFrom
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    val calendarTo = Calendar.getInstance().apply {
        time = dateTo
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    val validDays = allDays.filter { it.ts in calendarFrom.time..calendarTo.time }

    if (validDays.isNotEmpty()) {
        val interval = (validDays.size - 1).toDouble() / (points - 1)
        for (i in 0 until points) {
            val index = (i * interval).toInt().coerceIn(0, validDays.lastIndex)
            dates.add(validDays[index])
        }
    } else {
        dates.add(DailyUI(ts = calendarFrom.time, temperature = 0.0))
    }

    return dates
}
