package com.rootscare.serviceprovider.ui.doctor.doctorpaymenthistory

import com.rootscare.data.model.response.doctor.payment.PaymentResponse

interface FragmentDoctorPaymentHistoryNavigator {
    fun onSuccessPaymentList(response: PaymentResponse)

    fun onThrowable(throwable: Throwable)


}