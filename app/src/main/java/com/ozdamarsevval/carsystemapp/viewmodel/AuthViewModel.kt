package com.ozdamarsevval.carsystemapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.ozdamarsevval.carsystemapp.model.User
import com.ozdamarsevval.carsystemapp.repository.AuthRepository
import com.ozdamarsevval.carsystemapp.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
        val repository: AuthRepository
    ): ViewModel() {

    private val _register = MutableLiveData<UiState<String>>()
    val register: LiveData<UiState<String>> get() = _register

    private val _login = MutableLiveData<UiState<String>>()
    val login: LiveData<UiState<String>> get() = _login

    private val _current = MutableLiveData<UiState<FirebaseUser?>>()
    val cur: LiveData<UiState<FirebaseUser?>> get() = _current


    fun register(email: String, password: String, user: User) {
        _register.value = UiState.Loading
        repository.registerUser(email = email, password = password, user = user){
            _register.value = it
        }
    }

    fun login(email: String, password: String) {
        _login.value = UiState.Loading
        repository.loginUser(email, password){
            _login.value = it
        }
    }

    fun logout(result: () -> Unit){
        repository.logout(result)
    }


}