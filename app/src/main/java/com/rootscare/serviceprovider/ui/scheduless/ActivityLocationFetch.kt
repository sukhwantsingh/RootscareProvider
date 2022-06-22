package com.rootscare.serviceprovider.ui.scheduless

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProviders
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.LayoutLocationChooserBinding
import com.rootscare.serviceprovider.ui.base.BaseActivity
import com.rootscare.serviceprovider.ui.home.HomeActivityNavigator
import com.rootscare.serviceprovider.ui.home.HomeActivityViewModel
import com.rootscare.serviceprovider.utilitycommon.navigate


class ActivityLocationFetch : BaseActivity<LayoutLocationChooserBinding, HomeActivityViewModel>(), HomeActivityNavigator {
    private var activityHomeBinding: LayoutLocationChooserBinding? = null
    private var homeViewModel: HomeActivityViewModel? = null

  private val AUTOCOMPLETE_REQUEST_CODE = 9098

    override val bindingVariable: Int
        get() = BR.viewModel

    override val layoutId: Int
        get() = R.layout.layout_location_chooser

    override val viewModel: HomeActivityViewModel
        get() {
            homeViewModel = ViewModelProviders.of(this).get(HomeActivityViewModel::class.java)
            return homeViewModel as HomeActivityViewModel
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel?.navigator = this
        activityHomeBinding = viewDataBinding
        try {
          if (!Places.isInitialized()) {
              Places.initialize(this, homeViewModel?.appSharedPref?.plcKey.orEmpty())
          }
        } catch (e:Exception) { println(e) }


        initViews()
    }

    private fun initViews() {
        activityHomeBinding?.run {
            imgBackArrow.setOnClickListener { finish() }
            edtSearch.setOnClickListener {
                if (Places.isInitialized()) {
                    onGoogleSearchCalled()
                }else {
                    showToast(getString(R.string.something_went_wrong))
                }

            }
            cnsCurrLoc.setOnClickListener {
              ScheduleActivity.isCurrLocChoosed.value = true
              finish()
            }
        }
    }

    fun onGoogleSearchCalled() {
        // Set the fields to specify which types of place data to return.
        val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            //.setCountry("NG")
            .build(this)
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode,resultCode,data)
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == -1) {
                try {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(it)
                       activityHomeBinding?.edtSearch?.setText(place.address)
                        val destinationLatLng = place.latLng
                        val latitude = destinationLatLng?.latitude.toString()
                        val longitude = destinationLatLng?.longitude.toString()
                        ScheduleActivity.currLat = destinationLatLng?.latitude ?: 0.0
                        ScheduleActivity.currLong= destinationLatLng?.longitude ?: 0.0
                        ScheduleActivity.diffAddressPicked = place.address.orEmpty()
                        ScheduleActivity.isDiffLocChoosed.value = true
                        finish()
                        Log.e("PLACE","latitude $latitude")
                        Log.e("PLACE","longitude $longitude")
                    }
                } catch (e:Exception){
                    println(e)
                }
            }
        }
    }


    //        val city: String = addresses[0].getLocality()
    //        val state: String = addresses[0].getAdminArea()
    //        val country: String = addresses[0].getCountryName()
    //        val postalCode: String = addresses[0].getPostalCode()
    //        val knownName: String = addresses[0].getFeatureName() // Only if available else return NULL


}
