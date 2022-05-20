package com.example.chatting_application.chatting

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatting_application.MainModel
import com.example.chatting_application.data.Message

class ChattingViewModel(activity: Activity): ViewModel() {
    private val model: MainModel = MainModel.getInstance(activity)
    private var userChat: MutableLiveData<ArrayList<Message>?> = model.getChat()
    fun getCurrentUserId(): String {
        return model.getCurrentUserId()
    }

    fun sentMessage(message: Message) {
        model.sentMessage(message)
    }

    fun getChat(): MutableLiveData<ArrayList<Message>?> {
        return userChat
    }

    fun onCreate(message: Message) {
        model.getChatting(message)
    }
}