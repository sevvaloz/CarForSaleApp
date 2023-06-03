package com.ozdamarsevval.carsystemapp.repository

import com.ozdamarsevval.carsystemapp.model.Brand
import com.ozdamarsevval.carsystemapp.model.Car
import com.ozdamarsevval.carsystemapp.model.Model
import com.ozdamarsevval.carsystemapp.model.Type
import com.ozdamarsevval.carsystemapp.utils.UiState

interface CarSpecialityRepository {
    fun addModel(model: Model, result: (UiState<String>) -> Unit)
    fun getModels(result: (UiState<List<Model>>) -> Unit)
    fun addBrand(brand: Brand, result: (UiState<String>) -> Unit)
    fun addType(type: Type, result: (UiState<String>) -> Unit)
}