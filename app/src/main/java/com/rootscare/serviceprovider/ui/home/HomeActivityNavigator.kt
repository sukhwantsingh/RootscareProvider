package com.rootscare.serviceprovider.ui.home

import com.rootscare.data.model.response.CommonResponse

interface HomeActivityNavigator {
    fun successLogoutResponse(commonResponse: CommonResponse?){}
    fun errorLogoutResponse(throwable: Throwable?){}
}