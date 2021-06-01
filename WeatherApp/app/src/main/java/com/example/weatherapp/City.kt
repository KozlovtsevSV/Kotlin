package com.example.weatherapp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class City(
    val cityName: String,
    val latitude: Double,
    val longitude: Double
    ): Parcelable
