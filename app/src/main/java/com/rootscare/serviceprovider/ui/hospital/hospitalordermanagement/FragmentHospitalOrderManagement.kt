package com.rootscare.serviceprovider.ui.hospital.hospitalordermanagement

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.data.model.request.hospital.getdoctorhospital
import com.rootscare.data.model.response.hospital.ResponseLabPastAppointment
import com.rootscare.data.model.response.hospital.ResultItemLab
import com.rootscare.interfaces.OnItemClickWithReportIdListener
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentHospitalOrderManagementBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.FragmentAppointmentDetailsForAll
import com.rootscare.serviceprovider.ui.hospital.HospitalHomeActivity
import com.rootscare.serviceprovider.ui.hospital.hospitalordermanagement.adapter.AdapterHospitalCancelledorderRecyclerview
import com.rootscare.serviceprovider.ui.hospital.hospitalordermanagement.adapter.AdapterHospitalPastOrderRecyclerview
import com.rootscare.serviceprovider.ui.hospital.hospitalordermanagement.adapter.AdapterHospitalUpcomingOrderRecyclerview
import com.rootscare.serviceprovider.ui.login.subfragment.login.FragmentLogin
import kotlinx.android.synthetic.main.fragment_hospital_order_management.*
import java.util.*

class FragmentHospitalOrderManagement : BaseFragment<FragmentHospitalOrderManagementBinding, FragmentHospitalOrderManagementViewModel>(),
    FragmentHospitalOrderManagementNavigator {
    private var fragmentHospitalOrderManagementBinding: FragmentHospitalOrderManagementBinding? = null
    private var fragmentHospitalOrderManagementViewModel: FragmentHospitalOrderManagementViewModel? = null
    private var lastPosition: Int? = null

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_hospital_order_management
    override val viewModel: FragmentHospitalOrderManagementViewModel
        get() {
            fragmentHospitalOrderManagementViewModel = ViewModelProviders.of(this).get(
                FragmentHospitalOrderManagementViewModel::class.java
            )
            return fragmentHospitalOrderManagementViewModel as FragmentHospitalOrderManagementViewModel
        }

    companion object {
        fun newInstance(): FragmentHospitalOrderManagement {
            val args = Bundle()
            val fragment = FragmentHospitalOrderManagement()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentHospitalOrderManagementViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHospitalOrderManagementBinding = viewDataBinding
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            val getDoctorUpcomingAppointmentRequest = getdoctorhospital()
            getDoctorUpcomingAppointmentRequest.hospital_id =
                fragmentHospitalOrderManagementViewModel?.appSharedPref?.loginUserId
//            getDoctorUpcomingAppointmentRequest.userId="18"
            fragmentHospitalOrderManagementViewModel!!.apidoctorappointmentPastList(
                getDoctorUpcomingAppointmentRequest
            )

        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT)
                .show()
        }

        fragmentHospitalOrderManagementBinding?.btnHospitalPastOrder?.setOnClickListener(
            View.OnClickListener {
                btn_hospital_upcoming_order.setBackgroundResource(R.drawable.oval_blue_bg)
                btn_hospital_past_order.setBackgroundResource(R.drawable.oval_green_bg)
                btn_hospital_cancelled_order.setBackgroundResource(R.drawable.oval_blue_bg)
                val getDoctorUpcommingAppointmentRequest = getdoctorhospital()
                getDoctorUpcommingAppointmentRequest.hospital_id =
                    fragmentHospitalOrderManagementViewModel?.appSharedPref?.loginUserId
//            getDoctorUpcommingAppointmentRequest.userId="18"
                fragmentHospitalOrderManagementViewModel!!.apidoctorappointmentPastList(
                    getDoctorUpcommingAppointmentRequest
                )
                fragmentHospitalOrderManagementBinding?.llHospitalPastOrder?.visibility = View.GONE
                fragmentHospitalOrderManagementBinding?.llHospitalUpcomingOrder?.visibility = View.GONE
                fragmentHospitalOrderManagementBinding?.llHospitalCancelledOrder?.visibility = View.VISIBLE
                //setUpViewPastAppointlistingRecyclerview()
            })

        fragmentHospitalOrderManagementBinding?.btnHospitalUpcomingOrder?.setOnClickListener(
            View.OnClickListener {
                btn_hospital_upcoming_order.setBackgroundResource(R.drawable.oval_green_bg)
                btn_hospital_past_order.setBackgroundResource(R.drawable.oval_blue_bg)
                btn_hospital_cancelled_order.setBackgroundResource(R.drawable.oval_blue_bg)
                baseActivity?.showLoading()
                val getDoctorUpcommingAppointmentRequest = getdoctorhospital()
                getDoctorUpcommingAppointmentRequest.hospital_id =
                    fragmentHospitalOrderManagementViewModel?.appSharedPref?.loginUserId
                fragmentHospitalOrderManagementViewModel!!.apidoctorappointmentupcomingList(
                    getDoctorUpcommingAppointmentRequest
                )
                fragmentHospitalOrderManagementBinding?.llHospitalPastOrder?.visibility = View.GONE
                fragmentHospitalOrderManagementBinding?.llHospitalUpcomingOrder?.visibility = View.VISIBLE
                fragmentHospitalOrderManagementBinding?.llHospitalCancelledOrder?.visibility = View.GONE


            })

        fragmentHospitalOrderManagementBinding?.btnHospitalCancelledOrder?.setOnClickListener(
            View.OnClickListener {
                btn_hospital_upcoming_order.setBackgroundResource(R.drawable.oval_blue_bg)
                btn_hospital_past_order.setBackgroundResource(R.drawable.oval_blue_bg)
                btn_hospital_cancelled_order.setBackgroundResource(R.drawable.oval_green_bg)
                fragmentHospitalOrderManagementBinding?.llHospitalPastOrder?.visibility = View.GONE
                fragmentHospitalOrderManagementBinding?.llHospitalUpcomingOrder?.visibility = View.GONE
                fragmentHospitalOrderManagementBinding?.llHospitalCancelledOrder?.visibility = View.VISIBLE
                //setUpViewCancelledAppointmentlistingRecyclerview()
                baseActivity?.showLoading()
                val getDoctorUpcommingAppointmentRequest = getdoctorhospital()
                getDoctorUpcommingAppointmentRequest.hospital_id = "114"
                //  fragmentHospitalManageAppointmentsViewModel?.appSharedPref?.loginUserId
                fragmentHospitalOrderManagementViewModel!!.apidoctorappointmentcancelList(
                    getDoctorUpcommingAppointmentRequest
                )
            })

    }

    private fun setUpDoctorMyAppointmentlistingRecyclerview(requestedAppointmentList: LinkedList<ResultItemLab>?) {
        assert(fragmentHospitalOrderManagementBinding!!.recyclerViewHospitalPastOrder != null)
        val recyclerView =
            fragmentHospitalOrderManagementBinding!!.recyclerViewHospitalPastOrder
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter =
            AdapterHospitalPastOrderRecyclerview(requestedAppointmentList, requireContext())
        recyclerView.adapter = contactListAdapter
        contactListAdapter.recyclerViewItemClickWithView = object : OnItemClickWithReportIdListener {
            override fun onItemClick(id: Int) {
                lastPosition = id
                println("test $lastPosition")
                (activity as HospitalHomeActivity).checkFragmentInBackStackAndOpen(
                    FragmentAppointmentDetailsForAll.newInstance(requestedAppointmentList!![id].patientId!!, "doctor")
                )
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

    private fun setUpDoctorMyUpcomingAppointmentlistingRecyclerview(requestedappointmentList: LinkedList<ResultItemLab>?) {
        val recyclerView =
            fragmentHospitalOrderManagementBinding!!.recyclerViewHospitalUpcomingOrder
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter =
            AdapterHospitalUpcomingOrderRecyclerview(requestedappointmentList, requireContext())
        recyclerView.adapter = contactListAdapter
        contactListAdapter.recyclerViewItemClickWithView = object : OnItemClickWithReportIdListener {
            override fun onItemClick(id: Int) {
                lastPosition = id
                (activity as HospitalHomeActivity).checkFragmentInBackStackAndOpen(
                    FragmentAppointmentDetailsForAll.newInstance(contactListAdapter.upcomingAppointmentList!![id].patientId!!, "doctor")
                )
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

    private fun setUpDoctorCancelAppointmentlistingRecyclerview(requestedappointmentList: LinkedList<ResultItemLab>?) {
        assert(fragmentHospitalOrderManagementBinding!!.recyclerViewHospitalCancelledOrder != null)
        val recyclerView =
            fragmentHospitalOrderManagementBinding!!.recyclerViewHospitalCancelledOrder
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter =
            AdapterHospitalCancelledorderRecyclerview(requestedappointmentList, requireContext())
        recyclerView.adapter = contactListAdapter
        contactListAdapter.recyclerViewItemClickWithView = object : OnItemClickWithReportIdListener {
            override fun onItemClick(id: Int) {
                lastPosition = id
                (activity as HospitalHomeActivity).checkFragmentInBackStackAndOpen(
                    FragmentAppointmentDetailsForAll.newInstance(contactListAdapter.upcomingAppointmentList!![id].patientId!!, "doctor")
                )
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

    // Set up recycler view for service listing if available

    // Set up recycler view for service listing if available

    // Set up recycler view for service listing if available
    override fun responseListPastAppointment(getDoctorRequestAppointmentResponse: ResponseLabPastAppointment) {
        baseActivity?.hideLoading()
        if (getDoctorRequestAppointmentResponse.code.equals("200")) {
            if (getDoctorRequestAppointmentResponse.result != null && getDoctorRequestAppointmentResponse.result?.size!! > 0) {
                fragmentHospitalOrderManagementBinding?.recyclerViewHospitalCancelledOrder?.visibility =
                    View.VISIBLE
                fragmentHospitalOrderManagementBinding?.tvNoDate?.visibility = View.GONE
                setUpDoctorMyAppointmentlistingRecyclerview(getDoctorRequestAppointmentResponse.result)
            } else {
                fragmentHospitalOrderManagementBinding?.recyclerViewHospitalCancelledOrder?.visibility =
                    View.GONE
                fragmentHospitalOrderManagementBinding?.tvNoDate?.visibility = View.VISIBLE
                fragmentHospitalOrderManagementBinding?.tvNoDate?.text = "Doctor Appointment Not Found."
            }

        } else {
            fragmentHospitalOrderManagementBinding?.recyclerViewHospitalCancelledOrder?.visibility =
                View.GONE
            fragmentHospitalOrderManagementBinding?.tvNoDate?.visibility = View.VISIBLE
            fragmentHospitalOrderManagementBinding?.tvNoDate?.text = getDoctorRequestAppointmentResponse.message
            Toast.makeText(
                activity,
                getDoctorRequestAppointmentResponse.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun responseListUpcomingAppointment(response: ResponseLabPastAppointment) {
        baseActivity?.hideLoading()
        if (response.code.equals("200")) {
            if (response.result != null && response.result?.size!! > 0) {
                fragmentHospitalOrderManagementBinding?.recyclerViewHospitalUpcomingOrder?.visibility =
                    View.VISIBLE
                fragmentHospitalOrderManagementBinding?.tvNoDate?.visibility = View.GONE
                setUpDoctorMyUpcomingAppointmentlistingRecyclerview(response.result)
            } else {
                fragmentHospitalOrderManagementBinding?.recyclerViewHospitalUpcomingOrder?.visibility =
                    View.GONE
                fragmentHospitalOrderManagementBinding?.tvNoDate?.visibility = View.VISIBLE
                fragmentHospitalOrderManagementBinding?.tvNoDate?.text = "Doctor Appointment Not Found."
            }

        } else {
            fragmentHospitalOrderManagementBinding?.recyclerViewHospitalUpcomingOrder?.visibility =
                View.GONE
            fragmentHospitalOrderManagementBinding?.tvNoDate?.visibility = View.VISIBLE
            fragmentHospitalOrderManagementBinding?.tvNoDate?.text = response.message
            Toast.makeText(
                activity,
                response.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun responseLisCancelledAppointment(response: ResponseLabPastAppointment) {
        baseActivity?.hideLoading()
        if (response.code.equals("200")) {
            if (response.result != null && response.result?.size!! > 0) {
                fragmentHospitalOrderManagementBinding?.recyclerViewHospitalCancelledOrder?.visibility =
                    View.VISIBLE
                fragmentHospitalOrderManagementBinding?.tvNoDate?.visibility = View.GONE
                setUpDoctorCancelAppointmentlistingRecyclerview(response.result)
            } else {
                fragmentHospitalOrderManagementBinding?.recyclerViewHospitalCancelledOrder?.visibility =
                    View.GONE
                fragmentHospitalOrderManagementBinding?.tvNoDate?.visibility = View.VISIBLE
                fragmentHospitalOrderManagementBinding?.tvNoDate?.text = "Doctor Appointment Not Found."
            }

        } else {
            fragmentHospitalOrderManagementBinding?.recyclerViewHospitalCancelledOrder?.visibility =
                View.GONE
            fragmentHospitalOrderManagementBinding?.tvNoDate?.visibility = View.VISIBLE
            fragmentHospitalOrderManagementBinding?.tvNoDate?.text = response.message
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