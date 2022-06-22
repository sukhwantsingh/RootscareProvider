package com.rootscare.serviceprovider.ui.labtechnician.home.subfragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentLabTechnicianHomeBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctorreviewandrating.FragmentReviewAndRating
import com.rootscare.serviceprovider.ui.labtechnician.home.LabTechnicianHomeActivity
import com.rootscare.serviceprovider.ui.labtechnician.labtechnicianmanageprofile.FragmentLabtechnicianManageProfile
import com.rootscare.serviceprovider.ui.labtechnician.labtechnicianmyappointment.FragmentLabtechnicianMyAppointment
import com.rootscare.serviceprovider.ui.labtechnician.labtechnicianmyspeciality.FragmentLabTechnicianMySpeciality

class FragmentLabTechnicianHome: BaseFragment<FragmentLabTechnicianHomeBinding, FragmentLabTechnicianHomeViewModel>(), FragmentLabTechnicianHomeNavigator {
    private var fragmentLabTechnicianHomeBinding: FragmentLabTechnicianHomeBinding? = null
    private var fragmentLabTechnicianHomeViewModel: FragmentLabTechnicianHomeViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_lab_technician_home
    override val viewModel: FragmentLabTechnicianHomeViewModel
        get() {
            fragmentLabTechnicianHomeViewModel = ViewModelProviders.of(this).get(
                FragmentLabTechnicianHomeViewModel::class.java)
            return fragmentLabTechnicianHomeViewModel as FragmentLabTechnicianHomeViewModel
        }

    companion object {
        fun newInstance(): FragmentLabTechnicianHome {
            val args = Bundle()
            val fragment = FragmentLabTechnicianHome()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentLabTechnicianHomeViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentLabTechnicianHomeBinding = viewDataBinding

        fragmentLabTechnicianHomeBinding?.cardViewManageProfile?.setOnClickListener(View.OnClickListener {
            (activity as LabTechnicianHomeActivity).checkFragmentInBackstackAndOpen(
                FragmentLabtechnicianManageProfile.newInstance())
        })

        fragmentLabTechnicianHomeBinding?.cardViewMyAppointment?.setOnClickListener(View.OnClickListener {
            (activity as LabTechnicianHomeActivity).checkFragmentInBackstackAndOpen(
                FragmentLabtechnicianMyAppointment.newInstance())
        })
        //Need To change when actual developement will be done
        fragmentLabTechnicianHomeBinding?.cardViewReviewAndRating?.setOnClickListener(View.OnClickListener {
            (activity as LabTechnicianHomeActivity).checkFragmentInBackstackAndOpen(
                FragmentReviewAndRating.newInstance())
        })
        fragmentLabTechnicianHomeBinding?.cardViewMySpeciality?.setOnClickListener(View.OnClickListener {
            (activity as LabTechnicianHomeActivity).checkFragmentInBackstackAndOpen(
                FragmentLabTechnicianMySpeciality.newInstance())
        })




    }
}