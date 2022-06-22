package com.rootscare.serviceprovider.ui.caregiver.caregiverprofile

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentCaregiverProfileBinding
import com.rootscare.serviceprovider.ui.babySitter.babySitterProfile.FragmentBabySitterProfile
import com.rootscare.serviceprovider.ui.babySitter.babySitterProfile.FragmentBabySitterProfileNavigator
import com.rootscare.serviceprovider.ui.babySitter.babySitterProfile.FragmentBabySitterProfileViewModel
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.babySitter.babySitterProfile.adapter.AdapterBabySitterFeesListRecyclerView
import com.rootscare.serviceprovider.ui.babySitter.babySitterProfile.adapter.AdapterBabySitterUploadImportantDocument
import com.rootscare.serviceprovider.ui.caregiver.home.CaregiverHomeActivity
import com.rootscare.serviceprovider.ui.doctor.profile.editdoctoreprofile.FragmentEditDoctorProfile

class FragmentCaregiverProfile: BaseFragment<FragmentCaregiverProfileBinding, FragmentBabySitterProfileViewModel>(),
    FragmentBabySitterProfileNavigator {
    private var fragmentCaregiverProfileBinding: FragmentCaregiverProfileBinding? = null
    private var fragmentBabySitterProfileViewModel: FragmentBabySitterProfileViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_caregiver_profile
    override val viewModel: FragmentBabySitterProfileViewModel
        get() {
            fragmentBabySitterProfileViewModel = ViewModelProviders.of(this).get(
                FragmentBabySitterProfileViewModel::class.java
            )
            return fragmentBabySitterProfileViewModel as FragmentBabySitterProfileViewModel
        }

    companion object {
        fun newInstance(): FragmentBabySitterProfile {
            val args = Bundle()
            val fragment = FragmentBabySitterProfile()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentBabySitterProfileViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentCaregiverProfileBinding = viewDataBinding
//        setUpViewSeeAllNursesCategorieslistingRecyclerview()
        setUpViewNursesFeeslistingRecyclerview()
        setUpViewPrescriptionlistingRecyclerview()
        fragmentCaregiverProfileBinding?.btnCaregiverEditProfile?.setOnClickListener {
            (activity as CaregiverHomeActivity).checkFragmentInBackStackAndOpen(
                // FragmentCaregiverProfileEdit.newInstance())
                FragmentEditDoctorProfile.newInstance("caregiver")
            )
        }


    }


    // Set up recycler view for service listing if available
    private fun setUpViewNursesFeeslistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentCaregiverProfileBinding!!.recyclerViewCaregiverFeesListing != null)
        val recyclerView = fragmentCaregiverProfileBinding!!.recyclerViewCaregiverFeesListing
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterBabySitterFeesListRecyclerView(context!!)
        recyclerView.adapter = contactListAdapter
    }

    // Set up recycler view for service listing if available
    private fun setUpViewPrescriptionlistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentCaregiverProfileBinding!!.recyclerViewCaregiverImportentDocument != null)
        val recyclerView = fragmentCaregiverProfileBinding!!.recyclerViewCaregiverImportentDocument
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterBabySitterUploadImportantDocument(context!!)
        recyclerView.adapter = contactListAdapter


    }

}