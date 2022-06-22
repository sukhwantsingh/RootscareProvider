package com.rootscare.serviceprovider.ui.login.subfragment.registration.subfragment.registrationsetpthree

import com.rootscare.data.model.response.hospital.HospitalHDepartmentListResponse
import com.rootscare.data.model.response.registrationresponse.RegistrationResponse
import com.rootscare.model.ModelServiceFor
import com.rootscare.serviceprovider.ui.supportmore.models.ModelIssueTypes

interface FragmentRegistrationStepThreeNavigator {
    fun successRegistrationResponse(registrationResponse: RegistrationResponse?){}
    fun successDepartmentListResponse(departmentListResponse: HospitalHDepartmentListResponse?){}
    fun onSuccessSpeciality(specialityList: ModelIssueTypes?){}
    fun onSuccessServiceFor(specialityList: ModelServiceFor?){}
    fun errorRegistrationResponse(throwable: Throwable?){}
}