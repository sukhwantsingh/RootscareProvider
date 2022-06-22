package com.rootscare.serviceprovider.ui.hospital.adddoctoreinhospital

import com.rootscare.data.model.response.doctor.profileresponse.GetDoctorProfileResponse
import com.rootscare.data.model.response.hospital.HospitalHDepartmentListResponse
import com.rootscare.data.model.response.loginresponse.LoginResponse

interface FragmentAddDoctorProfileNavigator {

    fun successDepartmentListResponse(departmentListResponse: HospitalHDepartmentListResponse?)
    fun onSuccessEditProfile(response: LoginResponse)
    fun successGetDoctorProfileResponse(getDoctorProfileResponse: GetDoctorProfileResponse?)
    fun errorInApi(throwable: Throwable?)

}