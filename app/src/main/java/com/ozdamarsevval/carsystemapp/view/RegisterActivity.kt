package com.ozdamarsevval.carsystemapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter.LengthFilter
import android.widget.Toast
import androidx.activity.viewModels
import com.ozdamarsevval.carsystemapp.R
import com.ozdamarsevval.carsystemapp.databinding.ActivityRegisterBinding
import com.ozdamarsevval.carsystemapp.model.User
import com.ozdamarsevval.carsystemapp.utils.UiState
import com.ozdamarsevval.carsystemapp.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding
    val viewmodel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.text.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }

        binding.registerButton.setOnClickListener {
            viewmodel.register(
                email = binding.userEmail.text.toString(),
                password = binding.userPassword.text.toString(),
                user = getUserInfo())
        }
        observer()

    }

    private fun getUserInfo(): User{
        return User(
            id="",
            name = binding.userName.text.toString(),
            surname = binding.userSurname.text.toString(),
            email = binding.userEmail.text.toString(),
            password =  binding.userPassword.text.toString())
    }

    private fun observer(){
        viewmodel.register.observe(this){ state ->
            when(state){
                is UiState.Failure -> {
                    Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
                }
                is UiState.Success -> {
                    Toast.makeText(this, "Succeed!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                }
                is UiState.Loading -> {
                    Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}