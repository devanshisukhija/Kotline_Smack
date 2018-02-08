package com.devanshisukhija.smack.Model

/**
 * Created by devanshi on 08/02/18.
 */
class Channel(val name:String, val description : String, val id: String) {

    override fun toString(): String {
        return "#$name"
    }
}