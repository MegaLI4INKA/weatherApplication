package gr323.shalaev.weather.data.api

import gr323.shalaev.weather.data.models.CityLocationResponse
import gr323.shalaev.weather.data.models.CityResponse
import gr323.shalaev.weather.data.models.CoastlineResponse
import gr323.shalaev.weather.data.models.CountryResponse
import gr323.shalaev.weather.data.models.DailyResponse
import gr323.shalaev.weather.data.models.MeasurementTimeRangeResponse
import gr323.shalaev.weather.data.models.RegionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.Date


interface WeatherApi {

    @GET("rpc/get_measurement_time_range")
    suspend fun getMeasurementTimeRange(
        @Query("city") city: Int
    ): Response<List<MeasurementTimeRangeResponse>>

    @GET("rpc/get_daily_temperatures")
    suspend fun getDailyTemperatures(
        @Query("city") city: Int,
        @Query("ts_from") tsFrom: String,
        @Query("ts_to") tsTo: String,
    ): Response<List<DailyResponse>>

    @GET("coastline")
    suspend fun getCoastline(): Response<List<CoastlineResponse>>

    @GET("region_countries")
    suspend fun getRegionCountries(): Response<List<RegionResponse>>

    @GET("rpc/get_countries")
    suspend fun getCountriesFromRegion(@Query("region") region: Int): Response<List<CountryResponse>>

    @GET("rpc/get_cities")
    suspend fun getCitiesFromCountry(@Query("country") country: Int): Response<List<CityResponse>>

    @GET("rpc/get_city_locations")
    suspend fun getCityLocations(): Response<List<CityLocationResponse>>



}