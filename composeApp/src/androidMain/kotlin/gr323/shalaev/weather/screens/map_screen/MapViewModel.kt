package gr323.shalaev.weather.screens.map_screen

import androidx.lifecycle.viewModelScope
import gr323.shalaev.weather.data.api.handleApiResponse
import gr323.shalaev.weather.data.models.CityUi
import gr323.shalaev.weather.data.models.CountryUi
import gr323.shalaev.weather.data.models.MeasurementTimeRangeUi
import gr323.shalaev.weather.data.models.RegionUi
import gr323.shalaev.weather.data.models.toUi
import gr323.shalaev.weather.di.ApiModule
import gr323.shalaev.weather.screens.BaseScreenViewModel
import gr323.shalaev.weather.screens.graph_screen.GraphState
import kotlinx.coroutines.launch
import java.util.Date

class MapViewModel: BaseScreenViewModel<MapState>(MapState.InitState){

    private val api = ApiModule.provideApi()

    fun loadData(){
        viewModelScope.launch {
            getCostline()
            getRegions()
            getCitiesLocation()
        }
    }

    fun changeRegionMenuState(menuState: Boolean){
        reduce {
            state.copy(
                showRegionMenu = menuState
            )
        }
    }

    fun changeCountryMenuState(menuState: Boolean){
        reduce {
            state.copy(
                showCountryMenu = menuState
            )
        }
    }

    fun changeCityMenuState(menuState: Boolean){
        reduce {
            state.copy(
                showCityMenu = menuState
            )
        }
    }

    fun changeSelectedRegion(regionId: Int){
        reduce {
            state.copy(
                selectedRegion = state.regions.find { it.identifier == regionId }?: RegionUi.Default
            )
        }
        getCountries(regionId)
    }

    fun changeSelectedCountry(countryId: Int){
        reduce {
            state.copy(
                selectedCountry = state.countries.find { it.identifier == countryId }?: CountryUi.Default
            )
        }
        getCities(countryId)
    }

    fun changeSelectedCity(cityId: Int){
        reduce {
            state.copy(
                selectedCity = state.cities.find { it.identifier == cityId }?: CityUi.Default
            )
        }
    }



    fun getCostline(){
        viewModelScope.launch {
            handleApiResponse(
                call = { api.getCoastline() },
                onSuccess = { response ->
                    reduce {
                        state.copy(coastline = response.map { it.toUi() })
                    }
                }
            )
        }
    }

    fun getRegions(){
        viewModelScope.launch {
            handleApiResponse(
                call = { api.getRegionCountries() },
                onSuccess = { response ->
                    reduce {
                        state.copy(regions = response.map { it.toUi() })
                    }
                }
            )
        }
    }

    private fun getCountries(regionId: Int){
        viewModelScope.launch {
            handleApiResponse(
                call = { api.getCountriesFromRegion(regionId) },
                onSuccess = { response ->
                    reduce {
                        state.copy(countries = response.map { it.toUi() })
                    }
                }
            )
        }
    }

    fun getCities(countryId: Int){
        viewModelScope.launch {
            handleApiResponse(
                call = { api.getCitiesFromCountry(countryId) },
                onSuccess = { response ->
                    reduce {
                        state.copy(cities = response.map { it.toUi() })
                    }
                }
            )
        }
    }

    fun getCitiesLocation(){
        viewModelScope.launch {
            handleApiResponse(
                call = { api.getCityLocations() },
                onSuccess = { response ->
                    reduce {
                        state.copy(cityLocations = response.map { it.toUi() })
                    }
                }
            )
        }
    }
}