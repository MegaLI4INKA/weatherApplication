package gr323.shalaev.weather.screens.graph_screen


import androidx.compose.ui.graphics.Color
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
    val selectedGraphColor: Map<String, Pair<Color, Color>>,
    val openChangeColorDialog: Boolean,
    val currentColor: String,
){
    companion object {
        val InitState = GraphState(
            MeasurementTimeRangeUI.Default, emptyList(), Date(), Date(), 20, emptyList(), true,
            openChangeColorDialog = false,
            currentColor = "YlGnBu",
            selectedGraphColor = mapOf(
                "PuRd" to Pair(Color(0xFFF7F4F9), Color(0xFF67001F)),
                "RdPu" to Pair(Color(0xFFF7F4F9), Color(0xFF49006A)),
                "BuPu" to Pair(Color(0xFFF7FCFD), Color(0xFF4D004B)),
                "GnBu" to Pair(Color(0xFFF7FCF0), Color(0xFF084081)),
                "PuBu" to Pair(Color(0xFFF7FCFD), Color(0xFF08306B)),
                "YlGnBu" to Pair(Color(0xFFFFFFD9), Color(0xFF081D58)),
                "PuBuGn" to Pair(Color(0xFFFFF7FB), Color(0xFF1C0862)),
                "BuGn" to Pair(Color(0xFFF7FCFD), Color(0xFF00441B)),
                "YlGn" to Pair(Color(0xFFFFFEE5), Color(0xFF004529)),
                "Greys" to Pair(Color(0xFFFFFFFF), Color(0xFF000000)),
                "Purples" to Pair(Color(0xFFFCFBFD), Color(0xFF3F007D)),
                "Blues" to Pair(Color(0xFFF7FBFF), Color(0xFF08306B)),
                "Greens" to Pair(Color(0xFFF7FCF5), Color(0xFF00441B)),
                "Oranges" to Pair(Color(0xFFFFF5EB), Color(0xFF7F2704)),
                "Reds" to Pair(Color(0xFFFFF5F0), Color(0xFF67000D)),
                "YlOrBr" to Pair(Color(0xFFFFFEE5), Color(0xFF662506)),
                "YlOrRd" to Pair(Color(0xFFFFFFCC), Color(0xFF800026)),
                "OrRd" to Pair(Color(0xFFFFF7EC), Color(0xFF7F0000))
            )
        )
    }
}

object DateFormatter {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun formatDate(date: Date): String {
        return dateFormat.format(date)
    }
}