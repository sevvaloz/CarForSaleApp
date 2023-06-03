package com.ozdamarsevval.carsystemapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.core.view.isVisible
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ozdamarsevval.carsystemapp.R
import com.ozdamarsevval.carsystemapp.databinding.ActivityAdminBinding
import com.ozdamarsevval.carsystemapp.databinding.ActivityCarSpecialityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminActivity : AppCompatActivity() {

    lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        supportActionBar?.title = "Admin Panel"

        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listener()
    }

    private fun listener() {

        binding.apply {
            toModelButton.setOnClickListener {
                startActivity(Intent(this@AdminActivity, CarSpecialityActivity::class.java))
            }
        }
    }
}