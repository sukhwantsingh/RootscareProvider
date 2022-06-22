package com.rootscare.serviceprovider.ui.hospital.hospitalmanageappointments

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.rootscare.data.model.request.hospital.getdoctorhospital
import com.rootscare.data.model.response.doctor.appointment.pastappointment.ResponsePastAppointment
import com.rootscare.data.model.response.doctor.appointment.pastappointment.ResultItem
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentHospitalManageAppointmentBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.FragmentAppointmentDetailsForAll
import com.rootscare.serviceprovider.ui.hospital.HospitalHomeActivity
import com.rootscare.serviceprovider.ui.hospital.hospitalmanageappointments.adapter.AdapterHospitalCancelledAppointmentRecyclerview
import com.rootscare.serviceprovider.ui.hospital.hospitalmanageappointments.adapter.AdapterHospitalPastAppointmentRecyclerview
import com.rootscare.serviceprovider.ui.hospital.hospitalmanageappointments.adapter.AdapterHospitalUpcomingAppointmentRecyclerview
import com.rootscare.serviceprovider.ui.login.subfragment.login.FragmentLogin
import kotlinx.android.synthetic.main.fragment_hospital_manage_appointment.*
import java.util.*

class FragmentHospitalManageAppointments :
    BaseFragment<FragmentHospitalManageAppointmentBinding, FragmentHospitalManageAppointmentsViewModel>(),
    FragmentHospitalManageAppointmentsNavigator {
    private var fragmentHospitalManageAppointmentBinding: FragmentHospitalManageAppointmentBinding? = null
    private var fragmentHospitalManageAppointmentsViewModel: FragmentHospitalManageAppointmentsViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_hospital_manage_appointment
    private var lastPosition: Int? = null

    override val viewModel: FragmentHospitalManageAppointmentsViewModel
        get() {
            fragmentHospitalManageAppointmentsViewModel = ViewModelProviders.of(this).get(
                FragmentHospitalManageAppointmentsViewModel::class.java
            )
            return fragmentHospitalManageAppointmentsViewModel as FragmentHospitalManageAppointmentsViewModel
        }

    companion object {
        fun newInstance(): FragmentHospitalManageAppointments {
            val args = Bundle()
            val fragment = FragmentHospitalManageAppointments()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentHospitalManageAppointmentsViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHospitalManageAppointmentBinding = viewDataBinding


        setUpTabLayout()
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            val getDoctorUpcomingAppointmentRequest = getdoctorhospital()
            getDoctorUpcomingAppointmentRequest.hospital_id =
                fragmentHospitalManageAppointmentsViewModel?.appSharedPref?.loginUserId
//            getDoctorUpcomingAppointmentRequest.userId="18"
            fragmentHospitalManageAppointmentsViewModel!!.apiDoctorAppointmentPastListHospital(
                getDoctorUpcomingAppointmentRequest
            )
            fragmentHospitalManageAppointmentsViewModel!!.apiPathologyAppointmentPastListHospital(
                getDoctorUpcomingAppointmentRequest
            )

        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT)
                .show()
        }
        //  setUpViewPastAppointlistingRecyclerview()
        fragmentHospitalManageAppointmentBinding?.btnHospitalPastAppointment?.setOnClickListener {
            btn_hospital_upcoming_appointment.setBackgroundResource(R.drawable.oval_blue_bg)
            btn_hospital_past_appointment.setBackgroundResource(R.drawable.oval_green_bg)
            btn_hospital_cancelled_appointment.setBackgroundResource(R.drawable.oval_blue_bg)
            val getDoctorUpcomingAppointmentRequest = getdoctorhospital()
            getDoctorUpcomingAppointmentRequest.hospital_id =
                fragmentHospitalManageAppointmentsViewModel?.appSharedPref?.loginUserId
//            getDoctorUpcomingAppointmentRequest.userId="18"
            fragmentHospitalManageAppointmentsViewModel!!.apiDoctorAppointmentPastListHospital(
                getDoctorUpcomingAppointmentRequest
            )
            fragmentHospitalManageAppointmentsViewModel!!.apiPathologyAppointmentPastListHospital(
                getDoctorUpcomingAppointmentRequest
            )
            fragmentHospitalManageAppointmentBinding?.llHospitalPastAppointment?.visibility = View.VISIBLE
            fragmentHospitalManageAppointmentBinding?.llHospitalUpcomingAppointment?.visibility = View.GONE
            fragmentHospitalManageAppointmentBinding?.llHospitalCancelledAppointment?.visibility = View.GONE
            //setUpViewPastAppointlistingRecyclerview()
        }
        fragmentHospitalManageAppointmentBinding?.btnHospitalUpcomingAppointment?.setOnClickListener {
           /* btn_hospital_upcoming_appointment.setBackgroundResource(R.drawable.oval_green_bg)
            btn_hospital_past_appointment.setBackgroundResource(R.drawable.oval_blue_bg)
            btn_hospital_cancelled_appointment.setBackgroundResource(R.drawable.oval_blue_bg)
            baseActivity?.showLoading()
            val getDoctorUpcomingAppointmentRequest = getdoctorhospital()
            getDoctorUpcomingAppointmentRequest.hospital_id =
                fragmentHospitalManageAppointmentsViewModel?.appSharedPref?.loginUserId
            fragmentHospitalManageAppointmentsViewModel!!.apiDoctorAppointmentUpcomingListHospital(
                getDoctorUpcomingAppointmentRequest
            )
            fragmentHospitalManageAppointmentsViewModel!!.apiPathologyAppointmentUpcomingListHospital(
                getDoctorUpcomingAppointmentRequest
            )
            fragmentHospitalManageAppointmentBinding?.llHospitalPastAppointment?.visibility = View.GONE
            fragmentHospitalManageAppointmentBinding?.llHospitalUpcomingAppointment?.visibility = View.VISIBLE
            fragmentHospitalManageAppointmentBinding?.llHospitalCancelledAppointment?.visibility = View.GONE
      */  }

        fragmentHospitalManageAppointmentBinding?.btnHospitalCancelledAppointment?.setOnClickListener {
            btn_hospital_upcoming_appointment.setBackgroundResource(R.drawable.oval_blue_bg)
            btn_hospital_past_appointment.setBackgroundResource(R.drawable.oval_blue_bg)
            btn_hospital_cancelled_appointment.setBackgroundResource(R.drawable.oval_green_bg)
            fragmentHospitalManageAppointmentBinding?.llHospitalPastAppointment?.visibility = View.GONE
            fragmentHospitalManageAppointmentBinding?.llHospitalUpcomingAppointment?.visibility = View.GONE
            fragmentHospitalManageAppointmentBinding?.llHospitalCancelledAppointment?.visibility = View.VISIBLE
            //setUpViewCancelledAppointmentlistingRecyclerview()
            baseActivity?.showLoading()
            val getDoctorUpcomingAppointmentRequest = getdoctorhospital()
//                getDoctorUpcomingAppointmentRequest.hospital_id = "114"
            getDoctorUpcomingAppointmentRequest.hospital_id =
                fragmentHospitalManageAppointmentsViewModel?.appSharedPref?.loginUserId
            fragmentHospitalManageAppointmentsViewModel!!.apiDoctorAppointmentCancelList(
                getDoctorUpcomingAppointmentRequest
            )
            fragmentHospitalManageAppointmentsViewModel!!.apiPathologyAppointmentCancelListHospital(
                getDoctorUpcomingAppointmentRequest
            )
        }

    }

    // Set up recycler view for service listing if available
