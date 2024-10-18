package gr323.shalaev.weather.data.models

data class CityLocationResponse(
    val identifier: Int?,
    val latitude: Double?,
    val longitude: Double?
)

data class CityLocationUi(
    val identifier: Int,
    val latitude: Double,
    val longitude: Double
)

fun CityLocationResponse.toUi(): CityLocationUi {
    val data = this
    return CityLocationUi(
        identifier = data.identifier?: 0,
        latitude = data.latitude?: 0.0,
        longitude = data.longitude?: 0.0
    )
}