package com.rootscare.utils.commonadapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.newappointments.FragNewAppointmentListing
import com.rootscare.serviceprovider.utilitycommon.AppointmentTypes


class ViewPagerAdapterForTab(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {
    private val mFragmentList: MutableList<Fragment> = ArrayList()
    private val mFragmentTitleList: MutableList<String> = ArrayList()
    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList[position]
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

}


class ViewPagerAdapterForAppointment(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getCount(): Int { return 4 }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> FragNewAppointmentListing.newInstance(AppointmentTypes.ONGOING.get())
            1 -> FragNewAppointmentListing.newInstance(AppointmentTypes.PENDING.get())
            2 -> FragNewAppointmentListing.newInstance(AppointmentTypes.UPCOMING.get())
            3 -> FragNewAppointmentListing.newInstance(AppointmentTypes.PAST.get())
            else -> FragNewAppointmentListing.newInstance(AppointmentTypes.ONGOING.get())
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 ->  AppointmentTypes.ONGOING.get()
            1 -> AppointmentTypes.PENDING.get()
            2 -> AppointmentTypes.UPCOMING.get()
            3 -> AppointmentTypes.PAST.get()
            else -> AppointmentTypes.ONGOING.get()
        }
    }
    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }
}
