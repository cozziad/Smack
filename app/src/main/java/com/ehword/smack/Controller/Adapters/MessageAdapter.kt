package com.ehword.smack.Controller.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ehword.smack.Controller.Model.Message
import com.ehword.smack.Controller.Services.UserDataService
import com.ehword.smack.R
import kotlinx.android.synthetic.main.content_main.view.*
import java.text.FieldPosition

class MessageAdapter (val context:Context,val messages: ArrayList <Message> ): RecyclerView.Adapter<MessageAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.message_list_view, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return messages.count()
    }

    override fun onBindViewHolder(holder: ViewHolder,position: Int) {
        holder.bindMessage(context,messages[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val userImage = itemView.findViewById<ImageView>(R.id.msgUserImage)
        val timeStamp = itemView.findViewById<TextView>(R.id.msgUserDate)
        val userName = itemView.findViewById<TextView>(R.id.msgUserName)
        val messageBody = itemView.findViewById<TextView>(R.id.msgUserMessage)

        fun bindMessage(context: Context,message:Message){
            val resourceId = context.resources.getIdentifier(message.userAvatar,"drawable", context.packageName)
            userImage.setImageResource(resourceId)
            userImage.setBackgroundColor(UserDataService.returnAvatarColor(message.userAvatarColor))
            userName.text = message.userName
            timeStamp.text = message.timestamp
            messageBody.text = message.message
            println("BM ran")
        }
    }
}