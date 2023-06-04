package com.ozdamarsevval.carsystemapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.ozdamarsevval.carsystemapp.R
import com.ozdamarsevval.carsystemapp.databinding.ActivityAdminBinding
import com.ozdamarsevval.carsystemapp.databinding.ActivityCarSpecialityBinding
import com.ozdamarsevval.carsystemapp.model.Brand
import com.ozdamarsevval.carsystemapp.model.Model
import com.ozdamarsevval.carsystemapp.model.Type
import com.ozdamarsevval.carsystemapp.utils.UiState
import com.ozdamarsevval.carsystemapp.viewmodel.CarSpecialityViewModel
import com.ozdamarsevval.carsystemapp.viewmodel.CarViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CarSpecialityActivity : AppCompatActivity() {

    lateinit var binding: ActivityCarSpecialityBinding
    private val viewmodel: CarSpecialityViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_speciality)

        supportActionBar?.title = "Add Car Specialities"

        binding = ActivityCarSpecialityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listener()
        observer()
    }

    private fun getModel(): Model {
        return Model(
            id = "",
            name = binding.modelTxt.text.toString()
        )
    }

    private fun getBrand(): Brand {
        return Brand(
            id = "",
            name = binding.brandTxt.text.toString()
        )
    }

    private fun getType(): Type {
        return Type(
            id = "",
            name = binding.typeTxt.text.toString()
        )
    }

    private fun listener() {
        binding.addButton.setOnClickListener {
            if(!binding.modelTxt.text.isNullOrEmpty())
                viewmodel.addModel(getModel())

            if(!binding.brandTxt.text.isNullOrEmpty())
                viewmodel.addBrand(getBrand())

            if(!binding.typeTxt.text.isNullOrEmpty())
                viewmodel.addType(getType())
        }
    }

    private fun observer() {
        viewmodel.addModel.observe(this){ state ->
            when(state){
                is UiState.Loading -> Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT).show()
                is UiState.Failure -> Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                is UiState.Success -> Toast.makeText(this, "Model is added", Toast.LENGTH_SHORT).show()
            }
        }
        viewmodel.addBrand.observe(this){ state ->
            when(state){
                is UiState.Loading -> Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT).show()
                is UiState.Failure -> Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                is UiState.Success -> Toast.makeText(this, "Brand is added", Toast.LENGTH_SHORT).show()
            }
        }
        viewmodel.addType.observe(this){ state ->
            when(state){
                is UiState.Loading -> Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT).show()
                is UiState.Failure -> Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                is UiState.Success -> Toast.makeText(this, "Type is added", Toast.LENGTH_SHORT).show()
            }
        }
    }

}