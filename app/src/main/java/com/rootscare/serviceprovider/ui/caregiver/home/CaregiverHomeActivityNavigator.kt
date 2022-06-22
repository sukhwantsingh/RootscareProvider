package com.rootscare.serviceprovider.ui.caregiver.home

import com.rootscare.data.model.response.CommonResponse

interface CaregiverHomeActivityNavigator {
    fun successLogoutResponse(commonResponse: CommonResponse?)
    fun errorLogoutResponse(throwable: Throwable?)
}