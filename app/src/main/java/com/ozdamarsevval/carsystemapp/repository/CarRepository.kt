package com.ozdamarsevval.carsystemapp.repository

import android.net.Uri
import com.ozdamarsevval.carsystemapp.model.Car
import com.ozdamarsevval.carsystemapp.utils.UiState

interface CarRepository {
    fun getCars(result: (UiState<List<Car>>) -> Unit)
    fun addCar(car: Car, result: (UiState<String>) -> Unit)
    fun updateCar(car: Car, result: (UiState<String>) -> Unit)
    fun deleteCar(car: Car, result: (UiState<String>) -> Unit)
    suspend fun uploadMultipleFile(fileUri: List<Uri>, onResult: (UiState<List<Uri>>) -> Unit)
}