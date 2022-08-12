package com.rootscare.data.datasource.api


import com.google.gson.JsonObject
import com.rootscare.data.model.request.commonuseridrequest.CommonNotificationIdRequest
import com.rootscare.data.model.request.commonuseridrequest.CommonUserIdRequest
import com.rootscare.data.model.request.commonuseridrequest.NeedSupportRequest
import com.rootscare.data.model.request.doctor.appointment.upcomingappointment.filterappointmentrequest.FilterAppointmentRequest
import com.rootscare.data.model.request.doctor.appointment.upcomingappointment.getuppcomingappoint.GetDoctorUpcommingAppointmentRequest
import com.rootscare.data.model.request.doctor.appointment.updateappointmentrequest.UpdateAppointmentRequest
import com.rootscare.data.model.request.doctor.myscheduleaddhospital.AddHospitalRequest
import com.rootscare.data.model.request.doctor.myscheduleaddtimeslot.AddTimeSlotRequest
import com.rootscare.data.model.request.doctor.myscheduletimeslot.GetTimeSlotMyScheduleRequest
import com.rootscare.data.model.request.fetchScheduleData.FetchScheduleRequest
import com.rootscare.data.model.request.forgotpassword.forgotpasswordchangerequest.ForgotPasswordChangeRequest
import com.rootscare.data.model.request.forgotpassword.forgotpasswordemailrequest.ForgotPasswordSendEmailRequest
import com.rootscare.data.model.request.hospital.*
import com.rootscare.data.model.request.loginrequest.LoginRequest
import com.rootscare.data.model.request.pushNotificationRequest.PushNotificationRequest
import com.rootscare.data.model.request.sendschedule.SendScheduleRequest
import com.rootscare.data.model.request.twilio.TwilioAccessTokenRequest
import com.rootscare.data.model.request.videoPushRequest.VideoPushRequest
import com.rootscare.data.model.response.CommonResponse
import com.rootscare.data.model.response.NotificationCountResponse
import com.rootscare.data.model.response.deaprtmentlist.DepartmentListResponse
import com.rootscare.data.model.response.doctor.appointment.pastappointment.ResponsePastAppointment
import com.rootscare.data.model.response.doctor.appointment.requestappointment.getrequestappointment.GetDoctorRequestAppointmentResponse
import com.rootscare.data.model.response.doctor.appointment.todaysappointment.GetDoctorTodaysAppointmentResponse
import com.rootscare.data.model.response.doctor.appointment.upcomingappointment.DoctorUpcomingAppointmentResponse
import com.rootscare.data.model.response.doctor.myschedule.homeVisitList.HomeVisitSchedule
import com.rootscare.data.model.response.doctor.myschedule.timeslotlist.MyScheduleTimeSlotResponse
import com.rootscare.data.model.response.doctor.payment.PaymentResponse
import com.rootscare.data.model.response.doctor.profileresponse.GetDoctorProfileResponse
import com.rootscare.data.model.response.doctor.review.ReviewResponse
import com.rootscare.data.model.response.fetchscheduleresponse.FetchScheduleResponse
import com.rootscare.data.model.response.forgotpasswordresponse.forgotpasswordchangepassword.ForgotPasswordChangePasswordResponse
import com.rootscare.data.model.response.forgotpasswordresponse.forgotpasswordsendmailresponse.ForgotPasswordSendMailResponse
import com.rootscare.data.model.response.hospital.*
import com.rootscare.data.model.response.loginresponse.BankDetailsResponse
import com.rootscare.data.model.response.loginresponse.LoginResponse
import com.rootscare.data.model.response.loginresponse.TaskListResponse
import com.rootscare.data.model.response.registrationresponse.RegistrationResponse
import com.rootscare.data.model.response.twilio.TwilioAccessTokenResponse
import com.rootscare.data.model.response.videoPushResponse.VideoPushResponse
import com.rootscare.model.ModelServiceFor
import com.rootscare.serviceprovider.ui.manageDocLab.model.ModelHospitalDocs
import com.rootscare.serviceprovider.ui.notificationss.ModelUpdateRead
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.models.ModelHospDeparts
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.models.ModelUserProfile
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.newappointments.ModelAppointmentsListing
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.newappointments.models.ModelAppointmentDetails
import com.rootscare.serviceprovider.ui.pricelistss.models.ModelPackageDetails
import com.rootscare.serviceprovider.ui.pricelistss.models.ModelPriceListing
import com.rootscare.serviceprovider.ui.pricelistss.models.ModelPackages
import com.rootscare.serviceprovider.ui.scheduless.ModelScheduleTiming
import com.rootscare.serviceprovider.ui.splash.model.NetworkAppCheck
import com.rootscare.serviceprovider.ui.supportmore.models.ModelIssueTypes
import com.rootscare.serviceprovider.ui.transactionss.models.ModelTransactions
import com.rootscare.serviceprovider.ui.transactionss.models.ModelWithdrawalDetails
import com.rootscare.serviceprovider.utilitycommon.asReqBody
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    //    @Headers({
    //            "x-api-key: 123456"
    //    })

