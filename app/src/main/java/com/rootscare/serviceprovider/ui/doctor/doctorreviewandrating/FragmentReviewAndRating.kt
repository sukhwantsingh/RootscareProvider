package com.rootscare.serviceprovider.ui.doctor.doctorreviewandrating

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rootscare.data.model.request.doctor.appointment.upcomingappointment.getuppcomingappoint.GetDoctorUpcommingAppointmentRequest
import com.rootscare.data.model.response.doctor.review.ReviewResponse
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentReviewAndRatingBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctorreviewandrating.adapter.AdapterReviewAndRatingRecyclerview

class FragmentReviewAndRating : BaseFragment<FragmentReviewAndRatingBinding, FragmentReviewAndRatingViewModel>(),
    FragmentReviewAndRatingNavigator {


    private var contactListAdapter: AdapterReviewAndRatingRecyclerview? = null

    private var fragmentReviewAndRatingBinding: FragmentReviewAndRatingBinding? = null
    private var fragmentReviewAndRatingViewModel: FragmentReviewAndRatingViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_review_and_rating
    override val viewModel: FragmentReviewAndRatingViewModel
        get() {
            fragmentReviewAndRatingViewModel = ViewModelProviders.of(this).get(
                FragmentReviewAndRatingViewModel::class.java
            )
            return fragmentReviewAndRatingViewModel as FragmentReviewAndRatingViewModel
        }

    companion object {
        fun newInstance(): FragmentReviewAndRating {
            val args = Bundle()
            val fragment = FragmentReviewAndRating()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentReviewAndRatingViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentReviewAndRatingBinding = viewDataBinding
//        fragmentDoctorProfileBinding?.btnDoctorEditProfile?.setOnClickListener(View.OnClickListener {
//            (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                FragmentAddDoctorProfile.newInstance())
//        })
        setUpViewReviewAndRatinglistingRecyclerview()
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            var getDoctorUpcommingAppointmentRequest = GetDoctorUpcommingAppointmentRequest()
            getDoctorUpcommingAppointmentRequest.userId =
                fragmentReviewAndRatingViewModel?.appSharedPref?.loginUserId
            fragmentReviewAndRatingViewModel!!.getReviewFromApi(
                getDoctorUpcommingAppointmentRequest
            )
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    // Set up recycler view for service listing if available
    private fun setUpViewReviewAndRatinglistingRecyclerview() {
        assert(fragmentReviewAndRatingBinding!!.recyclerViewRootscareReviewandrating != null)
        val recyclerView = fragmentReviewAndRatingBinding!!.recyclerViewRootscareReviewandrating
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        contactListAdapter = AdapterReviewAndRatingRecyclerview(context!!)
        recyclerView.adapter = contactListAdapter
//        contactListAdapter?.recyclerViewItemClickWithView= object : OnItemClikWithIdListener {
//            override fun onItemClick(id: Int) {
//                (activity as HomeActivity).checkFragmentInBackstackAndOpen(
//                    FragmentDoctorListingDetails.newInstance())
//            }
//
//        }


    }

    override fun onSuccessReview(response: ReviewResponse) {
        baseActivity?.hideLoading()
        if (response.code.equals("200")) {
            if (response.result != null && response.result.size > 0) {
                fragmentReviewAndRatingBinding?.recyclerViewRootscareReviewandrating?.visibility = View.VISIBLE
                fragmentReviewAndRatingBinding?.tvNoDate?.visibility = View.GONE
                contactListAdapter?.result = response.result
                contactListAdapter?.notifyDataSetChanged()
            }else{
                fragmentReviewAndRatingBinding?.recyclerViewRootscareReviewandrating?.visibility = View.INVISIBLE
                fragmentReviewAndRatingBinding?.tvNoDate?.visibility = View.VISIBLE
            }
        }else{
            fragmentReviewAndRatingBinding?.recyclerViewRootscareReviewandrating?.visibility = View.INVISIBLE
            fragmentReviewAndRatingBinding?.tvNoDate?.visibility = View.VISIBLE
        }
    }

    override fun onThrowable(throwable: Throwable) {
        baseActivity?.hideLoading()
    }
}