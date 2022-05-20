package com.example.chatting_application.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.chatting_application.MainActivity
import com.example.chatting_application.R
import com.example.chatting_application.UtilConstant
import com.example.chatting_application.UtilConstant.PREFIX
import com.example.chatting_application.databinding.ActivityLogInBinding

class LogIn : AppCompatActivity() {
    private lateinit var binding: ActivityLogInBinding
    private lateinit var viewModel: LogInViewModel
    private lateinit var sharedPreferences: SharedPreferences

    private val observer = Observer<Boolean?> {
        when {
            it == null -> {
                Log.i(TAG, "LogIn Ongoing...")
            }
            it -> {
                moveToMainActivity()
            }
            else -> {
                Log.i(TAG, "LogIn failed or loggedOut")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_log_in)
        supportActionBar?.hide()

        sharedPreferences = getSharedPreferences(UtilConstant.LOGIN_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean(UtilConstant.IS_LOGIN, false)) {
            moveToMainActivity()
        }
        viewModel = ViewModelProvider(this, LogInViewModelFactory(this))[LogInViewModel::class.java]
        viewModel.getLogInLiveData().observe(this, observer)

        initUI()
    }

    private fun initUI() {
        binding.logIn.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val password = binding.editPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                Log.i(TAG, "Logging.............")
                viewModel.login(email, password)
            }
            else {
                Toast.makeText(this, "email or password is empty",Toast.LENGTH_SHORT).show()
            }
        }

        binding.signUp.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            Log.i(TAG, "Singing up.....")
            startActivity(intent)
        }
    }

    private fun moveToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        Log.i(TAG, "LogIn Successful")
        finish()
        startActivity(intent)
    }

    companion object {
        private val TAG = PREFIX + LogIn::class.java.simpleName
    }
}