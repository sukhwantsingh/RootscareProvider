package com.rootscare.serviceprovider.ui.nurses.home

import com.rootscare.data.model.response.CommonResponse
import com.rootscare.data.model.response.NotificationCountResponse

interface NursrsHomeActivityNavigator {
    fun successLogoutResponse(commonResponse: CommonResponse?)
    fun onSuccessUnreadNotification(commonResponse: NotificationCountResponse?){}
    fun errorLogoutResponse(throwable: Throwable?)
}