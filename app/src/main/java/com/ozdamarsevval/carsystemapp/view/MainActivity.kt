package com.ozdamarsevval.carsystemapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import com.ozdamarsevval.carsystemapp.R
import com.ozdamarsevval.carsystemapp.databinding.ActivityMainBinding
import com.ozdamarsevval.carsystemapp.viewmodel.AuthViewModel
import com.ozdamarsevval.carsystemapp.viewmodel.CarViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    val authviewmodel: AuthViewModel by viewModels()
    val carviewmodel: CarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menuSellCar -> Toast.makeText(this, "Go to SellCarActivity", Toast.LENGTH_SHORT).show()
            R.id.menuProfile -> Toast.makeText(this, "Go to ProfileActivity", Toast.LENGTH_SHORT).show()
            R.id.menuLogout -> authviewmodel.logout {
                Toast.makeText(this, "Logout successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}