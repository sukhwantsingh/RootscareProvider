package com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.subfragment.upcomingappointment

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.dialog.CommonDialog
import com.rootscare.data.model.request.doctor.appointment.upcomingappointment.filterappointmentrequest.FilterAppointmentRequest
import com.rootscare.data.model.request.doctor.appointment.upcomingappointment.getuppcomingappoint.GetDoctorUpcommingAppointmentRequest
import com.rootscare.data.model.request.doctor.appointment.updateappointmentrequest.UpdateAppointmentRequest
import com.rootscare.data.model.response.doctor.appointment.requestappointment.getrequestappointment.GetDoctorRequestAppointmentResponse
import com.rootscare.data.model.response.doctor.appointment.upcomingappointment.DoctorUpcomingAppointmentResponse
import com.rootscare.data.model.response.doctor.appointment.upcomingappointment.ResultItem
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.interfaces.OnClickOfDoctorAppointment2
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentDoctorUpcomingAppointmentBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.FragmentAppointmentDetailsForAll
import com.rootscare.serviceprovider.ui.login.subfragment.login.FragmentLogin
import com.rootscare.serviceprovider.ui.nurses.home.NursrsHomeActivity
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.subfragment.upcomingappointment.adapter.AdapterNurseUpcommingAppointment
import java.util.*


