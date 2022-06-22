package com.rootscare.serviceprovider.ui.labtechnician.labtechnicianmanageprofile

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentLabtechnicianManageProfileBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment

class FragmentLabtechnicianManageProfile :
    BaseFragment<FragmentLabtechnicianManageProfileBinding, FragmentLabtechnicianManageProfileViewModel>(),
    FragmentLabtechnicianManageProfileNavigator {
    private var fragmentLabTechnicianManageProfileBinding: FragmentLabtechnicianManageProfileBinding? = null
    private var fragmentLabTechnicianManageProfileViewModel: FragmentLabtechnicianManageProfileViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_labtechnician_manage_profile
    override val viewModel: FragmentLabtechnicianManageProfileViewModel
        get() {
            fragmentLabTechnicianManageProfileViewModel = ViewModelProviders.of(this).get(
                FragmentLabtechnicianManageProfileViewModel::class.java
            )
            return fragmentLabTechnicianManageProfileViewModel as FragmentLabtechnicianManageProfileViewModel
        }

    companion object {
        fun newInstance(): FragmentLabtechnicianManageProfile {
            val args = Bundle()
            val fragment = FragmentLabtechnicianManageProfile()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentLabTechnicianManageProfileViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentLabTechnicianManageProfileBinding = viewDataBinding
    }
}