package com.mynewproject.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        Log.i("fcm msg", message.data.toString())
    }

    override fun onNewToken(token: String) {
        Log.i("fcm token", token)
    }
}