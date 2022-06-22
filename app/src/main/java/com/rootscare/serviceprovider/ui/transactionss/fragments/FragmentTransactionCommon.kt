package com.rootscare.serviceprovider.ui.transactionss.fragments

import android.os.Bundle
import android.view.View
import androidx.core.text.parseAsHtml
import androidx.lifecycle.ViewModelProviders
import com.google.gson.JsonObject
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentPaymentTransactionCommonBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.models.ModelUserProfile
import com.rootscare.serviceprovider.ui.transactionss.PaymentTransactionsNavigator
import com.rootscare.serviceprovider.ui.transactionss.PaymentTransactionsViewModel
import com.rootscare.serviceprovider.ui.transactionss.adapters.AdapterPayTransactions
import com.rootscare.serviceprovider.ui.transactionss.models.ModelTransactions
import com.rootscare.serviceprovider.utilitycommon.SUCCESS_CODE
import com.rootscare.serviceprovider.utilitycommon.getModelFromPref
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class FragmentTransactionCommon : BaseFragment<FragmentPaymentTransactionCommonBinding, PaymentTransactionsViewModel>(),
    PaymentTransactionsNavigator {

    private var priceType: String? = null
    private var binding: FragmentPaymentTransactionCommonBinding? = null

    private var mViewModel: PaymentTransactionsViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_payment_transaction_common

    override val viewModel: PaymentTransactionsViewModel
        get() {
            mViewModel = ViewModelProviders.of(this).get(PaymentTransactionsViewModel::class.java)
            return mViewModel as PaymentTransactionsViewModel
        }

    private var mAdapterPayHistory: AdapterPayTransactions? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdapterPayHistory = activity?.let { AdapterPayTransactions(it) }
        mViewModel?.navigator = this
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String = "", param2: String = "") =
            FragmentTransactionCommon().apply {
                arguments = Bundle().apply {
                    //   putString(ARG_PRICE_TYPE, priceType_)
                }
            }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = viewDataBinding
        initViews()
        fetchTransactions()
    }


    private fun initViews() {
        binding?.tvhProviderFee?.text = "${getString(R.string.fee)}(${mViewModel?.appSharedPref?.loginmodeldata?.getModelFromPref<ModelUserProfile>()?.result?.currency_symbol})"
        binding?.tvhAdminFee?.text =  "${getString(R.string.fee)}(${mViewModel?.appSharedPref?.loginmodeldata?.getModelFromPref<ModelUserProfile>()?.result?.currency_symbol})"

        binding?.rvPaymentHistory?.adapter = mAdapterPayHistory
        binding?.ibcross?.setOnClickListener { binding?.cnsChrgeFees?.visibility = View.GONE }
    }

    private fun fetchTransactions() {
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            val jsonObject = JsonObject().apply {
                addProperty("service_type", mViewModel?.appSharedPref?.loginUserType)
                addProperty("user_id", mViewModel?.appSharedPref?.loginUserId)
            }
            val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
            mViewModel?.getPaymentHistoryFromApi(body)
        } else {
            noData(getString(R.string.check_network_connection))
        }
    }

    override fun onSuccessPaymentList(response: ModelTransactions) {
        try {
            if (response.code.equals(SUCCESS_CODE)) {
                response.result?.let {
                    binding?.run {
                        if (null != it.content) {
                            cnsChrgeFees.visibility = View.VISIBLE
                            tvhFeesCharge.text = it.content.heading
                            tvshFeesCharges.text = it.content.content?.parseAsHtml()
                        }

                        if (it.paymentdetails.isNullOrEmpty().not()) {
                            tvNoDate.visibility = View.GONE
                            rvPaymentHistory.visibility = View.VISIBLE

                            mAdapterPayHistory?.submitList(it.paymentdetails?.toMutableList())
                        } else noData(response.message)
                    }
                } ?: run { noData(response.message) }
            } else {
                noData(response.message)
            }

            baseActivity?.hideLoading()
        } catch (e: Exception) {
            baseActivity?.hideLoading()
            noData(e.message)
        }

    }

    private fun noData(message: String?) {
        binding?.run {
            tvNoDate.visibility = View.VISIBLE
            rvPaymentHistory.visibility = View.GONE
            tvNoDate.text = message ?: getString(R.string.something_went_wrong)
        }
    }

    override fun onThrowable(throwable: Throwable) {
        baseActivity?.hideLoading()
        noData(throwable.message)
    }

}