package com.rootscare.serviceprovider.ui.labtechnician.home

import com.rootscare.data.model.response.CommonResponse

interface LabTechnicianHomeActivityNavigator {

    fun successLogoutResponse(commonResponse: CommonResponse?)
    fun errorLogoutResponse(throwable: Throwable?)
}