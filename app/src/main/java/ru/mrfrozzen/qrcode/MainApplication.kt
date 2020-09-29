package ru.mrfrozzen.qrcode

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics




class MainApplication : Application() {

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onCreate() {
        super.onCreate()
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
    }

}