package com.example.chatting_application.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.chatting_application.MainActivity
import com.example.chatting_application.R
import com.example.chatting_application.UtilConstant
import com.example.chatting_application.databinding.ActivitySignUpBinding

class SignUp : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: LogInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initUI()
    }

    private fun initUI() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        supportActionBar?.hide()
        viewModel = ViewModelProvider(this, LogInViewModelFactory(this))[LogInViewModel::class.java]
        viewModel.getLogInLiveData().observe(this) {
            when {
                it == null -> {
                    Log.i(TAG, "Register onGoing")
                }
                it -> {
                    val intent = Intent(this, MainActivity::class.java)
                    Log.i(TAG, "Register Successful")
                    finish()
                    startActivity(intent)
                }
                else -> {
                    Log.i(TAG, "Register Failed!")
                }
            }
        }

        binding.signUp.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val password = binding.editPassword.text.toString()
            val name = binding.editName.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.register(name, email, password)
            }
            else {
                Toast.makeText(this, "email or password is empty",Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private val TAG = UtilConstant.PREFIX + SignUp::class.java.simpleName
    }
}