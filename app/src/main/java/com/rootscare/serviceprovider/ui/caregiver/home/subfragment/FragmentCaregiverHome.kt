package com.rootscare.serviceprovider.ui.caregiver.home.subfragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentCaregiverHomeBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.caregiver.caregivermyappointment.FragmentCaregiverUpdateMyAppointment
import com.rootscare.serviceprovider.ui.caregiver.caregiverprofile.FragmentCaregiverUpdateProfile
import com.rootscare.serviceprovider.ui.caregiver.home.CaregiverHomeActivity
import com.rootscare.serviceprovider.ui.nurses.home.NursrsHomeActivity
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.FragmentNursesProfile
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.subfragment.nursesprofileedit.FragmentNursesEditProfile
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.FragmentNursesMyAppointment
import com.rootscare.serviceprovider.ui.pricelistss.PriceListScreen
import com.rootscare.serviceprovider.ui.scheduless.ScheduleActivity
import com.rootscare.serviceprovider.ui.supportmore.CommonWebviewScreen
import com.rootscare.serviceprovider.ui.supportmore.OPEN_FOR
import com.rootscare.serviceprovider.ui.supportmore.OPEN_FOR_ABOUT
import com.rootscare.serviceprovider.ui.supportmore.SupportAndMore
import com.rootscare.serviceprovider.ui.transactionss.TransactionsMore
import com.rootscare.serviceprovider.utilitycommon.IntentParams
import com.rootscare.serviceprovider.utilitycommon.navigate

class FragmentCaregiverHome: BaseFragment<FragmentCaregiverHomeBinding, FragmentCaregiverHomeViewModel>(),
    FragmentCaregiverHomeNavigator {
    private var fragmentCaregiverHomeBinding: FragmentCaregiverHomeBinding? = null
    private var fragmentCaregiverHomeViewModel: FragmentCaregiverHomeViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_caregiver_home
    override val viewModel: FragmentCaregiverHomeViewModel
        get() {
            fragmentCaregiverHomeViewModel = ViewModelProviders.of(this).get(
                FragmentCaregiverHomeViewModel::class.java
            )
            return fragmentCaregiverHomeViewModel as FragmentCaregiverHomeViewModel
        }

    companion object {
        fun newInstance(): FragmentCaregiverHome {
            val args = Bundle()
            val fragment = FragmentCaregiverHome()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentCaregiverHomeViewModel?.navigator = this
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentCaregiverHomeBinding = viewDataBinding

        initViews()

    }

    override fun onStart() {
        super.onStart()
        if (FragmentNursesEditProfile.IS_PROFILE_UPDATE_) {
            FragmentNursesEditProfile.IS_PROFILE_UPDATE_ = false
            (activity as? CaregiverHomeActivity)?.init()
        }
    }
    private fun initViews() {
        fragmentCaregiverHomeBinding?.layoutNewDashboard?.run {
            rlDashAppointment.setOnClickListener {
                (activity as CaregiverHomeActivity).checkFragmentInBackStackAndOpen(
                    FragmentNursesMyAppointment.newInstance()
                )
            }
            rlDashSchedule.setOnClickListener {
                navigate<ScheduleActivity>()
            }
            rlDashPriceList.setOnClickListener {
                navigate<PriceListScreen>()
            }
            rlDashTransactionAccounting.setOnClickListener {
                navigate<TransactionsMore>()
            }
            rlDashAccountSetting.setOnClickListener {
                (activity as CaregiverHomeActivity).checkFragmentInBackStackAndOpen(
                    FragmentNursesProfile.newInstance()
                )
            }
            tvhAboutRootscare.setOnClickListener {
                navigate<CommonWebviewScreen>(listOf(IntentParams(OPEN_FOR, OPEN_FOR_ABOUT))) }
            tvhHelpNSuppot.setOnClickListener {
                navigate<SupportAndMore>()
            }

        }

    }



}