package com.devanshisukhija.smack.Controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import com.devanshisukhija.smack.Model.Channel
import com.devanshisukhija.smack.R
import com.devanshisukhija.smack.Services.AuthService
import com.devanshisukhija.smack.Services.MessageService
import com.devanshisukhija.smack.Services.UserDataService
import com.devanshisukhija.smack.Utilities.BROADCAST_USER_DATA_CHANGE
import com.devanshisukhija.smack.Utilities.SOCKET_URL
import io.socket.client.IO
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity() {

    val socket = IO.socket(SOCKET_URL)
    lateinit var channelAdapter :ArrayAdapter<Channel>
    var selectableChannel : Channel? = null

    private fun setupAdapters() {
        channelAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,MessageService.channels)

        channel_list.adapter = channelAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        setupAdapters()

        if(App.prefs.isloogedIn) {
            AuthService.findUserbyEmail(this){}
        }


        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangeReceiver, IntentFilter(
                BROADCAST_USER_DATA_CHANGE ))

        socket.connect()
        socket.on("channelCreated", onNewchannel)

        channel_list.setOnItemClickListener { _ ,_, position, _ ->
            selectableChannel = MessageService.channels[position]
            drawer_layout.closeDrawer(GravityCompat.START)
            updateWithChannel()
        }

    }

    override fun onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangeReceiver,
                IntentFilter(BROADCAST_USER_DATA_CHANGE))
        super.onResume()
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(userDataChangeReceiver)

        socket.disconnect()
        super.onDestroy()
    }

    private val userDataChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if(App.prefs.isloogedIn){
                userNameNavHeader.text = UserDataService.name
                userEmailNavHeader.text = UserDataService.email
                val resourceId = resources.getIdentifier(UserDataService.avatarName, "drawable",
                        packageName)
                userImageNavHeader.setImageResource(resourceId)
               // userImageNavHeader.setBackgroundColor(UserDataService.returnAvatarColor(UserDataService.avatarColor))
                loginBtnNavHeader.text = "Logout"

                MessageService.getChannels{complete ->
                    if(complete){
                        if(MessageService.channels.count() > 0){
                            selectableChannel = MessageService.channels[0]

                            channelAdapter.notifyDataSetChanged()

                            updateWithChannel()
                        }
                    }

                }
            }
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun loginNavBtnClicked(view:View) {

       lateinit var loginIntent : Intent
        if(App.prefs.isloogedIn) {

            UserDataService.logout()
            userNameNavHeader.text = "Login"
            userEmailNavHeader.text = ""
            userImageNavHeader.setImageResource(R.drawable.profiledefault)
            userImageNavHeader.setBackgroundColor(Color.TRANSPARENT)

            loginBtnNavHeader.text = "Login"
            loginIntent = Intent(this, MainActivity::class.java)


        } else {
            loginIntent = Intent(this, LoginActivity::class.java)

        }
        startActivity(loginIntent)

    }

    fun addChannelClicked(view: View) {

       // if(AuthService.isLoggedIn) {
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.add_channel_dialog, null)
            builder.setView(dialogView)
                    .setPositiveButton("Add") { _, _: Int ->

                        val nameTextField = dialogView.findViewById<EditText>(R.id.dialogChannelName)
                        val desxTextField = dialogView.findViewById<EditText>(R.id.channelDescription)

                        val channelName = nameTextField.text.toString()
                        val channelDesc = desxTextField.text.toString()
                        hideKeyboard()

                        socket.emit("newChannel", channelName , channelDesc)

                    }
                    .setNegativeButton("Cancel" ){ _, _ ->
                        hideKeyboard()
                    }
                    .show()

       // }

    }

    //this works on the worker thread
    private val onNewchannel = Emitter.Listener { args ->
        runOnUiThread {
            val channelName= args[0] as String
            val channelDesc = args[1] as String
            val channelId = args[2] as String

            val newChannel = Channel(channelName, channelDesc, channelId)

            MessageService.channels.add(newChannel)
        }
    }

    fun senMsgBtnClicked(view:View) {
        hideKeyboard()
    }

    fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }

    fun updateWithChannel(){
        mainChannelName.text = "#${selectableChannel?.name}"
    }
}
