package com.ehword.smack.Controller.Services

import android.content.Context
import android.util.Log
import android.widget.ArrayAdapter
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.ehword.smack.Controller.Model.Channel
import com.ehword.smack.Controller.Utilities.URL_FINDALLCHANNELS
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException

object MessageService {
    val channels = ArrayList<Channel>()

    fun getChannels(context: Context, complete: (Boolean) -> Unit){
        val channelsRequest = object : JsonArrayRequest(Method.GET,URL_FINDALLCHANNELS, null, Response.Listener{ response ->
            complete(true)
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
                headers.put("Authorization", "Bearer ${AuthService.authToken}")
                return headers
            }
        }
        Volley.newRequestQueue(context).add(channelsRequest)
    }
}

