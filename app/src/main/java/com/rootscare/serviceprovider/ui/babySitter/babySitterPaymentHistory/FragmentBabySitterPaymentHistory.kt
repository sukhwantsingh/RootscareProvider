package com.rootscare.serviceprovider.ui.babySitter.babySitterPaymentHistory

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentCaregiverPaymenthistoryBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.babySitter.babySitterPaymentHistory.adapter.AdapterBabySitterPaymentHistoryRecyclerview


class FragmentBabySitterPaymentHistory : BaseFragment<FragmentCaregiverPaymenthistoryBinding, FragmentBabySitterPaymentHistoryViewModel>(),
    FragmentBabySitterPaymentHistoryNavigator {
    private var fragmentCaregiverPaymenthistoryBinding: FragmentCaregiverPaymenthistoryBinding? = null
    private var fragmentBabySitterPaymentHistoryViewModel: FragmentBabySitterPaymentHistoryViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_caregiver_paymenthistory
    override val viewModel: FragmentBabySitterPaymentHistoryViewModel
        get() {
            fragmentBabySitterPaymentHistoryViewModel = ViewModelProviders.of(this).get(
                FragmentBabySitterPaymentHistoryViewModel::class.java!!)
            return fragmentBabySitterPaymentHistoryViewModel as FragmentBabySitterPaymentHistoryViewModel
        }
    companion object {
        fun newInstance(): FragmentBabySitterPaymentHistory {
            val args = Bundle()
            val fragment = FragmentBabySitterPaymentHistory()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentBabySitterPaymentHistoryViewModel!!.navigator = this
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentCaregiverPaymenthistoryBinding = viewDataBinding
//        fragmentDoctorProfileBinding?.btnDoctorEditProfile?.setOnClickListener(View.OnClickListener {
//            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                FragmentAddDoctorProfile.newInstance())
//        })
        setUpPaymentHistorylistingRecyclerview()
    }
    // Set up recycler view for service listing if available
    private fun setUpPaymentHistorylistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentCaregiverPaymenthistoryBinding!!.recyclerViewCaregiverPaymenthistory != null)
        val recyclerView = fragmentCaregiverPaymenthistoryBinding!!.recyclerViewCaregiverPaymenthistory
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterBabySitterPaymentHistoryRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter


    }
}