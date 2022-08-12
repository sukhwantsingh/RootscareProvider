package com.rootscare.serviceprovider.ui.login.subfragment.forgotpassword

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.rootscare.data.model.request.forgotpassword.forgotpasswordchangerequest.ForgotPasswordChangeRequest
import com.rootscare.data.model.request.forgotpassword.forgotpasswordemailrequest.ForgotPasswordSendEmailRequest
import com.rootscare.data.model.response.forgotpasswordresponse.forgotpasswordchangepassword.ForgotPasswordChangePasswordResponse
import com.rootscare.data.model.response.forgotpasswordresponse.forgotpasswordsendmailresponse.ForgotPasswordSendMailResponse
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentNewForgotPasswordSsBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.login.LoginActivity
import com.rootscare.serviceprovider.ui.login.subfragment.registration.FragmentRegistration
import com.rootscare.serviceprovider.utilitycommon.SUCCESS_CODE

class FragmentForgotPassword : BaseFragment<FragmentNewForgotPasswordSsBinding, FragmentForgotPasswordViewModel>(),
    FragmentForgotPasswordNavigator {
    private var fragmentForgotPasswordBinding: FragmentNewForgotPasswordSsBinding? = null
    private var fragmentForgotPasswordViewModel: FragmentForgotPasswordViewModel? = null
    private var registerEmailId = ""
    private var sendOTP = ""
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_new_forgot_password_ss
    override val viewModel: FragmentForgotPasswordViewModel
        get() {
            fragmentForgotPasswordViewModel = ViewModelProviders.of(this).get(FragmentForgotPasswordViewModel::class.java)
            return fragmentForgotPasswordViewModel as FragmentForgotPasswordViewModel
        }

    companion object {
        val TAG = FragmentForgotPassword::class.java.simpleName
        fun newInstance(): FragmentForgotPassword {
            val args = Bundle()
            val fragment = FragmentForgotPassword()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentForgotPasswordViewModel?.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentForgotPasswordBinding = viewDataBinding
        fragmentForgotPasswordBinding?.llEmailContent?.visibility = View.VISIBLE

        fragmentForgotPasswordBinding?.imageViewBack?.setOnClickListener {
            resetFields()
            (activity as LoginActivity).onBackPressed()
        }

        fragmentForgotPasswordBinding?.btnForgotpasswordSendMail?.setOnClickListener {
          sendMailAPi()
        }
        fragmentForgotPasswordBinding?.txtSendAgain?.setOnClickListener {
          sendAgainMailAPi()
        }
//       fragmentForgotPasswordBinding?.txtPostIssue?.setOnClickListener {
//        showToast("Will be available soon!")
//        }

        fragmentForgotPasswordBinding?.btnForgotpasswordSubmit?.setOnClickListener {
            if (checkFieldValidationForChangePassword()) {
                if (isNetworkConnected) {
                    hideKeyboard()
                    baseActivity?.showLoading()
                    val forgotPasswordChangeRequest = ForgotPasswordChangeRequest()
                    forgotPasswordChangeRequest.emailId = registerEmailId
                    forgotPasswordChangeRequest.code = fragmentForgotPasswordBinding?.firstPinView?.text?.toString()
                    forgotPasswordChangeRequest.password = fragmentForgotPasswordBinding?.edtRootscareForgotPassword?.text?.toString()
                    fragmentForgotPasswordViewModel?.apiforgotchangepassword(forgotPasswordChangeRequest)
                } else {
                    Toast.makeText(activity, getString(R.string.check_network_connection), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
fun resetFields() {
    fragmentForgotPasswordBinding?.edtEmail?.setText("")
    fragmentForgotPasswordBinding?.firstPinView?.setText("")
    fragmentForgotPasswordBinding?.edtRootscareForgotPassword?.setText("")
}
    override fun successForgotPasswordSendMailResponse(forgotPasswordSendMailResponse: ForgotPasswordSendMailResponse?) {
        baseActivity?.hideLoading()
        if (forgotPasswordSendMailResponse?.code.equals(SUCCESS_CODE)) {
            Toast.makeText(activity, forgotPasswordSendMailResponse?.message, Toast.LENGTH_SHORT).show()
            sendOTP = forgotPasswordSendMailResponse?.result!!
            fragmentForgotPasswordBinding?.llEmailContent?.visibility = View.GONE
            fragmentForgotPasswordBinding?.llForgotOtpContain?.visibility = View.VISIBLE
        } else {
            Toast.makeText(activity, forgotPasswordSendMailResponse?.message, Toast.LENGTH_SHORT).show()
            fragmentForgotPasswordBinding?.llEmailContent?.visibility = View.VISIBLE
            fragmentForgotPasswordBinding?.llForgotOtpContain?.visibility = View.GONE
        }
        resetFields()
    }
    private fun sendMailAPi() {
            val emailOrPassword = fragmentForgotPasswordBinding?.edtEmail?.text?.toString()?:""
            if (emailOrPassword.isNotBlank()) {
                val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
                if (checkFieldValidation(emailOrPassword)) {
                    if (isNetworkConnected) {
                        hideKeyboard()
                        baseActivity?.showLoading()
                        registerEmailId = fragmentForgotPasswordBinding?.edtEmail?.text?.toString()?.trim()?:""
                        val req = ForgotPasswordSendEmailRequest()
                        req.emailId = registerEmailId
                        fragmentForgotPasswordViewModel?.apiforgotpasswordemail(req)
                    } else {
                        Toast.makeText(activity, getString(R.string.check_network_connection), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                fragmentForgotPasswordBinding?.edtEmail?.error = "Email can not be empty."
                fragmentForgotPasswordBinding?.edtEmail?.requestFocus()
            }
    }
    private fun sendAgainMailAPi() {
        if (isNetworkConnected) {
            hideKeyboard()
            baseActivity?.showLoading()
            val req = ForgotPasswordSendEmailRequest()
            req.emailId = registerEmailId
            fragmentForgotPasswordViewModel?.apiforgotpasswordemail(req)
        } else {
            showToast(getString(R.string.check_network_connection))
        }
    }

    override fun successForgotPasswordChangePasswordResponse(forgotPasswordChangePasswordResponse: ForgotPasswordChangePasswordResponse?) {
        baseActivity?.hideLoading()
        if (forgotPasswordChangePasswordResponse?.code.equals(SUCCESS_CODE)) {
            Toast.makeText(activity, forgotPasswordChangePasswordResponse?.message, Toast.LENGTH_SHORT).show()
            fragmentForgotPasswordBinding?.firstPinView?.setText("")
            fragmentForgotPasswordBinding?.edtRootscareForgotPassword?.setText("")
            (activity as LoginActivity?)?.setCurrentItem(0, true)
        } else {
            Toast.makeText(activity, forgotPasswordChangePasswordResponse?.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun errorForgotPasswordSendMailResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(FragmentRegistration.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkFieldValidation(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (!email.matches(emailPattern.toRegex())) {
            fragmentForgotPasswordBinding?.edtEmail?.error = getString(R.string.enter_valid_email)
            fragmentForgotPasswordBinding?.edtEmail?.requestFocus()
            return false
        }
        return true
    }

    private fun checkFieldValidationForChangePassword(): Boolean {
        if((fragmentForgotPasswordBinding?.edtRootscareForgotPassword?.text?.toString()?.length ?: 0) < 8) {
            fragmentForgotPasswordBinding?.edtRootscareForgotPassword?.error = getString(R.string.must_be_at_least_8_characters)
            fragmentForgotPasswordBinding?.edtRootscareForgotPassword?.requestFocus()
            return false
        }
        if (fragmentForgotPasswordBinding?.firstPinView?.text.toString().trim() != sendOTP) {
            Toast.makeText(activity, "Please enter correct otp", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}