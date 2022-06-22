package com.rootscare.serviceprovider.ui.caregiver.caregivermyschedule.subfragment.manageschedule

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.rootscare.data.model.response.loginresponse.LoginResponse
import com.rootscare.data.model.response.loginresponse.Result
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentNursesManageRateBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.caregiver.home.CaregiverHomeActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

class FragmentCaregiverUpdateManageRate : BaseFragment<FragmentNursesManageRateBinding, FragmentCaregiverUpdateManageRateViewModel>(),
    FragmentCaregiverUpdateManageRateNavigator {
    private var fragmentNursesManageRateBinding: FragmentNursesManageRateBinding? = null
    private var fragmentNursesManageRateViewModel: FragmentCaregiverUpdateManageRateViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_nurses_manage_rate
    override val viewModel: FragmentCaregiverUpdateManageRateViewModel
        get() {
            fragmentNursesManageRateViewModel = ViewModelProviders.of(this).get(
                FragmentCaregiverUpdateManageRateViewModel::class.java
            )
            return fragmentNursesManageRateViewModel as FragmentCaregiverUpdateManageRateViewModel
        }

    companion object {
        fun newInstance(): FragmentCaregiverUpdateManageRate {
            val args = Bundle()
            val fragment = FragmentCaregiverUpdateManageRate()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentNursesManageRateViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentNursesManageRateBinding = viewDataBinding
//        fragmentDoctorProfileBinding?.btnDoctorEditProfile?.setOnClickListener(View.OnClickListener {
//            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                FragmentAddDoctorProfile.newInstance())
//        })
        fragmentNursesManageRateBinding?.tvSlotRate?.visibility = View.GONE
        fragmentNursesManageRateBinding?.rlSlotList?.visibility = View.GONE


        with(fragmentNursesManageRateBinding!!) {
            restrictEditTextFromEnteringOnlyZero(etForPrice1)
            restrictEditTextFromEnteringOnlyZero(etForPrice2)
            restrictEditTextFromEnteringOnlyZero(etForPrice3)
            restrictEditTextFromEnteringOnlyZero(etForPrice4)
            restrictEditTextFromEnteringOnlyZero(etForPrice5)


            btnSubmit1.setOnClickListener {
                if (checkValidation()) {
                    apiHitToSaveSlotPrice()
                }
            }
        }

        with(fragmentNursesManageRateBinding!!) {

            btnSubmit2.setOnClickListener {
                if (checkValidation2()) {
                    apiHitToSaveHourlyPrice()
                }
            }

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
        return true
    }

    override fun onSuccessAfterSavePrice(loginResponse: LoginResponse) {
        baseActivity?.hideLoading()
        if (loginResponse.code.equals("200")) {
            if (loginResponse.result?.hourlyRates != null && loginResponse.result.hourlyRates.size > 0) {
                fragmentNursesManageRateViewModel?.appSharedPref?.loggedInDataForNurseAfterLogin = Gson().toJson(loginResponse.result)
                activity?.startActivity(activity?.let { CaregiverHomeActivity.newIntent(it) })
            }
        }
    }

    override fun onThrowable(throwable: Throwable) {
        baseActivity?.hideLoading()
    }


    private fun apiHitToSaveHourlyPrice() {
        with(fragmentNursesManageRateBinding!!) {
            baseActivity?.showLoading()
            val jsonObject = JsonObject()
            jsonObject.addProperty("user_id", fragmentNursesManageRateViewModel?.appSharedPref?.loginUserId!!)
            jsonObject.addProperty("daily_rate", etForPrice6.text.toString().trim())

            val body = RequestBody.create("application/json".toMediaTypeOrNull(), jsonObject.toString())
            fragmentNursesManageRateViewModel?.saveHourlyPrice(body)
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


    private fun showDataField() {
        with(fragmentNursesManageRateBinding!!) {
            if (fragmentNursesManageRateViewModel?.appSharedPref?.loggedInDataForNurseAfterLogin != null) {
                val nurseDataAfterLogIn =
                    Gson().fromJson(fragmentNursesManageRateViewModel?.appSharedPref?.loggedInDataForNurseAfterLogin, Result::class.java)
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
                if (nurseDataAfterLogIn?.dailyRate != null && !TextUtils.isEmpty(nurseDataAfterLogIn.dailyRate.trim())) {
                    etForPrice6.setText(nurseDataAfterLogIn.dailyRate)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        showDataField()
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