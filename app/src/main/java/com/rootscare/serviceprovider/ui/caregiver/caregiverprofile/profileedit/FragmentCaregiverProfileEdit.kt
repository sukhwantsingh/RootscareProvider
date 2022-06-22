package com.rootscare.serviceprovider.ui.caregiver.caregiverprofile.profileedit

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentCaregiverProfileEditBinding
import com.rootscare.serviceprovider.ui.babySitter.babySitterProfile.profileEdit.FragmentBabySitterProfileEdit
import com.rootscare.serviceprovider.ui.babySitter.babySitterProfile.profileEdit.FragmentBabySitterProfileEditNavigator
import com.rootscare.serviceprovider.ui.babySitter.babySitterProfile.profileEdit.FragmentBabySitterProfileEditViewModel
import com.rootscare.serviceprovider.ui.base.BaseFragment

class FragmentCaregiverProfileEdit: BaseFragment<FragmentCaregiverProfileEditBinding, FragmentBabySitterProfileEditViewModel>(),
    FragmentBabySitterProfileEditNavigator {
    private var fragmentCaregiverProfileEditBinding: FragmentCaregiverProfileEditBinding? = null
    private var fragmentBabySitterProfileEditViewModel: FragmentBabySitterProfileEditViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_caregiver_profile_edit
    override val viewModel: FragmentBabySitterProfileEditViewModel
        get() {
            fragmentBabySitterProfileEditViewModel = ViewModelProviders.of(this).get(
                FragmentBabySitterProfileEditViewModel::class.java
            )
            return fragmentBabySitterProfileEditViewModel as FragmentBabySitterProfileEditViewModel
        }

    companion object {
        fun newInstance(): FragmentBabySitterProfileEdit {
            val args = Bundle()
            val fragment = FragmentBabySitterProfileEdit()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentBabySitterProfileEditViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentCaregiverProfileEditBinding = viewDataBinding


    }
}