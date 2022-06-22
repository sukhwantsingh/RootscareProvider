package com.rootscare.serviceprovider.ui.nurses.nurseprofile.models


import androidx.annotation.Keep

@Keep
data class ModelHospDeparts(val code: String?, val message: String?,
                            val result: List<Result?>?, val status: Boolean?) {
    @Keep
    data class Result(val id: String?, val title: String?)
}