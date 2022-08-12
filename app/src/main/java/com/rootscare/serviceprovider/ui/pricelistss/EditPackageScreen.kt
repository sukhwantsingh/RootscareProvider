package com.rootscare.serviceprovider.ui.pricelistss

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.gson.JsonObject
import com.rootscare.data.model.response.CommonResponse
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ActivityEditPackageBinding
import com.rootscare.serviceprovider.databinding.ActivityPriceListScreenBinding
import com.rootscare.serviceprovider.ui.base.BaseActivity
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.models.ModelUserProfile
import com.rootscare.serviceprovider.ui.pricelistss.adapter.AdapterLabPackages
import com.rootscare.serviceprovider.ui.pricelistss.adapter.AdapterLabTestsIncluded
import com.rootscare.serviceprovider.ui.pricelistss.fragments.FragmentPackages
import com.rootscare.serviceprovider.ui.pricelistss.fragments.FragmentPriceListCommon
import com.rootscare.serviceprovider.ui.pricelistss.models.ModelPackageDetails
import com.rootscare.serviceprovider.utilitycommon.LoginTypes
import com.rootscare.serviceprovider.utilitycommon.PriceTypes
import com.rootscare.serviceprovider.utilitycommon.SUCCESS_CODE
import com.rootscare.serviceprovider.utilitycommon.getModelFromPref
import com.rootscare.utils.commonadapters.ViewPagerAdapterForTab
import kotlinx.coroutines.delay
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.ArrayList
import kotlin.math.min

class EditPackageScreen : BaseActivity<ActivityEditPackageBinding, ManagePriceViewModel>(),ManagePriceNavigator {
    private var binding: ActivityEditPackageBinding? = null
    private var mViewModel: ManagePriceViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_edit_package
    override val viewModel: ManagePriceViewModel
        get() {
            mViewModel = ViewModelProviders.of(this).get(ManagePriceViewModel::class.java)
            return mViewModel as ManagePriceViewModel
        }
    private var mTestsAdapter: AdapterLabTestsIncluded? = null
    var minPrice = 0
    var maxPrice = 0
    companion object{
        var packId = ""
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = viewDataBinding
        mViewModel?.navigator = this
        mTestsAdapter = AdapterLabTestsIncluded(this)
        binding?.topToolbar?.run {
            tvHeader.text = getString(R.string.edit_packages)
            btnBack.setOnClickListener { finish() }
        }

        initViews()
        fetchPackageDetail()
    }

    private fun initViews() {
        binding?.rvTestsIncluded?.adapter = mTestsAdapter
        binding?.btnSubmit?.setOnClickListener { apiSavePrice() }

    }
    private fun fetchPackageDetail() {
        if (isNetworkConnected) {
            showLoading()
            val jsonObject = JsonObject().apply {
                addProperty("package_id", packId)
                addProperty("user_id", mViewModel?.appSharedPref?.loginUserId)
            }
            val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
            mViewModel?.getLabPackagesDetails(body)
        } else {
            showToast( getString(R.string.check_network_connection))
        }

    }

   private fun apiSavePrice() {
        if (isNetworkConnected) {
            val priceValue = binding?.edtPrice?.text?.toString()?.trim().orEmpty()
            if(priceValue.isBlank()){
                binding?.edtPrice?.error = getString(R.string.provide_price)
                binding?.edtPrice?.requestFocus()
                return
            } else if(priceValue.toInt() < minPrice){
                binding?.edtPrice?.error = "Price must be within $minPrice to $maxPrice price"
                binding?.edtPrice?.requestFocus()
                return
            }
             else if(priceValue.toInt() > maxPrice){
                binding?.edtPrice?.error = "Price must be within $minPrice to $maxPrice price"
                binding?.edtPrice?.requestFocus()
                return
            }


            val jsonObject = JsonObject().apply {
                addProperty("service_type", mViewModel?.appSharedPref?.loginUserType)
                addProperty("user_id", mViewModel?.appSharedPref?.loginUserId)
                addProperty("task_type", PriceTypes.PACKAGE_BASED.getMode())
                addProperty("task_id", packId)
                addProperty("price", priceValue)
            }

            try {
                hideKeyboard(); showLoading()
                val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
                mViewModel?.saveLabTestsPrice(body)
            } catch (e:Exception) {
                print(e)
            }
        } else {
           showToast(getString(R.string.check_network_connection))
        }
    }

    override fun onSuccessPackageDetails(response: ModelPackageDetails?) {
        try {
            hideLoading()
            if (response?.code.equals(SUCCESS_CODE)) {
                response?.result?.let {
                    binding?.run {
                         minPrice = it.minrp?.toInt() ?: 0
                         maxPrice = it.maxrp?.toInt() ?: 0

                        setVariable(BR.node, it)
                        tvhCurrency.text = "${mViewModel?.appSharedPref?.loginmodeldata?.getModelFromPref<ModelUserProfile>()?.result?.currency_symbol}"
                        mTestsAdapter?.submitList(it.task_name?.toMutableList())
                    }

                }
            }

        } catch (e: Exception) {
            hideLoading()
            println(e)
        }
    }
     override fun onSuccessAfterSavePrice(response: CommonResponse) {
        hideLoading()
        if (response.code.equals(SUCCESS_CODE)) {
            showToast(response.message.orEmpty())
            FragmentPackages.NEED_REFRESH = true
        }
    }

     override fun onThrowable(throwable: Throwable) {
        hideLoading()
       showToast(throwable.message?: getString(R.string.something_went_wrong))
    }
}