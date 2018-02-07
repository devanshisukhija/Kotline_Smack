package com.devanshisukhija.smack

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }


    fun loginCreateUSerBtnClicked(view: View) {
        val createuserIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(createuserIntent)
    }

    fun loginLoginBtnClicked(view:View) {

    }
}
