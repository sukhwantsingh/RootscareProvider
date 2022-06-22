package com.rootscare.serviceprovider.ui.babySitter.babySitterProfile

interface FragmentBabySitterProfileUpdateNavigator {
    fun successGetDoctorProfileResponse(getDoctorProfileResponse: com.rootscare.data.model.response.caregiver.profileresponse.GetDoctorProfileResponse?)
    fun errorGetDoctorProfileResponse(throwable: Throwable?)
}