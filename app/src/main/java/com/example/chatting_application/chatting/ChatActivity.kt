package com.example.chatting_application.chatting

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatting_application.R
import com.example.chatting_application.UtilConstant
import com.example.chatting_application.data.Message
import com.example.chatting_application.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding

    private lateinit var adapter: MessageAdapter
    private lateinit var viewModel: ChattingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)

        val name = intent.getStringExtra(UtilConstant.NAME)
        val receiverUid = intent.getStringExtra(UtilConstant.UID)
        supportActionBar?.title =name

        viewModel = ViewModelProvider(this, ChattingViewModelFactory(this))[ChattingViewModel::class.java]
        viewModel.getChat().observe(this) {
            it?.let {
                Log.i(TAG, "chat size: ${it.size}")
                adapter = MessageAdapter(it)
                binding.recyclerView.adapter = adapter
            }
            Log.i(TAG, "chat list is null")
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val senderUid = viewModel.getCurrentUserId()
        viewModel.onCreate(Message(null, senderUid, receiverUid!!))


        binding.sentButton.setOnClickListener {
            val message = binding.message.text.toString()
            val messageObj = Message(message, senderUid, receiverUid)

            viewModel.sentMessage(messageObj)
            binding.message.setText("")
        }
    }

    companion object {
        private val TAG = UtilConstant.PREFIX + ChatActivity::class.java.simpleName
    }
}