package com.ehword.smack.Controller.Controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.os.Message
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import com.ehword.smack.Controller.Model.Channel
import com.ehword.smack.Controller.Services.AuthService
import com.ehword.smack.Controller.Services.MessageService
import com.ehword.smack.Controller.Services.UserDataService
import com.ehword.smack.Controller.Utilities.BROADCAST_USER_DATA_CHANGE
import com.ehword.smack.Controller.Utilities.SOCKET_URL
import com.ehword.smack.R
import io.socket.client.IO
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*

class MainActivity : AppCompatActivity() {

    val socket = IO.socket(SOCKET_URL)
    lateinit var channelAdapter: ArrayAdapter<Channel>

    private fun setupAdaptors (){
        channelAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, MessageService.channels)
        channel_list.adapter = channelAdapter

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        socket.connect()
        socket.on("channelCreated",onNewChannel)
        //val fab: FloatingActionButton = findViewById(R.id.fab)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        setupAdaptors()
        if (App.sharedPreferences.isLoggedIn){
            AuthService.findUserByEmail(this){}
        }
    }

    override fun onResume() {

        println("on resume: ${UserDataService.email}")
        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangeReceiver, IntentFilter(
            BROADCAST_USER_DATA_CHANGE))
        super.onResume()

    }

    override fun onPause() {
        super.onPause()
        println("on pause")
    }

    override fun onDestroy() {
        socket.disconnect()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(userDataChangeReceiver)
        super.onDestroy()
    }

    private val onNewChannel = Emitter.Listener { args ->
//        println(args[0] as String)
//        println(args[1] as String)
//        println(args[2] as String)
        // socket listeners run on a background thread
        runOnUiThread{
            val channelName = args[0] as String
            val channelDesc = args[1] as String
            val channelId = args[2] as String

            val newChannel = Channel(channelName,channelDesc,channelId)
            MessageService.channels.add(newChannel)
            channelAdapter.notifyDataSetChanged()
        }
    }

    private val userDataChangeReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent?) {
            if (App.sharedPreferences.isLoggedIn) {
                println("in UDCR")
                nav_drawer_header_include.userNameNavHeader.text = UserDataService.name
                nav_drawer_header_include.userEmailNavHeader.text = UserDataService.email
                val resourceId = resources.getIdentifier(UserDataService.avatarName,"drawable",packageName)
                nav_drawer_header_include.userImageNavHeader.setImageResource(resourceId)
                nav_drawer_header_include.loginButtonNavHeader.text = "Logout"
                println(UserDataService.avatarColor)
                nav_drawer_header_include.userImageNavHeader.setBackgroundColor(UserDataService.returnAvatarColor(UserDataService.avatarColor))

                MessageService.getChannels(context){complete ->
                    if (complete){
                        channelAdapter.notifyDataSetChanged()
                    }

                }
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
        if (App.sharedPreferences.isLoggedIn)
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
    fun addChannelButtonClicked (view:View){
        hideKeyboard ()
        if (App.sharedPreferences.isLoggedIn){
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.add_channel_dialog,null)

            builder.setView(dialogView)
                .setPositiveButton("Add"){dialog, which ->
                    val nameTextField = dialogView.findViewById<EditText>(R.id.addChannelNametxt)
                    val descTextField = dialogView.findViewById<EditText>(R.id.addChannelDescriptionTxt)
                    val channelName = nameTextField.text.toString()
                    val channelDesc = descTextField.text.toString()
                    socket.emit("newChannel",channelName,channelDesc)
                }
                .setNegativeButton("Cancel"){dialog, which ->
                    Toast.makeText(this,"Channel Not Added, please try again",Toast.LENGTH_SHORT).show()
                    hideKeyboard ()
                }.show()
        }
        else
        {
            Toast.makeText(this,"Please Login First",Toast.LENGTH_SHORT).show()
        }

    }
    fun sendMessageButtonClicked (view:View){
        hideKeyboard()
    }

    fun hideKeyboard ()
    {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputManager.isAcceptingText){
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken,0)
        }
    }
}

