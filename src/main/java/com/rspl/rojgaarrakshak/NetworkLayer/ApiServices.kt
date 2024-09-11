package com.rspl.rojgaarrakshak.NetworkLayer


import com.rspl.rojgaarrakshak.models.AddJobPrefrencesModel.AddJobPrefrences
import com.rspl.rojgaarrakshak.models.ApplyJobResponse.AppyJob
import com.rspl.rojgaarrakshak.models.CityResponce.CityResponce
import com.rspl.rojgaarrakshak.models.ConcernHistoryResponse.ConcernHistoryResponce
import com.rspl.rojgaarrakshak.models.CreateConcernResponse.ConcernResponce
import com.rspl.rojgaarrakshak.models.GetSalarySlipResponse.GetSalarySlipResponse
import com.rspl.rojgaarrakshak.models.GetSubSkillModel.SubSkillResponse
import com.rspl.rojgaarrakshak.models.GetUserDetails.UserDataResponse
import com.rspl.rojgaarrakshak.models.Get_All_Job_Response.GetjobResponce
import com.rspl.rojgaarrakshak.models.GetallyearsResponse.GETallyearsResponse
import com.rspl.rojgaarrakshak.models.GetappliedJobResponse.GetAppliedJobResponse
import com.rspl.rojgaarrakshak.models.PaymentInisialRespose.PaymentInisialResponse
import com.rspl.rojgaarrakshak.models.PaymentSatusResponse.PaymentStatusResponse
import com.rspl.rojgaarrakshak.models.RequestForRefund.RequestforRefundResponse
import com.rspl.rojgaarrakshak.models.RequestforAccountDeactivation.RequestforAccountDeactivationResponse
import com.rspl.rojgaarrakshak.models.ResendSigninResponse.ResendsigninResponse
import com.rspl.rojgaarrakshak.models.ResendSignupResponse.ResendSignUpResponse
import com.rspl.rojgaarrakshak.models.SignUpResponce.signupresponce
import com.rspl.rojgaarrakshak.models.SigninResponse.SigninResponse
import com.rspl.rojgaarrakshak.models.SkillModel.UserSkillResponse
import com.rspl.rojgaarrakshak.models.UpdateUserdata.UpdateUserData
import com.rspl.rojgaarrakshak.models.UploadDocResponse.UploadDocResponse
import com.rspl.rojgaarrakshak.models.UploadPhotoResponse.UploadPhotoResponse
import com.rspl.rojgaarrakshak.models.VerifyOTPSignIn.SignInOtp
import com.rspl.rojgaarrakshak.models.VerifyOtpSignup.Signup
import com.rspl.rojgaarrakshak.models.WorkHistoryResponse.WorkHistoryResponse
import com.rspl.rojgaarrakshak.models.search_response.SearchResponse
import com.rspl.rojgaarrakshak.models.search_suggestion.SearchSuggestionResponse
import com.rspl.rojgaarrakshak.models.skill_response.SkillResponse
import com.rspl.rojgaarrakshak.models.state_response.StateResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path


interface ApiServices {
    companion object {
        const val BASE_URL= "https://mobileapp.rakshaksecuritas.com/"
        const val privacy_policy = "https://rakshaksecuritas.com/privacy-policy/"
        const val refund_policy = "https://rakshaksecuritas.com/refund-policy/"
        const val about_us_url = "https://rakshaksecuritas.com/about-company/"

//        const val BASE_URL = "https://api-rakshak.synchsoft.in/"
        const val SEARCH_SUGGESTION = "api/V1/search-suggestion"
        const val SEARCH = "api/V1/search-filter"
        const val SIGN_UP = "api/V1/sign-up"
        const val UPLOAD_DOCS = "api/V1/upload-docs"
        const val SIGN_IN = "api/V1/sign-in"
        const val VERIFY_OTP = "api/V1/verify-otp-sign-up"
        const val VERIFY_OTP_SIGN_IN = "api/V1/verify-otp-sigin-in"
        const val GET_SKILLS = "api/V1/get-skills"
        const val GET_USER_DATA = "api/V1/get-user-data"
        const val JOB_APPLY = "api/V1/apply-job"
        const val SELECT_STATE = "api/V1/search/all-state"
        const val GET_APPLYED_JOB = "api/V1/get-applied-jobs"
        const val SELECT_CITY = "api/V1/search/all-city-by-state"
        const val CREATE_CONCERN = "api/V1/create-concern"
        const val CONCERN_HISTORY = "api/V1/concern-history"
        const val WORK_HISTORY = "api/V1/work-history"
        const val SALARY_GET_ALL_JOB = "api/V1/salary/get-all-job"
        const val SALARY_GET_ALL_YEAR = "api/V1/salary/get-all-years"
        const val SALARY_GET_SALARY_SLIP = "api/V1/salary/get-salary-slip"
        const val ACCOUNT_DEACTIVATION = "api/V1/account-deactivation"
        const val DELETE = "api/V1/delete-user/{user_id}"
        const val UPDATE_USER_DATA = "api/V1/update-user-data"
        const val Payment_Status = "api/V1/payment/payment-status"
        const val Inititle_Payment = "api/V1/payment/initiate-payment"
        const val PROFILE_UPLOAD = "api/V1/upload-profile-picture"
        const val REQUEST_FOR_REFUND = "api/V1/payment/payment-refund"
        const val RESEND_OTP_SIGN_IN = "api/V1/resend-otp-signin"
        const val RESEND_OTP_SIGN_UP = "api/V1/resend-otp-signup"
        const val GET_USER_SKILL = "api/V1/get-preference-skills"
        const val GET_SUB_SKILL = "api/V1/get-preference-sub_skills/{id}"
        const val JOB_PERMARMANCE = "api/V1/add-job-preference"

    }

