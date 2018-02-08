package com.devanshisukhija.smack.Controller

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.devanshisukhija.smack.R
import com.devanshisukhija.smack.Services.AuthService
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginSpinner.visibility = View.INVISIBLE
    }


    fun loginCreateUSerBtnClicked(view: View) {
        val createuserIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(createuserIntent)
        finish()
    }

    fun loginLoginBtnClicked(view:View) {

        enableSpinner(true)
        val email = loginemailTxt.text.toString()
        val password = loginpasswordTxt.text.toString()
hideKeyboard()
        if (email.isNotEmpty() && password.isNotEmpty()){

            AuthService.loginUser(this, email, password) { loginSuccess ->
                if(loginSuccess) {
                    AuthService.findUserbyEmail(this) { findSuccess->
                        if(findSuccess){
                            enableSpinner(false)
                            finish()
                        } else {errorToast()}
                    }
                } else {errorToast()}
            }
        } else {

            Toast.makeText(this, "Please fill in both email and password", Toast.LENGTH_LONG).show()
        }
    }

    fun enableSpinner(enable:Boolean) {

        if(enable) {
            loginSpinner.visibility = View.VISIBLE
        } else {
            loginSpinner.visibility = View.INVISIBLE
        }

        loginLoginBtn.isEnabled = true
        loginCreateUserBtn.isEnabled = true
    }

    fun errorToast() {
        enableSpinner(false)
//        Log.d("CREATE", msg)
        Toast.makeText(this, "Something went wrong, please try again", Toast.LENGTH_LONG).show()
    }

    fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if(inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }

    }
}
