package com.rootscare.serviceprovider.ui.babySitter.home

import com.rootscare.data.model.response.CommonResponse

interface BabySitterHomeActivityNavigator {
    fun successLogoutResponse(commonResponse: CommonResponse?)
    fun errorLogoutResponse(throwable: Throwable?)
}