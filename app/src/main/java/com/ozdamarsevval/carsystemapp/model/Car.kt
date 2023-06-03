package com.ozdamarsevval.carsystemapp.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Car(
    var id: String = "",
    var type: Type = Type("", ""),
    var year: String = "",
    var brand: Brand = Brand("",""),
    var model: Model = Model("",""),
    var fuelType: String = "",
    var motor: String = "",
    var transmission: String = "",
    var kilometer: String = "",
    var price: String = "",
    var owner: String = "",
    @ServerTimestamp
    val date: Date = Date(),
    val images: List<String> = arrayListOf()
) : java.io.Serializable

