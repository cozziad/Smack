package com.ehword.smack.Controller.Controller

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ehword.smack.R
import kotlinx.android.synthetic.main.activity_signup.*
import java.util.*

class SignupActivity : AppCompatActivity() {

    var userAvatar  = "profileDefault"
    var avatarColor = "[0.5,0.5,0.5,1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
    }
    fun generateUserAvatar (view: View){
        val random = Random()
        val color = random.nextInt(2)
        val avatar = random.nextInt(28)
        if (color == 0){
            userAvatar = "light$avatar"
        }
        else{
            userAvatar = "dark$avatar"
        }
        val resourceId = resources.getIdentifier(userAvatar,"drawable",packageName)
        createAvatarImageView.setImageResource(resourceId)
    }
    fun generateColorClicked (view: View){
        val random = Random()
        val r = random.nextInt(255)
        val g = random.nextInt(255)
        val b = random.nextInt(255)

        createAvatarImageView.setBackgroundColor(Color.rgb(r,g,b))

        val sR = r.toDouble() /255
        val sG = r.toDouble() /255
        val sB = r.toDouble() / 255

        avatarColor = "[$sR,$sG,$sB,1]"
    }
    fun generateUserClicked (view: View){}

}
