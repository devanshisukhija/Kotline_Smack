package com.devanshisukhija.smack.Utilities

/**
 * Created by devanshi on 07/02/18.
 */


const val BASE_URL = "https://chatysmackchat.herokuapp.com/v1/"
const val SOCKET_URL =  "https://chatysmackchat.herokuapp.com/"
const val URL_REGISTER = "${BASE_URL}account/register"

const val URL_LOGIN = "${BASE_URL}account/login"
const val URL_CREATE_USER = "${BASE_URL}user/add"


const val BROADCAST_USER_DATA_CHANGE =" BROADCAST_USER_DATA_CHANGE"


const val URL_GET_USER = "${BASE_URL}user/byEmail/"
const val URL_GET_CHANNELS = "${BASE_URL}channel/"
const val URL_GET_MESSAGES = "${BASE_URL}message/byChannel/"