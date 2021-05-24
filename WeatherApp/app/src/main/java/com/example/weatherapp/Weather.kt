package com.example.weatherapp

data class Weather (
    val city: City = getDefaultCity(),
    val temperature: Int = 18,
    val descrizioneWeather: String = "Облачно"

)

fun getDefaultCity() = City("Новосибирск", 55.0057511,82.836092)