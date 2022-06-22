package com.rootscare.serviceprovider.ui.babySitter.babySitterUpdateReviewAndRating

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.data.model.request.doctor.appointment.upcomingappointment.getuppcomingappoint.GetDoctorUpcommingAppointmentRequest
import com.rootscare.data.model.response.doctor.review.ReviewResponse
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentNursesReviewAndRatingBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.babySitter.babySitterUpdateReviewAndRating.adapter.AdapterBabySitterUpdateReviewAndRating

class FragmentBabySitterUpdateReviewAndRating :

    BaseFragment<FragmentNursesReviewAndRatingBinding, FragmentBabySitterUpdateReviewAndRatingViewModel>(),
    FragmentBabySitterUpdateReviewAndRatingNavigator {

    private var contactListAdapter: AdapterBabySitterUpdateReviewAndRating? = null

    private var fragmentNursesReviewAndRatingBinding: FragmentNursesReviewAndRatingBinding? = null
    private var fragmentNursesReviewAndRatingViewModel: FragmentBabySitterUpdateReviewAndRatingViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_nurses_review_and_rating
    override val viewModel: FragmentBabySitterUpdateReviewAndRatingViewModel
        get() {
            fragmentNursesReviewAndRatingViewModel = ViewModelProviders.of(this).get(
                FragmentBabySitterUpdateReviewAndRatingViewModel::class.java
            )
            return fragmentNursesReviewAndRatingViewModel as FragmentBabySitterUpdateReviewAndRatingViewModel
        }

    companion object {
        fun newInstance(): FragmentBabySitterUpdateReviewAndRating {
            val args = Bundle()
            val fragment = FragmentBabySitterUpdateReviewAndRating()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentNursesReviewAndRatingViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentNursesReviewAndRatingBinding = viewDataBinding
//        fragmentDoctorProfileBinding?.btnDoctorEditProfile?.setOnClickListener(View.OnClickListener {
//            (activity as HomeActivity).checkFragmentInBackStackAndOpen(
//                FragmentAddDoctorProfile.newInstance())
//        })
        setUpViewReviewAndRatingListingRecyclerView()

        if (isNetworkConnected) {
            baseActivity?.showLoading()
            val getDoctorUpcomingAppointmentRequest = GetDoctorUpcommingAppointmentRequest()
            getDoctorUpcomingAppointmentRequest.userId =
                fragmentNursesReviewAndRatingViewModel?.appSharedPref?.loginUserId
            fragmentNursesReviewAndRatingViewModel!!.getReviewFromApi(
                getDoctorUpcomingAppointmentRequest
            )
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    // Set up recycler view for service listing if available
    private fun setUpViewReviewAndRatingListingRecyclerView() {
//        trainerList: ArrayList<TrainerListItem?>?
        val recyclerView = fragmentNursesReviewAndRatingBinding!!.recyclerViewNursesReviewandrating
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//        val contactListAdapter = AdapterHospitalRecyclerviw(trainerList,context!!)
        contactListAdapter = AdapterBabySitterUpdateReviewAndRating(context!!)
        recyclerView.adapter = contactListAdapter
//        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClickWithIdListener {
//            override fun onItemClick(id: Int) {
//                (activity as HomeActivity).checkFragmentInBackStackAndOpen(
//                    FragmentDoctorListingDetails.newInstance())
//            }
//
//        }
    }

    override fun onSuccessReview(response: ReviewResponse) {
        baseActivity?.hideLoading()
        if (response.code.equals("200")) {
            if (response.result != null && response.result.size > 0) {
                fragmentNursesReviewAndRatingBinding?.recyclerViewNursesReviewandrating?.visibility = View.VISIBLE
                fragmentNursesReviewAndRatingBinding?.tvNoDate?.visibility = View.GONE
                contactListAdapter?.result = response.result
                contactListAdapter?.notifyDataSetChanged()
            } else {
                fragmentNursesReviewAndRatingBinding?.recyclerViewNursesReviewandrating?.visibility = View.INVISIBLE
                fragmentNursesReviewAndRatingBinding?.tvNoDate?.visibility = View.VISIBLE
            }
        } else {
            fragmentNursesReviewAndRatingBinding?.recyclerViewNursesReviewandrating?.visibility = View.INVISIBLE
            fragmentNursesReviewAndRatingBinding?.tvNoDate?.visibility = View.VISIBLE
        }
    }

    override fun onThrowable(throwable: Throwable) {
        baseActivity?.hideLoading()
    }
}