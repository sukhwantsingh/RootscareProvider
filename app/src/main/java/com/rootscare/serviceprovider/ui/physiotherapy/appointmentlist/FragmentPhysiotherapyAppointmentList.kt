package com.rootscare.serviceprovider.ui.physiotherapy.appointmentlist

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentPhysicitherapyAppointmentlistBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.labtechnician.labtechnicianmyappointment.adapter.AdapterLabTechnicianMyAppointmentRecyclerview
import com.rootscare.serviceprovider.ui.labtechnician.labtechnicianmyappointment.subfragment.FragmentLabTechnicianMyAppointmentDetails
import com.rootscare.serviceprovider.ui.physiotherapy.home.PhysiotherapyHomeActivity

class FragmentPhysiotherapyAppointmentList :
    BaseFragment<FragmentPhysicitherapyAppointmentlistBinding, FragmentPhysiotherapyAppointmentListViewModel>(),
    FragmentPhysiotherapyAppointmentListNavigator {
    private var fragmentPhysiotherapyAppointmentListBinding: FragmentPhysicitherapyAppointmentlistBinding? = null
    private var fragmentPhysiotherapyAppointmentListViewModel: FragmentPhysiotherapyAppointmentListViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_physicitherapy_appointmentlist
    override val viewModel: FragmentPhysiotherapyAppointmentListViewModel
        get() {
            fragmentPhysiotherapyAppointmentListViewModel = ViewModelProviders.of(this).get(
                FragmentPhysiotherapyAppointmentListViewModel::class.java
            )
            return fragmentPhysiotherapyAppointmentListViewModel as FragmentPhysiotherapyAppointmentListViewModel
        }

    companion object {
        fun newInstance(): FragmentPhysiotherapyAppointmentList {
            val args = Bundle()
            val fragment = FragmentPhysiotherapyAppointmentList()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentPhysiotherapyAppointmentListViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentPhysiotherapyAppointmentListBinding = viewDataBinding
        setUpDoctorMyAppointmentListingRecyclerview()
    }

    // Set up recycler view for service listing if available
    private fun setUpDoctorMyAppointmentListingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
//        assert(fragmentPhysiotherapyAppointmentListBinding!!.recyclerViewRootscarePhysicotherapyMyappointment != null)
        val recyclerView = fragmentPhysiotherapyAppointmentListBinding!!.recyclerViewRootscarePhysicotherapyMyappointment
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterLabTechnicianMyAppointmentRecyclerview(requireContext())
        recyclerView.adapter = contactListAdapter
        contactListAdapter.recyclerViewItemClickWithView = object : OnItemClikWithIdListener {
            override fun onItemClick(id: Int) {
                (activity as PhysiotherapyHomeActivity).checkFragmentInBackStackAndOpen(
                    FragmentLabTechnicianMyAppointmentDetails.newInstance()
                )
            }

            override fun onAcceptBtnClick(id: String, text: String) {
            }

            override fun onRejectBtnClick(id: String, text: String) {
            }

        }

    }
}
