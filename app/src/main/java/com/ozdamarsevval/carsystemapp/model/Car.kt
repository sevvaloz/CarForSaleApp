package com.ozdamarsevval.carsystemapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Car(
    var id: String = "",
    var type: String = "",
    var year: Int = 0,
    var brand: String = "",
    var model: String = "",
    var fuelType: String = "",
    var motor: String = "",
    var transmission: String = "",
    var kilometer: Int = 0,
    var price: Int = 0,
    var photo: List<String> = arrayListOf()
) : Parcelable

