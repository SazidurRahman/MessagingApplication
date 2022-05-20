package com.example.chatting_application

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatting_application.data.User


class MainViewModel(activity: Activity) : ViewModel() {

    private var model: MainModel
    private var userList: MutableLiveData<ArrayList<User>?>
    private var loginLiveData: MutableLiveData<Boolean?>

    init {
        Log.i(TAG, "init is called")
        model = MainModel.getInstance(activity)
        userList = model.getUserList()
        loginLiveData = model.getLoginLiveData()
    }

    fun logOut() {
        model.logOut()
    }

    fun getUserList(): MutableLiveData<ArrayList<User>?> {
        return userList
    }

    fun getLoginLiveData(): MutableLiveData<Boolean?> {
        return loginLiveData
    }

    companion object {
        private val TAG = UtilConstant.PREFIX + MainViewModel::class.java.simpleName
    }
}