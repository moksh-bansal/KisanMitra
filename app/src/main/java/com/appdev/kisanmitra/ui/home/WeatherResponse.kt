package com.appdev.kisanmitra.ui.home

data class WeatherResponse(
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind,
    val name: String
)

data class Main(
    val temp: Double,
    val humidity: Int
)

data class Weather(
    val description: String
)

data class Wind(
    val speed: Double
)