package com.rootscare.serviceprovider.ui.physiotherapy.physiotherapyMyAppointment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentNursesMyAppointmentBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.caregiver.caregivermyappointment.FragmentCaregiverUpdateMyAppointment
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.subfragment.newappointment.FragmentRequestedAppointmentForNurse
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.subfragment.pastappointment.FragmentPastAppointmentForNurse
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.subfragment.todayappointment.FragmentTodaysAppointmentForNurse
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.subfragment.upcomingappointment.FragmentUpcommingAppointmentForNurse
import com.rootscare.serviceprovider.ui.physiotherapy.home.PhysiotherapyHomeActivity
import com.rootscare.serviceprovider.ui.physiotherapy.physiotherapyMyAppointment.subfragment.newappointment.FragmentRequestedAppointmentForPhysiotherapist
import com.rootscare.serviceprovider.ui.physiotherapy.physiotherapyMyAppointment.subfragment.pastappointment.FragmentPastAppointmentForPhysiotherapist
import com.rootscare.serviceprovider.ui.physiotherapy.physiotherapyMyAppointment.subfragment.todayappointment.FragmentTodaysAppointmentForPhysiotherapist
import com.rootscare.serviceprovider.ui.physiotherapy.physiotherapyMyAppointment.subfragment.upcomingappointment.FragmentUpcomingAppointmentForPhysiotherapist
import java.util.*

