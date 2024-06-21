package com.emreakpinar.filmeet.arkadasekle

import com.google.firebase.firestore.PropertyName

data class Message(
    @PropertyName("senderId") var senderId: String = "",
    @PropertyName("receiverId") var receiverId: String = "",
    @PropertyName("message") var message: String = "",
    @PropertyName("timestamp") var timestamp: Long = 0L
)
