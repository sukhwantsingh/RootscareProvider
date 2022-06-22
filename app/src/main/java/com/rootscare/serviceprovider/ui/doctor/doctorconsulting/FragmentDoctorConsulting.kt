package com.rootscare.serviceprovider.ui.doctor.doctorconsulting

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentDoctorConsultingBinding
import com.rootscare.serviceprovider.databinding.FragmentDoctorMyAppointmentBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.FragmentMyAppointment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.FragmentMyAppointmentNavigator
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.FragmentMyAppointmentViewModel

class FragmentDoctorConsulting: BaseFragment<FragmentDoctorConsultingBinding, FragmentDoctorConsultingViewModel>(),
    FragmentDoctorConsultingNavigator {
    private var fragmentDoctorConsultingBinding: FragmentDoctorConsultingBinding? = null
    private var fragmentDoctorConsultingViewModel: FragmentDoctorConsultingViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_doctor_consulting
    override val viewModel: FragmentDoctorConsultingViewModel
        get() {
            fragmentDoctorConsultingViewModel = ViewModelProviders.of(this).get(
                FragmentDoctorConsultingViewModel::class.java!!)
            return fragmentDoctorConsultingViewModel as FragmentDoctorConsultingViewModel
        }
    companion object {
        fun newInstance(): FragmentDoctorConsulting {
            val args = Bundle()
            val fragment = FragmentDoctorConsulting()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentDoctorConsultingViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentDoctorConsultingBinding = viewDataBinding
    }


}