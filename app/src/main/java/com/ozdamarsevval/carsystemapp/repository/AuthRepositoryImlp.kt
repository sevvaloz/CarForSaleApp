package com.ozdamarsevval.carsystemapp.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ozdamarsevval.carsystemapp.model.User
import com.ozdamarsevval.carsystemapp.utils.UiState

class AuthRepositoryImlp(
    val auth: FirebaseAuth,
    val db: FirebaseFirestore,
    ): AuthRepository {


    override fun registerUser(
        email: String,
        password: String,
        user: User,
        result: (UiState<String>) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    user.id = task.result.user?.uid ?: ""
                    db.collection("Users").document(user.id).set(user)
                        .addOnSuccessListener {
                            result.invoke(UiState.Success("User has been update successfully"))
                        }
                        .addOnFailureListener {
                            result.invoke(UiState.Failure(it.localizedMessage))
                        }
                }else{
                    try {
                        throw task.exception ?: java.lang.Exception("Invalid authentication")
                    } catch (e: Exception) {
                        result.invoke(UiState.Failure(e.message))
                    }
                }
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }

    override fun loginUser(email: String, password: String, result: (UiState<String>) -> Unit) {
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    storeSession(id = task.result.user?.uid ?: ""){
                        if (it == null){
                            result.invoke(UiState.Failure("Failed to store local session"))
                        }else{
                            result.invoke(UiState.Success("Login successfully!"))
                        }
                    }
                }
            }.addOnFailureListener {
                result.invoke(UiState.Failure("Authentication failed, check email and password"))
            }
    }

    override fun logout(result: () -> Unit) {
        auth.signOut()
        result.invoke()
    }

    override fun storeSession(id: String, result: (User?) -> Unit) {
        db.collection("Users").document(id).get()
            .addOnCompleteListener {
                if (it.isSuccessful){
                    val user = it.result.toObject(User::class.java)
                    result.invoke(user)
                }else{
                    result.invoke(null)
                }
            }
            .addOnFailureListener {
                result.invoke(null)
            }
    }
/*    override fun getSession(result: (User?) -> Unit) {
        val user_str = appPreferences.getString("user_session",null)
        if (user_str == null){
            result.invoke(null)
        }else{
            val user = gson.fromJson(user_str,User::class.java)
            result.invoke(user)
        }
    }*/


}