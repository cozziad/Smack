package com.ehword.smack.Controller.Services

import android.content.Context
import android.util.Log
import android.widget.ArrayAdapter
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.ehword.smack.Controller.Controller.App
import com.ehword.smack.Controller.Model.Channel
import com.ehword.smack.Controller.Model.Message
import com.ehword.smack.Controller.Utilities.URL_FINDALLCHANNELS
import com.ehword.smack.Controller.Utilities.URL_GET_MESSAGES
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException

object MessageService {
    val channels = ArrayList<Channel>()
    val messages = ArrayList<Message>()

    fun getChannels(context: Context, complete: (Boolean) -> Unit){
        val channelsRequest = object : JsonArrayRequest(Method.GET,URL_FINDALLCHANNELS, null, Response.Listener{ response ->
            try {
                for (x in 0 until response.length()){
                    var channel = response.getJSONObject(x)
                    val name = channel.getString("name")
                    val desc = channel.getString("description")
                    val id = channel.getString("_id")

                    val newChannel = Channel(name,desc,id)
                    this.channels.add(newChannel)
                }
                complete (true)

            } catch (e: JSONException){
                Log.d("JSON", "EXC: " + e.localizedMessage)
                complete (false)
            }
        }, Response.ErrorListener {
                error -> Log.d("Error","Could not retrieve channels: $error")
            complete (false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.sharedPreferences.authToken}")
                return headers
            }
        }
        App.sharedPreferences.requestQueue.add(channelsRequest)
    }

    fun getMessages(channelId: String, complete: (Boolean) -> Unit){

        val url ="$URL_GET_MESSAGES$channelId"

        val messageRequest = object : JsonArrayRequest(Method.GET, url, null, Response.Listener { response ->
            clearMessages()
            try {
                for (x in 0 until response.length()){
                    val message = response.getJSONObject(x)
                    val channelId = message.getString("channelId")
                    val msg = message.getString("messageBody")
                    val userName = message.getString("userName")
                    val userAvatar = message.getString("userAvatar")
                    val userAvatarColor = message.getString("userAvatarColor")
                    val timestamp = message.getString("timeStamp")
                    val id = message.getString("_id")

                    val newMessage = Message(msg,userName,channelId,userAvatar,userAvatarColor, id, timestamp)
                    this.messages.add(newMessage)
                }
                complete (true)

            } catch (e: JSONException){
                Log.d("JSON", "EXC: " + e.localizedMessage)
                complete (false)
            }
        }, Response.ErrorListener{
                error -> Log.d("Error","Could not retrieve channels: $error")
            complete (false)
        }){
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer ${App.sharedPreferences.authToken}")
                return headers
            }
        }
        App.sharedPreferences.requestQueue.add(messageRequest)
    }

    fun clearMessages(){
        messages.clear()
    }
    fun clearChannels(){
        channels.clear()
    }
}

