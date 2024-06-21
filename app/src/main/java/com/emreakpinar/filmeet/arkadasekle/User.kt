package com.emreakpinar.filmeet.arkadasekle

import com.google.firebase.firestore.PropertyName

data class User(
    @PropertyName("uid") var uid: String = "",
    @PropertyName("username") var username: String = "",
    @PropertyName("email") var email: String = ""
)
