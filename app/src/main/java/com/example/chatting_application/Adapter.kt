package com.example.chatting_application

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatting_application.chatting.ChatActivity
import com.example.chatting_application.data.User

class Adapter(private val users: List<User>) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name: String? = users[position].name
        val uid = users[position].uid

        holder.setData(name!!)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)

            intent.putExtra(UtilConstant.UID, uid)
            intent.putExtra(UtilConstant.NAME, name)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    class ViewHolder(viewItem : View) : RecyclerView.ViewHolder(viewItem) {
        private val nameTv: TextView
            get() = itemView.findViewById(R.id.name)

        fun setData(name: String) {
            nameTv.text = name
        }
    }
}
