package com.rootscare.serviceprovider.ui.transactionss

import com.rootscare.data.model.response.CommonResponse
import com.rootscare.data.model.response.loginresponse.BankDetailsResponse
import com.rootscare.serviceprovider.ui.transactionss.models.ModelTransactions
import com.rootscare.serviceprovider.ui.transactionss.models.ModelWithdrawalDetails

interface PaymentTransactionsNavigator {

    fun onSuccessPaymentList(response: ModelTransactions){}

    fun onSuccessWithdrawal(response: ModelWithdrawalDetails){}
    fun onSuccessUpdateBankDetails(response: BankDetailsResponse){}
    fun onSuccressDeleteBankDetails(response: CommonResponse){}
    fun onThrowable(throwable: Throwable)
}