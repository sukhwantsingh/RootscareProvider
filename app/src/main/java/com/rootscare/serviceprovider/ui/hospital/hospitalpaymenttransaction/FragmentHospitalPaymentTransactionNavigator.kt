package com.rootscare.serviceprovider.ui.hospital.hospitalpaymenttransaction

import com.rootscare.data.model.response.hospital.PaymentResponseHospital

interface FragmentHospitalPaymentTransactionNavigator {
    fun successDoctorDepartmentListingResponse(doctorDepartmentListingResponse: PaymentResponseHospital?)

    fun errorDoctorDepartmentListingResponse(throwable: Throwable?)
}