package com.rootscare.serviceprovider.ui.doctor.doctormyappointment

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
import com.rootscare.serviceprovider.databinding.FragmentDoctorAppointmentListBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.pastappointment.FragmentPastAppointment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.requestedappointment.FragmentRequestedAppointment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.todaysappointment.FragmentTodaysAppointment
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.upcomingappointment.FragmentUpcommingAppointment
import com.rootscare.serviceprovider.ui.home.HomeActivity
import com.rootscare.serviceprovider.ui.physiotherapy.physiotherapyMyAppointment.subfragment.newappointment.FragmentRequestedAppointmentForPhysiotherapist
import com.rootscare.serviceprovider.ui.physiotherapy.physiotherapyMyAppointment.subfragment.pastappointment.FragmentPastAppointmentForPhysiotherapist
import com.rootscare.serviceprovider.ui.physiotherapy.physiotherapyMyAppointment.subfragment.todayappointment.FragmentTodaysAppointmentForPhysiotherapist
import com.rootscare.serviceprovider.ui.physiotherapy.physiotherapyMyAppointment.subfragment.upcomingappointment.FragmentUpcomingAppointmentForPhysiotherapist
import java.util.*

class FragmentMyAppointment: BaseFragment<FragmentDoctorAppointmentListBinding, FragmentMyAppointmentViewModel>(),
    FragmentMyAppointmentNavigator {
    private var fragmentDoctorMyAppointmentBinding: FragmentDoctorAppointmentListBinding? = null
    private var fragmentMyAppointmentViewModel: FragmentMyAppointmentViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_doctor_appointment_list
    override val viewModel: FragmentMyAppointmentViewModel
        get() {
            fragmentMyAppointmentViewModel = ViewModelProviders.of(this).get(
                FragmentMyAppointmentViewModel::class.java)
            return fragmentMyAppointmentViewModel as FragmentMyAppointmentViewModel
        }

    companion object {
        fun newInstance(): FragmentMyAppointment {
            val args = Bundle()
            val fragment = FragmentMyAppointment()
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
        fragmentDoctorMyAppointmentBinding?.btnDoctorUpcommingAppointment?.setOnClickListener {
            (activity as HomeActivity).checkFragmentInBackStackAndOpen(
                FragmentUpcommingAppointment.newInstance()
            )
        }

        fragmentDoctorMyAppointmentBinding?.btnDoctorTodaysAppointment?.setOnClickListener {
            (activity as HomeActivity).checkFragmentInBackStackAndOpen(
                FragmentTodaysAppointment.newInstance()
            )
        }

        fragmentDoctorMyAppointmentBinding?.btnDoctorRequestedAppointment?.setOnClickListener {
            (activity as HomeActivity).checkFragmentInBackStackAndOpen(
                FragmentRequestedAppointment.newInstance()
            )
        }
        fragmentDoctorMyAppointmentBinding?.btnDoctorPastAppointment?.setOnClickListener {
            (activity as HomeActivity).checkFragmentInBackStackAndOpen(
                FragmentPastAppointment.newInstance()
            )
        }


        setTabs()
        setUpTabLayout()
    }
//    // Set up recycler view for service listing if available
//    private fun setUpDoctorMyAppointmentlistingRecyclerview() {
////        trainerList: ArrayList<TrainerListItem?>?
//        assert(fragmentDoctorMyAppointmentBinding!!.recyclerViewRootscareDoctorMyappointment != null)
//        val recyclerView = fragmentDoctorMyAppointmentBinding!!.recyclerViewRootscareDoctorMyappointment
//        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
//        recyclerView.layoutManager = gridLayoutManager
//        recyclerView.setHasFixedSize(true)
////        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
////        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
//        val contactListAdapter = AddapterDoctorMyAppointmentListRecyclerview(context!!)
//        recyclerView.adapter = contactListAdapter
//        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
//            override fun onItemClick(id: Int) {
//                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                    FragmentDoctorAppointmentDetails.newInstance())
//            }
//
//        }
//
//    }



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
                    FragmentTodaysAppointment.newInstance()
                }
                1 -> {
                    FragmentRequestedAppointment.newInstance()
                }  2 -> {
                    FragmentUpcommingAppointment.newInstance()
                }  3 -> {
                    FragmentPastAppointment.newInstance()
                }
                else -> {
                    FragmentTodaysAppointment.newInstance()
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
