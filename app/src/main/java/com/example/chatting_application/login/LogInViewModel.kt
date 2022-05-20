package com.example.chatting_application.login

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatting_application.MainModel
import com.example.chatting_application.UtilConstant

class LogInViewModel(activity: Activity) : ViewModel() {
    private var model: MainModel
    private var userLogInLiveData: MutableLiveData<Boolean?>
    init {
        Log.i(TAG, "init is called")
        model = MainModel.getInstance(activity)
        userLogInLiveData = model.getLoginLiveData()
    }
    fun login(email: String, password: String) {
        model.login(email, password)
    }

    fun register(name: String, email: String, password: String) {
        model.register(name, email, password)
    }

    fun getLogInLiveData(): MutableLiveData<Boolean?> {
        return userLogInLiveData
    }

    companion object {
        private val TAG = UtilConstant.PREFIX + LogInViewModel::class.java.simpleName
    }

}