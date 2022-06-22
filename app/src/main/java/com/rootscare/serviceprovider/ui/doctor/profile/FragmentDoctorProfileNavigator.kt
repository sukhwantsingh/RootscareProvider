package com.rootscare.serviceprovider.ui.doctor.profile

import com.rootscare.data.model.response.doctor.profileresponse.GetDoctorProfileResponse

interface FragmentDoctorProfileNavigator {
    fun successGetDoctorProfileResponse(getDoctorProfileResponse: GetDoctorProfileResponse?)
    fun errorGetDoctorProfileResponse(throwable: Throwable?)
}