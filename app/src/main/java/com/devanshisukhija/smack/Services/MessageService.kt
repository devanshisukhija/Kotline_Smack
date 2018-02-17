package com.devanshisukhija.smack.Services

import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.devanshisukhija.smack.Controller.App
import com.devanshisukhija.smack.Model.Channel
import com.devanshisukhija.smack.Model.Message
import com.devanshisukhija.smack.Utilities.URL_GET_CHANNELS
import com.devanshisukhija.smack.Utilities.URL_GET_MESSAGES
import org.json.JSONException

/**
 * Created by devanshi on 08/02/18.
 */
object MessageService {

    val channels = ArrayList<com.devanshisukhija.smack.Model.Channel>()

    val messages = ArrayList<Message>()

    fun getChannels( complete:(Boolean)-> Unit)
    {

        val channelsRequest = object : JsonArrayRequest(Method.GET, URL_GET_CHANNELS, null, Response.Listener {
            response ->
            clearMessages()
            try {
                for (x in 0 until response.length()) {
                    val channel = response.getJSONObject(x)
                    val name = channel.getString("name")
                    val desc = channel.getString("description")
                    val id = channel.getString("_id")

                    val newChannel = Channel(name, desc, id)

                    this.channels.add(newChannel)
                }
                complete(true)

            } catch (e: JSONException){
                Log.d("JSON", "EXC:" + e.localizedMessage)
                complete(false)
            }
        }, Response.ErrorListener {

            Log.d("ERROR" ,"Could not retrive channels")
            complete(false)
        }) {

            override fun getBodyContentType(): String {
                return "application/json;  charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()

                headers.put("Authorization", "Bearer ${App.prefs.authToken}")

                return headers
            }
        }

        App.prefs.requestQueue.add(channelsRequest)
    }

    fun getMessages(channelId : String, complete: (Boolean) -> Unit){

        val url = "$URL_GET_MESSAGES$channelId"

        val messagesRequest = object : JsonArrayRequest(Method.GET, url, null, Response.Listener{ response ->

            try{
                for (x in 0 until response.length()){
                    val msg = response.getJSONObject(x)

                    val msgBody = msg.getString("messageBody")
                    val channelId = msg.getString("channelId")
                    val id = msg.getString("_id")
                    val username = msg.getString("userName")
                    val userAvatar =  msg.getString("userAvatar")
                    val userAvatarColor = msg.getString("userAvatarColor")
                    val timestamp = msg.getString("timestamp")

                    val newMessage = Message(msgBody, username, channelId, userAvatar, userAvatarColor, id,timestamp)
                    this.messages.add(newMessage)

                    complete(true)
                }
            } catch(e:JSONException) {
                Log.d("JSON", "EXC: " + e.localizedMessage)
                complete(false)
            }
        }, Response.ErrorListener {

        }) {
            override fun getBodyContentType(): String {
                return "application/json;  charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()

                headers.put("Authorization", "Bearer ${App.prefs.authToken}")

                return headers
            }
        }

        App.prefs.requestQueue.add(messagesRequest)
    }

    fun clearMessages() {
        messages.clear()
    }

    fun clearChannels(){
        channels.clear()
    }
}