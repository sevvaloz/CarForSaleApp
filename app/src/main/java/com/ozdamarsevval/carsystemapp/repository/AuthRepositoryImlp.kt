package com.ozdamarsevval.carsystemapp.repository

import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.ozdamarsevval.carsystemapp.model.User
import com.ozdamarsevval.carsystemapp.utils.ADMIN
import com.ozdamarsevval.carsystemapp.utils.UiState

class AuthRepositoryImlp(
    val auth: FirebaseAuth,
    val db: FirebaseFirestore,
    val appPref: SharedPreferences,
    val gson: Gson
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
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        result.invoke(UiState.Failure("Authentication failed, Password should be at least 6 characters"))
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        result.invoke(UiState.Failure("Authentication failed, Invalid email entered"))
                    } catch (e: FirebaseAuthUserCollisionException) {
                        result.invoke(UiState.Failure("Authentication failed, Email already registered."))
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
            .addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    storeSession(id = authResult.result.user?.uid ?: ""){ user ->
                        if (user == null){
                            result.invoke(UiState.Failure("Failed to store local session"))
                        } else{
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
        val document = db.collection("Users").document(id).get()
        document.addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val user = task.result.toObject(User::class.java)
                    appPref.edit().putString("user_session", gson.toJson(user)).apply()
                    result.invoke(user)
                }else{
                    result.invoke(null)
                }
            }
            .addOnFailureListener {
                result.invoke(null)
            }
    }


}