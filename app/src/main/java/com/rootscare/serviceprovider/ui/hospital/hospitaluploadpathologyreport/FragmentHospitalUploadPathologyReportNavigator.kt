package com.rootscare.serviceprovider.ui.hospital.hospitaluploadpathologyreport

import com.rootscare.data.model.response.hospital.ReportResultItem

interface FragmentHospitalUploadPathologyReportNavigator {

    fun successPathologyListResponse(response: ReportResultItem)

    fun errorPathologyListResponse(throwable: Throwable?)
}