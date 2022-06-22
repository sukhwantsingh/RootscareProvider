package com.rootscare.serviceprovider.ui.splash

import com.rootscare.data.model.response.CommonResponse


interface SplashActivityNavigator {
    fun onSuccessVersion(response: CommonResponse?)
    fun errorInApi(throwable: Throwable?)
}