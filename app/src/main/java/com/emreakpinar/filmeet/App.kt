package com.emreakpinar.filmeet

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore

class App : Application() {

    companion object {
        lateinit var firestore: FirebaseFirestore
    }

    override fun onCreate() {
        super.onCreate()
        // Firestore instance'ını burada alın ve referans alın
        firestore = FirebaseFirestore.getInstance()
    }
}
