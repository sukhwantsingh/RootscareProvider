package com.rootscare.serviceprovider.ui.caregiver.caregiverupdatepaymenthistory

import com.rootscare.data.model.response.doctor.payment.PaymentResponse

interface FragmentCaregiverUpdatePaymentHistoryNavigator {

    fun onSuccessPaymentList(response: PaymentResponse)

    fun onThrowable(throwable: Throwable)
}