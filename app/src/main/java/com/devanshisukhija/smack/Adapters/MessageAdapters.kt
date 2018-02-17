package com.devanshisukhija.smack.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.devanshisukhija.smack.Model.Message
import com.devanshisukhija.smack.R
import com.devanshisukhija.smack.Services.UserDataService
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by devanshi on 09/02/18.
 */
class MessageAdapters(val context : Context, val messages : ArrayList<Message>) : RecyclerView.Adapter<MessageAdapters.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.message_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
            return messages.count()
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        holder?.BindMessage(context, messages[position])
    }


    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        val userImage = itemView?.findViewById<ImageView>(R.id.messageAvatar)
        val timestamp = itemView?.findViewById<TextView>(R.id.messageTimestamp)
        val username = itemView?.findViewById<TextView>(R.id.messageNameTxt)
        val msgbody = itemView?.findViewById<TextView>(R.id.messageBodyTxt)

        fun BindMessage( context: Context,  message : Message) {

            val resourceId = context.resources.getIdentifier(message.userAvatarName, "drawable", context.packageName)

            userImage?.setImageResource(resourceId)
            userImage?.setBackgroundColor(UserDataService.returnAvatarColor(message.userAvatarColor))
            timestamp?.text = returndateString(message.timestamp)
            username?.text = message.userName
            msgbody?.text = message.message
        }
    }

    fun returndateString(isoString : String) : String {

      val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

        isoFormatter.timeZone = TimeZone.getTimeZone("UTC")
        var converterDate = Date()

        try {
            converterDate = isoFormatter.parse(isoString)
        } catch (e : ParseException) {
            Log.d("PARSE" , "EXC:  can not parse date")
        }

        val outDateString = SimpleDateFormat("E, h:mm a", Locale.getDefault())
        return outDateString.format(converterDate)
    }
}