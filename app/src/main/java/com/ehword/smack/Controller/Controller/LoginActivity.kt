package com.ehword.smack.Controller.Controller

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ehword.smack.R

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