//    @POST("api-login")
//    fun login(@Body loginRequestBody: LogInRequest): Single<LogInResponse>
////
////    @POST("admin/api-student-registration")
////    fun apistudentregistration(@Body registrationRequestBody: RegistrationRequest): Single<RegistrationResponse>
////

    //Login Api Call
    @POST("api-service-provider-login")
    fun apiServiceProviderLogin(@Body loginRequestBody: LoginRequest): Single<ModelUserProfile>

    //Get Doctor Profile Api Call
    @POST("api-pathology-hospital_details")
    fun apipathodetails(@Body commonUserIdRequestBody: getpathodetails): Single<OrderDetails>

    @POST("api-doctor-profile")
    fun apidoctorprofile(@Body commonUserIdRequestBody: CommonUserIdRequest): Single<GetDoctorProfileResponse>

    @POST("api-nurse-profile")
    fun apinurseprofile(@Body commonUserIdRequestBody: CommonUserIdRequest): Single<GetDoctorProfileResponse>

    @POST("api-caregiver-profile")
    fun apicaregivereprofile(@Body commonUserIdRequestBody: CommonUserIdRequest): Single<com.rootscare.data.model.response.caregiver.profileresponse.GetDoctorProfileResponse>

    @POST("api-babysitter-profile")
    fun apiBabySitterProfile(@Body commonUserIdRequestBody: CommonUserIdRequest): Single<com.rootscare.data.model.response.caregiver.profileresponse.GetDoctorProfileResponse>

    @POST("api-physiotherapy-profile")
    fun apiPhysiotherapyProfile(@Body commonUserIdRequestBody: CommonUserIdRequest): Single<com.rootscare.data.model.response.caregiver.profileresponse.GetDoctorProfileResponse>

    @POST("insert-preferable-time")
    fun sendProviderSchedule(@Body sendScheduleRequest: SendScheduleRequest): Single<GetDoctorProfileResponse>

    @POST("api-doctor-add-preferable-time")
    fun sendProviderScheduleForDoctor(@Body req: SendScheduleRequest): Single<GetDoctorProfileResponse>

    @POST("provider-get-time-slot")
    fun fetchProviderSchedule(@Body fetchScheduleRequest: FetchScheduleRequest): Single<ModelScheduleTiming>

    @POST("api-doctor-get-timeslot")
    fun fetchProviderScheduleForDoctor(@Body req: FetchScheduleRequest): Single<ModelScheduleTiming>

    @POST("provider-nurse-task-base-time-slot")
    fun fetchProviderScheduleForNurseTaskBase(@Body fetchScheduleRequest: FetchScheduleRequest): Single<ModelScheduleTiming>

    @POST("provider-nurse-hour-base-time-slot")
    fun fetchProviderScheduleForNurseHourBase(@Body fetchScheduleRequest: FetchScheduleRequest): Single<ModelScheduleTiming>


    //    @POST("api-department-hospital-list")
    @POST("api-hospital-departments-list")
    fun apihospitaldepartment(@Body commonUserIdRequestBody: CommonHospitalId): Single<HospitalHDepartmentListResponse>

    @POST("api-department-hospital-delete")
    fun apideletedepartment(@Body commonUserIdRequestBody: DeleteDepartment): Single<HospitalHDepartmentListResponse>

    @POST("api-department-save-for-hospital")
    fun apieditdepartment(@Body commonUserIdRequestBody: EditDepart): Single<HospitalHDepartmentListResponse>

    @POST("api-department-save-for-hospital")
    fun apieditnewdepartment(@Body commonUserIdRequestBody: EditNew): Single<HospitalHDepartmentListResponse>


    @POST("api-hospital-doctor-list")
    fun apidoctorlistviahospital(@Body homeSearchRequestRequestBody: requestdoctor): Single<AllDoctorHosListingRes>

    @POST("api-hospital-doctor-list-by-hospitalid")
    fun apidoctorlistviahospitalModule(@Body homeSearchRequestRequestBody: requestdoctor): Single<AllDoctorHosListingRes>

    @POST("api-payment-list-by-hospital")
    fun PaymenetModule(@Body homeSearchRequestRequestBody: requestdoctor): Single<PaymentResponseHospital>

    @POST("api-doctor-delete-by-hospital")
    fun apideletedoctor(@Body homeSearchRequestRequestBody: hospitaldelte): Single<AllDoctorHosListingRes>

    @POST("api-provider-get-speciality")
    fun fetchSpeciality(@Body reqBody: RequestBody): Single<ModelIssueTypes>

    @GET("api-medical-service-area")
    fun apiServiceFor(): Single<ModelServiceFor>

    @Multipart
    @POST("api-service-provider-registration")
    fun apiSignup(
        @Part("service_type") user_type: RequestBody,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("mobile_number") mobile_number: RequestBody,
        @Part("dob") dob: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("password") password: RequestBody,
        @Part("confirm_password") confirm_password: RequestBody,

        @Part("id_number") id_number: RequestBody,
        @Part("speciality") speciality: RequestBody,
        @Part("qualification") qualification: RequestBody,
        @Part("experience") experience: RequestBody,
        @Part("scfhs_number") scfhs_number: RequestBody,

        @Part id_image: MultipartBody.Part?,
        @Part certificate: MultipartBody.Part?,
        @Part scfhs_image: MultipartBody.Part?,
        @Part("hosp_moh_lic_no") hosp_moh_lic_no: RequestBody,
        @Part("hosp_reg_no") hosp_reg_no: RequestBody,
        @Part("work_area") work_area: RequestBody
    ): Single<RegistrationResponse>

    @Multipart
    @POST("api-service-provider-registration")
    fun apiserviceproviderregistration(
        @Part("user_type") user_type: RequestBody,
        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("mobile_number") mobile_number: RequestBody,
        @Part("dob") dob: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("password") password: RequestBody,
        @Part("confirm_password") confirm_password: RequestBody,
        @Part("qualification") qualification: RequestBody,
        @Part("passing_year") passing_year: RequestBody,
        @Part("institute") institute: RequestBody,
        @Part("description") description: RequestBody,
        @Part("experience") experience: RequestBody,
        @Part("available_time") available_time: RequestBody,
        @Part("fees") fees: RequestBody,
        @Part("department") department: RequestBody? = null,
        @Part certificate: List<MultipartBody.Part>,
        @Part image: MultipartBody.Part
    ): Single<RegistrationResponse>

    @Multipart
    @POST("api-hospital-provider-registration")
    fun apiServiceProviderHospitalRegistration(
        @Part("user_type") user_type: RequestBody,
        @Part("first_name") first_name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("mobile_number") mobile_number: RequestBody,
        @Part("password") password: RequestBody,
        @Part("confirm_password") confirm_password: RequestBody,
        @Part("description") description: RequestBody,
        @Part("experience") experience: RequestBody,
        @Part("available_time") available_time: RequestBody,
        @Part("licence") department: RequestBody? = null,
        @Part image: MultipartBody.Part,
        @Part license: MultipartBody.Part
    ): Single<RegistrationResponse>

    //Forgot Password Api Call
    @POST("api-forgot-password-email")
    fun apiforgotpasswordemail(@Body forgotPasswordSendEmailRequestBody: ForgotPasswordSendEmailRequest): Single<ForgotPasswordSendMailResponse>

    @POST("api-forgot-change-password")
    fun apiforgotchangepassword(@Body forgotPasswordChangeRequestBody: ForgotPasswordChangeRequest): Single<ForgotPasswordChangePasswordResponse>


    //Doctor Upcoming Appointment
    @POST("api-doctor-upcoming-appointment-list")
    fun apidoctorupcomingappointmentlis(@Body getDoctorUpcomingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<DoctorUpcomingAppointmentResponse>

    @POST("api-nurse-upcoming-appointment-list")
    fun apiNurseUpComingAppointmentList(@Body getDoctorUpcomingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<DoctorUpcomingAppointmentResponse>

    @POST("api-caregiver-upcoming-appointment-list")
    fun apiCaregiverUpComingAppointmentList(@Body getDoctorUpcomingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<DoctorUpcomingAppointmentResponse>

    @POST("api-babysitter-upcoming-appointment-list")
    fun apiBabySitterUpComingAppointmentList(@Body getDoctorUpcomingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<DoctorUpcomingAppointmentResponse>

    @POST("api-physiotherapy-upcoming-appointment-list")
    fun apiPhysiotherapistUpComingAppointmentList(@Body getDoctorUpcomingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<DoctorUpcomingAppointmentResponse>

    @POST("api-filter-doctor-upcoming-appointment-list")
    fun apifilterdoctorupcomingappointmentlist(@Body filterAppointmentRequestBody: FilterAppointmentRequest): Single<DoctorUpcomingAppointmentResponse>

    @POST("api-filter-nurse-upcoming-appointment-list")
    fun apiFilterNurseUpComingAppointmentList(@Body filterAppointmentRequestBody: FilterAppointmentRequest): Single<DoctorUpcomingAppointmentResponse>

    @POST("api-filter-caregiver-upcoming-appointment-list")
    fun apiFilterCaregiverUpComingAppointmentList(@Body filterAppointmentRequestBody: FilterAppointmentRequest): Single<DoctorUpcomingAppointmentResponse>

    @POST("api-filter-babysitter-upcoming-appointment-list")
    fun apiFilterBabySitterUpComingAppointmentList(@Body filterAppointmentRequestBody: FilterAppointmentRequest): Single<DoctorUpcomingAppointmentResponse>

    @POST("api-filter-physiotherapy-upcoming-appointment-list")
    fun apiFilterPhysiotherapyUpComingAppointmentList(@Body filterAppointmentRequestBody: FilterAppointmentRequest): Single<DoctorUpcomingAppointmentResponse>

    @POST("api-doctor-today-appointment-list")
    fun apidoctortodayappointmentlist(@Body getDoctorUpcomingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<GetDoctorTodaysAppointmentResponse>

    @POST("api-nurse-today-appointment-list")
    fun apiNurseTodayAppointmentList(@Body getDoctorUpcomingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<GetDoctorTodaysAppointmentResponse>

    @POST("api-caregiver-today-appointment-list")
    fun apiCaregiverTodayAppointmentList(@Body getDoctorUpcomingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<GetDoctorTodaysAppointmentResponse>

    @POST("api-babysitter-today-appointment-list")
    fun apiBabySitterTodayAppointmentList(@Body getDoctorUpcomingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<GetDoctorTodaysAppointmentResponse>

    @POST("api-physiotherapy-today-appointment-list")
    fun apiPhysiotherapistTodayAppointmentList(@Body getDoctorUpcomingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<GetDoctorTodaysAppointmentResponse>

    @POST("api-doctor-appointment-request-list")
    fun apidoctorappointmentrequestlist(@Body getDoctorUpcomingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<GetDoctorRequestAppointmentResponse>

    @POST("api-nurse-appointment-request-list")
    fun apiNurseAppointmentRequestList(@Body getDoctorUpcomingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<GetDoctorRequestAppointmentResponse>

    @POST("api-caregiver-appointment-request-list")
    fun apiCaregiverAppointmentRequestList(@Body getDoctorUpcomingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<GetDoctorRequestAppointmentResponse>

    @POST("api-babysitter-appointment-request-list")
    fun apiBabySitterAppointmentRequestList(@Body getDoctorUpcomingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<GetDoctorRequestAppointmentResponse>

    @POST("api-physiotherapy-appointment-request-list")
    fun apiPhysiotherapistAppointmentRequestList(@Body getDoctorUpcomingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<GetDoctorRequestAppointmentResponse>

    @POST("api-update-doctor-appointment-request")
    fun apiUpdateDoctorAppointmentRequest(@Body updateAppointmentRequestBody: UpdateAppointmentRequest): Single<GetDoctorRequestAppointmentResponse>

    @POST("api-update-nurse-appointment-request")
    fun apiUpdateNurseAppointmentRequest(@Body updateAppointmentRequestBody: UpdateAppointmentRequest): Single<GetDoctorRequestAppointmentResponse>

    @POST("api-update-caregiver-appointment-request")
    fun apiUpdateCaregiverAppointmentRequest(@Body updateAppointmentRequestBody: UpdateAppointmentRequest): Single<GetDoctorRequestAppointmentResponse>

    @POST("api-update-babysitter-appointment-request")
    fun apiUpdateBabySitterAppointmentRequest(@Body updateAppointmentRequestBody: UpdateAppointmentRequest): Single<GetDoctorRequestAppointmentResponse>

    @POST("api-update-physiotherapy-appointment-request")
    fun apiUpdatePhysiotherapistAppointmentRequest(@Body updateAppointmentRequestBody: UpdateAppointmentRequest): Single<GetDoctorRequestAppointmentResponse>

    @POST("api-department-list")
    fun apidepartmentlist(): Single<DepartmentListResponse>

    @POST("api-doctor-past-appointment-list")
    fun apidoctorappointmentPastList(@Body getDoctorUpcomingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<ResponsePastAppointment>

    @POST("api-past-pathology-appointment-list-by-hospital")
    fun apiPathologyAppointmentPastListHospital(@Body getDoctorUpcomingAppointmentRequestBody: getdoctorhospital): Single<ResponsePastAppointment>

    @POST("api-hospital-doctor-past-appointment-list")
    fun apiDoctorAppointmentPastListHospital(@Body getDoctorUpcomingAppointmentRequestBody: getdoctorhospital): Single<ResponsePastAppointment>

    @POST("api-pathology-past-order-list")
    fun apidoctorappointmentPastListLab(@Body getDoctorUpcomingAppointmentRequestBody: getdoctorhospital): Single<ResponseLabPastAppointment>

    @POST("api-pathology-upcomming-order-list")
    fun apipathologyappointmentPastListHos(@Body getDoctorUpcomingAppointmentRequestBody: getdoctorhospital): Single<ResponseLabPastAppointment>

    @POST("api-hospital-doctor-upcoming-appointment-list")
    fun apiDoctorAppointmentUpcomingListHospital(@Body getDoctorUpcomingAppointmentRequestBody: getdoctorhospital): Single<ResponsePastAppointment>

    @POST("api-upcomming-pathology-appointment-list-by-hospital")
    fun apiPathologyAppointmentUpcomingListHospital(@Body getDoctorUpcomingAppointmentRequestBody: getdoctorhospital): Single<ResponsePastAppointment>

    @POST("api-pathology-list-by-hospital")
    fun apiLab(@Body getDoctorUpcommingAppointmentRequestBody: getdoctorhospital): Single<HospitalLabUpload>

    @POST("api-hospital-pathology-report-list")
    fun apiReportPathology(@Body getDoctorUpcomingAppointmentRequestBody: getdoctorhospital): Single<ReportResultItem>

    @POST("api-search-sample-collection-list-by-hospital")
    fun searchApiLab(@Body getDoctorUpcomingAppointmentRequestBody: LabSearch): Single<HospitalLabUpload>

    @POST("api-hospital-doctor-cancelled-appointment-list")
    fun apiDoctorAppointmentCancelListHospital(@Body getDoctorUpcomingAppointmentRequestBody: getdoctorhospital): Single<ResponsePastAppointment>

    @POST("api-cancelled-pathology-appointment-list-by-hospital")
    fun apiPathologyAppointmentCancelListHospital(@Body getDoctorUpcomingAppointmentRequestBody: getdoctorhospital): Single<ResponsePastAppointment>

    @POST("api-pathology-cancelled-order-list")
    fun apiDoctorAppointmentCancelledListLab(@Body getDoctorUpcomingAppointmentRequestBody: getdoctorhospital): Single<ResponseLabPastAppointment>

    @POST("api-nurse-past-appointment-list")
    fun apiNurseAppointmentPastList(@Body getDoctorUpcomingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<ResponsePastAppointment>

    @POST("api-caregiver-past-appointment-list")
    fun apiCaregiverAppointmentPastList(@Body getDoctorUpcomingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<ResponsePastAppointment>

    @POST("api-babysitter-past-appointment-list")
    fun apiBabySitterAppointmentPastList(@Body getDoctorUpcomingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<ResponsePastAppointment>

    @POST("api-physiotherapy-past-appointment-list")
    fun apiPhysiotherapistAppointmentPastList(@Body getDoctorUpcomingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<ResponsePastAppointment>

    @Multipart
    @POST("api-sp-edit-profile")
    fun apiHitUpdateProfileWithImageAndCertificate(
        @Part("user_id") user_id: RequestBody,
        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("mobile_number") mobile_number: RequestBody,
        @Part("dob") dob: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("qualification") qualification: RequestBody? = null,
        @Part("passing_year") passing_year: RequestBody? = null,
        @Part("institute") institute: RequestBody? = null,
        @Part("description") description: RequestBody,
        @Part("experience") experience: RequestBody,
        @Part("available_time") available_time: RequestBody,
        @Part("fees") fees: RequestBody,
        @Part("department") department: RequestBody,
        @Part("start_time") startTime: RequestBody,
        @Part("end_time") endTime: RequestBody,
        @Part image: MultipartBody.Part? = null,
        @Part certificate: List<MultipartBody.Part>? = null
    ): Single<LoginResponse>

    @Multipart
    @POST("api-doctor-save-by-hospital")
    fun apiHitUpdateProfileWithImageAndCertificateNew(
        @Part("hospital_id") hospital_id: RequestBody,
        @Part("first_name") first_name: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("confirm_password") confirmPassword: RequestBody,
        @Part("mobile_number") mobile_number: RequestBody,
        @Part("dob") dob: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("qualification") qualification: RequestBody? = null,
        @Part("passing_year") passing_year: RequestBody? = null,
        @Part("institute") institute: RequestBody? = null,
        @Part("description") description: RequestBody,
        @Part("experience") experience: RequestBody,
        @Part("available_time") available_time: RequestBody,
        @Part("fees") fees: RequestBody,
        @Part("department") department: RequestBody,
        @Part("start_time") startTime: RequestBody,
        @Part("end_time") endTime: RequestBody,
        @Part image: MultipartBody.Part? = null,
        @Part certificate: List<MultipartBody.Part>? = null
    ): Single<LoginResponse>

//@Multipart
//@POST("api-sp-doctor-edit-profile")
//fun apiHitUpdateProfileWithImageAndCertificate(
//    @Part("user_id") user_id: RequestBody,
//    @Part("first_name") first_name: RequestBody,
//    @Part("last_name") last_name: RequestBody,
//    @Part("email") email: RequestBody,
//    @Part("mobile_number") mobile_number: RequestBody,
//    @Part("dob") dob: RequestBody,
//    @Part("gender") gender: RequestBody,
//    @Part("description") description: RequestBody,
//    @Part("experience") experience: RequestBody,
//    @Part("available_time") available_time: RequestBody,
//    @Part("fees") fees: RequestBody,
//    @Part("department") department: RequestBody
//): Single<RegistrationResponse>


    @POST("api-doctor-review")
    fun getDoctorReview(@Body reqBody: GetDoctorUpcommingAppointmentRequest): Single<ReviewResponse>

    @POST("api-all-provider-review")
    fun apiAllProviders(@Body reqBody: NeedSupportRequest): Single<ReviewResponse>

    @POST("api-caregiver-review")
    fun getCaregiverReview(@Body reqBody: GetDoctorUpcommingAppointmentRequest): Single<ReviewResponse>

    @POST("api-babysitter-review")
    fun getBabySitterReview(@Body reqBody: GetDoctorUpcommingAppointmentRequest): Single<ReviewResponse>

    @POST("api-physiotherapy-review")
    fun getPhysiotherapistReview(@Body reqBody: GetDoctorUpcommingAppointmentRequest): Single<ReviewResponse>

    @POST("api-doctor-payment-history")
    fun getPaymentHistory(@Body reqBody: GetDoctorUpcommingAppointmentRequest): Single<PaymentResponse>

    @POST("api-nurse-payment-history")
    fun getPaymentHistoryForNurse(@Body reqBody: GetDoctorUpcommingAppointmentRequest): Single<PaymentResponse>

    @POST("api-caregiver-payment-history")
    fun getPaymentHistoryForCaregiver(@Body reqBody: GetDoctorUpcommingAppointmentRequest): Single<PaymentResponse>

    @POST("api-babysitter-payment-history")
    fun getPaymentHistoryForBabySitter(@Body reqBody: GetDoctorUpcommingAppointmentRequest): Single<PaymentResponse>

    @POST("api-provider-withdrawal-history")
    fun getWithdrawalDetails(@Body reqBody: RequestBody): Single<ModelWithdrawalDetails>

    @POST("api-provider-transactions-history")
    fun getPaymentTransactionsDetails(@Body reqBody: RequestBody): Single<ModelTransactions>

    @POST("api-provider-delete-bank-details")
    fun delBankDetails(@Body requestBody: RequestBody): Single<CommonResponse>


//    @POST("api-doctor-hospital-clinic")
//    fun getMyScheduleHospitalList(@Body getDoctorUpcomingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<MyScheduleHospitalResponse>

//    @POST("api-doctor-homevisit-slot")
//    fun apiHomeVisitSlot(@Body getDoctorUpcomingAppointmentRequestBody: GetDoctorUpcommingAppointmentRequest): Single<HomeVisitSchedule>

    @POST("provider-get-time-slot-doctor-oneline")
    fun apiDoctorOnlineScheduleSlots(@Body getDoctorUpcomingAppointmentRequestBody: FetchScheduleRequest): Single<FetchScheduleResponse>

    @POST("provider-get-time-slot-doctor-home-visit")
    fun apiDoctorHomeVisitScheduleSlots(@Body getDoctorUpcomingAppointmentRequestBody: FetchScheduleRequest): Single<FetchScheduleResponse>

    @POST("api-insert-doctor-private-clinic")
    fun addHospitalMyschedule(@Body getDoctorUpcomingAppointmentRequestBody: AddHospitalRequest): Single<CommonResponse>

    @POST("api-doctor-clinic-private-slot")
    fun getAllTimeSlotMyschedule(@Body request: GetTimeSlotMyScheduleRequest): Single<MyScheduleTimeSlotResponse>

    @POST("api-doctor-clinic-slot-filter")
    fun getDaySpecificTimeSlotMyschedule(@Body request: GetTimeSlotMyScheduleRequest): Single<MyScheduleTimeSlotResponse>

    @POST("api-doctor-homevisit-slot-filter")
    fun getDaySpecificHomeSlot(@Body request: GetTimeSlotMyScheduleRequest): Single<HomeVisitSchedule>

    @POST("api-delete-doctor-private-slot")
    fun apiHitRemoveTimeSlotMySchedule(@Body request: GetTimeSlotMyScheduleRequest): Single<MyScheduleTimeSlotResponse>

    @POST("api-delete-doctor-homevisit-slot")
    fun apiHitRemoveHomeSlot(@Body request: GetTimeSlotMyScheduleRequest): Single<MyScheduleTimeSlotResponse>

    @POST("api-insert-doctor-private-slot")
    fun apiHitSaveTimeSlotForDoctor(@Body request: AddTimeSlotRequest): Single<CommonResponse>

    @POST("api-insert-doctor-homevisit-slot")
    fun apiHitSaveHomeVisitSlotForDoctor(@Body request: AddTimeSlotRequest): Single<CommonResponse>

    @POST("api-insert-hospita-doctor-slot")
    fun apiHitSaveTimeSlotForHospitalDoctor(@Body request: AddTimeSlotRequest): Single<CommonResponse>

    @POST("api-doctor-complete-appointment-status")
    fun apiHitForMarkAsComplete(@Body request: CommonUserIdRequest): Single<CommonResponse>

    @POST("api-nurse-complete-appointment-status")
    fun apiHitForMarkAsCompleteForNurse(@Body request: CommonUserIdRequest): Single<CommonResponse>

    @POST("api-caregiver-complete-appointment-status")
    fun apiHitForMarkAsCompleteForCaregiver(@Body request: CommonUserIdRequest): Single<CommonResponse>

    @POST("api-babysitter-complete-appointment-status")
    fun apiHitForMarkAsCompleteForBabySitter(@Body request: CommonUserIdRequest): Single<CommonResponse>

    @POST("api-physiotherapy-complete-appointment-status")
    fun apiHitForMarkAsCompleteForPhysiotherapist(@Body request: CommonUserIdRequest): Single<CommonResponse>

    @Multipart
    @POST("api-doctor-insert-prescription")
    fun uploadPrescription(
        @Part("patient_id") patient_id: RequestBody,
        @Part("doctor_id") doctor_id: RequestBody,
        @Part("appointment_id") appointment_id: RequestBody,
        @Part("prescription_number") prescription_number: RequestBody,
        @Part prescription: MultipartBody.Part? = null
    ): Single<CommonResponse>

    // price list new apis's common

    @POST("api-hopital-docotor-list")
    fun getHospitalDocList(@Body requestBody: RequestBody?): Single<ModelHospitalDocs>

    @POST("api-provider-get-price-list")
    fun getProvidersTasksApi(@Body requestBody: RequestBody): Single<ModelPriceListing>

    @POST("api-get-lab-task")
    fun getLabTestsApi(@Body requestBody: RequestBody?): Single<ModelPriceListing>

    @POST("api-get-lab-task")
    fun getLabPackagesApi(@Body requestBody: RequestBody?): Single<ModelPackages>

    @POST("api-get-lab-package-details")
    fun getLabPackagesDetails(@Body requestBody: RequestBody?): Single<ModelPackageDetails>

    @POST("api-doctor-get-price")
    fun getProvidersTasksApiForDoc(@Body requestBody: RequestBody): Single<ModelPriceListing>

    @POST("api-provider-insert-update-price-list")
    fun insertUpdatePriceApi(@Body requestBody: RequestBody): Single<CommonResponse>

    @POST("api-lab-package-disable")
    fun disableLabPackage(@Body requestBody: RequestBody): Single<CommonResponse>

    @POST("api-add-lab-task_price")
    fun insertUpdateLabTestsPriceApi(@Body requestBody: RequestBody): Single<CommonResponse>

    @POST("api-provider-insert-update-price-list")
    fun insertUpdatePackageApi(@Body requestBody: RequestBody): Single<CommonResponse>

    @POST("api-doctor-insertupdate-price")
    fun insertUpdatePriceApiForDoc(@Body requestBody: RequestBody): Single<CommonResponse>


    // for nurse
    @POST("api-hourly-rates")
    fun savePriceForSlot1(@Body requestBody: RequestBody): Single<LoginResponse>

    @POST("api-task-list")
    fun getTaskRateApi(@Body requestBody: RequestBody): Single<TaskListResponse>

    @POST("api-test-list")
    fun getTestRateApi(): Single<TaskListResponse>

    @POST("api-daily-rates")
    fun saveHourlyPrice(@Body requestBody: RequestBody): Single<LoginResponse>

    @Multipart
    @POST("api-update-nurse-task-slot")
    fun saveTaskPrice(
        @Part("user_id") userId: RequestBody,
        @Part("task_id") taskId: RequestBody,
        @Part("price") price: RequestBody
    ): Single<LoginResponse>

    @Multipart
    @POST("api-update-physiotherapy-task-slot")
    fun savePhysiotherapyTaskPrice(
        @Part("user_id") userId: RequestBody,
        @Part("task_id") taskId: RequestBody,
        @Part("price") price: RequestBody
    ): Single<LoginResponse>


    @POST("api-update-test")
    fun saveTestPriceApi(@Body requestBody: RequestBody): Single<LoginResponse>


    @POST("api-get-video-access-token")
    fun getAccessTokenForVideo(@Body request: TwilioAccessTokenRequest): Single<TwilioAccessTokenResponse>

    @POST("api-get-video-access-token-with-push-notification")
    fun sendVideoPushNotification(@Body request: VideoPushRequest): Single<VideoPushResponse>

    @FormUrlEncoded
    @POST("api-status-update-call-disconnects")
    fun disconnectCall(@Field("status") appCall: String,@Field("order_id") order_id: String?): Single<JsonObject?>?

    @Multipart
    @POST("api-update-bank-details")
    fun apiHitUpdateBankDetails(
        @Part("user_id") user_id: RequestBody,
        @Part("id") id: RequestBody,
        @Part("bank_name") bankName: RequestBody,
        @Part("your_bank_name") yourName: RequestBody,
        @Part("account_no") accountNo: RequestBody,
        @Part("iban_no") iranNo: RequestBody,
        @Part("swift_no") swiftNo: RequestBody,
        @Part("message") message: RequestBody
    ): Single<BankDetailsResponse>

    @Multipart
    @POST("api-pathology-insert-report")
    fun apiHitForInsertPathologyReport(
        @Part("patient_id") patient_id: RequestBody,
        @Part("hospital_id") doctor_id: RequestBody,
        @Part("appointment_id") appointment_id: RequestBody,
        @Part("report_number") prescription_number: RequestBody,
        @Part prescription: MultipartBody.Part? = null
    ): Single<CommonResponse>

    @POST("api-send_notification")
    fun sendPushNotification(@Body request: PushNotificationRequest): Single<JsonObject>

    @POST("api-get-all-notification")
    fun apiUserNotification(@Body commonUserIdRequestBody: CommonUserIdRequest): Single<NotificationResponse>

    @POST("api-update-notification")
    fun apiUpdateNotification(@Body reqBody: CommonNotificationIdRequest): Single<ModelUpdateRead>

    @POST("api-logout")
    fun apiLogoutUser(@Body request: RequestBody): Single<CommonResponse>


    @POST("api-insert-need-help")
    fun apiToSubmitNeedSupport(@Body requestBody: NeedSupportRequest): Single<CommonResponse>

    @GET("api-need-help-topic")
    fun getHelpTopics(): Single<ModelIssueTypes>

    @POST("api-get-provider-profile")
    fun apiUserprofile(@Body req: RequestBody): Single<ModelUserProfile>

    @POST("api-hospital-doctor-profile")
    fun apiHospitalDoctorProfile(@Body req: RequestBody): Single<ModelUserProfile>

    @POST("api-hopital-docotor-disable")
    fun apiHospitalDoctorDisable(@Body req: RequestBody?): Single<CommonResponse>

    @POST("api-hopital-docotor-delete")
    fun apiHospitalDoctorDelete(@Body req: RequestBody?): Single<CommonResponse>


    @GET("api-hospital-department")
    fun hospDepartmentList(): Single<ModelHospDeparts>

    // Appointment new api's
    @POST("api-provider-ongoing-appointment-list")
    fun apiAppointmentOngoing(@Body reqBody: RequestBody): Single<ModelAppointmentsListing>

    @POST("api-provider-pending-appointment-list")
    fun apiAppointmentPending(@Body reqBody: RequestBody): Single<ModelAppointmentsListing>

    @POST("api-provider-upcoming-appointment-list")
    fun apiAppointmentUpcoming(@Body reqBody: RequestBody): Single<ModelAppointmentsListing>

    @POST("api-provider-past-appointment-list")
    fun apiAppointmentPast(@Body reqBody: RequestBody): Single<ModelAppointmentsListing>

    @POST("api-update-provider-appointment-status")
    fun apiMarkAcptRej(@Body reqBody: RequestBody): Single<ModelAppointmentsListing>

    @POST("api-complete-provider-appointment-status")
    fun apiMarkAsCompleted(@Body reqBody: RequestBody): Single<ModelAppointmentDetails>

    @POST("api-provider-appointment-details")
    fun getAppointmentDetails(@Body req: RequestBody): Single<ModelAppointmentDetails>

    @Multipart
    @POST("api-doctor-upload-prescription")
    fun apiUploadPrescription(
        @Part("id") appointment_id: RequestBody?,
        @Part upload_prescription: MultipartBody.Part?
    ): Single<CommonResponse>

    @Multipart
    @POST("api-upload-lab-report")
    fun apiUploadLabReports(
        @Part("appointment_id") appointment_id: RequestBody?,
        @Part("patient_id") patient_id: RequestBody?,
        @Part("hospital_id") hospital_id: RequestBody?,
        @Part reportList: ArrayList<MultipartBody.Part?>
    ): Single<CommonResponse>

    @Multipart
    @POST("api-sp-edit-profile")
    fun apiEditProfileForProviders(
        @Part("user_id") user_id: RequestBody?,
        @Part("service_type") user_type: RequestBody?,
        @Part("first_name") name: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part("phone_number") phone_number: RequestBody?,
        @Part("dob") dob: RequestBody?,
        @Part("gender") gender: RequestBody?,

        @Part("id_number") id_number: RequestBody?,
        @Part("speciality") speciality: RequestBody?,
        @Part("qualification") qualification: RequestBody?,
        @Part("experience") experience: RequestBody?,
        @Part("scfhs_number") scfhs_number: RequestBody?,
        @Part("description") description: RequestBody?,

        @Part image: MultipartBody.Part?,
        @Part id_image: MultipartBody.Part?,
        @Part certificate: MultipartBody.Part?,
        @Part scfhs_image: MultipartBody.Part?,
        @Part("hosp_moh_lic_no") hosp_moh_lic_no: RequestBody?,
        @Part("hosp_reg_no") hosp_reg_no: RequestBody?,
        @Part("address") address: RequestBody?,
        @Part("department") departments: RequestBody?,
        @Part("work_area") work_area: RequestBody?,
        @Part("last_name") last_name: RequestBody= "".asReqBody()
    ): Single<ModelUserProfile>

    @POST("api-notification-count")
    fun apiNotificationUnreadCounts(@Body req: RequestBody?): Single<NotificationCountResponse>

    @Multipart
    @POST("api-hospital-create-doctor")
    fun apiCreateHospitalDoctor(
        @Part("user_type") user_type: RequestBody,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("mobile_number") mobile_number: RequestBody,
        @Part("dob") dob: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("password") password: RequestBody,
        @Part("confirm_password") confirm_password: RequestBody,

        @Part("id_number") id_number: RequestBody,
        @Part("speciality") speciality: RequestBody,
        @Part("qualification") qualification: RequestBody,
        @Part("experience") experience: RequestBody,
        @Part("scfhs_number") scfhs_number: RequestBody,

        @Part id_image: MultipartBody.Part?,
        @Part certificate: MultipartBody.Part?,
        @Part scfhs_image: MultipartBody.Part?,
        @Part("hospital_id") hospital_id: RequestBody,
        @Part("work_area") work_area: RequestBody?
        ): Single<RegistrationResponse>

    @Multipart
    @POST("api-edit-hospital-doctor")
    fun apiEditProfileHospitalDoctor(
        @Part("work_area") work_area: RequestBody?,
        @Part("user_id") user_id: RequestBody?,
        @Part("service_type") user_type: RequestBody?,
        @Part("first_name") name: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part("phone_number") phone_number: RequestBody?,
        @Part("dob") dob: RequestBody?,
        @Part("gender") gender: RequestBody?,

        @Part("id_number") id_number: RequestBody?,
        @Part("speciality") speciality: RequestBody?,
        @Part("qualification") qualification: RequestBody?,
        @Part("experience") experience: RequestBody?,
        @Part("scfhs_number") scfhs_number: RequestBody?,
        @Part("description") description: RequestBody?,

        @Part image: MultipartBody.Part?,
        @Part id_image: MultipartBody.Part?,
        @Part certificate: MultipartBody.Part?,
        @Part scfhs_image: MultipartBody.Part?,

        @Part("last_name") last_name: RequestBody= "".asReqBody()
    ): Single<ModelUserProfile>

    @GET("api-android-provider-update")
    fun apiVersionCheck(): Single<NetworkAppCheck>






}

