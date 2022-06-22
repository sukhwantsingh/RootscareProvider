package com.rootscare.serviceprovider.ui.hospital.hospitalsamplecollection

import com.rootscare.data.model.response.hospital.HospitalLabUpload

interface FragmentHospitalSampleCollectionNavigator {
    fun responseLisCancelledAppointment(response: HospitalLabUpload)
    fun errorGetDoctorRequestAppointmentResponse(throwable: Throwable?)
}