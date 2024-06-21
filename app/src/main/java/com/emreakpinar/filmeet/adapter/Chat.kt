package com.emreakpinar.filmeet.adapter

import com.google.firebase.firestore.FieldValue

data class Chat(
    val userId: String,
    val text: String,
    //val timestamp: FieldValue
)


