package gr323.shalaev.weather.di

import gr323.shalaev.weather.data.api.WeatherApi
import gr323.shalaev.weather.ip
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiModule {

    fun provideApi(): WeatherApi {

        return Retrofit.Builder().baseUrl("http://${ip.value}:3000").addConverterFactory(
            GsonConverterFactory.create())
            .build().create(WeatherApi::class.java)
    }
}