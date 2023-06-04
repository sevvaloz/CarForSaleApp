package com.ozdamarsevval.carsystemapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ozdamarsevval.carsystemapp.model.Brand
import com.ozdamarsevval.carsystemapp.model.Car
import com.ozdamarsevval.carsystemapp.model.Model
import com.ozdamarsevval.carsystemapp.model.Type
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


    private val _brands = MutableLiveData<UiState<List<Brand>>>()
    val brands: LiveData<UiState<List<Brand>>> get() = _brands

    private val _addBrand = MutableLiveData<UiState<String>>()
    val addBrand: LiveData<UiState<String>> get() = _addBrand


    private val _types = MutableLiveData<UiState<List<Type>>>()
    val types: LiveData<UiState<List<Type>>> get() = _types

    private val _addType = MutableLiveData<UiState<String>>()
    val addType: LiveData<UiState<String>> get() = _addType


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

    fun addBrand(brand: Brand){
        _addBrand.value = UiState.Loading
        repository.addBrand(brand) {
            _addBrand.value = it
        }
    }
    fun getBrands(){
        _brands.value = UiState.Loading
        repository.getBrands {
            _brands.value = it
        }
    }

    fun addType(type: Type){
        _addType.value = UiState.Loading
        repository.addType(type) {
            _addType.value = it
        }
    }
    fun getTypes(){
        _types.value = UiState.Loading
        repository.getTypes {
            _types.value = it
        }
    }


}