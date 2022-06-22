package com.rootscare.serviceprovider.ui.physiotherapy.physiotherapyManageProfile.editPhysiotherapyProfile

import com.rootscare.data.model.response.caregiver.profileresponse.GetDoctorProfileResponse
import com.rootscare.data.model.response.deaprtmentlist.DepartmentListResponse
import com.rootscare.data.model.response.loginresponse.LoginResponse

interface FragmentEditPhysiotherapyUpdateProfileNavigator {

    fun successDepartmentListResponse(departmentListResponse: DepartmentListResponse?)
    fun onSuccessEditProfile(response: LoginResponse)
    fun successGetDoctorProfileResponse(getDoctorProfileResponse: GetDoctorProfileResponse?)
    fun errorInApi(throwable: Throwable?)

}