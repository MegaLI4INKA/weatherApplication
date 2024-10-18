package gr323.shalaev.weather.screens.map_screen

import androidx.compose.animation.fadeIn
import gr323.shalaev.weather.data.models.CityUi
import gr323.shalaev.weather.data.models.CoastlineUi
import gr323.shalaev.weather.data.models.CountryUi
import gr323.shalaev.weather.data.models.RegionUi

data class MapState(
    val coastline: List<CoastlineUi>,
    val regions: List<RegionUi>,
    val countries: List<CountryUi>,
    val cities: List<CityUi>,
    val showRegionMenu: Boolean,
    val showCountryMenu: Boolean,
    val showCityMenu: Boolean,

    val selectedRegion: RegionUi,
    val selectedCountry: CountryUi,
    val selectedCity: CityUi,
){
    companion object{
        val InitState = MapState(
            coastline = emptyList(),
            regions = emptyList(),
            countries = emptyList(),
            cities = emptyList(),
            showRegionMenu = false,
            showCountryMenu = false,
            showCityMenu = false,
            selectedRegion = RegionUi.Default,
            selectedCountry = CountryUi.Default,
            selectedCity = CityUi.Default
        )
    }
}