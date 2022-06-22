package com.rootscare.serviceprovider.ui.hospital.hospitalmanagedoctor

import com.rootscare.data.model.response.hospital.AllDoctorHosListingRes

interface FragmenthospitalManageDoctorNavigator {

    fun successDoctorDepartmentListingResponse(doctorDepartmentListingResponse: AllDoctorHosListingRes?)

    fun errorDoctorDepartmentListingResponse(throwable: Throwable?)
}