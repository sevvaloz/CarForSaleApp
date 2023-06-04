package com.ozdamarsevval.carsystemapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ozdamarsevval.carsystemapp.R
import com.ozdamarsevval.carsystemapp.adapter.CarAdapter
import com.ozdamarsevval.carsystemapp.databinding.ActivityMainBinding
import com.ozdamarsevval.carsystemapp.utils.UiState
import com.ozdamarsevval.carsystemapp.viewmodel.AuthViewModel
import com.ozdamarsevval.carsystemapp.viewmodel.CarViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val authviewmodel: AuthViewModel by viewModels()
    private val carviewmodel: CarViewModel by viewModels()
    private val adapter by lazy {
        CarAdapter { pos, item ->
            startActivity(Intent(this@MainActivity, CarDetailActivity::class.java).putExtra("car", item))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Welcome " + (Firebase.auth.currentUser?.email)?.substringBefore("@")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rv.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        binding.rv.adapter = adapter

        observer()
    }

    private fun observer(){
        carviewmodel.getCars()
        carviewmodel.cars.observe(this){
            when(it){
                is UiState.Loading -> Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT).show()
                is UiState.Failure -> Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                is UiState.Success -> adapter.updateList(it.data.toMutableList())
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menuSellCar -> startActivity(Intent(this@MainActivity, CarDetailActivity::class.java))
            R.id.menuProfile -> Toast.makeText(this, "Go to ProfileActivity", Toast.LENGTH_SHORT).show()
            R.id.menuLogout -> authviewmodel.logout {
                Toast.makeText(this, "Logout successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
            R.id.menuAdminPanel -> {
                Toast.makeText(this, "Switched to Admin Panel", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@MainActivity, AdminActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if(Firebase.auth.currentUser?.email == "admin@mail.com"){
            val menuAdminPanelItem = menu?.findItem(R.id.menuAdminPanel)
            menuAdminPanelItem?.isVisible = true

            val menuProfileItem = menu?.findItem(R.id.menuProfile)
            menuProfileItem?.isVisible = false

            val menuSellCarItem = menu?.findItem(R.id.menuSellCar)
            menuSellCarItem?.isVisible = false
        }
        return super.onPrepareOptionsMenu(menu)
    }

}