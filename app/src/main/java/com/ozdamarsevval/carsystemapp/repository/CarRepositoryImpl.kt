package com.ozdamarsevval.carsystemapp.repository

import android.net.Uri
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.ozdamarsevval.carsystemapp.model.Car
import com.ozdamarsevval.carsystemapp.utils.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File

class CarRepositoryImpl(
    val db: FirebaseFirestore,
    val storageReference: StorageReference
): CarRepository {


    override fun getCars(result: (UiState<List<Car>>) -> Unit) {
        db.collection("Cars").get()
            .addOnSuccessListener { query ->
                val cars = arrayListOf<Car>()
                for(document in query){
                    val car = document.toObject(Car::class.java)
                    cars.add(car)
                }
                result.invoke(UiState.Success(cars))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun addCar(car: Car, result: (UiState<String>) -> Unit) {
        val document = db.collection("Cars").document()
        car.id = document.id
        document.set(car)
            .addOnSuccessListener {
                result.invoke(UiState.Success("Car is on sale"))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun updateCar(car: Car, result: (UiState<String>) -> Unit) {
        val document = db.collection("Cars").document(car.id)
        document.set(car)
            .addOnSuccessListener {
                result.invoke(UiState.Success("Car updated"))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun deleteCar(car: Car, result: (UiState<String>) -> Unit) {
        db.collection("Cars").document(car.id).delete()
            .addOnSuccessListener {
                result.invoke(UiState.Success("Car deleted"))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.message))
            }
    }

    override suspend fun uploadMultipleFile(fileUri: List<Uri>, onResult: (UiState<List<Uri>>) -> Unit) {
        try {
            val uri: List<Uri> = withContext(Dispatchers.IO) {
                fileUri.map { image ->
                    async {
                        storageReference.child(image.lastPathSegment ?: "${System.currentTimeMillis()}")
                            .putFile(image)
                            .await()
                            .storage
                            .downloadUrl
                            .await()
                    }
                }.awaitAll()
            }
            onResult.invoke(UiState.Success(uri))
        } catch (e: FirebaseException){
            onResult.invoke(UiState.Failure(e.message))
        }catch (e: Exception){
            onResult.invoke(UiState.Failure(e.message))
        }
    }
}