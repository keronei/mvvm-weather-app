package com.keronei.weatherapp.data.model

import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("1h")
    val one_hour: Double
)