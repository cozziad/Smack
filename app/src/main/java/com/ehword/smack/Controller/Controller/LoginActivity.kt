package com.ehword.smack.Controller.Controller

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ehword.smack.Controller.Services.AuthService
import com.ehword.smack.Controller.Services.UserDataService
import com.ehword.smack.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun loginLoginButtonClicked (view: View){}
    fun loginSignupButtonClicked (view:View) {

        val signUpIntent = Intent(this, SignupActivity::class.java)
        startActivity(signUpIntent)

    }

}
