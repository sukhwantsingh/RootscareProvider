package com.rootscare.serviceprovider.ui.nurses.nursespaymenthistory

import com.rootscare.data.model.response.doctor.payment.PaymentResponse

interface FragmentNursesPaymentHistoryNavigator {

    fun onSuccessPaymentList(response: PaymentResponse)

    fun onThrowable(throwable: Throwable)
}