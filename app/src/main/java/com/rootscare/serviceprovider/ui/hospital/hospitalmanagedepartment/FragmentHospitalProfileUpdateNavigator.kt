package com.rootscare.serviceprovider.ui.hospital.hospitalmanagedepartment

import com.rootscare.data.model.response.hospital.HospitalHDepartmentListResponse

interface FragmentHospitalProfileUpdateNavigator {
    fun successGetDoctorProfileResponse(getDoctorProfileResponse: HospitalHDepartmentListResponse?)
    fun errorGetDoctorProfileResponse(throwable: Throwable?)
}