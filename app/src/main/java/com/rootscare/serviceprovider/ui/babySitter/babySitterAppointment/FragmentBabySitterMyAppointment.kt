package com.rootscare.serviceprovider.ui.babySitter.babySitterAppointment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentCaregiverMyAppointmentBinding
import com.rootscare.serviceprovider.ui.babySitter.babySitterAppointment.adapter.AdapterBabySitterAppointmentRecyclerview
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.babySitter.babySitterAppointment.babySitterAppointmentDetails.FragmentBabySitterAppointmentDetails
import com.rootscare.serviceprovider.ui.caregiver.home.CaregiverHomeActivity

class FragmentBabySitterMyAppointment : BaseFragment<FragmentCaregiverMyAppointmentBinding, FragmentBabySitterMyAppointmentViewModel>(),
    FragmentBabySitterMyAppointmentNavigator {
    private var fragmentCaregiverMyAppointmentBinding: FragmentCaregiverMyAppointmentBinding? = null
    private var fragmentBabySitterMyAppointmentViewModel: FragmentBabySitterMyAppointmentViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_caregiver_my_appointment
    override val viewModel: FragmentBabySitterMyAppointmentViewModel
        get() {
            fragmentBabySitterMyAppointmentViewModel = ViewModelProviders.of(this).get(
                FragmentBabySitterMyAppointmentViewModel::class.java!!
            )
            return fragmentBabySitterMyAppointmentViewModel as FragmentBabySitterMyAppointmentViewModel
        }

    companion object {
        fun newInstance(): FragmentBabySitterMyAppointment {
            val args = Bundle()
            val fragment = FragmentBabySitterMyAppointment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentBabySitterMyAppointmentViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentCaregiverMyAppointmentBinding = viewDataBinding
        setUpNursesMyAppointmentlistingRecyclerview()
    }

    // Set up recycler view for service listing if available
    private fun setUpNursesMyAppointmentlistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentCaregiverMyAppointmentBinding!!.recyclerViewCaregiverMyappointment != null)
        val recyclerView = fragmentCaregiverMyAppointmentBinding!!.recyclerViewCaregiverMyappointment
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterBabySitterAppointmentRecyclerview(requireContext())
        recyclerView.adapter = contactListAdapter
        contactListAdapter.recyclerViewItemClickWithView = object : OnItemClikWithIdListener {
            override fun onItemClick(id: Int) {
                (activity as CaregiverHomeActivity).checkFragmentInBackStackAndOpen(
                    FragmentBabySitterAppointmentDetails.newInstance()
                )
            }

            override fun onAcceptBtnClick(id: String, text: String) {
            }

            override fun onRejectBtnClick(id: String, text: String) {
            }


        }

    }

}