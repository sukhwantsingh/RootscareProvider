package com.rootscare.serviceprovider.ui.hospital.hospitalmanagenotification

import com.rootscare.data.model.response.hospital.NotificationResponse
import com.rootscare.serviceprovider.ui.notificationss.ModelUpdateRead

interface FragmentHospitalManageNotificationNavigator {
    fun successNotificationListResponse(notificationResponse: NotificationResponse?)
    fun successUpdateRead(response: ModelUpdateRead?){}
    fun errorPatientPaymentHistoryResponse(throwable: Throwable?)
}