package com.rootscare.serviceprovider.ui.nurses.nursesmyappointment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragNewMyAppointmentBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.newappointments.FragNewAppointmentListing
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.subfragment.newappointment.FragmentRequestedAppointmentForNurse
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.subfragment.pastappointment.FragmentPastAppointmentForNurse
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.subfragment.todayappointment.FragmentTodaysAppointmentForNurse
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.subfragment.upcomingappointment.FragmentUpcommingAppointmentForNurse
import com.rootscare.serviceprovider.utilitycommon.AppointmentTypes
import com.rootscare.utils.commonadapters.ViewPagerAdapterForAppointment
import com.rootscare.utils.commonadapters.ViewPagerAdapterForTab

class FragmentNursesMyAppointment : BaseFragment<FragNewMyAppointmentBinding, FragmentNursesMyAppointmentViewModel>(),
    FragmentNursesMyAppointmentNavigator {
    private var fragmentDoctorMyAppointmentBinding: FragNewMyAppointmentBinding? = null
    private var fragmentMyAppointmentViewModel: FragmentNursesMyAppointmentViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.frag_new_my_appointment
    override val viewModel: FragmentNursesMyAppointmentViewModel
        get() {
            fragmentMyAppointmentViewModel = ViewModelProviders.of(this).get(
                FragmentNursesMyAppointmentViewModel::class.java
            )
            return fragmentMyAppointmentViewModel as FragmentNursesMyAppointmentViewModel
        }
    private var tabPosition = 0

    companion object {
        fun newInstance(): FragmentNursesMyAppointment {
            val args = Bundle()
            val fragment = FragmentNursesMyAppointment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentMyAppointmentViewModel?.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentDoctorMyAppointmentBinding = viewDataBinding
        tweakTabsLoading()
      lifecycleScope.launchWhenResumed {
            try {
             setupViewPager(fragmentDoctorMyAppointmentBinding?.viewPager)
            } catch (e: Exception) {
                println("$e")
            }
       }
    }

    private var currentFragment: Fragment? = null

    lateinit var adapter: ViewPagerAdapterForTab
  //  lateinit var adapter: ViewPagerAdapterForAppointment
    private fun setupViewPager(viewPager: ViewPager?) {
        try {
         //   adapter = ViewPagerAdapterForAppointment(childFragmentManager)
            adapter = ViewPagerAdapterForTab(childFragmentManager)
        //    currentFragment = adapter.getItem(tabPosition)
            setFragmentAccordingToUser()
            viewPager?.adapter = adapter
            try {
                fragmentDoctorMyAppointmentBinding?.run {
                    tablayout.setupWithViewPager(viewPager)
                    viewPager?.offscreenPageLimit = 3
                   }
            } catch (e: Exception) {
                println("$e")
            }

            //   viewPager?.isSaveFromParentEnabled = false
        } catch (e: Exception) {
            println("$e")
        }
        try {
            viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//                    if(position == tabPosition){
//                        try {
//                            currentFragment = adapter.getItem(tabPosition)
//                            //   Log.wtf("OkHttp", "onPageScrolled: $position")
//                        } catch (e: Exception) {
//                            println(e.message)
//                        }
//                    }
                }

                override fun onPageSelected(position: Int) {
                    try {
                        tabPosition = position
                        currentFragment = adapter.getItem(tabPosition)
                        Log.wtf("OkHttp", "onPageScrolled: $position")
                        (currentFragment as? FragNewAppointmentListing)?.fetchAppointments(showLoading = true)
                        } catch (e: Exception) {
                        println(e.message)
                    }
                }

            })
        } catch (e: Exception) {
            println(e.message)
        }

    }

    private fun tweakTabsLoading() {
        val tabTitles: MutableList<String> = arrayListOf(AppointmentTypes.ONGOING.get(),
            AppointmentTypes.PENDING.get(),AppointmentTypes.UPCOMING.get(), AppointmentTypes.PAST.get())
        for (i in tabTitles.indices) {
            fragmentDoctorMyAppointmentBinding?.tablayout?.newTab()?.setText(tabTitles[i])?.let {
                fragmentDoctorMyAppointmentBinding?.tablayout?.addTab(it, i)
            }
        }
        fragmentDoctorMyAppointmentBinding?.tablayout?.setTabTextColors(
            ContextCompat.getColor(requireActivity(), R.color.transparent_white),
            ContextCompat.getColor(requireActivity(), R.color.background_white)
        )
    }
    private fun setFragmentAccordingToUser() {
    adapter.apply {
        addFragment(FragNewAppointmentListing.newInstance(AppointmentTypes.ONGOING.get()), AppointmentTypes.ONGOING.get())
        addFragment(FragNewAppointmentListing.newInstance(AppointmentTypes.PENDING.get()), AppointmentTypes.PENDING.get())
        addFragment(FragNewAppointmentListing.newInstance(AppointmentTypes.UPCOMING.get()), AppointmentTypes.UPCOMING.get())
        addFragment(FragNewAppointmentListing.newInstance(AppointmentTypes.PAST.get()), AppointmentTypes.PAST.get())
        }
    }

    override fun onStart() {
        super.onStart()
        currentFragment?.let {
            (it as? FragNewAppointmentListing)?.updateData()
        }
    }

}