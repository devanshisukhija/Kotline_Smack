package com.devanshisukhija.smack.Services

import android.graphics.Color
import java.util.*

/**
 * Created by devanshi on 08/02/18.
 */
object UserDataService {

    var id = ""
    var avatarColor = ""
    var avatarName = ""
    var email = ""
    var name = ""


    fun logout() {

        id = ""
        avatarColor = ""
        avatarName =""
        name = ""
        email = ""

        AuthService.authToken = ""
        AuthService.isLoggedIn = false
        AuthService.userEmail = ""

    }

    fun returnAvatarColor(components :String) : Int {

        val strippedColor = components
                .replace("[", "")
                .replace("]", "")
                .replace(",", "")

        var r =0
        var g =0
        var b =0

        val scanner = Scanner(strippedColor)
        if (scanner.hasNext()) {
            r = (scanner.nextDouble() * 255).toInt()
            g = (scanner.nextDouble() * 255).toInt()
            b = (scanner.nextDouble() * 255).toInt()
        }


        return Color.rgb(r,g,b)
    }
}