package gr323.shalaev.weather.screens.graph_screen

import android.util.Log
import androidx.lifecycle.viewModelScope
import gr323.shalaev.weather.data.api.handleApiResponse
import gr323.shalaev.weather.data.models.DailyTemperatureResponse
import gr323.shalaev.weather.data.models.MeasurementTimeRangeUi
import gr323.shalaev.weather.data.models.toUi
import gr323.shalaev.weather.di.ApiModule
import gr323.shalaev.weather.navigation.Screens
import gr323.shalaev.weather.screens.BaseScreenViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class GraphViewModel: BaseScreenViewModel<GraphState>(GraphState.InitState){

    private val api = ApiModule.provideApi()


    fun changeDateStart(newDate: Date){
        reduce {
            state.copy(
                dateStart = newDate
            )
        }
    }

    fun changeDateEnd(newDate: Date){
        reduce {
            state.copy(
                dateEnd = newDate
            )
        }
    }

    fun loadData(city: Int) {
        viewModelScope.launch {
            handleApiResponse(
                call = { api.getMeasurementTimeRange(city) },
                onSuccess = { response ->
                    val measurementTimeRangeUi = response.firstOrNull()?.toUi() ?: MeasurementTimeRangeUi.Default
                    reduce {
                        state.copy(
                            dateStart = measurementTimeRangeUi.tsMin,
                            dateEnd = measurementTimeRangeUi.tsMax
                        )
                    }
                    getDailyTemperature(city, measurementTimeRangeUi.tsMin, measurementTimeRangeUi.tsMax)
                }
            )
        }
    }

    private fun getDailyTemperature(city: Int, fromDate: Date, toDate: Date) {
        viewModelScope.launch {
            handleApiResponse(
                call = { api.getDailyTemperatures(city, formatDateToISOString(fromDate), formatDateToISOString(toDate)) },
                onSuccess = { response ->
                    reduce {
                        state.copy(dailyTemperatures = response.map { it.toUi() })
                    }
                }
            )
        }
    }


    fun formatDateToISOString(date: Date): String {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        return format.format(date)
    }
}