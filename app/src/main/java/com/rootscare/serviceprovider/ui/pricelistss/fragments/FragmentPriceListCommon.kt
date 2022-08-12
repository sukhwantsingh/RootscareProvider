package com.rootscare.serviceprovider.ui.pricelistss.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.google.gson.JsonObject
import com.rootscare.data.model.response.CommonResponse
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.pricelistss.ManagePriceNavigator
import com.rootscare.serviceprovider.ui.pricelistss.ManagePriceViewModel
import com.rootscare.serviceprovider.ui.pricelistss.adapter.AdapterPriceListCommon
import com.rootscare.serviceprovider.utilitycommon.PriceTypes
import com.rootscare.serviceprovider.utilitycommon.SUCCESS_CODE
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import com.rootscare.serviceprovider.databinding.FragmentPriceListCommonBinding
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.models.ModelUserProfile
import com.rootscare.serviceprovider.ui.pricelistss.models.ModelPriceListing
import com.rootscare.serviceprovider.utilitycommon.getModelFromPref
import java.util.*

const val ARG_PRICE_TYPE = "ARG_PRICE_TYPE"

class FragmentPriceListCommon : BaseFragment<FragmentPriceListCommonBinding, ManagePriceViewModel>(), ManagePriceNavigator {

    private var priceType: String? = null
    private var binding: FragmentPriceListCommonBinding? = null

    private var mViewModel: ManagePriceViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_price_list_common

    override val viewModel: ManagePriceViewModel
        get() {
            mViewModel = ViewModelProviders.of(this).get(ManagePriceViewModel::class.java)
            return mViewModel as ManagePriceViewModel
        }


    private var mPriceAdapter: AdapterPriceListCommon? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPriceAdapter = activity?.let { AdapterPriceListCommon(it) }
        mViewModel?.navigator = this
        arguments?.let {
            priceType = it.getString(ARG_PRICE_TYPE) ?: ""
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(priceType_: String = "", param2: String = "") =
            FragmentPriceListCommon().apply {
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
        binding?.tvRate?.text = "${getString(R.string.rate_)} (${mViewModel?.appSharedPref?.loginmodeldata?.getModelFromPref<ModelUserProfile>()?.result?.currency_symbol})"
        binding?.rvPricelist?.adapter = mPriceAdapter
        binding?.btnSubmit?.setOnClickListener { apiSavePriceList() }
        when {
                priceType.equals(PriceTypes.TASK_BASED.get(), true) -> {
                    binding?.run {
                        tvBookingDurationHourly.visibility = View.GONE
                        rd30minSlots.visibility = View.VISIBLE
                        tvhlt.text = getString(R.string.list_of_tasks)
                        tvh1.text = getString(R.string.tasks_you_as_wish_service_text)
                    }
                    fetchTasksApi(PriceTypes.TASK_BASED.getMode())
                }
                priceType.equals(PriceTypes.HOURLY_BASED.get(), true) -> {
                    binding?.run {
                        tvBookingDurationHourly.visibility = View.VISIBLE
                        rd30minSlots.visibility = View.GONE
                        tvhlt.text = getString(R.string.hourly_slot)
                        tvh1.text = getString(R.string.hours_you_as_wish_service_text)
                    }
                    fetchTasksApi(PriceTypes.HOURLY_BASED.getMode())
                }
                priceType.equals(PriceTypes.ONLINE.get(), true) -> {
                    binding?.run {
                        tvh1.text = getString(R.string.switch_to_enable_task)
                        tvBookingDurationHourly.visibility = View.GONE
                        rd30minSlots.visibility = View.VISIBLE

                        rd30minSlots.text = getString(R.string.min_15_slots)
                        tvhlt.text = getString(R.string.online_cons)
                    }
                    fetchTasksApiForDoctor(PriceTypes.ONLINE.getMode())
                }
                priceType.equals(PriceTypes.HOME_VISIT.get(), true) -> {
                    binding?.run {
                        tvh1.text = getString(R.string.switch_to_enable_task)
                        tvBookingDurationHourly.visibility = View.GONE
                        rd30minSlots.visibility = View.VISIBLE

                        rd30minSlots.text = getString(R.string.min_45_slots)
                        tvhlt.text = getString(R.string.home_visit_cons)
                    }
                    fetchTasksApiForDoctor(PriceTypes.HOME_VISIT.getMode())
                }

                priceType.equals(PriceTypes.TEST_BASED.get(), true) -> {
                binding?.run {
                    tvBookingDurationHourly.visibility = View.GONE
                    rd30minSlots.visibility = View.VISIBLE

                    rd30minSlots.text = getString(R.string.min_45_slots)
                    tvhlt.text = getString(R.string.list_of_tasks)
                    tvh1.text = getString(R.string.tasks_you_as_wish_service_text)
                }
                 fetchLabTestsApi(PriceTypes.TEST_BASED.getMode())
            }

            }

    }

    private fun fetchTasksApiForDoctor(taskType: String) {
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            val jsonObject = JsonObject().apply {
                addProperty("service_type", mViewModel?.appSharedPref?.loginUserType)
                addProperty("user_id", mViewModel?.appSharedPref?.loginUserId)
                addProperty("task_type", taskType)
            }
            val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
            mViewModel?.getTasksListForDoctor(body)
        } else {
            noData(getString(R.string.check_network_connection))
        }

    }

