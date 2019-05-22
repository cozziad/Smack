package com.ehword.smack.Controller.Services

import android.graphics.Color
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
        AuthService.authToken = ""
        AuthService.isLoggedIn = false
        AuthService.userEmail = ""
    }

    fun returnAvatarColor (components: String) : Int {
        println (components)
        val stripedColor = components.replace("[","")
            .replace("]","")
            .replace(",","")
        println (stripedColor)
        var r  = 0
        var g = 0
        var b = 0

        val scanner = Scanner(stripedColor)
        r = (scanner.nextDouble() * 255).toInt()
        g = (scanner.nextDouble() * 255).toInt()
        b = (scanner.nextDouble() * 255).toInt()

        println ("R:$r G: $g B:$b ")
        return Color.rgb(r,g,b)
    }
}