package com.example.chatting_application

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.chatting_application.data.Message
import com.example.chatting_application.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


@SuppressLint("StaticFieldLeak")
class MainModel {
    companion object {
        private val TAG = UtilConstant.PREFIX + MainModel::class.java.simpleName

        private lateinit var firebaseAuth: FirebaseAuth
        private lateinit var messageList: MutableLiveData<ArrayList<Message>?>
        private lateinit var fDBRef: DatabaseReference
        private lateinit var sharedPreferences: SharedPreferences
        private lateinit var userList: MutableLiveData<ArrayList<User>?>
        private lateinit var loginLiveData: MutableLiveData<Boolean?>

        @Volatile
        private var instance: MainModel? = null
        private lateinit var mActivity: Activity

        fun getInstance(activity: Activity): MainModel {
            Log.i(TAG, "getInstance called.")
            if (instance == null) {
                instance = MainModel(activity)
                loginLiveData.postValue(null)
            }
            firebaseAuth.currentUser?.let {
                getUsers(firebaseAuth.currentUser?.uid!!)
            }
            messageList.postValue(null)
            return instance!!
        }

        private fun getUsers(user: String) {
            val list = ArrayList<User>()
            fDBRef.child(UtilConstant.USER).addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (postSnapshot in snapshot.children) {
                        val currentUser = postSnapshot.getValue(User::class.java)
                        if(user != currentUser?.uid) {
                            list.add(currentUser!!)
                        }
                    }
                    userList.postValue(list)
                }

                override fun onCancelled(error: DatabaseError) { }

            })
        }
    }

    private constructor (activity: Activity) {
        firebaseAuth = FirebaseAuth.getInstance()
        fDBRef = FirebaseDatabase.getInstance().reference
        loginLiveData =  MutableLiveData()
        sharedPreferences = activity.applicationContext.getSharedPreferences(UtilConstant.LOGIN_SHARED_PREFERENCES,
            Context.MODE_PRIVATE)
        userList = MutableLiveData()
        messageList = MutableLiveData()
        mActivity = activity
    }

    fun getUserList(): MutableLiveData<ArrayList<User>?> {
        return userList
    }

    fun getChat(): MutableLiveData<ArrayList<Message>?> {
        return messageList
    }

    fun getLoginLiveData(): MutableLiveData<Boolean?> {
        Log.i(TAG, " is login ${loginLiveData.value}.")
        return loginLiveData
    }

    fun register(name: String, email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(mActivity) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    Log.i(TAG, " ref : $fDBRef")
                    fDBRef.child(UtilConstant.USER).child(user!!.uid).setValue(User(name,email, user.uid))
                        .addOnSuccessListener {
                            addToDB()
                            loginLiveData.postValue(true)
                        }
                        .addOnFailureListener {
                            Log.i(TAG, "${it.message} cause: ${it.cause}")
                            Toast.makeText(
                                mActivity.applicationContext,
                                "Data Adding Failure: " + it.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            logOut()
                        }
                } else {
                    Toast.makeText(
                        mActivity.applicationContext,
                        "Registration Failure: " + task.exception?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun addToDB() {
        sharedPreferences.edit().putBoolean(UtilConstant.IS_LOGIN, true).apply()
    }

    fun login(email: String, password: String) {
        Log.i(TAG, "Logging.............")
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(mActivity) { task ->
                if (task.isSuccessful) {
                    addToDB()
                    loginLiveData.postValue(true)
                } else {
                    Toast.makeText(
                        mActivity.applicationContext,
                        "Login Failure: " + task.exception?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun logOut() {
        Log.i(TAG, "logging out...........")
        firebaseAuth.signOut()
        loginLiveData.postValue(false)
        Log.i(TAG, "loginLiveData $loginLiveData ")
        removeFromDB()
    }

    private fun removeFromDB() {
        sharedPreferences.edit().putBoolean(UtilConstant.IS_LOGIN,false).apply()
    }

    fun getCurrentUserId(): String {
        return firebaseAuth.uid ?: "Invalid"
    }

    fun sentMessage(message: Message) {
        val senderRoom = message.receiverId + message.senderId
        val receiverRoom = message.senderId + message.receiverId
        fDBRef.child("chats").child(senderRoom).child("messages").push()
            .setValue(message).addOnSuccessListener {
                fDBRef.child("chats").child(receiverRoom).child("messages").push()
                    .setValue(message)
            }
    }

    fun getChatting(message: Message) {
        val list = ArrayList<Message>()
        val senderRoom = message.receiverId + message.senderId
        fDBRef.child("chats").child(senderRoom).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (postSnapshot in snapshot.children) {
                        val msg = postSnapshot.getValue(Message::class.java)
                        list.add(msg!!)
                    }
                    Log.i(TAG, " model list sie : ${list.size}")
                    messageList.postValue(list)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}