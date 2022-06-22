package com.rootscare.serviceprovider.ui.login.subfragment.forgotpassword

import com.rootscare.data.model.response.forgotpasswordresponse.forgotpasswordchangepassword.ForgotPasswordChangePasswordResponse
import com.rootscare.data.model.response.forgotpasswordresponse.forgotpasswordsendmailresponse.ForgotPasswordSendMailResponse

interface FragmentForgotPasswordNavigator {

    fun successForgotPasswordSendMailResponse(forgotPasswordSendMailResponse: ForgotPasswordSendMailResponse?)
    fun successForgotPasswordChangePasswordResponse(forgotPasswordChangePasswordResponse: ForgotPasswordChangePasswordResponse?)
    fun errorForgotPasswordSendMailResponse(throwable: Throwable?)
}