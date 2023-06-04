package com.ozdamarsevval.carsystemapp.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ozdamarsevval.carsystemapp.R
import com.ozdamarsevval.carsystemapp.adapter.CarAdapter
import com.ozdamarsevval.carsystemapp.adapter.ImageAdapter
import com.ozdamarsevval.carsystemapp.databinding.ActivityCarDetailBinding
import com.ozdamarsevval.carsystemapp.model.Brand
import com.ozdamarsevval.carsystemapp.model.Car
import com.ozdamarsevval.carsystemapp.model.Model
import com.ozdamarsevval.carsystemapp.model.Type
import com.ozdamarsevval.carsystemapp.utils.UiState
import com.ozdamarsevval.carsystemapp.viewmodel.CarSpecialityViewModel
import com.ozdamarsevval.carsystemapp.viewmodel.CarViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CarDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityCarDetailBinding
    private val carviewmodel: CarViewModel by viewModels()
    private val csviewmodel: CarSpecialityViewModel by viewModels()
    var imageUris: MutableList<Uri> = arrayListOf()
    var objectCar: Car? = null
    val imagesAdapter by lazy{
        ImageAdapter()
    }
    val carAdapter by lazy {
        CarAdapter { pos, item ->
            startActivity(Intent(this@CarDetailActivity, MainActivity::class.java))
            finish()
        }
    }
    lateinit var modelAdapter: ArrayAdapter<String>
    lateinit var selectedModel: Model

    lateinit var brandAdapter: ArrayAdapter<String>
    lateinit var selectedBrand: Brand

    lateinit var typeAdapter: ArrayAdapter<String>
    lateinit var selectedType: Type

    private val pickImagesLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data
                if (data?.clipData != null) {
                    val clipData = data.clipData
                    for (i in 0 until clipData?.itemCount!!) {
                        val uri = clipData.getItemAt(i)?.uri
                        uri?.let {
                            imageUris.add(it)
                        }
                    }
                } else if (data?.data != null) {
                    val uri = data.data
                    uri?.let {
                        imageUris.add(it)
                    }
                }
                imagesAdapter.updateList(imageUris)
                binding.rvImages.isVisible = true
                binding.chooseImages.isVisible = false
            } else {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_detail)

        supportActionBar?.title = "Details"

        binding = ActivityCarDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        objectCar = intent.getSerializableExtra("car") as? Car
        getCarInfo()
        listener()
        observer()
    }

    fun getCarInfo(){
        objectCar.let { car ->
            if(car != null){
                isEditable(false)
                binding.apply {
                    carYear.setText(car.year)
                    carFuelType.setText(car.fuelType)
                    carMotor.setText(car.motor)
                    carTransmission.setText(car.transmission)
                    carKilometer.setText(car.kilometer)
                    carPrice.setText(car.price)
                }
                if(objectCar?.owner != Firebase.auth.currentUser?.email){
                    binding.apply {
                        this.deleteButton.isVisible = false
                        this.editButton.isVisible = false
                        this.okButton.isVisible = false
                        this.chooseImages.isClickable = false
                    }
                }
            }
        }
    }

    private fun listener(){
        binding.okButton.setOnClickListener {
            startActivity(Intent(this@CarDetailActivity, MainActivity::class.java))
            finish()

            okPressed()
        }
        binding.editButton.setOnClickListener {
            isEditable(true)
        }
        binding.deleteButton.setOnClickListener {
            objectCar.let { car ->
                if (car != null) {
                    startActivity(Intent(this@CarDetailActivity, MainActivity::class.java))
                    finish()

                    carviewmodel.deleteCar(car)
                    carviewmodel.cars.observe(this){ carList ->
                        when(carList){
                            is UiState.Loading -> Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT).show()
                            is UiState.Failure -> Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                            is UiState.Success -> carAdapter.updateList(carList.data.toMutableList())
                        }
                    }
                }
            }
        }

        binding.rvImages.layoutManager = LinearLayoutManager(this@CarDetailActivity, LinearLayoutManager.HORIZONTAL, false)
        binding.rvImages.adapter = imagesAdapter
        imageUris = objectCar?.images?.map { it.toUri() }?.toMutableList() ?: arrayListOf()
        imagesAdapter.updateList(imageUris)

        binding.chooseImages.setOnClickListener {
            val _intent = Intent(Intent.ACTION_GET_CONTENT)
            _intent.type = "image/*"
            _intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            pickImagesLauncher.launch(Intent.createChooser(_intent, "Select Pictures"))
        }
    }

    private fun isEditable(isDisable: Boolean) {
        binding.spinnerType.isEnabled = isDisable
        binding.carPrice.isEnabled = isDisable
        binding.carKilometer.isEnabled = isDisable
        binding.carMotor.isEnabled = isDisable
        binding.spinnerModel.isEnabled = isDisable
        binding.carTransmission.isEnabled = isDisable
        binding.carFuelType.isEnabled = isDisable
        binding.spinnerBrand.isEnabled = isDisable
        binding.carYear.isEnabled = isDisable
    }

    private fun observer(){
        carviewmodel.addCar.observe(this){ state ->
            when(state){
                UiState.Loading -> Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT).show()
                is UiState.Failure -> Toast.makeText(this, "Ad failed", Toast.LENGTH_SHORT).show()
                is UiState.Success -> Toast.makeText(this, "Car is on sale", Toast.LENGTH_SHORT).show()
            }
        }
        carviewmodel.updateCar.observe(this){ state ->
            when(state){
                is UiState.Loading -> Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT).show()
                is UiState.Failure -> Toast.makeText(this, "Update failed!", Toast.LENGTH_SHORT).show()
                is UiState.Success -> Toast.makeText(this, "Updated successfully.", Toast.LENGTH_SHORT).show()
            }
        }
        carviewmodel.deleteCar.observe(this){ state ->
            when(state){
                is UiState.Loading -> Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT).show()
                is UiState.Failure -> Toast.makeText(this, "Delete failed!", Toast.LENGTH_SHORT).show()
                is UiState.Success -> Toast.makeText(this, "Deleted successfully.", Toast.LENGTH_SHORT).show()
            }
        }
        csviewmodel.getModels()
        csviewmodel.models.observe(this){
            when(it){
                is UiState.Loading -> Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT).show()
                is UiState.Failure -> Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                is UiState.Success -> {
                    val dataList = it.data
                    val dataArrayList: Array<Model> = dataList.toTypedArray()
                    val stringList: List<String> = dataArrayList.map { model -> model.name }
                    modelAdapter = ArrayAdapter(this@CarDetailActivity, android.R.layout.simple_spinner_item, stringList)
                    modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
                    binding.spinnerModel.adapter = modelAdapter

                    //spinner selected item
                    val spinner: Spinner = binding.spinnerModel
                    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            selectedModel = dataList[position]
                            val sm: Model? = objectCar?.model
                            val smIndex = dataList.indexOfFirst { it.id == sm?.id }
                            if(smIndex != -1){
                                spinner.setSelection(smIndex)
                            }
                        }
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            //selectedModel = null
                        }
                    }
                }
            }
        }

        csviewmodel.getBrands()
        csviewmodel.brands.observe(this){
            when(it){
                is UiState.Loading -> Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT).show()
                is UiState.Failure -> Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                is UiState.Success -> {
                    val dataList = it.data
                    val dataArrayList: Array<Brand> = dataList.toTypedArray()
                    val stringList: List<String> = dataArrayList.map { model -> model.name }
                    brandAdapter = ArrayAdapter(this@CarDetailActivity, android.R.layout.simple_spinner_item, stringList)
                    brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
                    binding.spinnerBrand.adapter = brandAdapter

                    //spinner selected item
                    val spinner: Spinner = binding.spinnerBrand
                    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            selectedBrand = dataList[position]
                            val sb: Brand? = objectCar?.brand
                            val sbIndex = dataList.indexOfFirst { it.id == sb?.id }
                            if(sbIndex != -1){
                                spinner.setSelection(sbIndex)
                            }
                        }
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            //selectedModel = null
                        }
                    }
                }
            }
        }

        csviewmodel.getTypes()
        csviewmodel.types.observe(this){
            when(it){
                is UiState.Loading -> Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT).show()
                is UiState.Failure -> Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                is UiState.Success -> {
                    val spinner: Spinner = binding.spinnerType
                    val dataList = it.data
                    val dataArrayList: Array<Type> = dataList.toTypedArray()
                    val stringList: List<String> = dataArrayList.map { model -> model.name }
                    typeAdapter = ArrayAdapter(this@CarDetailActivity, android.R.layout.simple_spinner_item, stringList)
                    typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
                    spinner.adapter = typeAdapter

                    //spinner selected item
                    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            selectedType = dataList[position]
                            val st: Type? = objectCar?.type
                            val stIndex = dataList.indexOfFirst { it.id == st?.id }
                            if(stIndex != -1){
                                spinner.setSelection(stIndex)
                            }
                        }
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            //selectedModel = null
                        }
                    }
                }
            }
        }

    }

    private fun getCar(): Car{
        return Car(
            id = objectCar?.id ?: "",
            type = selectedType,
            year = binding.carYear.text.toString(),
            brand = selectedBrand,
            model = selectedModel,
            fuelType = binding.carFuelType.text.toString(),
            motor = binding.carMotor.text.toString(),
            transmission = binding.carTransmission.text.toString(),
            kilometer = (binding.carKilometer.text.toString()),
            price = (binding.carPrice.text.toString()),
            owner = Firebase.auth.currentUser?.email.toString(),
            date = Date(),
            images = getImageUrls()
        )
    }

    private fun getImageUrls(): List<String> {
        if(imageUris.isNotEmpty()){
            return imageUris.map { it.toString() }
        }else {
            return objectCar?.images ?: arrayListOf()
        }
    }

    private fun okPressed() {
        if (objectCar == null) {
            carviewmodel.addCar(getCar())
        } else {
            carviewmodel.updateCar(getCar())
        }
    }

}