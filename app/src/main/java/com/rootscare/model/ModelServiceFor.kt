package com.rootscare.model


import androidx.annotation.Keep

@Keep
data class ModelServiceFor(
    val code: String?,
    val message: String?,
    val result: List<Result?>?,
    val status: Boolean?
) {
    @Keep
    data class Result(
        val country_code: String?,
        val country_short_code: String?,
        val name: String?
    )
}