package com.rootscare.serviceprovider.ui.splash

import com.rootscare.serviceprovider.ui.splash.model.NetworkAppCheck


interface SplashActivityNavigator {
    fun onSuccessVersion(response: NetworkAppCheck?)
    fun errorInApi(throwable: Throwable?)
}