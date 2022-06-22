package com.rootscare.serviceprovider.ui.caregiver.caregiverschedule

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentCaregiverScheduleBinding
import com.rootscare.serviceprovider.ui.babySitter.babySitterSchedule.FragmentBabySitterSchedule
import com.rootscare.serviceprovider.ui.babySitter.babySitterSchedule.FragmentBabySitterScheduleNavigator
import com.rootscare.serviceprovider.ui.babySitter.babySitterSchedule.FragmentBabySitterScheduleViewModel
import com.rootscare.serviceprovider.ui.base.BaseFragment

class FragmentCaregiverSchedule : BaseFragment<FragmentCaregiverScheduleBinding, FragmentBabySitterScheduleViewModel>(),
    FragmentBabySitterScheduleNavigator {
    private var fragmentCaregiverScheduleBinding: FragmentCaregiverScheduleBinding? = null
    private var fragmentBabySitterScheduleViewModel: FragmentBabySitterScheduleViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_caregiver_schedule
    override val viewModel: FragmentBabySitterScheduleViewModel
        get() {
            fragmentBabySitterScheduleViewModel = ViewModelProviders.of(this).get(
                FragmentBabySitterScheduleViewModel::class.java
            )
            return fragmentBabySitterScheduleViewModel as FragmentBabySitterScheduleViewModel
        }

    companion object {
        fun newInstance(): FragmentCaregiverSchedule {
            val args = Bundle()
            val fragment = FragmentCaregiverSchedule()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentBabySitterScheduleViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentCaregiverScheduleBinding = viewDataBinding
    }


}