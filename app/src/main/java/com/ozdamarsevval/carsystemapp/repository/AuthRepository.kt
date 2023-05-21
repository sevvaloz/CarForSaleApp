package com.ozdamarsevval.carsystemapp.repository

import com.ozdamarsevval.carsystemapp.model.User
import com.ozdamarsevval.carsystemapp.utils.UiState

interface AuthRepository {

    fun registerUser(email: String, password: String, user: User, result: (UiState<String>) -> Unit)

    fun loginUser(email: String, password: String, result: (UiState<String>) -> Unit)

    fun logout(result: () -> Unit)

    fun storeSession(id: String, result: (User?) -> Unit)

    //fun getSession(result: (User?) -> Unit)

}