    // GET API CALL
    @GET(WORK_HISTORY)
    suspend fun WorkHistory(
        @Header("Authorization") UserToken: String,
    ): Response<WorkHistoryResponse>


    @GET(GET_SKILLS)
    suspend fun getSkills(): Response<SkillResponse>

    @GET(GET_APPLYED_JOB)
    suspend fun getAppliedjob(
        @Header("Authorization") UserToken: String,
    ): Response<GetAppliedJobResponse>

    @GET(CONCERN_HISTORY)
    suspend fun Concernhistory(
        @Header("Authorization") headerToken: String

    ): Response<ConcernHistoryResponce>


    @GET(Inititle_Payment)
    suspend fun InsilialPayment(
        @Header("Authorization") UserToken: String,
    ): Response<PaymentInisialResponse>

    @GET(GET_USER_SKILL)
    suspend fun GetUserSkill(

    ): Response<UserSkillResponse>

    @GET(GET_SUB_SKILL)
    suspend fun GetSubSkill(

        @Path("id") id: String
    ): Response<SubSkillResponse>


    @GET(SALARY_GET_ALL_JOB)
    suspend fun Getalljob(
        @Header("Authorization") UserToken: String,
    ): Response<GetjobResponce>


    // GET API CALL END


    // POST API STARTED
    @POST(RESEND_OTP_SIGN_UP)
    suspend fun signUpResendOtp(
        @Body body: Any
    ): Response<ResendSignUpResponse>

    @POST(RESEND_OTP_SIGN_IN)
    suspend fun signinResendOtp(
        @Body body: Any
    ): Response<ResendsigninResponse>

    @POST(ACCOUNT_DEACTIVATION)
    suspend fun DeleteAccount(
        @Header("Authorization") UserToken: String,
    ): Response<RequestforAccountDeactivationResponse>

    @POST(REQUEST_FOR_REFUND)
    suspend fun Requestforrefund(
        @Header("Authorization") UserToken: String,
    ): Response<RequestforRefundResponse>

    @POST(Payment_Status)
    suspend fun PaymentStatus(
        @Header("Authorization") headerToken: String,
        @Body body: Any
    ): Response<PaymentStatusResponse>


    @POST(GET_USER_DATA)
    suspend fun GetUserdata(
        @Header("Authorization") UserToken: String,
    ): Response<UserDataResponse>

    @Multipart
    @POST(UPLOAD_DOCS)
    suspend fun uploadDocs(
        @Part("user_id") userId: Int,
        @Part resume: MultipartBody.Part
    ): Response<UploadDocResponse>

    @Multipart
    @POST(PROFILE_UPLOAD)
    suspend fun profileupload(
        @Part("user_id") userId: Int,
        @Part resume: MultipartBody.Part
    ): Response<UploadPhotoResponse>

    @POST(SIGN_IN)
    suspend fun signInUser(
        @Body body: Any
    ): Response<SigninResponse>

    @POST(SALARY_GET_SALARY_SLIP)
    suspend fun GetSalarySlip(
        @Header("Authorization") headerToken: String,
        @Body body: Any
    ): Response<GetSalarySlipResponse>


    @POST(CREATE_CONCERN)
    suspend fun creteconcern(
        @Header("Authorization") headerToken: String,
        @Body body: Any
    ): Response<ConcernResponce>

    @POST(SALARY_GET_ALL_YEAR)
    suspend fun GetAllyear(
        @Header("Authorization") headerToken: String,
        @Body body: Any
    ): Response<GETallyearsResponse>

    @POST(VERIFY_OTP)
    suspend fun verifyOTP(
        @Body body: Any
    ): Response<Signup>

    @POST(VERIFY_OTP_SIGN_IN)
    suspend fun verifyOTPSignin(
        @Body body: Any
    ): Response<SignInOtp>

    @POST(JOB_APPLY)
    suspend fun Applyjob(
        @Header("Authorization") headerToken: String,
        @Body body: Any

    ): Response<AppyJob>


    @POST(SEARCH_SUGGESTION)
    suspend fun searchSuggestion(
        @Body body: Any
    ): Response<SearchSuggestionResponse>

    @POST(SEARCH)
    suspend fun search(
        @Body body: Any
    ): Response<SearchResponse>


    @POST(JOB_PERMARMANCE)
    suspend fun addJobPerefences(
        @Header("Authorization") headerToken: String,
        @Body body: Any
    ): Response<AddJobPrefrences>

    @POST(SELECT_STATE)
    suspend fun getstate(): Response<StateResponse>

    @POST(SELECT_CITY)
    suspend fun getcity(@Body body: Any)
            : Response<CityResponce>

    @POST(SIGN_UP)
    suspend fun signUpUser(
        @Body body: Any
    ): Response<signupresponce>


    // POST API ENDS


    // PUT API STARTED


    @PUT(UPDATE_USER_DATA)
    suspend fun UpdateUser(
        @Header("Authorization") headerToken: String,
        @Body body: Any
    ): Response<UpdateUserData>

    // PUT API ENDS

}