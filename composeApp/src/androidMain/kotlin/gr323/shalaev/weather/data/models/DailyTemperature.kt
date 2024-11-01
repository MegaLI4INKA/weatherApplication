package gr323.shalaev.weather.data.models

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.concurrent.thread

data class DailyResponse(
    val ts: String?,
    val temperature: Double?
)

data class DailyUI(
    val ts: Date,
    val temperature: Double
) {
    companion object {
        val Default = DailyUI(
            ts = Date(),
            temperature = 0.0
        )
    }
}

fun DailyResponse.toUI(): DailyUI {
    return DailyUI(
        ts = ts.orEmpty().parseDate(),
        temperature = temperature.orEmpty()
    )
}

fun Double?.orEmpty(): Double = this ?: 0.0