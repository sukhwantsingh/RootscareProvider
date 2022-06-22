package com.rootscare.serviceprovider.ui.transactionss

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.rootscare.data.model.response.loginresponse.BankDetailsResponse
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.LayoutAddBankNewBinding
import com.rootscare.serviceprovider.ui.base.BaseActivity
import com.rootscare.serviceprovider.utilitycommon.SUCCESS_CODE
import com.rootscare.utils.commonadapters.ViewPagerAdapterForTab
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class AddBankDetailCommon : BaseActivity<LayoutAddBankNewBinding, PaymentTransactionsViewModel>(), PaymentTransactionsNavigator {
    private var binding: LayoutAddBankNewBinding? = null
    private var mViewModel: PaymentTransactionsViewModel? = null

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.layout_add_bank_new
    override val viewModel: PaymentTransactionsViewModel
        get() {
            mViewModel = ViewModelProviders.of(this).get(PaymentTransactionsViewModel::class.java)
            return mViewModel as PaymentTransactionsViewModel
        }
    lateinit var adapter: ViewPagerAdapterForTab
    private var bankId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = viewDataBinding
        mViewModel?.navigator = this
        binding?.topToolbar?.run {
            tvHeader.text = getString(R.string.add_bank)
            btnBack.setOnClickListener { onBackPressed() }
        }

        initView()
    }

    private fun initView() {
        binding?.btnSubmit?.setOnClickListener {
            addBankApi()
        }
    }


    private fun addBankApi() {
        binding?.run {
            if (mViewModel?.appSharedPref?.loginUserId != null && checkValidation()) {
                showLoading(); hideKeyboard()

                val userId = mViewModel?.appSharedPref?.loginUserId!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val id = bankId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val bankName = edtBn.text?.trim().toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val yourName = edtRegAcn.text?.trim().toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val accountNo = edtAccNum.text?.trim().toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val iranNo = edtIbnCode.text?.trim().toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val swiftNo = edtSwiftCode.text?.trim().toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val message = edtAddress.text?.trim().toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

                mViewModel?.updateBankDetails(userId, id, bankName, yourName, accountNo, iranNo, swiftNo, message)
            }
        }
    }

    //Start of All field Validation
    private fun checkValidation(): Boolean {
        if (TextUtils.isEmpty(binding?.edtBn?.text?.toString()?.trim())) {
            Toast.makeText(this, "Please enter Bank name!", Toast.LENGTH_SHORT).show()
            return false
        } else if (TextUtils.isEmpty(binding?.edtRegAcn?.text?.toString()?.trim())) {
            Toast.makeText(this, "Please enter your name!", Toast.LENGTH_SHORT).show()
            return false
        } else if (TextUtils.isEmpty(binding?.edtAccNum?.text?.toString()?.trim())) {
            Toast.makeText(this, "Please enter your Account Number!", Toast.LENGTH_SHORT).show()
            return false
        } else if (TextUtils.isEmpty(binding?.edtIbnCode?.text?.toString()?.trim())) {
            Toast.makeText(this, "Please enter your IBN number!", Toast.LENGTH_SHORT).show()
            return false
        } else if (!binding?.cbAgree!!.isChecked) {
            Toast.makeText(this, "Please tick the check box to continue", Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
    }

    override fun onSuccessUpdateBankDetails(response: BankDetailsResponse) {
        hideLoading()
        showToast(response.message ?: "")
        if (response.code.equals(SUCCESS_CODE)){
            TransactionsMore.isNeedToRefreshWithdrawable = true
            onBackPressed()
        }

    }

    override fun onThrowable(throwable: Throwable) {
        hideLoading()
        showToast(throwable.message ?: "")
    }




}