package com.rootscare.model

import com.rootscare.data.model.response.doctor.profileresponse.QualificationDataItem
import java.io.File

class RegistrationModel(
    var userType: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var emailAddress: String? = null,
    var mobileNumber: String? = null,
    var dob: String? = null,
    var gender: String? = null,
    var password: String? = null,
    var confirmPassword: String? = null,
    var imageFile: File? = null,
    var imageName: String? = null,
    var certificateFile: File? = null,
    var certificatename: String? = null,
    var qualification: String? = "",
    var passingYear: String? = "",
    var institude: String? = "",
    var description: String? = null,
    var experience: String? = null,
    var availableTime: String? = null,
    var fees: String? = null,
    var department: String? = null,
    var license: String? = null,
    var licenseFile: File? = null,
    var licenseName: String? = null,
    var qualificationDataList: ArrayList<QualificationDataItem>? = null
)