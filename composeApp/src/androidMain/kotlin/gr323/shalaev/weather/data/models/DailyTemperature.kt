package gr323.shalaev.weather.data.models

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.concurrent.thread

data class DailyTemperatureResponse(
    val ts: String?,
    val temperature: Double?
)

data class DailyTemperatureUi(
    val ts: Date,
    val temperature: Double
){
    companion object{
        val Default = DailyTemperatureUi(
            ts = Date(),
            temperature = 0.0
        )
    }
}

fun DailyTemperatureResponse.toUi(): DailyTemperatureUi {
    val data = this
    return DailyTemperatureUi(
        ts = data.ts.orEmpty().parseDate(),
        temperature = data.temperature?: 0.0
    )
}