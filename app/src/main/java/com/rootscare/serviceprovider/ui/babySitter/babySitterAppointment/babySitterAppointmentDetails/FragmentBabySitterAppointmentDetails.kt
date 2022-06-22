package com.rootscare.serviceprovider.ui.babySitter.babySitterAppointment.babySitterAppointmentDetails

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentCaregiverAppointmentDetailsBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment

class FragmentBabySitterAppointmentDetails: BaseFragment<FragmentCaregiverAppointmentDetailsBinding, FragmentBabySitterAppointmentDetailsViewModel>(),
    FragmentBabySitterAppointmentDetailsNavigator {
    private var fragmentCaregiverAppointmentDetailsBinding: FragmentCaregiverAppointmentDetailsBinding? = null
    private var fragmentBabySitterAppointmentDetailsViewModel: FragmentBabySitterAppointmentDetailsViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_caregiver_appointment_details
    override val viewModel: FragmentBabySitterAppointmentDetailsViewModel
        get() {
            fragmentBabySitterAppointmentDetailsViewModel = ViewModelProviders.of(this).get(
                FragmentBabySitterAppointmentDetailsViewModel::class.java
            )
            return fragmentBabySitterAppointmentDetailsViewModel as FragmentBabySitterAppointmentDetailsViewModel
        }
    companion object {
        fun newInstance(): FragmentBabySitterAppointmentDetails {
            val args = Bundle()
            val fragment = FragmentBabySitterAppointmentDetails()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentBabySitterAppointmentDetailsViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentCaregiverAppointmentDetailsBinding = viewDataBinding
    }
}