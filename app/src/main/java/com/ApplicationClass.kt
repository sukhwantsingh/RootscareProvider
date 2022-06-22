package com

import android.app.Application
import com.google.firebase.FirebaseApp
import com.rootscare.data.datasource.api.ApiClient
import com.rootscare.data.datasource.api.ApiService
import com.rootscare.data.datasource.sharedpref.AppSharedPref
import com.rootscare.serviceprovider.utilitycommon.API_BASE_URL


import com.rootscare.utils.AppConstants
import io.branch.referral.Branch

class ApplicationClass : Application() {

    var appSharedPref: AppSharedPref? = null
        private set

    val apiServiceWithJacksonFactory: ApiService
        get() = ApiClient.retrofit(this, API_BASE_URL, ApiClient.CONVERTER_TYPE_JACKSON)!!.create(ApiService::class.java!!)

    val apiServiceWithGsonFactory: ApiService
        get() = ApiClient.retrofit(this, API_BASE_URL, ApiClient.CONVERTER_TYPE_GSON)!!.create(ApiService::class.java!!)


    override fun onCreate() {
        super.onCreate()
        instance = this
        appSharedPref = AppSharedPref(this, AppConstants.SHARED_PREF_NAME)

        // Branch logging for debugging
        Branch.enableLogging();
        // Branch object initialization
        Branch.getAutoInstance(this)

        FirebaseApp.initializeApp(this)
    }


    companion object {

        @get:Synchronized
        var instance: ApplicationClass? = null
            private set

        lateinit var user: String


    }

}
