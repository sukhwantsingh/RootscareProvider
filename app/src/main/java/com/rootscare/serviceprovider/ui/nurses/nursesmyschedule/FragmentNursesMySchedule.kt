package com.rootscare.serviceprovider.ui.nurses.nursesmyschedule

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentNursesMySdheduleBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.nurses.nursesmyschedule.adapter.AdapterSlotWiserecyclerView

class FragmentNursesMySchedule: BaseFragment<FragmentNursesMySdheduleBinding, FragmentNursesMyScheduleViewModel>(),
    FragmentNursesMyScheduleNavigator {
    private var fragmentNursesMySdheduleBinding: FragmentNursesMySdheduleBinding? = null
    private var fragmentNursesMyScheduleViewModel: FragmentNursesMyScheduleViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_nurses_my_sdhedule
    override val viewModel: FragmentNursesMyScheduleViewModel
        get() {
            fragmentNursesMyScheduleViewModel = ViewModelProviders.of(this).get(
                FragmentNursesMyScheduleViewModel::class.java
            )
            return fragmentNursesMyScheduleViewModel as FragmentNursesMyScheduleViewModel
        }

    companion object {
        fun newInstance(): FragmentNursesMySchedule {
            val args = Bundle()
            val fragment = FragmentNursesMySchedule()
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
        setUpViewSlotWiseRatelistingRecyclerview()

    }

    // Set up recycler view for service listing if available
    private fun setUpViewSlotWiseRatelistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentNursesMySdheduleBinding!!.recyclerViewSlotwiseRate != null)
        val recyclerView = fragmentNursesMySdheduleBinding!!.recyclerViewSlotwiseRate
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterSlotWiserecyclerView(context!!)
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