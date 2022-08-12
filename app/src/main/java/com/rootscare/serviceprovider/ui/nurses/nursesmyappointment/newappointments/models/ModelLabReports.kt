package com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.newappointments.models

import androidx.annotation.Keep
import java.io.File

@Keep
data class ModelLabReports(
    val id: String?,
    val reportUrl: String?,
    val reportName: String?,
    val reportDate: String?
)


@Keep
data class ModelReportUploading(
    val reportFile: File?,
    val reportName: String?
)


interface OnBottomSheetCallbacks {

    fun onPickImage()
    fun onSubmitReports(data: ArrayList<File?>?)
}