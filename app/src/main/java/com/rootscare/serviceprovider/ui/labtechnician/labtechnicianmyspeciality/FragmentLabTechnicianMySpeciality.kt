package com.rootscare.serviceprovider.ui.labtechnician.labtechnicianmyspeciality

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.dialog.CommonDialog
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentLabtechnicianMySpecialityBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.labtechnician.home.LabTechnicianHomeActivity
import com.rootscare.serviceprovider.ui.labtechnician.labtechnicianmyappointment.FragmentLabtechnicianMyAppointment
import com.rootscare.serviceprovider.ui.labtechnician.labtechnicianmyappointment.adapter.AdapterLabTechnicianMyAppointmentRecyclerview
import com.rootscare.serviceprovider.ui.labtechnician.labtechnicianmyappointment.subfragment.FragmentLabTechnicianMyAppointmentDetails
import com.rootscare.serviceprovider.ui.labtechnician.labtechnicianmyspeciality.adapter.AdapterLabTechnicianMySpecilitiesList

class FragmentLabTechnicianMySpeciality :
    BaseFragment<FragmentLabtechnicianMySpecialityBinding, FragmentLabTechnicianMySpecialityViewModel>(),
    FragmentLabTechnicianMySpecialityNavigator {
    private var fragmentLabtechnicianMySpecialityBinding: FragmentLabtechnicianMySpecialityBinding? = null
    private var fragmentLabTechnicianMySpecialityViewModel: FragmentLabTechnicianMySpecialityViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_labtechnician_my_speciality
    override val viewModel: FragmentLabTechnicianMySpecialityViewModel
        get() {
            fragmentLabTechnicianMySpecialityViewModel = ViewModelProviders.of(this).get(
                FragmentLabTechnicianMySpecialityViewModel::class.java!!
            )
            return fragmentLabTechnicianMySpecialityViewModel as FragmentLabTechnicianMySpecialityViewModel
        }

    companion object {
        fun newInstance(): FragmentLabTechnicianMySpeciality {
            val args = Bundle()
            val fragment = FragmentLabTechnicianMySpeciality()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentLabTechnicianMySpecialityViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentLabtechnicianMySpecialityBinding = viewDataBinding
        setUpDoctorMyAppointmentListingRecyclerview()
        fragmentLabtechnicianMySpecialityBinding?.txtAddSpeciality?.setOnClickListener {
            CommonDialog.showDialogForAddLabtechnicianSpeciality(requireContext(), object :
                DialogClickCallback {
                override fun onConfirm() {

                }

                override fun onDismiss() {
                }
            })
        }

//
    }

    // Set up recycler view for service listing if available
    private fun setUpDoctorMyAppointmentListingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentLabtechnicianMySpecialityBinding!!.recyclerViewRootscareLabtechnicianMyappointment != null)
        val recyclerView = fragmentLabtechnicianMySpecialityBinding!!.recyclerViewRootscareLabtechnicianMyappointment
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterLabTechnicianMySpecilitiesList(requireContext())
        recyclerView.adapter = contactListAdapter
//        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
//            override fun onItemClick(id: Int) {
//                (activity as LabTechnicianHomeActivity).checkFragmentInBackstackAndOpen(
//                    FragmentLabTechnicianMyAppointmentDetails.newInstance())
//            }
//
//        }

    }

}