package com.rootscare.serviceprovider.ui.hospital

import com.rootscare.data.model.response.CommonResponse
import com.rootscare.data.model.response.NotificationCountResponse

interface HospitalHomeActivityNavigator {

    fun successLogoutResponse(commonResponse: CommonResponse?)
    fun onSuccessUnreadNotification(commonResponse: NotificationCountResponse?){}
    fun errorLogoutResponse(throwable: Throwable?)
}