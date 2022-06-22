package com.rootscare.serviceprovider.ui.doctor.doctormyschedule.subfragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.dialog.CommonDialog
import com.rootscare.data.model.request.doctor.myscheduletimeslot.GetTimeSlotMyScheduleRequest
import com.rootscare.data.model.response.doctor.myschedule.homeVisitList.HomeVisitSchedule
import com.rootscare.data.model.response.doctor.myschedule.homeVisitList.Result
import com.rootscare.data.model.response.doctor.myschedule.hospitallist.ResultItem
import com.rootscare.data.model.response.doctor.myschedule.timeslotlist.MyScheduleTimeSlotResponse
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.interfaces.DropDownDialogCallBack
import com.rootscare.interfaces.OnItemClickWithReportIdListener
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentDoctorManegeScheduleBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctormyschedule.subfragment.adapter.AdapterDoctoeViewScheduleRecyclerview
import com.rootscare.serviceprovider.ui.doctor.doctormyschedule.subfragment.adapter.AdapterDoctorHomeVisitRecyclerview
import com.rootscare.serviceprovider.ui.doctor.doctormyschedule.subfragment.adddoctorscheduletime.FragmentAddDoctorScheduleTime
import com.rootscare.serviceprovider.ui.home.HomeActivity
import java.util.*
import kotlin.collections.ArrayList

