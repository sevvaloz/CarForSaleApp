package com.ozdamarsevval.carsystemapp.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.ozdamarsevval.carsystemapp.model.Car
import com.ozdamarsevval.carsystemapp.utils.UiState

class CarRepositoryImpl(
    val db: FirebaseFirestore
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

    override fun addCar(car: Car, result: (UiState<Pair<Car, String>>) -> Unit) {
        val document = db.collection("Cars").document()
        car.id = document.id
        document.set(car)
            .addOnSuccessListener {
                result.invoke(UiState.Success(Pair(car, "Car is on sale")))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun updateCar(car: Car, result: (UiState<String>) -> Unit) {
        val document = db.collection("Cars").document(car.id)
        document.set(car)
            .addOnSuccessListener {
                result.invoke(UiState.Success("Car has been update successfully"))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun deleteCar(car: Car, result: (UiState<String>) -> Unit) {
        db.collection("Cars").document(car.id).delete()
            .addOnSuccessListener {
                result.invoke(UiState.Success("Car deleted successfully"))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.message))
            }
    }
}