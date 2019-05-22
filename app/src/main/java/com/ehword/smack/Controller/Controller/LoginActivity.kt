package com.ehword.smack.Controller.Controller

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.ehword.smack.Controller.Services.AuthService
import com.ehword.smack.Controller.Services.UserDataService
import com.ehword.smack.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.nav_header_main.view.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginSpinner.visibility = View.INVISIBLE
    }

    fun loginLoginButtonClicked (view: View){
        enableSpinner(true)
        val email = loginEmailText.text.toString()
        val password = loginPasswordText.text.toString()

        hideKeyboard()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            AuthService.loginUser(this, email, password) { loginSuccess ->
                if (loginSuccess) {
                    AuthService.findUserByEmail(this) { findSuccess ->
                        if (findSuccess) {
                            enableSpinner(false)
                            finish()
                        } else {
                            errorToast()
                        }
                    }
                } else {
                    errorToast()
                }
            }
        }
        else{
            enableSpinner(false)
            Toast.makeText(this,"No empty fields please",Toast.LENGTH_LONG).show()
        }
    }
    fun loginSignupButtonClicked (view:View) {

        val signUpIntent = Intent(this, SignupActivity::class.java)
        startActivity(signUpIntent)

    }

    fun errorToast(){
        Toast.makeText(this,"Error, please try again", Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }

    fun enableSpinner(enable: Boolean)
    {
        if (enable)
        {
            loginSpinner.visibility = View.VISIBLE
        }
        else
        {
            loginSpinner.visibility = View.INVISIBLE
        }
        loginButton.isEnabled = !enable
        signUpButton.isEnabled = !enable

    }
    fun hideKeyboard ()
    {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputManager.isAcceptingText){
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken,0)
        }
    }

}
