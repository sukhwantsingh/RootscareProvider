package com.rootscare.serviceprovider.ui.physiotherapy.physiotherapyMyAppointment.subfragment.pastappointment

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.rootscare.data.model.request.doctor.appointment.upcomingappointment.getuppcomingappoint.GetDoctorUpcommingAppointmentRequest
import com.rootscare.data.model.response.doctor.appointment.pastappointment.ResponsePastAppointment
import com.rootscare.data.model.response.doctor.appointment.pastappointment.ResultItem
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentDoctorPastAppointmentBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.FragmentAppointmentDetailsForAll
import com.rootscare.serviceprovider.ui.login.subfragment.login.FragmentLogin
import com.rootscare.serviceprovider.ui.physiotherapy.home.PhysiotherapyHomeActivity
import com.rootscare.serviceprovider.ui.physiotherapy.physiotherapyMyAppointment.subfragment.pastappointment.adapter.AdapterPastAppointmentListForPhysiotherapist
import java.util.*


class FragmentPastAppointmentForPhysiotherapist :
    BaseFragment<FragmentDoctorPastAppointmentBinding, FragmentPastAppointmentForPhysiotherapistViewModel>(),
    FragmentPastAppointmentForPhysiotherapistNavigator {
    private var lastPosition: Int? = null
    private var fragmentDoctorRequestedAppointmentBinding: FragmentDoctorPastAppointmentBinding? =
        null
    private var fragmentRequestedAppointmentViewModel: FragmentPastAppointmentForPhysiotherapistViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_doctor_past_appointment
    override val viewModel: FragmentPastAppointmentForPhysiotherapistViewModel
        get() {
            fragmentRequestedAppointmentViewModel = ViewModelProviders.of(this).get(
                FragmentPastAppointmentForPhysiotherapistViewModel::class.java
            )
            return fragmentRequestedAppointmentViewModel as FragmentPastAppointmentForPhysiotherapistViewModel
        }

    companion object {
        fun newInstance(): FragmentPastAppointmentForPhysiotherapist {
            val args = Bundle()
            val fragment = FragmentPastAppointmentForPhysiotherapist()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentRequestedAppointmentViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentDoctorRequestedAppointmentBinding = viewDataBinding
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            val getDoctorUpcomingAppointmentRequest = GetDoctorUpcommingAppointmentRequest()
            getDoctorUpcomingAppointmentRequest.userId =
                fragmentRequestedAppointmentViewModel?.appSharedPref?.loginUserId
//            getDoctorUpcommingAppointmentRequest.userId="18"
            fragmentRequestedAppointmentViewModel!!.apiDoctorAppointmentPastList(
                getDoctorUpcomingAppointmentRequest
            )
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    // Set up recycler view for service listing if available
    private fun setUpDoctorMyAppointmentListingRecyclerview(requestedAppointmentList: LinkedList<ResultItem>?) {
//        assert(fragmentDoctorRequestedAppointmentBinding!!.recyclerViewDoctorPastAppointment != null)
        val recyclerView =
            fragmentDoctorRequestedAppointmentBinding!!.recyclerViewDoctorPastAppointment
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter =
            AdapterPastAppointmentListForPhysiotherapist(requestedAppointmentList, requireContext())
        recyclerView.adapter = contactListAdapter
        contactListAdapter.recyclerViewItemClickWithView = object : OnItemClikWithIdListener {
            override fun onItemClick(id: Int) {
                lastPosition = id
                (activity as PhysiotherapyHomeActivity).checkFragmentInBackStackAndOpen(
                    FragmentAppointmentDetailsForAll.newInstance(
                        contactListAdapter.upcomingAppointmentList!![id].id!!,
                        "physiotherapy"
                    )
                )
            }

            override fun onAcceptBtnClick(id: String, text: String) {
            }

            override fun onRejectBtnClick(id: String, text: String) {
            }

        }
        if (lastPosition != null) {
            Handler().postDelayed({
                activity?.runOnUiThread {
                    val smoothScroller: SmoothScroller = object : LinearSmoothScroller(context) {
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


    override fun responseListPastAppointment(response: ResponsePastAppointment) {
        baseActivity?.hideLoading()
        if (response.code.equals("200")) {
            if (response.result != null && response.result?.size!! > 0) {
                fragmentDoctorRequestedAppointmentBinding?.recyclerViewDoctorPastAppointment?.visibility =
                    View.VISIBLE
                fragmentDoctorRequestedAppointmentBinding?.tvNoDate?.visibility = View.GONE
                setUpDoctorMyAppointmentListingRecyclerview(response.result)
            } else {
                fragmentDoctorRequestedAppointmentBinding?.recyclerViewDoctorPastAppointment?.visibility =
                    View.GONE
                fragmentDoctorRequestedAppointmentBinding?.tvNoDate?.visibility = View.VISIBLE
                fragmentDoctorRequestedAppointmentBinding?.tvNoDate?.text = "Physiotherapy Appointment Not Found."
            }

        } else {
            fragmentDoctorRequestedAppointmentBinding?.recyclerViewDoctorPastAppointment?.visibility =
                View.GONE
            fragmentDoctorRequestedAppointmentBinding?.tvNoDate?.visibility = View.VISIBLE
            fragmentDoctorRequestedAppointmentBinding?.tvNoDate?.text = response.message
            Toast.makeText(
                activity,
                response.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun errorGetDoctorRequestAppointmentResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(FragmentLogin.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }
}