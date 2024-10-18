package gr323.shalaev.weather.screens.graph_screen

import gr323.shalaev.weather.data.models.DailyTemperatureUi
import gr323.shalaev.weather.data.models.MeasurementTimeRangeUi
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class GraphState(
    val dateStart: Date,
    val dateEnd: Date,
    val dailyTemperatures: List<DailyTemperatureUi>
){
    companion object{
        val InitState = GraphState(
            dateStart = Date(),
            dateEnd = Date(),
            dailyTemperatures = emptyList()
        )
    }
}

object DateFormatter {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun formatDate(date: Date): String {
        return dateFormat.format(date)
    }
}