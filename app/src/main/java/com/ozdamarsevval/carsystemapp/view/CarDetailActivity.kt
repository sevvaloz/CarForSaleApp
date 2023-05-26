package com.ozdamarsevval.carsystemapp.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ozdamarsevval.carsystemapp.R
import com.ozdamarsevval.carsystemapp.adapter.ImageAdapter
import com.ozdamarsevval.carsystemapp.databinding.ActivityCarDetailBinding
import com.ozdamarsevval.carsystemapp.model.Car
import com.ozdamarsevval.carsystemapp.utils.UiState
import com.ozdamarsevval.carsystemapp.viewmodel.AuthViewModel
import com.ozdamarsevval.carsystemapp.viewmodel.CarViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CarDetailActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var binding: ActivityCarDetailBinding
    private val authviewmodel: AuthViewModel by viewModels()
    private val carviewmodel: CarViewModel by viewModels()
    var imageUris: MutableList<Uri> = arrayListOf()
    var objectCar: Car? = null
    val imagesAdapter by lazy{
        ImageAdapter()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_detail)

        supportActionBar?.title = "Details"

        auth = Firebase.auth

        binding = ActivityCarDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listener()
        observer()

    }

    private fun listener(){

        binding.okButton.setOnClickListener {
            startActivity(Intent(this@CarDetailActivity, MainActivity::class.java))
            finish()

            onDonePressed()
        }

        objectCar = intent.getSerializableExtra("car") as? Car
        objectCar.let { car ->
            binding.carType.setText(car?.type)
            binding.carYear.setText(car?.year)
            binding.carBrand.setText(car?.brand)
            binding.carModel.setText(car?.model)
            binding.carFuelType.setText(car?.fuelType)
            binding.carMotor.setText(car?.motor)
            binding.carTransmission.setText(car?.transmission)
            binding.carKilometer.setText(car?.kilometer)
            binding.carPrice.setText(car?.price)
        }

        binding.rvImages.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            pickImagesLauncher.launch(Intent.createChooser(intent, "Select Pictures"))
        }

        binding.rvImages.layoutManager = LinearLayoutManager(this@CarDetailActivity, LinearLayoutManager.HORIZONTAL, false)
        binding.rvImages.adapter = imagesAdapter
        imageUris = objectCar?.images?.map { it.toUri() }?.toMutableList() ?: arrayListOf()
        imagesAdapter.updateList(imageUris)

    }

    private val pickImagesLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val resultCode = it.resultCode
            if(resultCode == Activity.RESULT_OK){
                val data = it.data
                val fileUri = data?.data
                imageUris.add(fileUri!!)
                imagesAdapter.updateList(imageUris)
                binding.rvImages.isVisible = true
                binding.chooseImages.isVisible = false
            }else{
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
            }
        }


    private fun getImageUrls(): List<String> {
        if(imageUris.isNotEmpty()){
            return imageUris.map {
                it.toString()
            }
        }else {
            return objectCar?.images ?: arrayListOf()
        }
    }

    private fun observer(){
        carviewmodel.addCar.observe(this){ state ->
            when(state){
                UiState.Loading -> Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT).show()
                is UiState.Failure -> Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                is UiState.Success -> Toast.makeText(this, "Car is on sale", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getCar(): Car{
        return Car(
            id = objectCar?.id ?: "",
            type = binding.carType.text.toString(),
            year = binding.carYear.text.toString(),
            brand = binding.carBrand.text.toString(),
            model = binding.carModel.text.toString(),
            fuelType = binding.carFuelType.text.toString(),
            motor = binding.carMotor.text.toString(),
            transmission = binding.carTransmission.text.toString(),
            kilometer = (binding.carKilometer.text.toString()),
            price = (binding.carPrice.text.toString()),
            owner = auth.currentUser?.email.toString(),
            date = Date(),
            images = getImageUrls()
        )
    }

    private fun onDonePressed() {
        if (imageUris.isNotEmpty()){
            carviewmodel.onUploadMultipleFile(imageUris){ state ->
                when (state) {
                    is UiState.Loading -> {
                        Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT).show()
                    }
                    is UiState.Failure -> {
                        Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
                    }
                    is UiState.Success -> {
                        if (objectCar == null) {
                            carviewmodel.addCar(getCar())
                        } else {
                            carviewmodel.updateCar(getCar())
                        }
                    }
                }
            }
        }else{
            if (objectCar == null) {
                carviewmodel.addCar(getCar())
            } else {
                carviewmodel.updateCar(getCar())
            }
        }
    }


}