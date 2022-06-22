package com.rootscare.serviceprovider.ui.doctor.profile.editdoctoreprofile

import com.rootscare.data.model.response.doctor.profileresponse.GetDoctorProfileResponse
import com.rootscare.data.model.response.hospital.HospitalHDepartmentListResponse
import com.rootscare.data.model.response.loginresponse.LoginResponse
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.models.ModelUserProfile

interface FragmentEditDoctorProfileNavigator {
    fun onSuccessUserProfile(response: ModelUserProfile?){}
    fun successDepartmentListResponse(departmentListResponse: HospitalHDepartmentListResponse?)
    fun onSuccessEditProfile(response: LoginResponse)
    fun successGetDoctorProfileResponse(getDoctorProfileResponse: GetDoctorProfileResponse?){}
    fun successGetPhysiotherapyProfileResponse(getDoctorProfileResponse: com.rootscare.data.model.response.caregiver.profileresponse.GetDoctorProfileResponse?)
    fun errorInApi(throwable: Throwable?)
}