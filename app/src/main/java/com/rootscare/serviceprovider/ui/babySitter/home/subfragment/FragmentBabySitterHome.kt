package com.rootscare.serviceprovider.ui.babySitter.home.subfragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentCaregiverHomeBinding
import com.rootscare.serviceprovider.ui.babySitter.babySitterMyAppointment.FragmentBabySitterUpdateMyAppointment
import com.rootscare.serviceprovider.ui.babySitter.babySitterProfile.FragmentBabySitterUpdateProfile
import com.rootscare.serviceprovider.ui.babySitter.home.BabySitterHomeActivity
import com.rootscare.serviceprovider.ui.base.BaseFragment
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

class FragmentBabySitterHome : BaseFragment<FragmentCaregiverHomeBinding, FragmentBabySitterHomeViewModel>(),
    FragmentBabySitterHomeNavigator {
    private var fragmentCaregiverHomeBinding: FragmentCaregiverHomeBinding? = null
    private var fragmentCaregiverHomeViewModel: FragmentBabySitterHomeViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_caregiver_home
    override val viewModel: FragmentBabySitterHomeViewModel
        get() {
            fragmentCaregiverHomeViewModel = ViewModelProviders.of(this).get(
                FragmentBabySitterHomeViewModel::class.java
            )
            return fragmentCaregiverHomeViewModel as FragmentBabySitterHomeViewModel
        }

    companion object {
        fun newInstance(): FragmentBabySitterHome {
            val args = Bundle()
            val fragment = FragmentBabySitterHome()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentCaregiverHomeViewModel!!.navigator = this
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
            (activity as? BabySitterHomeActivity)?.init()
        }
    }

    private fun initViews() {
        fragmentCaregiverHomeBinding?.layoutNewDashboard?.run {
            rlDashAppointment.setOnClickListener {
                (activity as BabySitterHomeActivity).checkFragmentInBackStackAndOpen(
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
                (activity as BabySitterHomeActivity).checkFragmentInBackStackAndOpen(
                    FragmentNursesProfile.newInstance()
                )
            }
            tvhAboutRootscare.setOnClickListener {
                navigate<CommonWebviewScreen>(listOf(IntentParams(OPEN_FOR, OPEN_FOR_ABOUT)))}
            tvhHelpNSuppot.setOnClickListener {
                navigate<SupportAndMore>()
            }

        }


    }

}