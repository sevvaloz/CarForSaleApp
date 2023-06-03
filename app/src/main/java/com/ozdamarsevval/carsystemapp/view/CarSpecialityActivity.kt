package com.ozdamarsevval.carsystemapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.ozdamarsevval.carsystemapp.R
import com.ozdamarsevval.carsystemapp.databinding.ActivityAdminBinding
import com.ozdamarsevval.carsystemapp.databinding.ActivityCarSpecialityBinding
import com.ozdamarsevval.carsystemapp.model.Model
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

    private fun listener() {
        binding.addButton.setOnClickListener {
            viewmodel.addModel(getModel())
        }
    }

    private fun observer() {
        viewmodel.addModel.observe(this){ state ->
            when(state){
                UiState.Loading -> Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT).show()
                is UiState.Failure -> Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                is UiState.Success -> Toast.makeText(this, "Model is added", Toast.LENGTH_SHORT).show()
            }
        }
    }

}