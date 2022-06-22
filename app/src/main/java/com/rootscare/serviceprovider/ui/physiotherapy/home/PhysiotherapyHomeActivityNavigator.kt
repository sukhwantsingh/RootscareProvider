package com.rootscare.serviceprovider.ui.physiotherapy.home

import com.rootscare.data.model.response.CommonResponse

interface PhysiotherapyHomeActivityNavigator {
    fun successLogoutResponse(commonResponse: CommonResponse?)
    fun errorLogoutResponse(throwable: Throwable?)
}