package com.rootscare.serviceprovider.ui.physiotherapy.home.subfragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentPhysiotheraphyHomeBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.nurses.home.NursrsHomeActivity
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.FragmentNursesProfile
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.subfragment.nursesprofileedit.FragmentNursesEditProfile
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.FragmentNursesMyAppointment
import com.rootscare.serviceprovider.ui.physiotherapy.home.PhysiotherapyHomeActivity
import com.rootscare.serviceprovider.ui.physiotherapy.physiotherapyManageProfile.FragmentPhysiotherapistUpdateProfile
import com.rootscare.serviceprovider.ui.physiotherapy.physiotherapyMyAppointment.FragmentPhysiotherapyUpdateMyAppointment
import com.rootscare.serviceprovider.ui.pricelistss.PriceListScreen
import com.rootscare.serviceprovider.ui.scheduless.ScheduleActivity
import com.rootscare.serviceprovider.ui.supportmore.CommonWebviewScreen
import com.rootscare.serviceprovider.ui.supportmore.OPEN_FOR
import com.rootscare.serviceprovider.ui.supportmore.OPEN_FOR_ABOUT
import com.rootscare.serviceprovider.ui.supportmore.SupportAndMore
import com.rootscare.serviceprovider.ui.transactionss.TransactionsMore
import com.rootscare.serviceprovider.utilitycommon.IntentParams
import com.rootscare.serviceprovider.utilitycommon.navigate

class FragmentPhysiotherapyHome: BaseFragment<FragmentPhysiotheraphyHomeBinding, FragmentPhysiotherapyHomeViewModel>(), FragmentPhysiotherapyHomeNavigator  {
    private var fragmentPhysiotherapyHomeBinding: FragmentPhysiotheraphyHomeBinding? = null
    private var fragmentPhysiotherapyHomeViewModel: FragmentPhysiotherapyHomeViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_physiotheraphy_home
    override val viewModel: FragmentPhysiotherapyHomeViewModel
        get() {
            fragmentPhysiotherapyHomeViewModel = ViewModelProviders.of(this).get(
                FragmentPhysiotherapyHomeViewModel::class.java
            )
            return fragmentPhysiotherapyHomeViewModel as FragmentPhysiotherapyHomeViewModel
        }

    companion object {
        fun newInstance(): FragmentPhysiotherapyHome {
            val args = Bundle()
            val fragment = FragmentPhysiotherapyHome()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentPhysiotherapyHomeViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentPhysiotherapyHomeBinding = viewDataBinding


        initViews()

    }

    override fun onStart() {
        super.onStart()
        if (FragmentNursesEditProfile.IS_PROFILE_UPDATE_) {
            FragmentNursesEditProfile.IS_PROFILE_UPDATE_ = false
            (activity as? PhysiotherapyHomeActivity)?.initViews()
        }
    }

    private fun initViews() {
        fragmentPhysiotherapyHomeBinding?.layoutNewDashboard?.run {
            rlDashAppointment.setOnClickListener {
                (activity as PhysiotherapyHomeActivity).checkFragmentInBackStackAndOpen(
                    FragmentNursesMyAppointment.newInstance()
                )
            }
            rlDashSchedule.setOnClickListener {
                navigate<ScheduleActivity>()
            }
            rlDashPriceList.setOnClickListener {
                navigate<PriceListScreen>()
            }
            rlDashTransactionAccounting.setOnClickListener { navigate<TransactionsMore>()  }
            rlDashAccountSetting.setOnClickListener {
                (activity as PhysiotherapyHomeActivity).checkFragmentInBackStackAndOpen(FragmentNursesProfile.newInstance())
            }
            tvhAboutRootscare.setOnClickListener {
                navigate<CommonWebviewScreen>(listOf(IntentParams(OPEN_FOR, OPEN_FOR_ABOUT)))}
            tvhHelpNSuppot.setOnClickListener {
                navigate<SupportAndMore>()
            }

        }
    }

}