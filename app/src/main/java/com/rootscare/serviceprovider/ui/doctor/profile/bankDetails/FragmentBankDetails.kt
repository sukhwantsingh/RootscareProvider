package com.rootscare.serviceprovider.ui.doctor.profile.bankDetails

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.rootscare.data.model.response.loginresponse.BankDetailsResponse
import com.rootscare.data.model.response.loginresponse.Result
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentBankDetailsBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.login.subfragment.login.FragmentLogin
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class FragmentBankDetails : BaseFragment<FragmentBankDetailsBinding, FragmentBankDetailsViewModel>(),
    FragmentDoctorBankDetailsNavigator {

    private var fragmentBankDetailsBinding: FragmentBankDetailsBinding? = null
    private var fragmentBankDetailsViewModel: FragmentBankDetailsViewModel? = null
    private var bankId: String = ""
    private lateinit var loginResponse: Result
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_bank_details
    override val viewModel: FragmentBankDetailsViewModel
        get() {
            fragmentBankDetailsViewModel =
                ViewModelProviders.of(this).get(FragmentBankDetailsViewModel::class.java)
            return fragmentBankDetailsViewModel as FragmentBankDetailsViewModel
        }

    companion object {
        fun newInstance(): FragmentBankDetails {
            val args = Bundle()
            val fragment = FragmentBankDetails()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentBankDetailsViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentBankDetailsBinding = viewDataBinding
        loginResponse = Gson().fromJson(fragmentBankDetailsViewModel?.appSharedPref?.loggedInDataForNurseAfterLogin!!, Result::class.java)
        println("loginResponse $loginResponse")
        if (loginResponse.bankDetails != null && loginResponse.bankDetails!!.size > 0) {
            fragmentBankDetailsBinding?.etBankName?.setText(loginResponse.bankDetails?.get(0)?.bankName.toString())
            fragmentBankDetailsBinding?.etUserName?.setText(loginResponse.bankDetails?.get(0)?.yourBankName.toString())
            fragmentBankDetailsBinding?.etAccountNumber?.setText(loginResponse.bankDetails?.get(0)?.accountNo.toString())
            fragmentBankDetailsBinding?.etIranNumber?.setText(loginResponse.bankDetails?.get(0)?.ibanNo.toString())
            fragmentBankDetailsBinding?.etSwiftNumber?.setText(loginResponse.bankDetails?.get(0)?.swiftNo.toString())
            fragmentBankDetailsBinding?.etMessage?.setText(loginResponse.bankDetails?.get(0)?.message.toString())
            bankId = loginResponse.bankDetails?.get(0)?.id.toString()
        }

        fragmentBankDetailsBinding?.btnSave?.setOnClickListener {
            with(fragmentBankDetailsBinding!!) {
                if (fragmentBankDetailsViewModel?.appSharedPref?.loginUserId != null && checkValidation()) {
                    baseActivity?.showLoading()
                    baseActivity?.hideKeyboard()

                    val userId =
                        fragmentBankDetailsViewModel?.appSharedPref?.loginUserId!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    val id = bankId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    val bankName = etBankName.text?.trim().toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    val yourName = etUserName.text?.trim().toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    val accountNo = etAccountNumber.text?.trim().toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    val iranNo = etIranNumber.text?.trim().toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    val swiftNo = etSwiftNumber.text?.trim().toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    val message = etMessage.text?.trim().toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

                    fragmentBankDetailsViewModel?.updateBankDetails(userId, id, bankName, yourName, accountNo, iranNo, swiftNo, message)
                }
            }
        }
    }

    //Start of All field Validation
    private fun checkValidation(): Boolean {
        if (TextUtils.isEmpty(fragmentBankDetailsBinding?.etBankName?.text?.toString()?.trim())) {
            Toast.makeText(activity, "Please enter Bank name!", Toast.LENGTH_SHORT).show()
            return false
        } else if (TextUtils.isEmpty(fragmentBankDetailsBinding?.etUserName?.text?.toString()?.trim())) {
            Toast.makeText(activity, "Please enter your name!", Toast.LENGTH_SHORT).show()
            return false
        } else if (TextUtils.isEmpty(fragmentBankDetailsBinding?.etAccountNumber?.text?.toString()?.trim())) {
            Toast.makeText(activity, "Please enter your Account Number!", Toast.LENGTH_SHORT).show()
            return false
        } else if (TextUtils.isEmpty(fragmentBankDetailsBinding?.etIranNumber?.text?.toString()?.trim())) {
            Toast.makeText(activity, "Please enter your IRAN number!", Toast.LENGTH_SHORT).show()
            return false
        } else if (!fragmentBankDetailsBinding?.cbInformation!!.isChecked) {
            Toast.makeText(activity, "Please tick the check box to continue", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }

    override fun onSuccessUpdateBankDetails(response: BankDetailsResponse) {
        baseActivity?.hideLoading()
        if (response.code.equals("200")) {
//            response.result?.let { loginResponse.result?.bankDetails?.set(0, it) }
            Toast.makeText(requireActivity(), response.message, Toast.LENGTH_SHORT).show()
            baseActivity?.onBackPressed()
        } else {
            Toast.makeText(requireActivity(), response.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun errorInApi(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(FragmentLogin.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }
}