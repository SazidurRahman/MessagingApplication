package com.example.chatting_application.chatting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatting_application.R
import com.example.chatting_application.data.Message
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter (private val messages: List<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == MessageType.ITEM_SENT.ordinal) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.send, parent, false)
            return SentViewHolder(view)
        }
        val view = LayoutInflater.from(parent.context).inflate(R.layout.received_message, parent, false)
        return ReceivedViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        if (FirebaseAuth.getInstance().currentUser?.uid.equals(message.senderId)) {
            return MessageType.ITEM_SENT.ordinal
        }
        return MessageType.ITEM_RECEIVED.ordinal
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder.javaClass == SentViewHolder::class.java) {

            val viewHolder = holder as SentViewHolder
            val message = messages[position].message

            viewHolder.setData(message!!)

        } else {
            val viewHolder = holder as ReceivedViewHolder
            val message = messages[position].message

            viewHolder.setData(message!!)
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    class SentViewHolder(viewItem : View) : RecyclerView.ViewHolder(viewItem) {
        private val messageTv: TextView
            get() = itemView.findViewById(R.id.text_sent_message)

        fun setData(message: String) {
            messageTv.text = message
        }
    }

    class ReceivedViewHolder(viewItem : View) : RecyclerView.ViewHolder(viewItem) {
        private val messageTv: TextView
            get() = itemView.findViewById(R.id.text_received_message)

        fun setData(message: String) {
            messageTv.text = message
        }
    }

    enum class MessageType{
        ITEM_SENT,
        ITEM_RECEIVED
    }
}