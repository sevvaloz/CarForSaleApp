package com.ozdamarsevval.carsystemapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ozdamarsevval.carsystemapp.model.Car
import com.ozdamarsevval.carsystemapp.repository.CarRepository
import com.ozdamarsevval.carsystemapp.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CarViewModel @Inject constructor(
    val repository: CarRepository
): ViewModel() {

    private val _car = MutableLiveData<UiState<List<Car>>>()
    val car: LiveData<UiState<List<Car>>> get() = _car

    private val _addCar = MutableLiveData<UiState<Pair<Car,String>>>()
    val addCar: LiveData<UiState<Pair<Car, String>>> get() = _addCar

    private val _updateCar = MutableLiveData<UiState<String>>()
    val updateCar: LiveData<UiState<String>> get() = _updateCar

    private val _deleteCar = MutableLiveData<UiState<String>>()
    val deleteCar: LiveData<UiState<String>> get() = _deleteCar


    fun getCars() {
        _car.value = UiState.Loading
        repository.getCars() {carList ->
            _car.value = carList
        }
    }

    fun addCar(car: Car){
        _addCar.value = UiState.Loading
        repository.addCar(car) {
            _addCar.value = it
        }
    }

    fun updateCar(car: Car){
        _updateCar.value = UiState.Loading
        repository.updateCar(car) {
            _updateCar.value = it
        }
    }

    fun deleteCar(car: Car){
        _deleteCar.value = UiState.Loading
        repository.deleteCar(car) {
            _deleteCar.value = it
        }
    }

}