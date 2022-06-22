package com.rootscare.serviceprovider.ui.nurses.nursesmyschedule.subfragment.manageschedule

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.rootscare.data.model.response.loginresponse.LoginResponse
import com.rootscare.data.model.response.loginresponse.Result
import com.rootscare.data.model.response.loginresponse.ResultItem
import com.rootscare.data.model.response.loginresponse.TaskListResponse
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentNursesManageRateBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.hospital.HospitalHomeActivity
import com.rootscare.serviceprovider.ui.hospital.subfragment.FragmentHospitalHome
import com.rootscare.serviceprovider.ui.nurses.home.NursrsHomeActivity
import com.rootscare.serviceprovider.ui.nurses.nursesmyschedule.subfragment.manageschedule.adapter.AdapterTaskWiseRecyclerView
import com.rootscare.serviceprovider.ui.physiotherapy.home.PhysiotherapyHomeActivity
import com.rootscare.serviceprovider.ui.physiotherapy.home.subfragment.FragmentPhysiotherapyHome
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*

class FragmentNursesManageRate : BaseFragment<FragmentNursesManageRateBinding, FragmentNursesManageRateViewModel>(),
    FragmentNursesManageRateNavigator {
    private var fragmentNursesManageRateBinding: FragmentNursesManageRateBinding? = null
    private var fragmentNursesManageRateViewModel: FragmentNursesManageRateViewModel? = null
    private var userType: String = ""
    private var resultItemArrayList: ArrayList<ResultItem?> = ArrayList<ResultItem?>()
    private var checkedResultItemArrayListArrayList: ArrayList<ResultItem?>? = ArrayList<ResultItem?>()
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_nurses_manage_rate
    override val viewModel: FragmentNursesManageRateViewModel
        get() {
            fragmentNursesManageRateViewModel = ViewModelProviders.of(this).get(
                FragmentNursesManageRateViewModel::class.java
            )
            return fragmentNursesManageRateViewModel as FragmentNursesManageRateViewModel
        }


    companion object {
        fun newInstance(userType: String): FragmentNursesManageRate {
            val args = Bundle().apply {
                putString("userType", userType)
            }

            val fragment = FragmentNursesManageRate()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentNursesManageRateViewModel?.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentNursesManageRateBinding = viewDataBinding
        userType = arguments?.getString("userType")?:""
        Log.d("User Type ", ": $userType")

        fragmentNursesManageRateBinding?.tvSlotRate?.visibility = View.VISIBLE
        fragmentNursesManageRateBinding?.rlSlotList?.visibility = View.VISIBLE

        when (userType) {
            "other" -> {
                fragmentNursesManageRateBinding!!.llNurseRate.visibility = View.VISIBLE
                fragmentNursesManageRateBinding!!.btnSubmit2.text = "Save Slot Rate"
                apiHitForTaskPrice()
            }
            "physiotherapy" -> {
                fragmentNursesManageRateBinding!!.llNurseRate.visibility = View.GONE
                fragmentNursesManageRateBinding!!.btnSubmit2.text = "Save Task Rate"
                apiHitForTaskPrice()
            }
            else -> {
                fragmentNursesManageRateBinding!!.llNurseRate.visibility = View.GONE
                fragmentNursesManageRateBinding!!.btnSubmit2.text = "Save Task Price"
                apiHitForTestPrice()
            }
        }
//        fragmentDoctorProfileBinding?.btnDoctorEditProfile?.setOnClickListener(View.OnClickListener {
//            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                FragmentAddDoctorProfile.newInstance())
//        })

        fragmentNursesManageRateBinding?.run {
            restrictEditTextFromEnteringOnlyZero(etForPrice1)
            restrictEditTextFromEnteringOnlyZero(etForPrice2)
            restrictEditTextFromEnteringOnlyZero(etForPrice3)
            restrictEditTextFromEnteringOnlyZero(etForPrice4)
            restrictEditTextFromEnteringOnlyZero(etForPrice5)


            btnSubmit1.setOnClickListener { if (checkValidation()) { apiHitToSaveSlotPrice()  } }
            btnSubmit2.setOnClickListener {
                for (resultItem in resultItemArrayList) if (resultItem?.isChecked == true && resultItem.price != "") {
                    checkedResultItemArrayListArrayList?.add(resultItem)
                }
                if (checkedResultItemArrayListArrayList?.size == 0) {
                    Toast.makeText(requireActivity(), "Please select at least one task and price to continue", Toast.LENGTH_SHORT).show()
                } else {
                    when (userType) {
                        "other" -> apiHitToSaveHourlyPrice()
                        "physiotherapy" -> apiHitToSaveHourlyPrice()
                        else -> apiHitToSaveTestPrice()
                    }

                }
//                if (checkValidation2()) {
//                    apiHitToSaveHourlyPrice()
//                }
            }
        }

    }

    private fun apiHitForTaskPrice() {
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            val jsonObject = JsonObject()
            jsonObject.addProperty("type", if (userType == "physiotherapy") "physiotherapy" else "nurse")
            val body = RequestBody.create("application/json".toMediaTypeOrNull(), jsonObject.toString())
            fragmentNursesManageRateViewModel?.getTaskRate(body)
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun apiHitForTestPrice() {
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            fragmentNursesManageRateViewModel!!.getTestRate()
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun apiHitToSaveSlotPrice() {
        with(fragmentNursesManageRateBinding!!) {
            baseActivity?.showLoading()
            val jsonObject = JsonObject()
            jsonObject.addProperty("user_id", fragmentNursesManageRateViewModel?.appSharedPref?.loginUserId!!)
            jsonObject.addProperty("slot_1", tvTitleForPrice1.text.toString().trim())
            jsonObject.addProperty("amount_1", etForPrice1.text.toString().trim())
            jsonObject.addProperty("slot_2", tvTitleForPrice2.text.toString().trim())
            jsonObject.addProperty("amount_2", etForPrice2.text.toString().trim())
            jsonObject.addProperty("slot_3", tvTitleForPrice3.text.toString().trim())
            jsonObject.addProperty("amount_3", etForPrice3.text.toString().trim())
            jsonObject.addProperty("slot_4", tvTitleForPrice4.text.toString().trim())
            jsonObject.addProperty("amount_4", etForPrice4.text.toString().trim())
            jsonObject.addProperty("slot_5", tvTitleForPrice5.text.toString().trim())
            jsonObject.addProperty("amount_5", etForPrice5.text.toString().trim())

            val body = RequestBody.create("application/json".toMediaTypeOrNull(), jsonObject.toString())
            fragmentNursesManageRateViewModel?.savePrice(body)
        }
    }

    private fun checkValidation(): Boolean {
        with(fragmentNursesManageRateBinding!!) {
            return if (!TextUtils.isEmpty(etForPrice1.text.toString().trim()) &&
                !TextUtils.isEmpty(etForPrice2.text.toString().trim()) &&
                !TextUtils.isEmpty(etForPrice3.text.toString().trim()) &&
                !TextUtils.isEmpty(etForPrice4.text.toString().trim()) &&
                !TextUtils.isEmpty(etForPrice5.text.toString().trim())
            ) {
                true
            } else {
                Toast.makeText(requireActivity(), "Please enter price for at those slot above", Toast.LENGTH_SHORT).show()
                false
            }
        }
    }

    override fun onSuccessAfterSavePrice(loginResponse: LoginResponse) {
        println("loginResponse $loginResponse")
        println("userType $userType")
        baseActivity?.hideLoading()
        if (loginResponse.code.equals("200")) {
//            if (loginResponse.result?.hourlyRates != null && loginResponse.result.hourlyRates.size > 0) {

            when (userType) {
                "other" -> {
                    fragmentNursesManageRateViewModel?.appSharedPref?.loggedInDataForNurseAfterLogin = Gson().toJson(loginResponse.result)
                    activity?.startActivity(activity?.let { NursrsHomeActivity.newIntent(it) })
                }
                "physiotherapy" -> {
                    fragmentNursesManageRateViewModel?.appSharedPref?.loggedInDataForNurseAfterLogin = Gson().toJson(loginResponse.result)
//                    activity?.startActivity(activity?.let { PhysiotherapyHomeActivity.newIntent(it) })
                    (activity as PhysiotherapyHomeActivity).checkFragmentInBackStackAndOpen(FragmentPhysiotherapyHome.newInstance())
                }
                else -> {
//                    println("activity $activity")
                    fragmentNursesManageRateViewModel?.appSharedPref?.loggedInDataForNurseAfterLogin = Gson().toJson(loginResponse.result)
                    (activity as HospitalHomeActivity).checkFragmentInBackStackAndOpen(FragmentHospitalHome.newInstance())
//                    activity?.startActivity(activity?.let { HospitalHomeActivity.newIntent(it) })
                }
            }
        }
    }

    override fun onSuccessAfterGetTaskPrice(loginResponse: TaskListResponse) {
        baseActivity?.hideLoading()
        if (loginResponse.code.equals("200")) {
            resultItemArrayList = loginResponse.result!!
            val recyclerView = fragmentNursesManageRateBinding!!.recyclerViewNurseTaskList
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            if (userType == "nurse" || userType == "physiotherapy") {
                val nurseDataAfterLogIn =
                    Gson().fromJson(fragmentNursesManageRateViewModel?.appSharedPref?.loggedInDataForNurseAfterLogin, Result::class.java)
                println("nurseDataAfterLogIn $nurseDataAfterLogIn")
                if (nurseDataAfterLogIn?.task != null && nurseDataAfterLogIn.task.size > 0) {
                    for (resultItem in resultItemArrayList) {
                        for (nurseTaskItem in nurseDataAfterLogIn.task) {
                            if (resultItem?.id == nurseTaskItem?.id) {
                                resultItem?.isChecked = true
                                resultItem?.price = nurseTaskItem?.price
                            }
                        }
                    }

                }
            } else {
                val hospitalDataAfterLogIn =
                    Gson().fromJson(fragmentNursesManageRateViewModel?.appSharedPref?.loggedInDataForNurseAfterLogin, Result::class.java)
                println("hospitalDataAfterLogIn $hospitalDataAfterLogIn")
                if (hospitalDataAfterLogIn?.test != null && hospitalDataAfterLogIn.test.size > 0) {
                    for (resultItem in resultItemArrayList) {
                        for (hospitalTestItem in hospitalDataAfterLogIn.test) {
                            if (resultItem?.id == hospitalTestItem?.id) {
                                resultItem?.isChecked = true
                                resultItem?.price = hospitalTestItem?.price
                            }
                        }
                    }

                }
            }
            val contactListAdapter = AdapterTaskWiseRecyclerView(resultItemArrayList, requireContext())

            recyclerView.adapter = contactListAdapter
            contactListAdapter.onItemClickListener = object : AdapterTaskWiseRecyclerView.OnItemClickListener {

                override fun onCheck(position: Int, checked: Boolean) {
                    println("checked $checked")
                    loginResponse.result[position]?.isChecked = checked
                    contactListAdapter.notifyDataSetChanged()
                }
            }
        }
    }


    override fun onThrowable(throwable: Throwable) {
        baseActivity?.hideLoading()
    }


    private fun apiHitToSaveHourlyPrice() {
        with(fragmentNursesManageRateBinding!!) {
            baseActivity?.showLoading()


            val taskIdList: MutableList<String> = ArrayList()
            val taskPriceList: MutableList<String> = ArrayList()
            for (resultItem in checkedResultItemArrayListArrayList!!) if (resultItem?.isChecked == true) {
                taskIdList.add(resultItem.id!!)
                taskPriceList.add(resultItem.price!!)
            }
            val tasksId: String = TextUtils.join(", ", taskIdList)
            val tasksPrice: String = TextUtils.join(", ", taskPriceList)
            val userId = fragmentNursesManageRateViewModel!!.appSharedPref!!.loginUserId!!
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val taskId = tasksId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val price = tasksPrice.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            fragmentNursesManageRateViewModel?.saveTaskSlot(userId, taskId, price, userType)
        }
    }

    private fun apiHitToSaveTestPrice() {
        with(fragmentNursesManageRateBinding!!) {
            baseActivity?.showLoading()


            val taskIdList: MutableList<String> = ArrayList()
            val taskPriceList: MutableList<String> = ArrayList()
            for (resultItem in checkedResultItemArrayListArrayList!!) if (resultItem?.isChecked == true) {
                taskIdList.add(resultItem.id!!)
                taskPriceList.add(resultItem.price!!)
            }
            val tasksId: String = TextUtils.join(", ", taskIdList)
            val tasksPrice: String = TextUtils.join(", ", taskPriceList)
            val jsonObject = JsonObject()
            jsonObject.addProperty("hospital_id", fragmentNursesManageRateViewModel!!.appSharedPref!!.loginUserId!!)
            jsonObject.addProperty("master_pathology_id", tasksId)
            jsonObject.addProperty("price", tasksPrice)
            val body = RequestBody.create("application/json".toMediaTypeOrNull(), jsonObject.toString())


//            val userId = fragmentNursesManageRateViewModel!!.appSharedPref!!.loginUserId!!
//                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
//            val taskId = tasksId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
//            val price = tasksPrice.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            fragmentNursesManageRateViewModel?.saveTestPrice(body)
        }
    }

    private fun checkValidation2(): Boolean {
        with(fragmentNursesManageRateBinding!!) {
            if (TextUtils.isEmpty(etForPrice6.text.toString().trim())) {
                Toast.makeText(requireActivity(), "Please enter price for hourly rate below", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }


    private fun showDataOnField() {
        with(fragmentNursesManageRateBinding!!) {
            if (fragmentNursesManageRateViewModel?.appSharedPref?.loggedInDataForNurseAfterLogin != null) {
                val nurseDataAfterLogIn =
                    Gson().fromJson(fragmentNursesManageRateViewModel?.appSharedPref?.loggedInDataForNurseAfterLogin, Result::class.java)
                println("nurseDataAfterLogIn?.hourlyRates ${nurseDataAfterLogIn?.hourlyRates}")
                if (nurseDataAfterLogIn?.hourlyRates != null && nurseDataAfterLogIn.hourlyRates.size > 0) {
                    for (i in 0 until nurseDataAfterLogIn.hourlyRates.size) {
                        when (i) {
                            0 -> etForPrice1.setText(nurseDataAfterLogIn.hourlyRates[i]?.price)
                            1 -> etForPrice2.setText(nurseDataAfterLogIn.hourlyRates[i]?.price)
                            2 -> etForPrice3.setText(nurseDataAfterLogIn.hourlyRates[i]?.price)
                            3 -> etForPrice4.setText(nurseDataAfterLogIn.hourlyRates[i]?.price)
                            4 -> etForPrice5.setText(nurseDataAfterLogIn.hourlyRates[i]?.price)
                        }
                    }
                }

            }
        }
    }

    override fun onResume() {
        super.onResume()
        showDataOnField()
    }

    private fun restrictEditTextFromEnteringOnlyZero(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
                val enteredString = s.toString()
                if (enteredString.startsWith("0")) {
                    if (enteredString.isNotEmpty()) {
                        editText.setText(enteredString.substring(1))
                    } else {
                        editText.setText("")
                    }
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }
}