class FragmentdoctorManageSchedule : BaseFragment<FragmentDoctorManegeScheduleBinding, FragmentdoctorManageScheduleViewModel>(),
    FragmentdoctorManageScheduleNavigator {


    private var passedItem: ResultItem? = null

    private var passedResult: Result? = null

    private var test: String? = null

    private var contactListAdapter: AdapterDoctoeViewScheduleRecyclerview? = null

    private var adapterDoctorHomeVisitRecyclerview: AdapterDoctorHomeVisitRecyclerview? = null

    private var fragmentDoctorManegeScheduleBinding: FragmentDoctorManegeScheduleBinding? = null
    private var fragmentdoctorManageScheduleViewModel: FragmentdoctorManageScheduleViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_doctor_manege_schedule
    override val viewModel: FragmentdoctorManageScheduleViewModel
        get() {
            fragmentdoctorManageScheduleViewModel = ViewModelProviders.of(this).get(
                FragmentdoctorManageScheduleViewModel::class.java
            )
            return fragmentdoctorManageScheduleViewModel as FragmentdoctorManageScheduleViewModel
        }

    companion object {
        fun newInstance(passedItem: ResultItem): FragmentdoctorManageSchedule {
            val args = Bundle()
            args.putSerializable("passedItem", passedItem)
            val fragment = FragmentdoctorManageSchedule()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(passedItem: Result): FragmentdoctorManageSchedule {
            val args = Bundle()
            args.putSerializable("passedResult", passedItem)
            val fragment = FragmentdoctorManageSchedule()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(test: String): FragmentdoctorManageSchedule {
            val args = Bundle()
            args.putString("test", test)
            val fragment = FragmentdoctorManageSchedule()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentdoctorManageScheduleViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentDoctorManegeScheduleBinding = viewDataBinding

        if (arguments != null && arguments?.getSerializable("passedItem") != null) {
            passedItem = arguments?.getSerializable("passedItem") as ResultItem
            Log.d("passedItem", ": $passedItem")
        }

        if (arguments != null && arguments?.getSerializable("passedResult") != null) {
            passedResult = arguments?.getSerializable("passedResult") as Result?
            Log.d("passedResult", ": $passedResult")
        }

        if (arguments != null && arguments?.getString("test") != null) {
            test = arguments?.getString("test")
        }


        with(fragmentDoctorManegeScheduleBinding!!) {
            if (passedItem != null) {
                if (passedItem?.name != null) {
                    tvHospitalTitle.text = passedItem?.name
                }
            }
        }

        if (passedResult != null) {
            fragmentDoctorManegeScheduleBinding?.tvHospitalTitle?.visibility = View.GONE
            fragmentDoctorManegeScheduleBinding?.txtSelectSecheduleDay?.text = passedResult!!.day
            fragmentDoctorManegeScheduleBinding?.txtSelectSecheduleDay?.isClickable = false
            fragmentDoctorManegeScheduleBinding?.txtSelectSecheduleDay?.setCompoundDrawables(null, null, null, null)
        } else {
            fragmentDoctorManegeScheduleBinding?.tvHospitalTitle?.visibility = View.VISIBLE
            fragmentDoctorManegeScheduleBinding?.txtSelectSecheduleDay?.isClickable = true
            val dropdownList = ArrayList<String?>()
            dropdownList.add("Monday")
            dropdownList.add("Tuesday")
            dropdownList.add("Wednesday")
            dropdownList.add("Thursday")
            dropdownList.add("Friday")
            dropdownList.add("Saturday")
            dropdownList.add("Sunday")
            fragmentDoctorManegeScheduleBinding?.txtSelectSecheduleDay?.setOnClickListener {
                CommonDialog.showDialogForDropDownList(this.activity!!, getString(R.string.select_a_day), dropdownList, object :
                    DropDownDialogCallBack {
                    override fun onConfirm(text: String) {
                        if (passedItem != null) {
                            fragmentDoctorManegeScheduleBinding?.txtSelectSecheduleDay?.text = text
                            apiHitFetchDaySpecificTimeSlotList(text)
                        } else {
                            fragmentDoctorManegeScheduleBinding?.txtSelectSecheduleDay?.text = text
                            apiHitFetchHomeSlotList(text)
                        }
                    }

                })
            }
        }


        if (passedItem != null)
            setUpDoctorMySchedulelistingRecyclerview()
        else
            setUpDoctorHomeVisitListRecyclerview()


        fragmentDoctorManegeScheduleBinding?.btnAddSlotAndTime?.setOnClickListener {


            (activity as HomeActivity).checkFragmentInBackStackAndOpen(
                when {
                    passedResult != null -> {
                        println("passedResult $passedResult")
                        FragmentAddDoctorScheduleTime.newInstance(passedResult!!)
                    }
                    passedItem != null -> {
                        println("passedItem $passedItem")
                        FragmentAddDoctorScheduleTime.newInstance(passedItem!!)
                    }
                    else -> {
                        println("else")
                        FragmentAddDoctorScheduleTime.newInstance(fragmentDoctorManegeScheduleBinding?.txtSelectSecheduleDay?.text.toString())
                    }
                }
            )
        }

        fragmentDoctorManegeScheduleBinding?.clearFilterButton?.setOnClickListener {
            apiHitFetchAllTimeSlotList()
        }

    }

    override fun onResume() {
        super.onResume()
        if (passedItem != null)
            apiHitFetchAllTimeSlotList()
        else {
            apiHitFetchHomeSlotList(fragmentDoctorManegeScheduleBinding?.txtSelectSecheduleDay?.text.toString())
//            if (passedResult?.slot != null && passedResult?.slot!!.isNotEmpty()) {
//
//                hideNoData()
//                adapterDoctorHomeVisitRecyclerview?.result = passedResult!!.slot!!
//                adapterDoctorHomeVisitRecyclerview?.day = passedResult!!.day
//                adapterDoctorHomeVisitRecyclerview?.notifyDataSetChanged()
//            } else {
//                showNoData()
//            }
        }
    }

    // Set up recycler view for service listing if available
    private fun setUpDoctorMySchedulelistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        val recyclerView = fragmentDoctorManegeScheduleBinding!!.recyclerViewDoctorViewscheduleListing
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        contactListAdapter = AdapterDoctoeViewScheduleRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView = object : OnItemClickWithReportIdListener {
            override fun onItemClick(id: Int) {
                CommonDialog.showDialog(activity!!, object :
                    DialogClickCallback {
                    override fun onDismiss() {

                    }

                    override fun onConfirm() {
                        apiHitRemoveTimeSlot(id)
                    }

                }, "Confirmation", "Are you sure to remove this time slot?")
            }

        }
    }

    // Set up recycler view for service listing if available
    private fun setUpDoctorHomeVisitListRecyclerview() {
//        assert(fragmentDoctorManegeScheduleBinding!!.recyclerViewDoctorViewscheduleListing != null)
        val recyclerView = fragmentDoctorManegeScheduleBinding!!.recyclerViewDoctorViewscheduleListing
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        adapterDoctorHomeVisitRecyclerview = AdapterDoctorHomeVisitRecyclerview(context!!)
        recyclerView.adapter = adapterDoctorHomeVisitRecyclerview
        adapterDoctorHomeVisitRecyclerview?.recyclerViewItemClickWithView = object : OnItemClickWithReportIdListener {
            override fun onItemClick(id: Int) {
                CommonDialog.showDialog(activity!!, object :
                    DialogClickCallback {
                    override fun onDismiss() {

                    }

                    override fun onConfirm() {
                        apiHitRemoveHomeTimeSlot(id)
                    }

                }, "Confirmation", "Are you sure to remove this time slot?")
            }

        }
    }

    private fun apiHitFetchAllTimeSlotList() {
        if (isNetworkConnected) {
            fragmentDoctorManegeScheduleBinding?.txtSelectSecheduleDay?.text = ""
            fragmentDoctorManegeScheduleBinding?.clearFilterButton?.visibility = View.GONE
            baseActivity?.showLoading()
            val getDoctorUpcomingAppointmentRequest = GetTimeSlotMyScheduleRequest()
            getDoctorUpcomingAppointmentRequest.doctor_id = fragmentdoctorManageScheduleViewModel?.appSharedPref?.loginUserId
            getDoctorUpcomingAppointmentRequest.clinic_id = passedItem?.id
            fragmentdoctorManageScheduleViewModel!!.getAllTimeSlotOfEveryday(getDoctorUpcomingAppointmentRequest)
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun apiHitFetchDaySpecificTimeSlotList(day: String) {
        if (isNetworkConnected) {
            fragmentDoctorManegeScheduleBinding?.clearFilterButton?.visibility = View.VISIBLE
            baseActivity?.showLoading()
            val getDoctorUpcomingAppointmentRequest = GetTimeSlotMyScheduleRequest()
            getDoctorUpcomingAppointmentRequest.doctor_id = fragmentdoctorManageScheduleViewModel?.appSharedPref?.loginUserId
            getDoctorUpcomingAppointmentRequest.clinic_id = passedItem?.id
            getDoctorUpcomingAppointmentRequest.day = day.toLowerCase(Locale.ROOT)
            fragmentdoctorManageScheduleViewModel!!.getAllTimeSlotOfDaySpecific(getDoctorUpcomingAppointmentRequest)
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun apiHitFetchHomeSlotList(day: String) {
        if (isNetworkConnected) {
            fragmentDoctorManegeScheduleBinding?.clearFilterButton?.visibility = View.GONE
            baseActivity?.showLoading()
            val getDoctorUpcomingAppointmentRequest = GetTimeSlotMyScheduleRequest()
            getDoctorUpcomingAppointmentRequest.doctor_id = fragmentdoctorManageScheduleViewModel?.appSharedPref?.loginUserId
            getDoctorUpcomingAppointmentRequest.day = day.toLowerCase(Locale.ROOT)
            fragmentdoctorManageScheduleViewModel!!.getAllTimeSlotOfHome(getDoctorUpcomingAppointmentRequest)
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun apiHitRemoveTimeSlot(position: Int) {
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            val getDoctorUpcomingAppointmentRequest = GetTimeSlotMyScheduleRequest()
            getDoctorUpcomingAppointmentRequest.id = contactListAdapter?.result!![position].id
//            getDoctorUpcomingAppointmentRequest.day = contactListAdapter?.result!![position].day
            getDoctorUpcomingAppointmentRequest.day = ""
            fragmentdoctorManageScheduleViewModel!!.apiHitRemoveTimeSlot(position, getDoctorUpcomingAppointmentRequest)
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun apiHitRemoveHomeTimeSlot(position: Int) {
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            val getDoctorUpcomingAppointmentRequest = GetTimeSlotMyScheduleRequest()
            getDoctorUpcomingAppointmentRequest.id = passedResult?.slot!![position].slotId
//            getDoctorUpcomingAppointmentRequest.day = contactListAdapter?.result!![position].day
            getDoctorUpcomingAppointmentRequest.day = ""
            fragmentdoctorManageScheduleViewModel!!.apiHitRemoveHomeSlot(position, getDoctorUpcomingAppointmentRequest)
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onSuccessTimeSlotList(response: MyScheduleTimeSlotResponse) {
        baseActivity?.hideLoading()
        if (response.code.equals("200")) {
            if (response.result != null && response.result.size > 0) {
                hideNoData()
                contactListAdapter?.result = response.result
                contactListAdapter?.notifyDataSetChanged()
            } else {
                showNoData()
            }
        } else {
            showNoData()
        }
    }

    override fun onSuccessTimeHomeSlotList(response: HomeVisitSchedule) {
        baseActivity?.hideLoading()
        if (response.code.equals("200")) {
            if (response.result != null && response.result.size > 0) {
                hideNoHomeVisitData()
                adapterDoctorHomeVisitRecyclerview?.result = response.result[0].slot!!
                adapterDoctorHomeVisitRecyclerview?.notifyDataSetChanged()
            } else {
                showNoHomeVisitData()
            }
        } else {
            showNoHomeVisitData()
        }
    }

    override fun onSuccessAfterRemoveTimeSlot(position: Int, response: MyScheduleTimeSlotResponse) {
        baseActivity?.hideLoading()
        if (response.code.equals("200")) {
            if (contactListAdapter?.result?.size!! > 0) {
                contactListAdapter?.result?.removeAt(position)
                contactListAdapter?.notifyItemRemoved(position)
                contactListAdapter?.notifyItemRangeChanged(position, contactListAdapter?.result?.size!!)
                if (contactListAdapter?.result?.size == 0) {
                    showNoData()
                } else {
                    hideNoData()
                }
            }
        }
    }

    override fun onSuccessAfterRemoveHomeTimeSlot(position: Int, response: MyScheduleTimeSlotResponse) {
        baseActivity?.hideLoading()
        if (response.code.equals("200")) {
            apiHitFetchHomeSlotList(fragmentDoctorManegeScheduleBinding?.txtSelectSecheduleDay?.text.toString())
        }
    }

    override fun onThrowable(throwable: Throwable) {
        baseActivity?.hideLoading()
    }

    private fun showNoData() {
        with(fragmentDoctorManegeScheduleBinding!!) {
            recyclerViewDoctorViewscheduleListing.visibility = View.GONE
            tvNoDate.visibility = View.VISIBLE
        }
    }

    private fun hideNoData() {
        with(fragmentDoctorManegeScheduleBinding!!) {
            recyclerViewDoctorViewscheduleListing.visibility = View.VISIBLE
            tvNoDate.visibility = View.GONE
        }
    }

    private fun showNoHomeVisitData() {
        with(fragmentDoctorManegeScheduleBinding!!) {
            recyclerViewDoctorViewscheduleListing.visibility = View.GONE
            tvNoDate.visibility = View.VISIBLE
        }
    }

    private fun hideNoHomeVisitData() {
        with(fragmentDoctorManegeScheduleBinding!!) {
            recyclerViewDoctorViewscheduleListing.visibility = View.VISIBLE
            tvNoDate.visibility = View.GONE
        }
    }

}