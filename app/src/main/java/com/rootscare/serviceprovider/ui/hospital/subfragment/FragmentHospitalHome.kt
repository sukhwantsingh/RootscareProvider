package com.rootscare.serviceprovider.ui.hospital.subfragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentHospitalHomeBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.hospital.HospitalHomeActivity
import com.rootscare.serviceprovider.ui.hospital.hospitalmanageappointments.FragmentHospitalManageAppointments
import com.rootscare.serviceprovider.ui.hospital.hospitalmanagedepartment.FragmentHospitalManageDepartment
import com.rootscare.serviceprovider.ui.hospital.hospitalmanagedoctor.FragmenthospitalManageDoctor
import com.rootscare.serviceprovider.ui.hospital.hospitalmanagenotification.FragmentHospitalManageNotification
import com.rootscare.serviceprovider.ui.hospital.hospitalordermanagement.FragmentHospitalOrderManagement
import com.rootscare.serviceprovider.ui.hospital.hospitalpaymenttransaction.FragmentHospitalPaymentTransaction
import com.rootscare.serviceprovider.ui.hospital.hospitalsamplecollection.FragmentHospitalSampleCollection
import com.rootscare.serviceprovider.ui.hospital.hospitaluploadpathologyreport.FragmentHospitalUploadPathologyReport
import com.rootscare.serviceprovider.ui.manageDocLab.ManageDocAndLab
import com.rootscare.serviceprovider.ui.nurses.home.NursrsHomeActivity
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.FragmentNursesProfile
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.subfragment.nursesprofileedit.FragmentNursesEditProfile
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.FragmentNursesMyAppointment
import com.rootscare.serviceprovider.ui.nurses.nursesmyschedule.subfragment.manageschedule.FragmentNursesManageRate
import com.rootscare.serviceprovider.ui.pricelistss.PriceListScreen
import com.rootscare.serviceprovider.ui.scheduless.ScheduleActivity
import com.rootscare.serviceprovider.ui.supportmore.CommonWebviewScreen
import com.rootscare.serviceprovider.ui.supportmore.OPEN_FOR
import com.rootscare.serviceprovider.ui.supportmore.OPEN_FOR_ABOUT
import com.rootscare.serviceprovider.ui.supportmore.SupportAndMore
import com.rootscare.serviceprovider.ui.transactionss.TransactionsMore
import com.rootscare.serviceprovider.utilitycommon.IntentParams
import com.rootscare.serviceprovider.utilitycommon.navigate

class FragmentHospitalHome: BaseFragment<FragmentHospitalHomeBinding, FragmentHospitalHomeViewModel>(),
    FragmentHospitalHomeNavigator {
    private var fragmentHospitalHomeBinding: FragmentHospitalHomeBinding? = null
    private var fragmentHospitalHomeViewModel: FragmentHospitalHomeViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_hospital_home
    override val viewModel: FragmentHospitalHomeViewModel
        get() {
            fragmentHospitalHomeViewModel = ViewModelProviders.of(this).get(
                FragmentHospitalHomeViewModel::class.java
            )
            return fragmentHospitalHomeViewModel as FragmentHospitalHomeViewModel
        }

    companion object {
        fun newInstance(): FragmentHospitalHome {
            val args = Bundle()
            val fragment = FragmentHospitalHome()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentHospitalHomeViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHospitalHomeBinding = viewDataBinding


//        fragmentHospitalHomeBinding?.cardViewManageDepartment?.setOnClickListener {
//            (activity as HospitalHomeActivity).checkFragmentInBackStackAndOpen(
//                FragmentHospitalManageDepartment.newInstance()
//            )
//        }
//
//        fragmentHospitalHomeBinding?.cardViewManageDoctor?.setOnClickListener {
//            (activity as HospitalHomeActivity).checkFragmentInBackStackAndOpen(
//                FragmenthospitalManageDoctor.newInstance()
//            )
//        }
//        fragmentHospitalHomeBinding?.cardViewManageAppointment?.setOnClickListener {
//            (activity as HospitalHomeActivity).checkFragmentInBackStackAndOpen(
//                FragmentHospitalManageAppointments.newInstance()
//            )
//        }
//        fragmentHospitalHomeBinding?.cardViewManageNotification?.setOnClickListener {
//            (activity as HospitalHomeActivity).checkFragmentInBackStackAndOpen(
//                FragmentHospitalManageNotification.newInstance()
//            )
//        }
//
//        fragmentHospitalHomeBinding?.cardViewOrderManagement?.setOnClickListener {
//            (activity as HospitalHomeActivity).checkFragmentInBackStackAndOpen(
//                FragmentHospitalOrderManagement.newInstance()
//            )
//        }
//        fragmentHospitalHomeBinding?.cardViewSampleCollection?.setOnClickListener {
//            (activity as HospitalHomeActivity).checkFragmentInBackStackAndOpen(
//                FragmentHospitalSampleCollection.newInstance()
//            )
//        }
//
//        fragmentHospitalHomeBinding?.cardViewUploadPathologyReport?.setOnClickListener {
//            (activity as HospitalHomeActivity).checkFragmentInBackStackAndOpen(
//                FragmentHospitalUploadPathologyReport.newInstance()
//            )
//        }
//
//        fragmentHospitalHomeBinding?.cardViewPaymentTransaction?.setOnClickListener {
//            (activity as HospitalHomeActivity).checkFragmentInBackStackAndOpen(
//                FragmentHospitalPaymentTransaction.newInstance()
//            )
//        }
//
//        fragmentHospitalHomeBinding?.cardViewLabRate?.setOnClickListener {
//            (activity as HospitalHomeActivity).checkFragmentInBackStackAndOpen(
//                FragmentNursesManageRate.newInstance("lab")
//            )
//        }
//
//        fragmentHospitalHomeBinding?.cardViewAllBooking?.setOnClickListener {
//            (activity as HospitalHomeActivity).checkFragmentInBackStackAndOpen(
//                FragmentHospitalManageAppointments.newInstance()
//            )
//        }
//
//        fragmentHospitalHomeBinding?.cardViewScheduleProfile?.setOnClickListener {
//            startActivity(Intent(context!!, ScheduleActivity::class.java))
//        }


        initViews()

    }

    override fun onStart() {
        super.onStart()
        if (FragmentNursesEditProfile.IS_PROFILE_UPDATE_) {
            FragmentNursesEditProfile.IS_PROFILE_UPDATE_ = false
            (activity as? HospitalHomeActivity)?.init()
        }
    }

    private fun initViews() {
        fragmentHospitalHomeBinding?.layoutNewDashboard?.run {
            rlDashAppointment.setOnClickListener {
              (activity as? HospitalHomeActivity)?.checkFragmentInBackStackAndOpen(FragmentNursesMyAppointment.newInstance())
            }
            rlDashLabAppointments.setOnClickListener {
            (activity as? HospitalHomeActivity)?.checkFragmentInBackStackAndOpen(FragmentNursesMyAppointment.newInstance())
            }

            rlDashManageDocLab.setOnClickListener {
                navigate<ManageDocAndLab>()
            }

            rlDashTransactionAccounting.setOnClickListener {
//            (activity as? HospitalHomeActivity)?.checkFragmentInBackStackAndOpen(FragmentNursesPaymentHistory.newInstance())
             navigate<TransactionsMore>()
            }
            rlDashAccountSetting.setOnClickListener {
            (activity as? HospitalHomeActivity)?.checkFragmentInBackStackAndOpen(FragmentNursesProfile.newInstance())
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