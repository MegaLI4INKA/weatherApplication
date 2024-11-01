package gr323.shalaev.weather.screens.graph_screen

import gr323.shalaev.weather.data.models.DailyUI
import gr323.shalaev.weather.data.models.MeasurementTimeRangeUI
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class GraphState(
    val measurementTimeRange: MeasurementTimeRangeUI,
    val daily: List<DailyUI>,
    val dateStart: Date,
    val dateEnd: Date,
    val points: Int,
    val datesByPoints: List<DailyUI>,
    val loading: Boolean,
){
    companion object {
        val InitState = GraphState(MeasurementTimeRangeUI.Default, emptyList(), Date(), Date(), 20, emptyList(), true)
    }
}

object DateFormatter {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun formatDate(date: Date): String {
        return dateFormat.format(date)
    }
}