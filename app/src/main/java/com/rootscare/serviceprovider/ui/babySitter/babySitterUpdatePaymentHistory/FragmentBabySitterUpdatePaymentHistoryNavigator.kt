package com.rootscare.serviceprovider.ui.babySitter.babySitterUpdatePaymentHistory

import com.rootscare.data.model.response.doctor.payment.PaymentResponse

interface FragmentBabySitterUpdatePaymentHistoryNavigator {

    fun onSuccessPaymentList(response: PaymentResponse)

    fun onThrowable(throwable: Throwable)
}