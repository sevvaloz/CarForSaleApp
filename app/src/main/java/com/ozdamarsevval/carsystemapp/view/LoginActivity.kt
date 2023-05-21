package com.ozdamarsevval.carsystemapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.ozdamarsevval.carsystemapp.R
import com.ozdamarsevval.carsystemapp.databinding.ActivityLoginBinding
import com.ozdamarsevval.carsystemapp.utils.UiState
import com.ozdamarsevval.carsystemapp.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    val viewmodel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.text.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finish()
        }

        binding.loginButton.setOnClickListener {
            viewmodel.login(
                email = binding.userEmail.text.toString(),
                password = binding.userPassword.text.toString()
            )
        }
        observer()


    }

    private fun observer(){
        viewmodel.login.observe(this){ state ->
            when(state){
                is UiState.Failure -> {
                    Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
                }
                is UiState.Success -> {
                    Toast.makeText(this, "Succeed!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
                is UiState.Loading -> {
                    Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}