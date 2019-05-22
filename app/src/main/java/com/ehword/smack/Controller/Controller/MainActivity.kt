package com.ehword.smack.Controller.Controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import com.ehword.smack.Controller.Services.AuthService
import com.ehword.smack.Controller.Services.UserDataService
import com.ehword.smack.Controller.Utilities.BROADCAST_USER_DATA_CHANGE
import com.ehword.smack.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        //val fab: FloatingActionButton = findViewById(R.id.fab)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangeReceiver, IntentFilter(
            BROADCAST_USER_DATA_CHANGE))
    }

    private val userDataChangeReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            if (AuthService.isLoggedIn) {
                nav_drawer_header_include.userNameNavHeader.text = UserDataService.name
                nav_drawer_header_include.userEmailNavHeader.text = UserDataService.email
                val resourceId = resources.getIdentifier(UserDataService.avatarName,"drawable",packageName)
                nav_drawer_header_include.userImageNavHeader.setImageResource(resourceId)
                nav_drawer_header_include.loginButtonNavHeader.text = "Logout"
                println(UserDataService.avatarColor)
                nav_drawer_header_include.userImageNavHeader.setBackgroundColor(UserDataService.returnAvatarColor(UserDataService.avatarColor))
            }
        }
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun loginBtnNavClicked (view: View){
        if (AuthService.isLoggedIn)
        {
            UserDataService.logout()
            nav_drawer_header_include.userNameNavHeader.text = ""
            nav_drawer_header_include.userEmailNavHeader.text = ""
            nav_drawer_header_include.userImageNavHeader.setImageResource(R.drawable.profiledefault)
            nav_drawer_header_include.loginButtonNavHeader.text = "Login"
            nav_drawer_header_include.userImageNavHeader.setBackgroundColor(Color.TRANSPARENT)
        }
        else
        {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
    }
    fun addChannelButtonClicked (view:View){}
    fun sendMessageButtonClicked (view:View){}
}

