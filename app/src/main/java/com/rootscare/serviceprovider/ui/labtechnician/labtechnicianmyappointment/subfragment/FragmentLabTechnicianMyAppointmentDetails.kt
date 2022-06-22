package com.rootscare.serviceprovider.ui.labtechnician.labtechnicianmyappointment.subfragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentLabtechnicianMyAppointmentBinding
import com.rootscare.serviceprovider.databinding.FragmentLabtechnicianMyAppointmentDetailsBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.labtechnician.labtechnicianmyappointment.FragmentLabtechnicianMyAppointment
import com.rootscare.serviceprovider.ui.labtechnician.labtechnicianmyappointment.FragmentLabtechnicianMyAppointmentNavigator
import com.rootscare.serviceprovider.ui.labtechnician.labtechnicianmyappointment.FragmentLabtechnicianMyAppointmentViewModel

class FragmentLabTechnicianMyAppointmentDetails: BaseFragment<FragmentLabtechnicianMyAppointmentDetailsBinding, FragmentLabTechnicianMyAppointmentDetailsViewModel>(),
    FragmentLabTechnicianMyAppointmentDetailsNavigator {
    private var fragmentLabtechnicianMyAppointmentDetailsBinding: FragmentLabtechnicianMyAppointmentDetailsBinding? = null
    private var fragmentLabTechnicianMyAppointmentDetailsViewModel: FragmentLabTechnicianMyAppointmentDetailsViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_labtechnician_my_appointment_details
    override val viewModel: FragmentLabTechnicianMyAppointmentDetailsViewModel
        get() {
            fragmentLabTechnicianMyAppointmentDetailsViewModel = ViewModelProviders.of(this).get(
                FragmentLabTechnicianMyAppointmentDetailsViewModel::class.java!!)
            return fragmentLabTechnicianMyAppointmentDetailsViewModel as FragmentLabTechnicianMyAppointmentDetailsViewModel
        }
    companion object {
        fun newInstance(): FragmentLabTechnicianMyAppointmentDetails {
            val args = Bundle()
            val fragment = FragmentLabTechnicianMyAppointmentDetails()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentLabTechnicianMyAppointmentDetailsViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentLabtechnicianMyAppointmentDetailsBinding = viewDataBinding
    }
}