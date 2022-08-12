package com.rootscare.serviceprovider.ui.splash.model


import androidx.annotation.Keep

@Keep
data class NetworkAppCheck(
    val code: String?,
    val message: String?,
    val result: Result?,
    val status: Boolean?
) {
    @Keep
    data class Result(
        val appVer: Int?,
        val helpTitle: String?,
        val helpUrl: String?,
        val rdrTo: String?,
        val rdrUrl: String?,
        val showHelp: Boolean?,
        val skipFlag: Boolean?,
        val skipText: String?,
        val updText: String?,
        val updTitle: String?
    ){
        fun getServerVersion() = appVer ?: 1
    }
}