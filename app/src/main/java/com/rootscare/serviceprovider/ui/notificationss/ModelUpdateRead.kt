package com.rootscare.serviceprovider.ui.notificationss


import androidx.annotation.Keep

@Keep
data class ModelUpdateRead(
    val code: String?,
    val message: String?,
    val result: Result?,
    val status: Boolean?
) {
    @Keep
    data class Result(
        val body: String?,
        val date: String?,
        val datetime: String?,
        val id: String?,
        val order_id: String?,
        val read: String?,
        val title: String?,
        val user_id: String?
    )
}