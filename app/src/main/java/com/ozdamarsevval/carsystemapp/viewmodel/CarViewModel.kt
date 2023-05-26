package com.ozdamarsevval.carsystemapp.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ozdamarsevval.carsystemapp.model.Car
import com.ozdamarsevval.carsystemapp.repository.CarRepository
import com.ozdamarsevval.carsystemapp.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarViewModel @Inject constructor(
        val repository: CarRepository
    ): ViewModel() {

    private val _cars = MutableLiveData<UiState<List<Car>>>()
    val cars: LiveData<UiState<List<Car>>> get() = _cars

    private val _addCar = MutableLiveData<UiState<String>>()
    val addCar: LiveData<UiState<String>> get() = _addCar

    private val _updateCar = MutableLiveData<UiState<String>>()
    val updateCar: LiveData<UiState<String>> get() = _updateCar

    private val _deleteCar = MutableLiveData<UiState<String>>()
    val deleteCar: LiveData<UiState<String>> get() = _deleteCar


    fun getCars() {
        _cars.value = UiState.Loading
        repository.getCars {
            _cars.value = it
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

    fun onUploadMultipleFile(fileUri: List<Uri>, onResult: (UiState<List<Uri>>) -> Unit){
        onResult.invoke(UiState.Loading)
        viewModelScope.launch {
            repository.uploadMultipleFile(fileUri,onResult)
        }
    }

}