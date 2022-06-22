package com.rootscare.serviceprovider.ui.hospital.hospitalordermanagement.subfragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentHospitalOrderDetailsBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment

class FragmentHospitalOrderDetails: BaseFragment<FragmentHospitalOrderDetailsBinding, FragmentHospitalOrderDetailsViewModel>(),
    FragmentHospitalOrderDetailsNavigator  {
    private var fragmentHospitalOrderDetailsBinding: FragmentHospitalOrderDetailsBinding? = null
    private var fragmentHospitalOrderDetailsViewModel: FragmentHospitalOrderDetailsViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_hospital_order_details
    override val viewModel: FragmentHospitalOrderDetailsViewModel
        get() {
            fragmentHospitalOrderDetailsViewModel = ViewModelProviders.of(this).get(
                FragmentHospitalOrderDetailsViewModel::class.java!!)
            return fragmentHospitalOrderDetailsViewModel as FragmentHospitalOrderDetailsViewModel
        }
    companion object {
        fun newInstance(): FragmentHospitalOrderDetails {
            val args = Bundle()
            val fragment = FragmentHospitalOrderDetails()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentHospitalOrderDetailsViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHospitalOrderDetailsBinding = viewDataBinding
    }
}