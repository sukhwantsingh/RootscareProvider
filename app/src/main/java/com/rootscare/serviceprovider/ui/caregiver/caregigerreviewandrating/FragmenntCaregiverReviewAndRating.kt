package com.rootscare.serviceprovider.ui.caregiver.caregigerreviewandrating

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentCaregiverReviewAndRatingBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.caregiver.caregigerreviewandrating.adapter.AdapterCaregiverReviewAndRatingRecyclerview

class FragmenntCaregiverReviewAndRating: BaseFragment<FragmentCaregiverReviewAndRatingBinding, FragmenntCaregiverReviewAndRatingViewModel>(),
    FragmenntCaregiverReviewAndRatingNavigator {
    private var fragmentCaregiverReviewAndRatingBinding: FragmentCaregiverReviewAndRatingBinding? = null
    private var fragmenntCaregiverReviewAndRatingViewModel: FragmenntCaregiverReviewAndRatingViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_caregiver_review_and_rating
    override val viewModel: FragmenntCaregiverReviewAndRatingViewModel
        get() {
            fragmenntCaregiverReviewAndRatingViewModel = ViewModelProviders.of(this).get(
                FragmenntCaregiverReviewAndRatingViewModel::class.java!!)
            return fragmenntCaregiverReviewAndRatingViewModel as FragmenntCaregiverReviewAndRatingViewModel
        }

    companion object {
        fun newInstance(): FragmenntCaregiverReviewAndRating {
            val args = Bundle()
            val fragment = FragmenntCaregiverReviewAndRating()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmenntCaregiverReviewAndRatingViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentCaregiverReviewAndRatingBinding = viewDataBinding
//        fragmentDoctorProfileBinding?.btnDoctorEditProfile?.setOnClickListener(View.OnClickListener {
//            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                FragmentAddDoctorProfile.newInstance())
//        })
        setUpViewReviewAndRatinglistingRecyclerview()
    }
    // Set up recycler view for service listing if available
    private fun setUpViewReviewAndRatinglistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        assert(fragmentCaregiverReviewAndRatingBinding!!.recyclerViewCaregiverReviewandrating != null)
        val recyclerView = fragmentCaregiverReviewAndRatingBinding!!.recyclerViewCaregiverReviewandrating
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        val contactListAdapter = AdapterCaregiverReviewAndRatingRecyclerview(context!!)
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