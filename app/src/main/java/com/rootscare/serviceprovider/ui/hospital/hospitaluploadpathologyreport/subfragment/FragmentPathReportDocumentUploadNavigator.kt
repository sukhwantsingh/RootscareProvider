package com.rootscare.serviceprovider.ui.hospital.hospitaluploadpathologyreport.subfragment

import com.rootscare.data.model.response.CommonResponse

interface FragmentPathReportDocumentUploadNavigator {

    fun onSuccessReportUpload(response: CommonResponse)
    fun errorInApi(throwable: Throwable?)
}