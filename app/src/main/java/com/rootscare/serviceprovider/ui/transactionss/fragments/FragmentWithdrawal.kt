package com.rootscare.serviceprovider.ui.transactionss.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.dialog.CommonDialog
import com.google.gson.JsonObject
import com.rootscare.data.model.response.CommonResponse
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentWithdrawalBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.models.ModelUserProfile
import com.rootscare.serviceprovider.ui.transactionss.AddBankDetailCommon
import com.rootscare.serviceprovider.ui.transactionss.PaymentTransactionsNavigator
import com.rootscare.serviceprovider.ui.transactionss.PaymentTransactionsViewModel
import com.rootscare.serviceprovider.ui.transactionss.adapters.AdapterWithdrawal
import com.rootscare.serviceprovider.ui.transactionss.models.ModelWithdrawalDetails
import com.rootscare.serviceprovider.utilitycommon.SUCCESS_CODE
import com.rootscare.serviceprovider.utilitycommon.getModelFromPref
import com.rootscare.serviceprovider.utilitycommon.navigate
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class FragmentWithdrawal : BaseFragment<FragmentWithdrawalBinding, PaymentTransactionsViewModel>(),
    PaymentTransactionsNavigator {

    private var binding: FragmentWithdrawalBinding? = null

    private var mViewModel: PaymentTransactionsViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_withdrawal

    override val viewModel: PaymentTransactionsViewModel
        get() {
            mViewModel = ViewModelProviders.of(this).get(PaymentTransactionsViewModel::class.java)
            return mViewModel as PaymentTransactionsViewModel
        }


    private var mAdapterWithdrawal: AdapterWithdrawal? = null
    private var hospId : String? = null


    companion object {
        @JvmStatic
        fun newInstance() = FragmentWithdrawal()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdapterWithdrawal = activity?.let { AdapterWithdrawal(it) }
        mViewModel?.navigator = this
        hospId = mViewModel?.appSharedPref?.loginmodeldata?.getModelFromPref<ModelUserProfile>()?.result?.hospital_id

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = viewDataBinding
        initViews()
        fetchWithdrawalInfoApi()
    }

    private fun initViews() {
        binding?.rvWithdrawal?.adapter = mAdapterWithdrawal
        binding?.tvhAddBank?.setOnClickListener { navigate<AddBankDetailCommon>() }
        binding?.ibcross?.setOnClickListener { delDialog() }
    }

    private fun deleteBkDetails() {
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            val jsonObject = JsonObject().apply {
                addProperty("user_id", mViewModel?.appSharedPref?.loginUserId)
            }
            val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
            mViewModel?.deleteBankDetails(body)
        } else {
            noData(getString(R.string.check_network_connection))
        }
    }


    private fun fetchWithdrawalInfoApi() {
        if (isNetworkConnected) {
            val jsonObject = JsonObject().apply {
                addProperty("service_type", mViewModel?.appSharedPref?.loginUserType)
                addProperty("user_id", mViewModel?.appSharedPref?.loginUserId)
            }
            val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
            mViewModel?.getWithdrawalDetail(body)
        } else {
            noData(getString(R.string.check_network_connection))
        }
    }

    override fun onSuccessWithdrawal(response: ModelWithdrawalDetails) {
        try {
            if (response.code.equals(SUCCESS_CODE)) {
                response.result?.let {
                    binding?.run {
                        tvhPrice.text = it.abal ?: "NA"

                        if(hospId.isNullOrBlank()){
                            tvhInfo.text = it.content?.heading ?: ""
                            tvshInfo.text = it.content?.content ?: ""

                            if (null != it.bankdetails) {
                                gpAddBank.visibility = View.GONE
                                cnsCba.visibility = View.VISIBLE

                                it.bankdetails.run {
                                    tvBankName.text = bank_name
                                    tvAcName.text = acname
                                    tvAcNum.text = account_no
                                    tvAddress.text = address
                                }
                            } else {
                                gpAddBank.visibility = View.VISIBLE
                                cnsCba.visibility = View.GONE
                            }

                        } else {
                            tvhInfo.visibility = View.GONE
                            tvshInfo.visibility = View.GONE

                            gpAddBank.visibility = View.GONE
                            cnsCba.visibility = View.GONE
                        }
                    }
                    if (it.withdrawal.isNullOrEmpty().not()) {
                        binding?.run {
                            tvNoDate.visibility = View.GONE
                            rvWithdrawal.visibility = View.VISIBLE
                        }
                        mAdapterWithdrawal?.submitList(it.withdrawal)
                    } else noData(response.message)
                } ?: run { noData(null) }
            } else {
                noData(response.message)
            }

            baseActivity?.hideLoading()
        } catch (e: Exception) {
            baseActivity?.hideLoading()
            noData(e.message)
        }

    }

    override fun onSuccressDeleteBankDetails(response: CommonResponse) {
        try {
            showToast(response.message ?: "")
            binding?.run {
                cnsCba.visibility = View.GONE
                gpAddBank.visibility = View.VISIBLE
            }

            baseActivity?.hideLoading()
        } catch (e: Exception) {
            baseActivity?.hideLoading()
            showToast(e.message ?: "")
        }

    }

    private fun noData(message: String?) {
        binding?.run {
            tvNoDate.visibility = View.VISIBLE
            rvWithdrawal.visibility = View.GONE
            tvNoDate.text = message ?: getString(R.string.something_went_wrong)
        }
    }

    override fun onThrowable(throwable: Throwable) {
        baseActivity?.hideLoading()
        noData(throwable.message)
    }

    fun refreshFragment() {
        baseActivity?.showLoading()
        fetchWithdrawalInfoApi()
    }

    private fun delDialog() {
        CommonDialog.showDialog(requireActivity(), object :
            DialogClickCallback {
            override fun onDismiss() {
            }

            override fun onConfirm() {
                deleteBkDetails()
            }

        }, "Bank detail", "Do you really want to delete?")
    }
}