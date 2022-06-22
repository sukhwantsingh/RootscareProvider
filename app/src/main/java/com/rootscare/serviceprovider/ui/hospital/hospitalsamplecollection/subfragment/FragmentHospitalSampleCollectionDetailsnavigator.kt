package com.rootscare.serviceprovider.ui.hospital.hospitalsamplecollection.subfragment

import com.rootscare.data.model.response.hospital.OrderDetails

interface FragmentHospitalSampleCollectionDetailsnavigator {
    fun successGetDoctorProfileResponse(getDoctorProfileResponse: OrderDetails?)
    fun errorInApi(throwable: Throwable?)

}