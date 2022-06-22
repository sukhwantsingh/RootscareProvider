package com.rootscare.serviceprovider.ui.hospital.hospitaluploadpathologyreport.subfragment

import android.util.Log
import com.google.gson.Gson
import com.rootscare.serviceprovider.ui.base.BaseViewModel
import com.rootscare.serviceprovider.ui.hospital.hospitaluploadpathologyreport.FragmentHospitalUploadPathologyReportNavigator
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FragmentPathReportDocumentUploadViewModel : BaseViewModel<FragmentPathReportDocumentUploadNavigator>() {

    private val TAG = "FragmentPathReportDocumentUpload"

    fun apiHitForReportUpload(
        hospitalId: RequestBody,
        patientId: RequestBody,
        appointmentId: RequestBody,
        reportNumber: RequestBody,
        imageMultipartBody: MultipartBody.Part? = null,
    ) {

        val disposable = apiServiceWithGsonFactory.apiHitForInsertPathologyReport(
            hospitalId,
            patientId,
            appointmentId,
            reportNumber,
            imageMultipartBody
        )
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    Log.d(TAG, ": " + Gson().toJson(response))
                    val gson = Gson()
                    val loginResponseJson = gson.toJson(response)
                    appSharedPref?.loginmodeldata = loginResponseJson

                    navigator.onSuccessReportUpload(response)
                } else {
                    Log.d(TAG, ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorInApi(throwable)
                    Log.d(TAG, ": " + throwable.message)
                }
            })
        compositeDisposable.add(disposable)

    }
}