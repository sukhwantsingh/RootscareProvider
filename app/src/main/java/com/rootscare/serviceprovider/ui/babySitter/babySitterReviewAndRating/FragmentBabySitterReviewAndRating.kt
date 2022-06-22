package com.rootscare.serviceprovider.ui.babySitter.babySitterReviewAndRating

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentCaregiverReviewAndRatingBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.caregiver.caregigerreviewandrating.adapter.AdapterCaregiverReviewAndRatingRecyclerview

class FragmentBabySitterReviewAndRating: BaseFragment<FragmentCaregiverReviewAndRatingBinding, FragmentBabySitterReviewAndRatingViewModel>(),
    FragmentBabySitterReviewAndRatingNavigator {
    private var fragmentCaregiverReviewAndRatingBinding: FragmentCaregiverReviewAndRatingBinding? = null
    private var fragmentCaregiverReviewAndRatingViewModel: FragmentBabySitterReviewAndRatingViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_caregiver_review_and_rating
    override val viewModel: FragmentBabySitterReviewAndRatingViewModel
        get() {
            fragmentCaregiverReviewAndRatingViewModel = ViewModelProviders.of(this).get(
                FragmentBabySitterReviewAndRatingViewModel::class.java
            )
            return fragmentCaregiverReviewAndRatingViewModel as FragmentBabySitterReviewAndRatingViewModel
        }

    companion object {
        fun newInstance(): FragmentBabySitterReviewAndRating {
            val args = Bundle()
            val fragment = FragmentBabySitterReviewAndRating()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentCaregiverReviewAndRatingViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentCaregiverReviewAndRatingBinding = viewDataBinding
//        fragmentDoctorProfileBinding?.btnDoctorEditProfile?.setOnClickListener(View.OnClickListener {
//            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                FragmentAddDoctorProfile.newInstance())
//        })
        setUpViewReviewAndRatingListingRecyclerview()
    }
    // Set up recycler view for service listing if available
    private fun setUpViewReviewAndRatingListingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        val recyclerView = fragmentCaregiverReviewAndRatingBinding!!.recyclerViewCaregiverReviewandrating
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerview(trainerList,context!!)
        val contactListAdapter = AdapterCaregiverReviewAndRatingRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter
//        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClickWithIdListener {
//            override fun onItemClick(id: Int) {
//                (activity as HomeActivity).checkFragmentInBackStackAndOpen(
//                    FragmentDoctorListingDetails.newInstance())
//            }
//
//        }


    }

}