/*
    private fun setUpViewPastAppointListingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentHospitalManageAppointmentBinding!!.recyclerViewHospitalPastAppointment != null)
        val recyclerView = fragmentHospitalManageAppointmentBinding!!.recyclerViewHospitalPastAppointment
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterHospitalPastAppointmentRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter
//        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
//            override fun onItemClick(id: Int) {
//                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                    FragmentDoctorListingDetails.newInstance())
//            }
//
//        }


    }
*/

    // Set up recycler view for service listing if available
/*
    private fun setUpViewUpcomingAppontmentlistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentHospitalManageAppointmentBinding!!.recyclerViewHospitalUpcomingAppointment != null)
        val recyclerView = fragmentHospitalManageAppointmentBinding!!.recyclerViewHospitalUpcomingAppointment
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterHospitalUpcomingAppointmentRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter
*/
/*
        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
            override fun onItemClick(id: Int) {
                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
                    FragmentDoctorListingDetails.newInstance())
            }

        }
*//*



    }
*/

    private fun setUpTabLayout() {
        val tabTitles: MutableList<String> =
            ArrayList()
        tabTitles.add("Ongoing")
        tabTitles.add("Pending")
        tabTitles.add("Upcoming")
        tabTitles.add("Past")

        for (i in tabTitles.indices) {
            fragmentHospitalManageAppointmentBinding?.tablayout?.addTab(
                fragmentHospitalManageAppointmentBinding?.tablayout?.newTab()?.setText(
                    tabTitles[i]
                )!!, i
            )
        }
//        fragmentAppointmentBinding?.tablayout?.tabGravity = TabLayout.GRAVITY_CENTER
        for (i in 0 until fragmentHospitalManageAppointmentBinding?.tablayout?.tabCount!!) {
            val view: View =
                LayoutInflater.from(activity).inflate(R.layout.tab_item_appointment_layout, null)
            val tab_item_tv = view.findViewById<TextView>(R.id.tab_item_tv)
            tab_item_tv.text = tabTitles[i]
            if (i == 0) {
                tab_item_tv.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.background_white
                    )
                )
                view.background = resources.getDrawable(R.drawable.tab_background_selected)
            } else {
                tab_item_tv.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.background_white
                    )
                )
