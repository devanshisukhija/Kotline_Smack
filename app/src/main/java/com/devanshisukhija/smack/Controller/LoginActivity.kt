package com.devanshisukhija.smack.Controller

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.devanshisukhija.smack.R
import com.devanshisukhija.smack.Services.AuthService
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }


    fun loginCreateUSerBtnClicked(view: View) {
        val createuserIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(createuserIntent)
        finish()
    }

    fun loginLoginBtnClicked(view:View) {

        val email = loginemailTxt.text.toString()
        val password = loginpasswordTxt.text.toString()

        AuthService.loginUser(this, email, password) {loginSuccess ->
            if(loginSuccess) {
                AuthService.findUserbyEmail(this) {findSuccess->
                    if(findSuccess){
                        finish()
                    }
                }
            }
        }
    }
}
