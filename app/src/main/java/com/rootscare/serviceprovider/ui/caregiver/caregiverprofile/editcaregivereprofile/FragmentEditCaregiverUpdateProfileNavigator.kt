package com.rootscare.serviceprovider.ui.caregiver.caregiverprofile.editcaregivereprofile

import com.rootscare.data.model.response.deaprtmentlist.DepartmentListResponse
import com.rootscare.data.model.response.loginresponse.LoginResponse

interface FragmentEditCaregiverUpdateProfileNavigator {

    fun successDepartmentListResponse(departmentListResponse: DepartmentListResponse?)
    fun onSuccessEditProfile(response: LoginResponse)
    fun successGetDoctorProfileResponse(getDoctorProfileResponse: com.rootscare.data.model.response.caregiver.profileresponse.GetDoctorProfileResponse?)
    fun errorInApi(throwable: Throwable?)

}