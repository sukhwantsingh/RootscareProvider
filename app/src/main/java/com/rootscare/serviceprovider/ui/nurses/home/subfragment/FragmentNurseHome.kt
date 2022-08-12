package com.rootscare.serviceprovider.ui.nurses.home.subfragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentNursesHomeBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.nurses.home.NursrsHomeActivity
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.FragmentNursesProfile
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.models.ModelUserProfile
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
import com.rootscare.serviceprovider.utilitycommon.LoginTypes
import com.rootscare.serviceprovider.utilitycommon.getModelFromPref
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
    var loginresponse: ModelUserProfile? = null

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
        initControls()

    }

    private fun initControls() {
        fragmentNursesHomeBinding?.layoutNewDashboard?.run {
            rlDashAppointment.setOnClickListener {
                if(allowProvider()) (activity as? NursrsHomeActivity)?.checkFragmentInBackStackAndOpen(FragmentNursesMyAppointment.newInstance())
            }
            rlDashSchedule.setOnClickListener { if(allowProvider()) navigate<ScheduleActivity>() }
            rlDashPriceList.setOnClickListener {  if(allowProvider()) navigate<PriceListScreen>() }
            rlDashAccountSetting.setOnClickListener { if(allowProvider()) (activity as? NursrsHomeActivity)?.checkFragmentInBackStackAndOpen(FragmentNursesProfile.newInstance()) }

            rlDashTransactionAccounting.setOnClickListener { if(allowProvider()) navigate<TransactionsMore>() }

            tvhAboutRootscare.setOnClickListener { if(allowProvider()) navigate<CommonWebviewScreen>(listOf(IntentParams(OPEN_FOR, OPEN_FOR_ABOUT))) }

            tvhHelpNSuppot.setOnClickListener { if(allowProvider())  navigate<SupportAndMore>()  }

        }
    }

    override fun onStart() {
        super.onStart()
        if (IS_PROFILE_UPDATE_) {
            IS_PROFILE_UPDATE_ = false
            (activity as? NursrsHomeActivity)?.init()
        }
    }

    private fun initViews() {
        loginresponse = fragmentNurseHomeViewModel?.appSharedPref?.loginmodeldata?.getModelFromPref()
        fragmentNursesHomeBinding?.layoutNewDashboard?.run {
            if (loginresponse?.result?.user_type?.lowercase().equals(LoginTypes.LAB.type)) {
                tvhH1.text = getString(R.string.all_appointments)
                tvhH2.text = getString(R.string.availability_schedule)
                tvhH3.text = getString(R.string.manage_test_n_packages)
                tvhPricelist.text = "Select Tests\nPrice Each Tests\nCreate Package\nON & OFF Task or Package"
            }

        }


    }


}