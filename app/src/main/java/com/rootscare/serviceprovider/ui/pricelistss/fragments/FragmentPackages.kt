package com.rootscare.serviceprovider.ui.pricelistss.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.google.gson.JsonObject
import com.rootscare.data.model.response.CommonResponse
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentLabPackagesBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.pricelistss.EditPackageScreen
import com.rootscare.serviceprovider.ui.pricelistss.ManagePriceNavigator
import com.rootscare.serviceprovider.ui.pricelistss.ManagePriceViewModel
import com.rootscare.serviceprovider.utilitycommon.PriceTypes
import com.rootscare.serviceprovider.utilitycommon.SUCCESS_CODE
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import com.rootscare.serviceprovider.ui.pricelistss.adapter.AdapterLabPackages
import com.rootscare.serviceprovider.ui.pricelistss.adapter.OnLabPackageCallback
import com.rootscare.serviceprovider.ui.pricelistss.models.ModelPackages
import com.rootscare.serviceprovider.utilitycommon.navigate
import java.util.*

class FragmentPackages : BaseFragment<FragmentLabPackagesBinding, ManagePriceViewModel>(), ManagePriceNavigator {

    private var priceType: String? = null
    private var binding: FragmentLabPackagesBinding? = null

    private var mViewModel: ManagePriceViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_lab_packages

     override val viewModel: ManagePriceViewModel
        get() {
            mViewModel = ViewModelProviders.of(this).get(ManagePriceViewModel::class.java)
            return mViewModel as ManagePriceViewModel
        }
    private var mPriceAdapter: AdapterLabPackages? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPriceAdapter = activity?.let { AdapterLabPackages(it) }
        mViewModel?.navigator = this
        arguments?.let {
            priceType = it.getString(ARG_PRICE_TYPE) ?: ""
        }
    }

    companion object {
        var NEED_REFRESH: Boolean? = null
        @JvmStatic
        fun newInstance(priceType_: String = "") =
            FragmentPackages().apply {
                arguments = Bundle().apply {
                    putString(ARG_PRICE_TYPE, priceType_)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = viewDataBinding
        initViews()
    }

    private fun initViews() {
        binding?.rvPackages?.adapter = mPriceAdapter
        mPriceAdapter?.mCallback = object :OnLabPackageCallback{
            override fun onLabPackage(packId: String?) {
              EditPackageScreen.packId = packId.orEmpty()
              navigate<EditPackageScreen>()
            }

            override fun onLabDisablePackage(packId: String?) {
                disablePackage(packId)
            }
        }
        fetchPackages()
    }

    override fun onResume() {
        super.onResume()
        if (true == NEED_REFRESH){
            fetchPackages()
            NEED_REFRESH = null
        }
    }

    private fun fetchPackages() {
        if (isNetworkConnected) {
            val jsonObject = JsonObject().apply {
                addProperty("service_type", mViewModel?.appSharedPref?.loginUserType)
                addProperty("user_id", mViewModel?.appSharedPref?.loginUserId)
                addProperty("task_type", PriceTypes.PACKAGE_BASED.getMode())
            }
            val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
            baseActivity?.showLoading()
            mViewModel?.getLabPackagesList(body)
        } else {
            noData(getString(R.string.check_network_connection))
        }

    }

    private fun disablePackage(packId: String?) {
        if (isNetworkConnected) {
            val jsonObject = JsonObject().apply {
                addProperty("service_type", mViewModel?.appSharedPref?.loginUserType)
                addProperty("user_id", mViewModel?.appSharedPref?.loginUserId)
                addProperty("package_id", packId)
            }
            try {
                baseActivity?.hideKeyboard()
                baseActivity?.showLoading()
                val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
                mViewModel?.disableLabPackage(body)

            } catch (e:Exception) {
                print(e)
            }
        } else {
            noData(getString(R.string.check_network_connection))
        }
    }

    override fun onSuccessAfterSavePrice(response: CommonResponse) {
        baseActivity?.hideLoading()
        if (response.code.equals(SUCCESS_CODE)) {
            showToast(response.message.orEmpty())
            fetchPackages()
        }
    }

    override fun onSuccessAfterGetTaskPrice(taskList: ModelPackages) {
        try {
            if (taskList.code.equals(SUCCESS_CODE)) {
                taskList.result?.let {
                    if (it.isNullOrEmpty().not()) {
                        binding?.run {
                            tvNoDate.visibility = View.GONE
                            rvPackages.visibility = View.VISIBLE
                        }
                        mPriceAdapter?.submitList(null)
                       mPriceAdapter?.submitList(it.toMutableList())
                    } else noData(taskList.message)
                } ?: run { noData(taskList.message) }
            } else noData(taskList.message)

            baseActivity?.hideLoading()
        } catch (e: Exception) {
            baseActivity?.hideLoading()
            println(e)
        }

    }

    private fun noData(message: String?) {
        binding?.run {
            tvNoDate.visibility = View.VISIBLE
            rvPackages.visibility = View.GONE
            tvNoDate.text = message ?: getString(R.string.something_went_wrong)
        }
    }


    override fun onThrowable(throwable: Throwable) {
        baseActivity?.hideLoading()
        noData(throwable.message)
    }

}