//                tab_item_tv.setTextColor(resources.getColor(R.color.modified_black_1))
                view.background = resources.getDrawable(R.drawable.tab_background_unselected)
            }
            fragmentHospitalManageAppointmentBinding?.tablayout?.getTabAt(i)?.customView = view
        }
        fragmentHospitalManageAppointmentBinding?.tablayout?.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                for (i in 0 until fragmentHospitalManageAppointmentBinding?.tablayout?.tabCount!!) {
//                    val view: View = fragmentProfileBinding?.tablayout?.getTabAt(i)?.customView!!
                    val view: View = Objects.requireNonNull<View>(
                        fragmentHospitalManageAppointmentBinding?.tablayout?.getTabAt(i)?.customView
                    )

                    val tab_item_tv =
                        Objects.requireNonNull(view)
                            .findViewById<TextView>(R.id.tab_item_tv)
                    if (i == tab.position) {
                        tab_item_tv.setTextColor(resources.getColor(R.color.background_white))
                        Objects.requireNonNull(view).background =
                            resources.getDrawable(R.drawable.tab_background_selected)
                    } else {
                        tab_item_tv.setTextColor(
                            ContextCompat.getColor(
                                activity!!,
                                R.color.background_white
                            )
                        )
//                        tab_item_tv.setTextColor(resources.getColor(R.color.modified_black_1))
                        Objects.requireNonNull(view).background =
                            resources.getDrawable(R.drawable.tab_background_unselected)
                    }
                }
                when (tab.position) {
                    0 -> {

                    }
                    1 -> {

                    }
                    2 -> {
                        btn_hospital_upcoming_appointment.setBackgroundResource(R.drawable.oval_green_bg)
                        btn_hospital_past_appointment.setBackgroundResource(R.drawable.oval_blue_bg)
                        btn_hospital_cancelled_appointment.setBackgroundResource(R.drawable.oval_blue_bg)
                        baseActivity?.showLoading()
                        val getDoctorUpcomingAppointmentRequest = getdoctorhospital()
                        getDoctorUpcomingAppointmentRequest.hospital_id =
                            fragmentHospitalManageAppointmentsViewModel?.appSharedPref?.loginUserId
                        fragmentHospitalManageAppointmentsViewModel!!.apiDoctorAppointmentUpcomingListHospital(
                            getDoctorUpcomingAppointmentRequest
                        )
                        fragmentHospitalManageAppointmentsViewModel!!.apiPathologyAppointmentUpcomingListHospital(
                            getDoctorUpcomingAppointmentRequest
                        )
                        fragmentHospitalManageAppointmentBinding?.llHospitalPastAppointment?.visibility = View.GONE
                        fragmentHospitalManageAppointmentBinding?.llHospitalUpcomingAppointment?.visibility = View.VISIBLE
                        fragmentHospitalManageAppointmentBinding?.llHospitalCancelledAppointment?.visibility = View.GONE


                    }
                    3 -> {




                    }

                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

    }    // Set up recycler view for service listing if available
    private fun setUpDoctorMyAppointmentListingRecyclerview(requestedAppointmentList: LinkedList<ResultItem>?) {
//        assert(fragmentHospitalManageAppointmentBinding!!.recyclerViewHospitalPastAppointment != null)
        val recyclerView =
            fragmentHospitalManageAppointmentBinding!!.recyclerViewHospitalPastAppointment
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter =
            AdapterHospitalPastAppointmentRecyclerview(requestedAppointmentList, requireContext(), true)
        recyclerView.adapter = contactListAdapter
        contactListAdapter.recyclerViewItemClickWithView = object : OnItemClikWithIdListener {
            override fun onItemClick(id: Int) {
                lastPosition = id
                (activity as HospitalHomeActivity).checkFragmentInBackStackAndOpen(
                    FragmentAppointmentDetailsForAll.newInstance(
                        contactListAdapter.upcomingAppointmentList!![id].id!!, "doctor"
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
                    val smoothScroller: RecyclerView.SmoothScroller = object : LinearSmoothScroller(context) {
                        override fun getVerticalSnapPreference(): Int {
                            return SNAP_TO_ANY
                        }
                    }
                    smoothScroller.targetPosition = lastPosition!!
                    gridLayoutManager.startSmoothScroll(smoothScroller)
                }
            }, 100)
        }

    }

    // Set up recycler view for service listing if available
    private fun setUpPathologyMyAppointmentListingRecyclerview(requestedAppointmentList: LinkedList<ResultItem>?) {
//        assert(fragmentHospitalManageAppointmentBinding!!.recyclerViewHospitalLabPastAppointment != null)
        val recyclerView =
            fragmentHospitalManageAppointmentBinding!!.recyclerViewHospitalLabPastAppointment
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter =
            AdapterHospitalPastAppointmentRecyclerview(requestedAppointmentList, requireContext(), false)
        recyclerView.adapter = contactListAdapter
        contactListAdapter.recyclerViewItemClickWithView = object : OnItemClikWithIdListener {
            override fun onItemClick(id: Int) {
                lastPosition = id
                (activity as HospitalHomeActivity).checkFragmentInBackStackAndOpen(
                    FragmentAppointmentDetailsForAll.newInstance(
                        contactListAdapter.upcomingAppointmentList!![id].id!!,
                        "lab-technician"
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
                    val smoothScroller: RecyclerView.SmoothScroller = object : LinearSmoothScroller(context) {
                        override fun getVerticalSnapPreference(): Int {
                            return SNAP_TO_ANY
                        }
                    }
                    smoothScroller.targetPosition = lastPosition!!
                    gridLayoutManager.startSmoothScroll(smoothScroller)
                }
            }, 100)
        }

    }

    private fun setUpDoctorMyUpcomingAppointmentListingRecyclerview(requestedAppointmentList: LinkedList<ResultItem>?) {
//        assert(fragmentHospitalManageAppointmentBinding!!.recyclerViewHospitalUpcomingAppointment != null)
        val recyclerView =
            fragmentHospitalManageAppointmentBinding!!.recyclerViewHospitalUpcomingAppointment
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter =
            AdapterHospitalUpcomingAppointmentRecyclerview(requestedAppointmentList, requireContext(), true)
        recyclerView.adapter = contactListAdapter
        contactListAdapter.recyclerViewItemClickWithView = object : OnItemClikWithIdListener {
            override fun onItemClick(id: Int) {
                lastPosition = id
                (activity as HospitalHomeActivity).checkFragmentInBackStackAndOpen(
                    FragmentAppointmentDetailsForAll.newInstance(
                        contactListAdapter.upcomingAppointmentList!![id].id!!, "doctor"
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
                    val smoothScroller: RecyclerView.SmoothScroller = object : LinearSmoothScroller(context) {
                        override fun getVerticalSnapPreference(): Int {
                            return SNAP_TO_ANY
                        }
                    }
                    smoothScroller.targetPosition = lastPosition!!
                    gridLayoutManager.startSmoothScroll(smoothScroller)
                }
            }, 100)
        }

    }

    private fun setUpPathologyMyUpcomingAppointmentListingRecyclerview(requestedAppointmentList: LinkedList<ResultItem>?) {
//        assert(fragmentHospitalManageAppointmentBinding!!.recyclerViewHospitalLabUpcomingAppointment != null)
        val recyclerView =
            fragmentHospitalManageAppointmentBinding!!.recyclerViewHospitalLabUpcomingAppointment
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter =
            AdapterHospitalUpcomingAppointmentRecyclerview(requestedAppointmentList, requireContext(), false)
        recyclerView.adapter = contactListAdapter
        contactListAdapter.recyclerViewItemClickWithView = object : OnItemClikWithIdListener {
            override fun onItemClick(id: Int) {
                lastPosition = id
                (activity as HospitalHomeActivity).checkFragmentInBackStackAndOpen(
                    FragmentAppointmentDetailsForAll.newInstance(
                        contactListAdapter.upcomingAppointmentList!![id].id!!, "lab-technician"
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
                    val smoothScroller: RecyclerView.SmoothScroller = object : LinearSmoothScroller(context) {
                        override fun getVerticalSnapPreference(): Int {
                            return SNAP_TO_ANY
                        }
                    }
                    smoothScroller.targetPosition = lastPosition!!
                    gridLayoutManager.startSmoothScroll(smoothScroller)
                }
            }, 100)
        }

    }

    private fun setUpDoctorCancelAppointmentListingRecyclerview(
        requestedAppointmentList: LinkedList<ResultItem>?, isDoctor: Boolean
    ) {
//        assert(fragmentHospitalManageAppointmentBinding!!.recyclerViewHospitalCancelledAppointment != null)
        val recyclerView =
            fragmentHospitalManageAppointmentBinding!!.recyclerViewHospitalCancelledAppointment
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        val contactListAdapter =
            AdapterHospitalCancelledAppointmentRecyclerview(requestedAppointmentList, requireContext(), isDoctor)
        recyclerView.adapter = contactListAdapter
        contactListAdapter.recyclerViewItemClickWithView = object : OnItemClikWithIdListener {
            override fun onItemClick(id: Int) {
                lastPosition = id
                (activity as HospitalHomeActivity).checkFragmentInBackStackAndOpen(
                    FragmentAppointmentDetailsForAll.newInstance(
                        contactListAdapter.upcomingAppointmentList!![id].id!!, "doctor"
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
                    val smoothScroller: RecyclerView.SmoothScroller = object : LinearSmoothScroller(context) {
                        override fun getVerticalSnapPreference(): Int {
                            return SNAP_TO_ANY
                        }
                    }
                    smoothScroller.targetPosition = lastPosition!!
                    gridLayoutManager.startSmoothScroll(smoothScroller)
                }
            }, 100)
        }

    }

    override fun responseListPastAppointmentDoctor(response: ResponsePastAppointment) {
        baseActivity?.hideLoading()
        if (response.code.equals("200")) {
            if (response.result != null && response.result?.size!! > 0) {
                fragmentHospitalManageAppointmentBinding?.recyclerViewHospitalPastAppointment?.visibility =
                    View.VISIBLE
                fragmentHospitalManageAppointmentBinding?.tvNoDate?.visibility = View.GONE
                setUpDoctorMyAppointmentListingRecyclerview(response.result)
            } else {
                fragmentHospitalManageAppointmentBinding?.recyclerViewHospitalPastAppointment?.visibility =
                    View.GONE
                fragmentHospitalManageAppointmentBinding?.tvNoDate?.visibility = View.VISIBLE
                fragmentHospitalManageAppointmentBinding?.tvNoDate?.text = "Doctor Appointment Not Found."
            }

        } else {
            fragmentHospitalManageAppointmentBinding?.recyclerViewHospitalPastAppointment?.visibility =
                View.GONE
            fragmentHospitalManageAppointmentBinding?.tvNoDate?.visibility = View.VISIBLE
            fragmentHospitalManageAppointmentBinding?.tvNoDate?.text = response.message
//            Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun responseListPastAppointmentHospital(response: ResponsePastAppointment) {
        baseActivity?.hideLoading()
        if (response.code.equals("200")) {
            if (response.result != null && response.result?.size!! > 0) {
                fragmentHospitalManageAppointmentBinding?.recyclerViewHospitalLabPastAppointment?.visibility =
                    View.VISIBLE
                fragmentHospitalManageAppointmentBinding?.tvNoDate?.visibility = View.GONE
                setUpPathologyMyAppointmentListingRecyclerview(response.result)
            } else {
                fragmentHospitalManageAppointmentBinding?.recyclerViewHospitalLabPastAppointment?.visibility =
                    View.GONE
                fragmentHospitalManageAppointmentBinding?.tvNoDate?.visibility = View.VISIBLE
                fragmentHospitalManageAppointmentBinding?.tvNoDate?.text = "Pathology Appointment Not Found."
            }

        } else {
            fragmentHospitalManageAppointmentBinding?.recyclerViewHospitalLabPastAppointment?.visibility =
                View.GONE
            fragmentHospitalManageAppointmentBinding?.tvNoDate?.visibility = View.VISIBLE
            fragmentHospitalManageAppointmentBinding?.tvNoDate?.text = response.message
//            Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun responseListUpcomingAppointmentDoctor(response: ResponsePastAppointment) {
        baseActivity?.hideLoading()
        if (response.code.equals("200")) {
            if (response.result != null && response.result?.size!! > 0) {
                fragmentHospitalManageAppointmentBinding?.recyclerViewHospitalUpcomingAppointment?.visibility =
                    View.VISIBLE
                fragmentHospitalManageAppointmentBinding?.tvUpcomingNoDate?.visibility = View.GONE
                setUpDoctorMyUpcomingAppointmentListingRecyclerview(response.result)
            } else {
                fragmentHospitalManageAppointmentBinding?.recyclerViewHospitalUpcomingAppointment?.visibility =
                    View.GONE
                fragmentHospitalManageAppointmentBinding?.tvUpcomingNoDate?.visibility = View.VISIBLE
                fragmentHospitalManageAppointmentBinding?.tvUpcomingNoDate?.text = "Doctor Appointment Not Found."
            }

        } else {
            fragmentHospitalManageAppointmentBinding?.recyclerViewHospitalUpcomingAppointment?.visibility =
                View.GONE
            fragmentHospitalManageAppointmentBinding?.tvUpcomingNoDate?.visibility = View.VISIBLE
            fragmentHospitalManageAppointmentBinding?.tvUpcomingNoDate?.text = response.message
//            Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun responseListUpcomingAppointmentHospital(response: ResponsePastAppointment) {
        baseActivity?.hideLoading()
        if (response.code.equals("200")) {
            if (response.result != null && response.result?.size!! > 0) {
                fragmentHospitalManageAppointmentBinding?.recyclerViewHospitalUpcomingAppointment?.visibility =
                    View.VISIBLE
                fragmentHospitalManageAppointmentBinding?.tvUpcomingNoDate?.visibility = View.GONE
                setUpPathologyMyUpcomingAppointmentListingRecyclerview(response.result)
            } else {
                fragmentHospitalManageAppointmentBinding?.recyclerViewHospitalUpcomingAppointment?.visibility =
                    View.GONE
                fragmentHospitalManageAppointmentBinding?.tvUpcomingNoDate?.visibility = View.VISIBLE
                fragmentHospitalManageAppointmentBinding?.tvUpcomingNoDate?.text = "Doctor Appointment Not Found."
            }

        } else {
            fragmentHospitalManageAppointmentBinding?.recyclerViewHospitalUpcomingAppointment?.visibility =
                View.GONE
            fragmentHospitalManageAppointmentBinding?.tvUpcomingNoDate?.visibility = View.VISIBLE
            fragmentHospitalManageAppointmentBinding?.tvUpcomingNoDate?.text = response.message
//            Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun responseLisCancelledAppointmentDoctor(response: ResponsePastAppointment) {
        baseActivity?.hideLoading()
        if (response.code.equals("200")) {
            if (response.result != null && response.result?.size!! > 0) {
                fragmentHospitalManageAppointmentBinding?.recyclerViewHospitalCancelledAppointment?.visibility =
                    View.VISIBLE
                fragmentHospitalManageAppointmentBinding?.tvCancelledNoDate?.visibility = View.GONE
                setUpDoctorCancelAppointmentListingRecyclerview(response.result, true)
            } else {
                fragmentHospitalManageAppointmentBinding?.recyclerViewHospitalCancelledAppointment?.visibility =
                    View.GONE
                fragmentHospitalManageAppointmentBinding?.tvCancelledNoDate?.visibility = View.VISIBLE
                fragmentHospitalManageAppointmentBinding?.tvCancelledNoDate?.text = "Doctor Appointment Not Found."
            }

        } else {
            fragmentHospitalManageAppointmentBinding?.recyclerViewHospitalCancelledAppointment?.visibility =
                View.GONE
            fragmentHospitalManageAppointmentBinding?.tvCancelledNoDate?.visibility = View.VISIBLE
            fragmentHospitalManageAppointmentBinding?.tvCancelledNoDate?.text = response.message
//            Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun responseLisCancelledAppointmentHospital(response: ResponsePastAppointment) {
        baseActivity?.hideLoading()
        if (response.code.equals("200")) {
            if (response.result != null && response.result?.size!! > 0) {
                fragmentHospitalManageAppointmentBinding?.recyclerViewHospitalCancelledAppointment?.visibility =
                    View.VISIBLE
                fragmentHospitalManageAppointmentBinding?.tvCancelledNoDate?.visibility = View.GONE
                setUpDoctorCancelAppointmentListingRecyclerview(response.result, false)
            } else {
                fragmentHospitalManageAppointmentBinding?.recyclerViewHospitalCancelledAppointment?.visibility =
                    View.GONE
                fragmentHospitalManageAppointmentBinding?.tvCancelledNoDate?.visibility = View.VISIBLE
                fragmentHospitalManageAppointmentBinding?.tvCancelledNoDate?.text = "Doctor Appointment Not Found."
            }

        } else {
            fragmentHospitalManageAppointmentBinding?.recyclerViewHospitalCancelledAppointment?.visibility =
                View.GONE
            fragmentHospitalManageAppointmentBinding?.tvCancelledNoDate?.visibility = View.VISIBLE
            fragmentHospitalManageAppointmentBinding?.tvCancelledNoDate?.text = response.message
//            Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
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