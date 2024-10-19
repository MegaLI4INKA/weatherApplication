package gr323.shalaev.weather.di

import gr323.shalaev.weather.data.api.WeatherApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiModule {

    fun provideApi(): WeatherApi {

        return Retrofit.Builder().baseUrl("http://192.168.0.105:3000").addConverterFactory(
            GsonConverterFactory.create())
            .build().create(WeatherApi::class.java)
    }
}