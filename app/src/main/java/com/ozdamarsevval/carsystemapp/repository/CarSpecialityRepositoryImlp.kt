package com.ozdamarsevval.carsystemapp.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.ozdamarsevval.carsystemapp.model.Brand
import com.ozdamarsevval.carsystemapp.model.Car
import com.ozdamarsevval.carsystemapp.model.Model
import com.ozdamarsevval.carsystemapp.model.Type
import com.ozdamarsevval.carsystemapp.utils.UiState

class CarSpecialityRepositoryImlp(
    val db: FirebaseFirestore
): CarSpecialityRepository {

    override fun addModel(model: Model, result: (UiState<String>) -> Unit) {
        val document = db.collection("Models").document()
        model.id = document.id
        document.set(model)
            .addOnSuccessListener {
                result.invoke(UiState.Success("New car model added"))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun getModels(result: (UiState<List<Model>>) -> Unit) {
        db.collection("Models").get()
            .addOnSuccessListener { query ->
                val models = arrayListOf<Model>()
                for(document in query){
                    val model = document.toObject(Model::class.java)
                    models.add(model)
                }
                result.invoke(UiState.Success(models))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun addBrand(brand: Brand, result: (UiState<String>) -> Unit) {
        val document = db.collection("Brands").document()
        brand.id = document.id
        document.set(brand)
            .addOnSuccessListener {
                result.invoke(UiState.Success("New car brand added"))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun addType(type: Type, result: (UiState<String>) -> Unit) {
        val document = db.collection("Types").document()
        type.id = document.id
        document.set(type)
            .addOnSuccessListener {
                result.invoke(UiState.Success("New car type added"))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }
}