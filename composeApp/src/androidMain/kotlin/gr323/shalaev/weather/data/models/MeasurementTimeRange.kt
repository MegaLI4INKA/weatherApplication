package gr323.shalaev.weather.data.models

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class MeasurementTimeRangeResponse(
    @SerializedName("ts_min")
    val tsMin: String?,
    @SerializedName("ts_max")
    val tsMax: String?
)

data class MeasurementTimeRangeUi(
    val tsMin: Date,
    val tsMax: Date
){
    companion object{
        val Default = MeasurementTimeRangeUi(
            tsMin = Date(),
            tsMax = Date()
        )
    }
}

fun MeasurementTimeRangeResponse.toUi(): MeasurementTimeRangeUi{
    val data = this
    return MeasurementTimeRangeUi(
        tsMax = data.tsMax.orEmpty().parseDate(),
        tsMin = data.tsMin.orEmpty().parseDate()
    )
}


fun String.parseDate(): Date {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        format.parse(this).let {
            it ?: Date()
        }

    } catch (e: Exception) {
        Date()
    }
}