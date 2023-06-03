package com.ozdamarsevval.carsystemapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ozdamarsevval.carsystemapp.model.Car
import com.ozdamarsevval.carsystemapp.model.Model
import com.ozdamarsevval.carsystemapp.repository.CarSpecialityRepository
import com.ozdamarsevval.carsystemapp.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CarSpecialityViewModel @Inject constructor(
    val repository: CarSpecialityRepository
): ViewModel() {

    private val _models = MutableLiveData<UiState<List<Model>>>()
    val models: LiveData<UiState<List<Model>>> get() = _models

    private val _addModel = MutableLiveData<UiState<String>>()
    val addModel: LiveData<UiState<String>> get() = _addModel

    fun addModel(model: Model){
        _addModel.value = UiState.Loading
        repository.addModel(model) {
            _addModel.value = it
        }
    }

    fun getModels(){
        _models.value = UiState.Loading
        repository.getModels {
            _models.value = it
        }
    }


}