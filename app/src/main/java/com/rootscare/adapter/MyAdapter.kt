package com.rootscare.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.rootscare.serviceprovider.ui.login.subfragment.forgotpassword.FragmentForgotPassword
import com.rootscare.serviceprovider.ui.login.subfragment.login.FragmentLogin
import com.rootscare.serviceprovider.ui.login.subfragment.registration.FragmentRegistration
import com.rootscare.serviceprovider.ui.login.subfragment.registration.subfragment.registrationsetpthree.FragmentRegistrationStepThree
import com.rootscare.serviceprovider.ui.login.subfragment.registration.subfragment.registrationstetwo.FragmentRegistrationStepTwo


class MyAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    val NUMBER_OF_PAGES =3
    override fun getCount(): Int {
        return NUMBER_OF_PAGES
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> FragmentLogin.newInstance()
            1 -> FragmentRegistration.newInstance()
            2 -> FragmentForgotPassword.newInstance()
            else -> FragmentLogin.newInstance()
        }
    }
}
/*
         2 ->  // return a different Fragment class here
                // if you want want a completely different layout
                FragmentRegistrationStepTwo.newInstance()

            3 ->  // return a different Fragment class here
                // if you want want a completely different layout
                FragmentRegistrationStepThree.newInstance()
* */
