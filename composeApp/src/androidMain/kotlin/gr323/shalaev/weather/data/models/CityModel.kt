package gr323.shalaev.weather.data.models

data class CityResponse(
    val identifier: Int?,
    val description: String?
)

data class CityUi(
    val identifier: Int,
    val description: String
){
    companion object{
        val Default = CityUi(
            identifier = 0,
            description = ""
        )
    }
}

fun CityResponse.toUi(): CityUi {
    val data = this
    return CityUi(
        identifier = data.identifier?: 0,
        description = data.description.orEmpty()
    )
}