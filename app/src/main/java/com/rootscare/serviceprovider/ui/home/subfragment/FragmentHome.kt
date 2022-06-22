package com.rootscare.serviceprovider.ui.home.subfragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentHomeBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.FragmentMyAppointment
import com.rootscare.serviceprovider.ui.doctor.doctormyschedule.FragmentDoctorMyschedule
import com.rootscare.serviceprovider.ui.doctor.doctorreviewandrating.FragmentReviewAndRating
import com.rootscare.serviceprovider.ui.doctor.profile.FragmentDoctorProfile
import com.rootscare.serviceprovider.ui.home.HomeActivity

class FragmentHome : BaseFragment<FragmentHomeBinding, FragmentHomeVirewModel>(), FragmentHomeNavigator {
    private var fragmentHomeBinding: FragmentHomeBinding? = null
    private var fragmentHomeVirewModel: FragmentHomeVirewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_home
    override val viewModel: FragmentHomeVirewModel
        get() {
            fragmentHomeVirewModel = ViewModelProviders.of(this).get(
                FragmentHomeVirewModel::class.java
            )
            return fragmentHomeVirewModel as FragmentHomeVirewModel
        }

    companion object {
        fun newInstance(): FragmentHome {
            val args = Bundle()
            val fragment = FragmentHome()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentHomeVirewModel!!.navigator = this


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHomeBinding = viewDataBinding
        fragmentHomeBinding?.cardViewManageProfile?.setOnClickListener {
            (activity as HomeActivity).checkFragmentInBackStackAndOpen(
                FragmentDoctorProfile.newInstance()
            )
        }

        fragmentHomeBinding?.cardViewReviewAndRating?.setOnClickListener {
            (activity as HomeActivity).checkFragmentInBackStackAndOpen(
                FragmentReviewAndRating.newInstance()
            )
        }

        fragmentHomeBinding?.cardViewMyAppointment?.setOnClickListener {
            (activity as HomeActivity).checkFragmentInBackStackAndOpen(
                FragmentMyAppointment.newInstance()
            )
        }

        fragmentHomeBinding?.cardViewMySchedule?.setOnClickListener {
            (activity as HomeActivity).checkFragmentInBackStackAndOpen(
                FragmentDoctorMyschedule.newInstance()
            )
        }
    }


}