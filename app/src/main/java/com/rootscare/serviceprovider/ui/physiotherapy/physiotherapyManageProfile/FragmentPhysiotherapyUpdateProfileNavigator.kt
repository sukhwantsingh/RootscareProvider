package com.rootscare.serviceprovider.ui.physiotherapy.physiotherapyManageProfile

import com.rootscare.data.model.response.caregiver.profileresponse.GetDoctorProfileResponse

interface FragmentPhysiotherapyUpdateProfileNavigator {
    fun successGetDoctorProfileResponse(getDoctorProfileResponse: GetDoctorProfileResponse?)
    fun errorGetDoctorProfileResponse(throwable: Throwable?)
}