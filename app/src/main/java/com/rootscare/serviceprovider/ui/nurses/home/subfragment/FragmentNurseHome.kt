package com.rootscare.serviceprovider.ui.nurses.home.subfragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentNursesHomeBinding
import com.rootscare.serviceprovider.ui.babySitter.babySitterMyAppointment.FragmentBabySitterUpdateMyAppointment
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.nurses.home.NursrsHomeActivity
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.FragmentNursesProfile
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.subfragment.nursesprofileedit.FragmentNursesEditProfile.Companion.IS_PROFILE_UPDATE_
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

class FragmentNurseHome : BaseFragment<FragmentNursesHomeBinding, FragmentNurseHomeViewModel>(),
    FragmentNurseHomeNavigator {
    private var fragmentNursesHomeBinding: FragmentNursesHomeBinding? = null
    private var fragmentNurseHomeViewModel: FragmentNurseHomeViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_nurses_home
    override val viewModel: FragmentNurseHomeViewModel
        get() {
            fragmentNurseHomeViewModel = ViewModelProviders.of(this).get(
                FragmentNurseHomeViewModel::class.java
            )
            return fragmentNurseHomeViewModel as FragmentNurseHomeViewModel
        }

    companion object {
        fun newInstance(): FragmentNurseHome {
            val args = Bundle()
            val fragment = FragmentNurseHome()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentNurseHomeViewModel?.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentNursesHomeBinding = viewDataBinding

        initViews()

    }

    override fun onStart() {
        super.onStart()
        if (IS_PROFILE_UPDATE_) {
            IS_PROFILE_UPDATE_ = false
            (activity as? NursrsHomeActivity)?.init()
        }
    }

    private fun initViews() {
        fragmentNursesHomeBinding?.layoutNewDashboard?.run {
            rlDashAppointment.setOnClickListener {
                (activity as? NursrsHomeActivity)?.checkFragmentInBackStackAndOpen(FragmentNursesMyAppointment.newInstance())
            }
            rlDashSchedule.setOnClickListener {
                navigate<ScheduleActivity>()
            }
            rlDashPriceList.setOnClickListener {
//                (activity as? NursrsHomeActivity)?.checkFragmentInBackStackAndOpen(FragmentNursesManageRate.newInstance("other"))
                navigate<PriceListScreen>()
            }
            rlDashTransactionAccounting.setOnClickListener {
//            (activity as? NursrsHomeActivity)?.checkFragmentInBackStackAndOpen(FragmentNursesPaymentHistory.newInstance())
                navigate<TransactionsMore>()
            }
            rlDashAccountSetting.setOnClickListener {
                (activity as? NursrsHomeActivity)?.checkFragmentInBackStackAndOpen(FragmentNursesProfile.newInstance())
            }
            tvhAboutRootscare.setOnClickListener {
                navigate<CommonWebviewScreen>(listOf(IntentParams(OPEN_FOR, OPEN_FOR_ABOUT)))
            }
            tvhHelpNSuppot.setOnClickListener {
                navigate<SupportAndMore>()
            }

        }


    }


}