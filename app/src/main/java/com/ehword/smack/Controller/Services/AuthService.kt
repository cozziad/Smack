package com.ehword.smack.Controller.Services

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.ehword.smack.Controller.Utilities.*
import org.json.JSONException
import org.json.JSONObject

object AuthService {

    var authToken = ""
    var userEmail = ""
    var isLoggedIn = false

    fun registerUser (context: Context, email:String, password:String, complete: (Boolean) -> Unit)
    {
        val jsonBody = JSONObject()
        jsonBody.put ("email", email)
        jsonBody.put ("password",password)
        val requestBody = jsonBody.toString()

        println(URL_REGISTER)

        val registerRequest = object : StringRequest(Request.Method.POST, URL_REGISTER, Response.Listener{ response ->
        complete(true)
        },Response.ErrorListener {
            error -> Log.d("Error","Could not register user: $error")
            complete (false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }
        Volley.newRequestQueue(context).add(registerRequest)
    }

    fun loginUser (context: Context, email:String, password:String, complete: (Boolean) -> Unit)
    {
        val jsonBody = JSONObject()
        jsonBody.put ("email", email)
        jsonBody.put ("password",password)
        val requestBody = jsonBody.toString()

        val registerRequest = object : JsonObjectRequest(Method.POST, URL_LOGIN, null,Response.Listener{ response ->
            try {
                authToken = response.getString("token")
                userEmail = response.getString("user")
                isLoggedIn = true
                complete(true)
            } catch (e: JSONException){
                Log.d("JSON", "EXC: " + e.localizedMessage)
                complete (false)
            }
        },Response.ErrorListener {
                error -> Log.d("Error","Could not login user: $error")
            complete (false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }
        Volley.newRequestQueue(context).add(registerRequest)
    }

    fun findUserByEmail (context: Context,complete: (Boolean) -> Unit) {
        val findUserRequest = object : JsonObjectRequest(Method.GET, "$URL_GETUSER$userEmail", null, Response.Listener {response ->
            try {
                UserDataService.avatarColor = response.getString("avatarColor")
                UserDataService.avatarName = response.getString("avatarName")
                UserDataService.email = response.getString("email")
                UserDataService.name = response.getString("name")
                UserDataService.id = response.getString("_id")

                val userDataChanged = Intent(BROADCAST_USER_DATA_CHANGE)
                LocalBroadcastManager.getInstance(context).sendBroadcast(userDataChanged)

                complete(true)
            } catch (e: JSONException){
                Log.d("JSON", "EXC: " + e.localizedMessage)
                complete (false)
            }
            complete (true)
        }, Response.ErrorListener {error ->
            Log.d("ERROR","Could not find user")
            complete (false)

        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Authorization", "Bearer $authToken")
                return headers
            }
        }
        Volley.newRequestQueue(context).add(findUserRequest)
    }

    fun createUser (context: Context, name:String, email:String, avatar:String, avatarColor:String,complete: (Boolean) -> Unit)
    {
        val jsonBody = JSONObject()
        jsonBody.put ("name", name)
        jsonBody.put ("email",email)
        jsonBody.put ("avatarName", avatar)
        jsonBody.put ("avatarColor",avatarColor)
        val requestBody = jsonBody.toString()

        val createRequest = object : JsonObjectRequest(Method.POST, URL_ADDUSER, null,Response.Listener{ response ->
            try {
                UserDataService.avatarColor = response.getString("avatarColor")
                UserDataService.avatarName = response.getString("avatarName")
                UserDataService.email = response.getString("email")
                UserDataService.name = response.getString("name")
                UserDataService.id = response.getString("_id")
                complete(true)
            } catch (e: JSONException){
                Log.d("JSON", "EXC: " + e.localizedMessage)
                complete (false)
            }
        },Response.ErrorListener {
                error -> Log.d("Error","Could not add user: $error")
            complete (false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            override fun getHeaders():MutableMap<String, String> {
                val headers = HashMap<String,String>()
                headers.put("Authorization", "Bearer $authToken")
                return headers
            }
        }
        Volley.newRequestQueue(context).add(createRequest)
    }

}