    private fun fetchTasksApi(taskType: String) {
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            val jsonObject = JsonObject().apply {
                addProperty("service_type", mViewModel?.appSharedPref?.loginUserType)
                addProperty("user_id", mViewModel?.appSharedPref?.loginUserId)
                addProperty("task_type", taskType)
            }
            val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
            mViewModel?.getTasksList(body)
        } else {
            noData(getString(R.string.check_network_connection))
        }
    }

    private fun fetchLabTestsApi(taskType: String) {
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            val jsonObject = JsonObject().apply {
                addProperty("service_type", mViewModel?.appSharedPref?.loginUserType)
                addProperty("user_id", mViewModel?.appSharedPref?.loginUserId)
                addProperty("task_type", taskType)
            }
            val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
            mViewModel?.getLabTestsList(body)
        } else {
            noData(getString(R.string.check_network_connection))
        }
    }

    private fun apiSavePriceList() {
        if (isNetworkConnected) {
            val mList = mPriceAdapter?.getUpdatedList()
            //    Log.wtf("Data", "apiHitSendSchedule: $mList")

            mList?.forEach {
                if (it?.isChecked == true && it.price.isNullOrBlank()) {
                    showToast(getString(R.string.slot_must_have_valid_price))
                    return
                } else if (it?.isChecked == true && it.price?.toInt() == 0) {
                    showToast(getString(R.string.slot_must_have_valid_price))
                    return
                } else Unit
            }

            baseActivity?.hideKeyboard()
            val taskIdList: MutableList<String> = ArrayList()
            val taskPriceList: MutableList<String> = ArrayList()
            mList?.let {
                for (ri in it) {
                    if (ri?.isChecked == true && ri.price.isNullOrBlank().not() && ri.price?.toInt() != 0) {
                        taskIdList.add(ri.id ?: "")
                        taskPriceList.add(ri.price ?: "")
                    }
                }
            }

            val tasksId: String = if (taskIdList.isNullOrEmpty()) "" else taskIdList.joinToString(separator = ",")
            val tasksPrice: String = if (taskPriceList.isNullOrEmpty()) "" else taskPriceList.joinToString(separator = ",")

            val jsonObject = JsonObject().apply {
                addProperty("service_type", mViewModel?.appSharedPref?.loginUserType)
                addProperty("user_id", mViewModel?.appSharedPref?.loginUserId)
                addProperty("task_id", tasksId)
                addProperty("price", tasksPrice)
            }

            when {
                priceType.equals(PriceTypes.TEST_BASED.get(), true) -> {
                    jsonObject.addProperty("task_type", PriceTypes.TEST_BASED.getMode())
                    fireSavePrice(jsonObject, PriceTypes.TEST_BASED.getMode())
                }
                priceType.equals(PriceTypes.TASK_BASED.get(), true) -> {
                    jsonObject.addProperty("task_type", PriceTypes.TASK_BASED.getMode())
                    fireSavePrice(jsonObject)
                }
                priceType.equals(PriceTypes.HOURLY_BASED.get(), true) -> {
                    jsonObject.addProperty("task_type", PriceTypes.HOURLY_BASED.getMode())
                    fireSavePrice(jsonObject)
                }
                priceType.equals(PriceTypes.ONLINE.get(), true) -> {
                    jsonObject.addProperty("task_type", PriceTypes.ONLINE.getMode())
                    fireSavePrice(jsonObject, PriceTypes.ONLINE.getMode())
                }
                priceType.equals(PriceTypes.HOME_VISIT.get(), true) -> {
                    jsonObject.addProperty("task_type", PriceTypes.HOME_VISIT.getMode())
                    fireSavePrice(jsonObject, PriceTypes.ONLINE.getMode())
                }
                else ->{ baseActivity?.showLoading(); return }
            }
        } else {
            noData(getString(R.string.check_network_connection))
        }
    }

   private fun fireSavePrice(jsonObject: JsonObject, hitFor:String = "") {
      try {
          baseActivity?.showLoading()
          val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())
          if(hitFor.equals(PriceTypes.ONLINE.getMode(), ignoreCase = true)) {
              mViewModel?.saveTaskSlotForDoc(body)
          } else if(hitFor.equals(PriceTypes.TEST_BASED.getMode(), ignoreCase = true)) {
              mViewModel?.saveLabTestsPrice(body)
          }
          else mViewModel?.saveTaskSlot(body)

      } catch (e:Exception) {
       print(e)
      }
   }

    override fun onSuccessAfterSavePrice(response: CommonResponse) {
        baseActivity?.hideLoading()
        if (response.code.equals(SUCCESS_CODE)) {
            showToast(response.message.orEmpty())
        }
    }

    override fun onSuccessAfterGetTaskPrice(taskList: ModelPriceListing) {
        try {
            if (taskList.code.equals(SUCCESS_CODE)) {
                taskList.result?.let {
                    if (it.isNullOrEmpty().not()) {
                        binding?.run {
                            tvNoDate.visibility = View.GONE
                            rvPricelist.visibility = View.VISIBLE
                        }
                        mPriceAdapter?.updatedArrayList?.addAll(it)
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
            rvPricelist.visibility = View.GONE
            tvNoDate.text = message ?: getString(R.string.something_went_wrong)
        }
    }


    override fun onThrowable(throwable: Throwable) {
        baseActivity?.hideLoading()
        noData(throwable.message)
    }

}