package com.rootscare.serviceprovider.ui.caregiver.caregiverprofile

interface FragmentCaregiverProfileUpdateNavigator {
    fun successGetDoctorProfileResponse(getDoctorProfileResponse: com.rootscare.data.model.response.caregiver.profileresponse.GetDoctorProfileResponse?)
    fun errorGetDoctorProfileResponse(throwable: Throwable?)
}