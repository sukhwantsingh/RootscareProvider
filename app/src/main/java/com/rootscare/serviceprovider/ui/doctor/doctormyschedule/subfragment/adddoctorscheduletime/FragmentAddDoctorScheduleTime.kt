package com.rootscare.serviceprovider.ui.doctor.doctormyschedule.subfragment.adddoctorscheduletime

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.dialog.CommonDialog
import com.google.gson.Gson
import com.rootscare.data.model.request.doctor.myscheduleaddtimeslot.AddTimeSlotRequest
import com.rootscare.data.model.request.doctor.myscheduleaddtimeslot.SlotItem
import com.rootscare.data.model.response.CommonResponse
import com.rootscare.data.model.response.doctor.myschedule.hospitallist.ResultItem
import com.rootscare.data.model.response.loginresponse.Result
import com.rootscare.interfaces.DropDownDialogCallBack
import com.rootscare.model.AddDoctorSlotTimeItmModel
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentAddDoctorSlotAndTimeBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctormyschedule.subfragment.adddoctorscheduletime.adapter.AdapterAddSlotPerDayRecyclerview
import java.util.*
import kotlin.collections.ArrayList

class FragmentAddDoctorScheduleTime : BaseFragment<FragmentAddDoctorSlotAndTimeBinding, FragmentAddDoctorScheduleTimeViewModel>(),
    FragmentAddDoctorScheduleTimeNavigator {

    private var passedItem: ResultItem? = null
    private var passedResult: com.rootscare.data.model.response.doctor.myschedule.homeVisitList.Result? = null
    private var day: String? = null

    private var fragmentAddDoctorSlotAndTimeBinding: FragmentAddDoctorSlotAndTimeBinding? = null
    private var fragmentAddDoctorScheduleTimeViewModel: FragmentAddDoctorScheduleTimeViewModel? = null
    private var adapterAddSlotPerDayRecyclerview: AdapterAddSlotPerDayRecyclerview? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_add_doctor_slot_and_time
    override val viewModel: FragmentAddDoctorScheduleTimeViewModel
        get() {
            fragmentAddDoctorScheduleTimeViewModel = ViewModelProviders.of(this).get(
                FragmentAddDoctorScheduleTimeViewModel::class.java
            )
            return fragmentAddDoctorScheduleTimeViewModel as FragmentAddDoctorScheduleTimeViewModel
        }

    companion object {
        fun newInstance(passedItem: ResultItem): FragmentAddDoctorScheduleTime {
            val args = Bundle()
            args.putSerializable("passedItem", passedItem)
            val fragment = FragmentAddDoctorScheduleTime()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(passedResult: com.rootscare.data.model.response.doctor.myschedule.homeVisitList.Result): FragmentAddDoctorScheduleTime {
            val args = Bundle()
            args.putSerializable("passedResult", passedResult)
            val fragment = FragmentAddDoctorScheduleTime()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(day: String): FragmentAddDoctorScheduleTime {
            val args = Bundle()
            args.putString("day", day)
            val fragment = FragmentAddDoctorScheduleTime()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentAddDoctorScheduleTimeViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentAddDoctorSlotAndTimeBinding = viewDataBinding

        if (arguments != null && arguments?.getSerializable("passedItem") != null) {
            passedItem = arguments?.getSerializable("passedItem") as ResultItem
//            Log.d("passedItem", ": $passedItem")
        }

        if (arguments != null && arguments?.getSerializable("passedResult") != null) {
            passedResult =
                arguments?.getSerializable("passedResult") as com.rootscare.data.model.response.doctor.myschedule.homeVisitList.Result?
        }

        if (arguments != null && arguments?.getString("day") != null) {
            day = arguments?.getString("day")
        }

        with(fragmentAddDoctorSlotAndTimeBinding!!) {
            if (passedItem != null) {
                if (passedItem?.name != null) {
                    tvHospitalTitle.text = passedItem?.name
                }
            }
        }

        when {
            passedResult != null -> {
                fragmentAddDoctorSlotAndTimeBinding?.tvHospitalTitle?.visibility = View.GONE
                fragmentAddDoctorSlotAndTimeBinding?.txtSelectDay?.text = passedResult!!.day
                fragmentAddDoctorSlotAndTimeBinding?.txtSelectDay?.isClickable = false
                fragmentAddDoctorSlotAndTimeBinding?.txtSelectDay?.setCompoundDrawables(null, null, null, null)
            }
            passedItem != null -> {
                fragmentAddDoctorSlotAndTimeBinding?.tvHospitalTitle?.visibility = View.VISIBLE
                fragmentAddDoctorSlotAndTimeBinding?.txtSelectDay?.isClickable = true
                val dropdownList = ArrayList<String?>()
                dropdownList.add("Monday")
                dropdownList.add("Tuesday")
                dropdownList.add("Wednesday")
                dropdownList.add("Thursday")
                dropdownList.add("Friday")
                dropdownList.add("Saturday")
                dropdownList.add("Sunday")
                fragmentAddDoctorSlotAndTimeBinding?.txtSelectDay?.setOnClickListener {
                    CommonDialog.showDialogForDropDownList(requireContext(), getString(R.string.select_a_day), dropdownList, object :
                        DropDownDialogCallBack {
                        override fun onConfirm(text: String) {
                            fragmentAddDoctorSlotAndTimeBinding?.txtSelectDay?.text = text
                        }

                    })
                }
            }
            else -> {
                fragmentAddDoctorSlotAndTimeBinding?.tvHospitalTitle?.visibility = View.GONE
                fragmentAddDoctorSlotAndTimeBinding?.txtSelectDay?.text = day
                fragmentAddDoctorSlotAndTimeBinding?.txtSelectDay?.isClickable = false
                fragmentAddDoctorSlotAndTimeBinding?.txtSelectDay?.setCompoundDrawables(null, null, null, null)
            }
        }
        setUpAddSlotAndTimeListing()

        fragmentAddDoctorSlotAndTimeBinding?.btnAddMoreSlotAndTime?.setOnClickListener {
            adapterAddSlotPerDayRecyclerview?.addField(AddDoctorSlotTimeItmModel("", "", ""))
        }
        fragmentAddDoctorSlotAndTimeBinding?.btnSubmit?.setOnClickListener {
            apiHitForAddingTimeSlot()
        }
    }

    // Set up recycler view for service listing if available
    private fun setUpAddSlotAndTimeListing() {
//        assert(fragmentAddDoctorSlotAndTimeBinding!!.recyclerViewAddSlotAndtime != null)
        val recyclerView = fragmentAddDoctorSlotAndTimeBinding!!.recyclerViewAddSlotAndtime
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        adapterAddSlotPerDayRecyclerview = AdapterAddSlotPerDayRecyclerview(activity!!)
        recyclerView.adapter = adapterAddSlotPerDayRecyclerview
        adapterAddSlotPerDayRecyclerview?.addField(AddDoctorSlotTimeItmModel("", "", ""))

    }


    private fun apiHitForAddingTimeSlot() {
        val nurseDataAfterLogIn =
            Gson().fromJson(fragmentAddDoctorScheduleTimeViewModel?.appSharedPref?.loggedInDataForNurseAfterLogin, Result::class.java)
        println("nurseDataAfterLogIn $nurseDataAfterLogIn")
        if (checkValidation()) {
            val request = AddTimeSlotRequest()
            request.doctorId = fragmentAddDoctorScheduleTimeViewModel?.appSharedPref?.loginUserId

            request.day = fragmentAddDoctorSlotAndTimeBinding?.txtSelectDay?.text.toString().toLowerCase(Locale.ROOT)
            if (adapterAddSlotPerDayRecyclerview?.productlistItem?.size!! > 0) {
                val slot: LinkedList<SlotItem> = LinkedList()
                for (i in 0 until adapterAddSlotPerDayRecyclerview?.productlistItem?.size!!) {
                    val tempSlot = SlotItem()
                    tempSlot.timeFrom = adapterAddSlotPerDayRecyclerview?.productlistItem!![i].stat_time
                    tempSlot.timeTo = adapterAddSlotPerDayRecyclerview?.productlistItem!![i].end_time
                    slot.add(tempSlot)
                }
                request.slot = slot
            }

            if (isNetworkConnected) {
                baseActivity?.showLoading()
                if (fragmentAddDoctorScheduleTimeViewModel?.appSharedPref?.loginUserType == "hospital_doctor") {
                    request.hospitalId = nurseDataAfterLogIn?.hospitalId
                    fragmentAddDoctorScheduleTimeViewModel!!.saveTimeSlotHospital(request)
                } else {
                    if (passedItem != null) {
                        request.clinicId = passedItem?.id
                        fragmentAddDoctorScheduleTimeViewModel!!.saveTimeSlot(request)
                    } else {
                        fragmentAddDoctorScheduleTimeViewModel!!.saveHomeTimeSlot(request)
                    }
                }

            } else {
                Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun checkValidation(): Boolean {
        with(fragmentAddDoctorSlotAndTimeBinding!!) {
            if (TextUtils.isEmpty(txtSelectDay.text.toString().trim())) {
                Toast.makeText(activity!!, activity?.getString(R.string.please_select_day), Toast.LENGTH_SHORT).show()
                return false
            }
            var temppBool = true
            for (i in 0 until adapterAddSlotPerDayRecyclerview?.productlistItem?.size!!) {
                val startTime = adapterAddSlotPerDayRecyclerview?.productlistItem!![i].stat_time
                val endTime = adapterAddSlotPerDayRecyclerview?.productlistItem!![i].end_time
                if (TextUtils.isEmpty(startTime.trim()) || TextUtils.isEmpty(endTime.trim())) {
                    temppBool = false
                    Toast.makeText(activity!!, activity?.getString(R.string.please_add_time_for_all_slot), Toast.LENGTH_LONG).show()
                    break
                }
            }
            return temppBool
        }
    }

    override fun onSuccessTimeSlotSave(response: CommonResponse) {
        baseActivity?.hideLoading()
        if (response.code.equals("200")) {
            Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
            activity?.onBackPressed()
            activity?.onBackPressed()
        } else {
            Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onThrowable(throwable: Throwable) {
        baseActivity?.hideLoading()
    }
}