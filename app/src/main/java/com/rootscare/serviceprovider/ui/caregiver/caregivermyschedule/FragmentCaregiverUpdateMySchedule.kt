package com.rootscare.serviceprovider.ui.caregiver.caregivermyschedule

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentNursesMySdheduleBinding
import com.rootscare.serviceprovider.ui.babySitter.babySitterMySchedule.FragmentBabySitterUpdateMyScheduleNavigator
import com.rootscare.serviceprovider.ui.babySitter.babySitterMySchedule.FragmentBabySitterUpdateMyScheduleViewModel
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.nurses.nursesmyschedule.adapter.AdapterSlotWiserecyclerView

class FragmentCaregiverUpdateMySchedule: BaseFragment<FragmentNursesMySdheduleBinding, FragmentBabySitterUpdateMyScheduleViewModel>(),
    FragmentBabySitterUpdateMyScheduleNavigator {
    private var fragmentNursesMySdheduleBinding: FragmentNursesMySdheduleBinding? = null
    private var fragmentNursesMyScheduleViewModel: FragmentBabySitterUpdateMyScheduleViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_nurses_my_sdhedule
    override val viewModel: FragmentBabySitterUpdateMyScheduleViewModel
        get() {
            fragmentNursesMyScheduleViewModel = ViewModelProviders.of(this).get(
                FragmentBabySitterUpdateMyScheduleViewModel::class.java!!)
            return fragmentNursesMyScheduleViewModel as FragmentBabySitterUpdateMyScheduleViewModel
        }

    companion object {
        fun newInstance(): FragmentCaregiverUpdateMySchedule {
            val args = Bundle()
            val fragment = FragmentCaregiverUpdateMySchedule()
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentNursesMyScheduleViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentNursesMySdheduleBinding = viewDataBinding
//        fragmentNursesMySdheduleBinding?.btnNursesManageRate?.setOnClickListener(View.OnClickListener {
//            (activity as NursrsHomeActivity).checkFragmentInBackstackAndOpen(
//                FragmentNursesManageRate.newInstance())
//        })
        setUpViewSlotWiseRateListingRecyclerview()

    }

    // Set up recycler view for service listing if available
    private fun setUpViewSlotWiseRateListingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
//        assert(fragmentNursesMySdheduleBinding!!.recyclerViewSlotwiseRate != null)
        val recyclerView = fragmentNursesMySdheduleBinding!!.recyclerViewSlotwiseRate
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterSlotWiserecyclerView(requireContext())
        recyclerView.adapter = contactListAdapter
//        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
//            override fun onItemClick(id: Int) {
//                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                    FragmentDoctorListingDetails.newInstance())
//            }
//
//        }


    }


}