class FragmentUpcommingAppointmentForNurse :
    BaseFragment<FragmentDoctorUpcomingAppointmentBinding, FragmentUpcommingAppointmentForNurseViewModel>(),
    FragmentUpcommingAppointmentForNurseNavigator {
    private var lastPosition: Int? = null
    private var contactListAdapter: AdapterNurseUpcommingAppointment? = null

    private var fragmentDoctorUpcomingAppointmentBinding: FragmentDoctorUpcomingAppointmentBinding? = null
    private var fragmentUpcommingAppointmentViewModel: FragmentUpcommingAppointmentForNurseViewModel? = null
    var monthOfDob: String = ""
    var dayOfDob: String = ""
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_doctor_upcoming_appointment
    override val viewModel: FragmentUpcommingAppointmentForNurseViewModel
        get() {
            fragmentUpcommingAppointmentViewModel = ViewModelProviders.of(this).get(
                FragmentUpcommingAppointmentForNurseViewModel::class.java
            )
            return fragmentUpcommingAppointmentViewModel as FragmentUpcommingAppointmentForNurseViewModel
        }

    companion object {
        fun newInstance(): FragmentUpcommingAppointmentForNurse {
            val args = Bundle()
            val fragment = FragmentUpcommingAppointmentForNurse()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentUpcommingAppointmentViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentDoctorUpcomingAppointmentBinding = viewDataBinding



        if (isNetworkConnected) {
            baseActivity?.showLoading()
            var getDoctorUpcommingAppointmentRequest = GetDoctorUpcommingAppointmentRequest()
            getDoctorUpcommingAppointmentRequest.userId = fragmentUpcommingAppointmentViewModel?.appSharedPref?.loginUserId
//            getDoctorUpcommingAppointmentRequest.userId="18"
            fragmentUpcommingAppointmentViewModel!!.apiNurseUpComingAppointmentList(getDoctorUpcommingAppointmentRequest)
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }


        fragmentDoctorUpcomingAppointmentBinding?.txtUpcomingDate?.setOnClickListener {
            // TODO Auto-generated method stub

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(this.requireActivity(), { _, yearInScope, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                // fragmentAdmissionFormBinding?.txtDob?.setText("" + dayOfMonth + "-" + (monthOfYear+1) + "-" + year)
                monthOfDob = if ((monthOfYear + 1) < 10) {
                    "0" + (monthOfYear + 1)
                } else {
                    (monthOfYear + 1).toString()
                }

                dayOfDob = if (dayOfMonth < 10) {
                    "0$dayOfMonth"

                } else {
                    dayOfMonth.toString()
                }
                fragmentDoctorUpcomingAppointmentBinding?.txtUpcomingDate?.text = "$yearInScope-$monthOfDob-$dayOfDob"
                if (fragmentDoctorUpcomingAppointmentBinding?.txtUpcomingDate?.text?.toString() != null && !fragmentDoctorUpcomingAppointmentBinding?.txtUpcomingDate?.text?.toString()
                        .equals("")
                ) {
                    if (isNetworkConnected) {
                        baseActivity?.showLoading()
                        val filterAppointmentRequest = FilterAppointmentRequest()
                        filterAppointmentRequest.userId = fragmentUpcommingAppointmentViewModel?.appSharedPref?.loginUserId
//                        filterAppointmentRequest.userId="18"
                        filterAppointmentRequest.fromDate = fragmentDoctorUpcomingAppointmentBinding?.txtUpcomingDate?.text?.toString()
                        fragmentUpcommingAppointmentViewModel!!.apiFilterNurseUpComingAppointmentList(filterAppointmentRequest)
                    } else {
                        Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                    }
                }
            }, year, month, day)
            var yearForReopen: Int? = null
            var monthForReopen: Int? = null
            var dayForReopen: Int? = null
            if (!TextUtils.isDigitsOnly(fragmentDoctorUpcomingAppointmentBinding?.txtUpcomingDate?.text.toString().trim())) {
                val strings = fragmentDoctorUpcomingAppointmentBinding?.txtUpcomingDate?.text.toString().split("-")
                yearForReopen = strings[0].toInt()
                monthForReopen = strings[1].toInt() - 1
                dayForReopen = strings[2].toInt()
            }
            if (yearForReopen != null && monthForReopen != null && dayForReopen != null) {
                dpd.updateDate(yearForReopen, monthForReopen, dayForReopen)
            }
            dpd.datePicker.minDate = System.currentTimeMillis() - 1000
            dpd.show()
        }
    }

    // Set up recycler view for service listing if available
    private fun setUpDoctorMyAppointmentlistingRecyclerview(upcomingAppointmentList: ArrayList<ResultItem?>?) {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentDoctorUpcomingAppointmentBinding!!.recyclerViewRootscareDoctorMyappointment != null)
        val recyclerView = fragmentDoctorUpcomingAppointmentBinding!!.recyclerViewRootscareDoctorMyappointment
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        contactListAdapter = AdapterNurseUpcommingAppointment(upcomingAppointmentList, requireContext())
        recyclerView.adapter = contactListAdapter
        contactListAdapter?.recyclerViewItemClickWithView = object : OnClickOfDoctorAppointment2 {
            override fun onItemClick(id: Int) {
                lastPosition = id
                (activity as NursrsHomeActivity).checkFragmentInBackStackAndOpen(
                    FragmentAppointmentDetailsForAll.newInstance(
                        contactListAdapter?.upcomingAppointmentList!![id]!!.id!!, "nurse"
                    )
                )
            }

            override fun onAcceptBtnClick(id: String, text: String) {

            }

            override fun onUploadBtnClick(id: String, text: String) {

            }

            override fun onRejectBtnBtnClick(position: String, text: String) {
                CommonDialog.showDialog(activity!!, object :
                    DialogClickCallback {
                    override fun onDismiss() {

                    }

                    override fun onConfirm() {
                        if (isNetworkConnected) {
                            baseActivity?.showLoading()
                            val updateAppointmentRequest = UpdateAppointmentRequest()
                            updateAppointmentRequest.id = contactListAdapter?.upcomingAppointmentList!![position.toInt()]?.id
                            updateAppointmentRequest.acceptanceStatus = text
                            fragmentUpcommingAppointmentViewModel!!.apiUpdateNurseAppointmentRequest(
                                updateAppointmentRequest,
                                position.toInt()
                            )
                        } else {
                            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                        }
                    }

                }, "Confirmation", "Are you sure to reject this appointment?")
            }

        }

        if (lastPosition != null) {
            Handler().postDelayed({
                activity?.runOnUiThread {
                    val smoothScroller: RecyclerView.SmoothScroller = object : LinearSmoothScroller(context) {
                        override fun getVerticalSnapPreference(): Int {
                            return SNAP_TO_ANY
                        }
                    }
                    smoothScroller.targetPosition = lastPosition!!
                    gridLayoutManager.startSmoothScroll(smoothScroller)
                }
            }, 400)
        }
    }

    override fun successDoctorUpcomingAppointmentResponse(doctorUpcomingAppointmentResponse: DoctorUpcomingAppointmentResponse?) {
        baseActivity?.hideLoading()
        if (doctorUpcomingAppointmentResponse?.code.equals("200")) {

            if (doctorUpcomingAppointmentResponse?.result != null && doctorUpcomingAppointmentResponse.result.size > 0) {
                fragmentDoctorUpcomingAppointmentBinding?.recyclerViewRootscareDoctorMyappointment?.visibility = View.VISIBLE
                fragmentDoctorUpcomingAppointmentBinding?.tvNoDate?.visibility = View.GONE
                setUpDoctorMyAppointmentlistingRecyclerview(doctorUpcomingAppointmentResponse.result)
            } else {
                fragmentDoctorUpcomingAppointmentBinding?.recyclerViewRootscareDoctorMyappointment?.visibility = View.GONE
                fragmentDoctorUpcomingAppointmentBinding?.tvNoDate?.visibility = View.VISIBLE
                fragmentDoctorUpcomingAppointmentBinding?.tvNoDate?.text = "No Upcoming Appointment List Found for this Doctor."
            }

        } else {
            fragmentDoctorUpcomingAppointmentBinding?.recyclerViewRootscareDoctorMyappointment?.visibility = View.GONE
            fragmentDoctorUpcomingAppointmentBinding?.tvNoDate?.visibility = View.VISIBLE
            fragmentDoctorUpcomingAppointmentBinding?.tvNoDate?.text = doctorUpcomingAppointmentResponse?.message
   //         Toast.makeText(activity, doctorUpcomingAppointmentResponse?.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun errorDoctorUpcomingAppointmentResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(FragmentLogin.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    override fun successGetDoctorRequestAppointmentUpdateResponse(
        getDoctorRequestAppointmentResponse: GetDoctorRequestAppointmentResponse?,
        position: Int
    ) {
        baseActivity?.hideLoading()
        if (getDoctorRequestAppointmentResponse?.code.equals("200")) {
            contactListAdapter?.upcomingAppointmentList!![position]?.acceptanceStatus = "reject"
            contactListAdapter?.notifyItemChanged(position)
            contactListAdapter?.upcomingAppointmentList?.removeAt(position)
            contactListAdapter?.notifyItemRemoved(position)
            contactListAdapter?.notifyItemRangeChanged(position, contactListAdapter?.upcomingAppointmentList?.size!!)
            if (contactListAdapter?.upcomingAppointmentList?.size == 0) {
                fragmentDoctorUpcomingAppointmentBinding?.tvNoDate?.visibility = View.VISIBLE
                fragmentDoctorUpcomingAppointmentBinding?.recyclerViewRootscareDoctorMyappointment?.visibility = View.GONE
            }
        }
    }


}