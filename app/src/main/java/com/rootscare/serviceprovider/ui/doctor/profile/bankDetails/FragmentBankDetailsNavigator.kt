package com.rootscare.serviceprovider.ui.doctor.profile.bankDetails

import com.rootscare.data.model.response.loginresponse.BankDetailsResponse

interface FragmentDoctorBankDetailsNavigator {
    fun onSuccessUpdateBankDetails(response: BankDetailsResponse)
    fun errorInApi(throwable: Throwable?)

}