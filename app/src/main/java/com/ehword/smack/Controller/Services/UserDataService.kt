package com.ehword.smack.Controller.Services

import android.graphics.Color
import com.ehword.smack.Controller.Controller.App
import java.util.*

object UserDataService {
    var id = ""
    var avatarColor = ""
    var avatarName = ""
    var email = ""
    var name = ""

    fun logout () {
        var id = ""
        var avatarColor = ""
        var avatarName = ""
        var email = ""
        var name = ""
        App.sharedPreferences.authToken = ""
        App.sharedPreferences.isLoggedIn = false
        App.sharedPreferences.userEmail = ""
        MessageService.clearChannels()
        MessageService.clearMessages()
    }

    fun returnAvatarColor (components: String) : Int {
        val stripedColor = components.replace("[","")
            .replace("]","")
            .replace(",","")
        var r  = 0
        var g = 0
        var b = 0

        val scanner = Scanner(stripedColor)
        r = (scanner.nextDouble() * 255).toInt()
        g = (scanner.nextDouble() * 255).toInt()
        b = (scanner.nextDouble() * 255).toInt()

        return Color.rgb(r,g,b)
    }
}