package com.devanshisukhija.smack.Controller

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.devanshisukhija.smack.R
import com.devanshisukhija.smack.Services.AuthService
import com.devanshisukhija.smack.Utilities.BROADCAST_USER_DATA_CHANGE
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    var userAvatar = "profileDefault"
    var avatarBgColor = "[0.5,0.5,0.5, 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)

        createSpinner.visibility = View.INVISIBLE
    }

    fun generateUserAvatar(view:View){

        val random = Random()
        val color = random.nextInt(2)
        val avatar = random.nextInt(28)


        when(color) {
            0 -> userAvatar="light$avatar"
            else -> userAvatar="dark$avatar"
        }

        val resourceId = resources.getIdentifier(userAvatar, "drawable", packageName)
        createAvatarImage.setImageResource(resourceId)

    }

    fun generateBgColorBtnClicked(view: View){

        val random = Random()
        val r = random.nextInt(255)
        val g = random.nextInt(255)
        val b = random.nextInt(255)

        createAvatarImage.setBackgroundColor(Color.rgb(r,g,b))

        val savedR = r.toDouble()/255
        val savedG = g.toDouble()/255
        val savedB = b.toDouble()/255
        avatarBgColor = "[$savedR,$savedG,$savedB , 1]"

    }

    fun createUserBtnClicked(view: View) {



        val email = createuserEmailTxt.text.toString()
        val password = createuserPasswordTxt.text.toString()

        val username = createuserUsernameTxt.text.toString()

        enableSpinner(true)

        if(username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {

            AuthService.registerUser( email , password) {
                registerSuccess ->
                if(registerSuccess) {
                    AuthService.loginUser(email, password) { loginSucess ->
                        if(loginSucess) {
                            AuthService.createUser(username, email, userAvatar , avatarBgColor) { createSucess ->
                                if(createSucess){
                                    val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                                    LocalBroadcastManager.getInstance(this).sendBroadcast(userDataChange)
                                    enableSpinner(false)
                                    finish()
                                } else { errorToast("createUser")}
                            }
                        } else { errorToast("login user")}
                    }
                } else { errorToast("account")}
            }
        } else {
            Toast.makeText(this, "Make sure username, email and password are filled in.", Toast.LENGTH_LONG).show()
            enableSpinner(false)
        }

    }

    fun enableSpinner(enable:Boolean) {

        if(enable) {
            createSpinner.visibility = View.VISIBLE
        } else {
            createSpinner.visibility = View.INVISIBLE
        }

        createuserCreateuserBtn.isEnabled = true
        createAvatarImage.isEnabled = true
        createuserBgcolorBtn.isEnabled = true
    }

    fun errorToast(msg : String) {
        enableSpinner(false)
        Log.d("CREATE", msg)
        Toast.makeText(this, "Something went wrong, please try again", Toast.LENGTH_LONG).show()
    }
}