class FragmentPhysiotherapyUpdateMyAppointment : BaseFragment<FragmentNursesMyAppointmentBinding, FragmentPhysiotherapyUpdateMyAppointmentViewModel>(),
    FragmentPhysiotherapyUpdateMyAppointmentNavigator {
    private var fragmentDoctorMyAppointmentBinding: FragmentNursesMyAppointmentBinding? = null
    private var fragmentMyAppointmentViewModel: FragmentPhysiotherapyUpdateMyAppointmentViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_nurses_my_appointment
    override val viewModel: FragmentPhysiotherapyUpdateMyAppointmentViewModel
        get() {
            fragmentMyAppointmentViewModel = ViewModelProviders.of(this).get(
                FragmentPhysiotherapyUpdateMyAppointmentViewModel::class.java
            )
            return fragmentMyAppointmentViewModel as FragmentPhysiotherapyUpdateMyAppointmentViewModel
        }

    companion object {
        fun newInstance(): FragmentPhysiotherapyUpdateMyAppointment {
            val args = Bundle()
            val fragment = FragmentPhysiotherapyUpdateMyAppointment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentMyAppointmentViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentDoctorMyAppointmentBinding = viewDataBinding

        setTabs()
        setUpTabLayout()

        fragmentDoctorMyAppointmentBinding?.btnDoctorUpcommingAppointment?.setOnClickListener {
            (activity as PhysiotherapyHomeActivity).checkFragmentInBackStackAndOpen(
                FragmentUpcomingAppointmentForPhysiotherapist.newInstance()
            )
        }

        fragmentDoctorMyAppointmentBinding?.btnDoctorTodaysAppointment?.setOnClickListener {
            (activity as PhysiotherapyHomeActivity).checkFragmentInBackStackAndOpen(
                FragmentTodaysAppointmentForPhysiotherapist.newInstance()
            )
        }

        fragmentDoctorMyAppointmentBinding?.btnDoctorRequestedAppointment?.setOnClickListener {
            (activity as PhysiotherapyHomeActivity).checkFragmentInBackStackAndOpen(
                FragmentRequestedAppointmentForPhysiotherapist.newInstance()
            )
        }
        fragmentDoctorMyAppointmentBinding?.btnDoctorPastAppointment?.setOnClickListener {
            (activity as PhysiotherapyHomeActivity).checkFragmentInBackStackAndOpen(
                FragmentPastAppointmentForPhysiotherapist.newInstance()
            )
        }
    }

    private fun setUpTabLayout() {
        val tabTitles: MutableList<String> =
            ArrayList()
        tabTitles.add("Ongoing")
        tabTitles.add("Pending")
        tabTitles.add("Upcoming")
        tabTitles.add("Past")

        for (i in tabTitles.indices) {
            fragmentDoctorMyAppointmentBinding?.tablayout?.addTab(
                fragmentDoctorMyAppointmentBinding?.tablayout?.newTab()?.setText(
                    tabTitles[i]
                )!!, i
            )
        }
//        fragmentAppointmentBinding?.tablayout?.tabGravity = TabLayout.GRAVITY_CENTER
        for (i in 0 until fragmentDoctorMyAppointmentBinding?.tablayout?.tabCount!!) {
            val view: View =
                LayoutInflater.from(activity).inflate(R.layout.tab_item_appointment_layout, null)
            val tab_item_tv = view.findViewById<TextView>(R.id.tab_item_tv)
            tab_item_tv.text = tabTitles[i]
            if (i == 0) {
                tab_item_tv.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.background_white
                    )
                )
                view.background = resources.getDrawable(R.drawable.tab_background_selected)
            } else {
                tab_item_tv.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        R.color.background_white
                    )
                )
//                tab_item_tv.setTextColor(resources.getColor(R.color.modified_black_1))
                view.background = resources.getDrawable(R.drawable.tab_background_unselected)
            }
            fragmentDoctorMyAppointmentBinding?.tablayout?.getTabAt(i)?.customView = view
        }
        fragmentDoctorMyAppointmentBinding?.tablayout?.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                for (i in 0 until fragmentDoctorMyAppointmentBinding?.tablayout?.tabCount!!) {
//                    val view: View = fragmentProfileBinding?.tablayout?.getTabAt(i)?.customView!!
                    val view: View = Objects.requireNonNull<View>(
                        fragmentDoctorMyAppointmentBinding?.tablayout?.getTabAt(i)?.customView
                    )

                    val tab_item_tv =
                        Objects.requireNonNull(view)
                            .findViewById<TextView>(R.id.tab_item_tv)
                    if (i == tab.position) {
                        tab_item_tv.setTextColor(resources.getColor(R.color.background_white))
                        Objects.requireNonNull(view).background =
                            resources.getDrawable(R.drawable.tab_background_selected)
                    } else {
                        tab_item_tv.setTextColor(
                            ContextCompat.getColor(
                                activity!!,
                                R.color.background_white
                            )
                        )
//                        tab_item_tv.setTextColor(resources.getColor(R.color.modified_black_1))
                        Objects.requireNonNull(view).background =
                            resources.getDrawable(R.drawable.tab_background_unselected)
                    }
                }
                when (tab.position) {
                    0 -> {

                    }
                    1 -> {

                    }
                    2 -> {


                    }
                    3 -> {




                    }

                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

    }
    fun setTabs()
    {
        fragmentDoctorMyAppointmentBinding?.tablayout?.tabGravity = TabLayout.GRAVITY_START
        val adapter = MyAdapter(
            context!!, childFragmentManager,
            fragmentDoctorMyAppointmentBinding?.tablayout?.tabCount!!
        )
        fragmentDoctorMyAppointmentBinding?.viewPager?.adapter = adapter
        fragmentDoctorMyAppointmentBinding?.viewPager?.offscreenPageLimit = 1
        fragmentDoctorMyAppointmentBinding?.viewPager?.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(fragmentDoctorMyAppointmentBinding?.tablayout))
        fragmentDoctorMyAppointmentBinding?.tablayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                fragmentDoctorMyAppointmentBinding?.viewPager?.currentItem = tab.position
                fragmentDoctorMyAppointmentBinding?.viewPager?.adapter?.notifyDataSetChanged()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    internal class MyAdapter(
        var context: Context,
        fm: FragmentManager,
        var totalTabs: Int
    ) :
        FragmentStatePagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    FragmentTodaysAppointmentForPhysiotherapist.newInstance()
                }
                1 -> {
                    FragmentRequestedAppointmentForPhysiotherapist.newInstance()
                }  2 -> {
                    FragmentUpcomingAppointmentForPhysiotherapist.newInstance()
                }  3 -> {
                    FragmentPastAppointmentForPhysiotherapist.newInstance()
                }
                else -> {
                    FragmentTodaysAppointmentForPhysiotherapist.newInstance()
                }

            }
        }

        override fun getCount(): Int {
            return 4
        }

        override fun getItemPosition(`object`: Any): Int {
            return POSITION_NONE
        }
    }

}