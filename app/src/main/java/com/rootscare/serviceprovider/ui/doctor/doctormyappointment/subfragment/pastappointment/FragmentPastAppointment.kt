package com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.pastappointment

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.data.model.request.doctor.appointment.upcomingappointment.getuppcomingappoint.GetDoctorUpcommingAppointmentRequest
import com.rootscare.data.model.response.doctor.appointment.pastappointment.ResponsePastAppointment
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentDoctorPastAppointmentBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.FragmentAppointmentDetailsForAll
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.pastappointment.adapter.AdapterPastAppointmentListRecyclerview
import com.rootscare.serviceprovider.ui.home.HomeActivity
import com.rootscare.serviceprovider.ui.login.subfragment.login.FragmentLogin
import java.util.*

class FragmentPastAppointment :
    BaseFragment<FragmentDoctorPastAppointmentBinding, FragmentPastAppointmentViewModel>(),
    FragmentPastAppointmentNavigator {
    private var lastPosition: Int? = null
    private var fragmentDoctorRequestedAppointmentBinding: FragmentDoctorPastAppointmentBinding? =
        null
    private var fragmentRequestedAppointmentViewModel: FragmentPastAppointmentViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_doctor_past_appointment
    override val viewModel: FragmentPastAppointmentViewModel
        get() {
            fragmentRequestedAppointmentViewModel = ViewModelProviders.of(this).get(
                FragmentPastAppointmentViewModel::class.java
            )
            return fragmentRequestedAppointmentViewModel as FragmentPastAppointmentViewModel
        }

    companion object {
        fun newInstance(): FragmentPastAppointment {
            val args = Bundle()
            val fragment = FragmentPastAppointment()
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
            fragmentRequestedAppointmentViewModel!!.apidoctorappointmentPastList(
                getDoctorUpcomingAppointmentRequest
            )
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    // Set up recycler view for service listing if available
    private fun setUpDoctorMyAppointmentListingRecyclerview(requestedAppointmentList: LinkedList<com.rootscare.data.model.response.doctor.appointment.pastappointment.ResultItem>?) {
//        assert(fragmentDoctorRequestedAppointmentBinding!!.recyclerViewDoctorPastAppointment != null)
        val recyclerView =
            fragmentDoctorRequestedAppointmentBinding!!.recyclerViewDoctorPastAppointment
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter =
            AdapterPastAppointmentListRecyclerview(requestedAppointmentList, requireContext())
        recyclerView.adapter = contactListAdapter
        contactListAdapter.recyclerViewItemClickWithView = object : OnItemClikWithIdListener {
            override fun onItemClick(id: Int) {
                lastPosition = id
                (activity as HomeActivity).checkFragmentInBackStackAndOpen(
                    FragmentAppointmentDetailsForAll.newInstance(contactListAdapter.upcomingAppointmentList!![id].id!!, "doctor")
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
                fragmentDoctorRequestedAppointmentBinding?.tvNoDate?.text = "Doctor Appointment Not Found."
            }

        } else {
            fragmentDoctorRequestedAppointmentBinding?.recyclerViewDoctorPastAppointment?.visibility =
                View.GONE
            fragmentDoctorRequestedAppointmentBinding?.tvNoDate?.visibility = View.VISIBLE
            fragmentDoctorRequestedAppointmentBinding?.tvNoDate?.text = response.message
            Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
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