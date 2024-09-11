package com.rspl.rojgaarrakshak.NetworkLayer

import androidx.lifecycle.MutableLiveData
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
import kotlinx.coroutines.CoroutineExceptionHandler
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

class Repository @Inject constructor(var apiServices: ApiServices) {

    //    @Inject var apiServices : ApiServices = provideRetrofitInstance()
    fun <T> getExceptionHandler(responseList: MutableLiveData<NetworkResult<T>>? = null): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { cctx, throwable ->
            throwable.printStackTrace()
            if (throwable is UnknownHostException) {
                responseList!!.postValue(NetworkResult.Error("No Internet connection found"))
            } else if (throwable is ConnectException) {
                responseList!!.postValue(NetworkResult.Error("Connection failed, Please check internet connection and retry."))
            } else {

                when {
                    throwable.localizedMessage?.toString()!!.contains("end of input") -> {
                        responseList!!.postValue(NetworkResult.Error("Server not reachable!!!"))
                    }
                    else -> {
                        responseList!!.postValue(NetworkResult.Error(throwable.localizedMessage))
                    }
                }

            }
        }
    }

    suspend fun getOtpOnMobileNumber(reqModel: Any): Response<SigninResponse> {
        return apiServices.signInUser(reqModel)
    }

    suspend fun verifyOTP(reqModel: Any): Response<Signup> {
        return apiServices.verifyOTP(reqModel)
    }

    suspend fun getAppliedjob(token: String): Response<GetAppliedJobResponse> {
        return apiServices.getAppliedjob(token)
    }

    suspend fun verifyOTPSignin(reqModel: Any): Response<SignInOtp> {
        return apiServices.verifyOTPSignin(reqModel)
    }

    suspend fun signUpRequest(reqModel: Any): Response<signupresponce> {
        return apiServices.signUpUser(reqModel)
    }

    suspend fun SignupResendOtp(reqModel: Any) :Response<ResendSignUpResponse>
    {
        return apiServices.signUpResendOtp(reqModel)
    }

    suspend fun SigninResendOtp(reqModel: Any) :Response<ResendsigninResponse>
    {
        return apiServices.signinResendOtp(reqModel)
    }

    suspend fun InsilialPayment(token: String): Response<PaymentInisialResponse> {
        return apiServices.InsilialPayment(token)
    }

    suspend fun DeleteAccount(token: String): Response<RequestforAccountDeactivationResponse> {
        return apiServices.DeleteAccount(token)
    }

    suspend fun RequestforRefund(token: String): Response<RequestforRefundResponse> {
        return apiServices.Requestforrefund(token)
    }

    suspend fun PaymentSatus(token: String,reqbody:Any): Response<PaymentStatusResponse> {
        return apiServices.PaymentStatus(token,reqbody)
    }

    suspend fun UpdateUser(token: String,reqbody:Any): Response<UpdateUserData> {
        return apiServices.UpdateUser(token,reqbody)
    }

    suspend fun uploadDocRequest(userID: Int,multiBody: MultipartBody.Part): Response<UploadDocResponse> {
        return apiServices.uploadDocs(userID,multiBody)
    }

    suspend fun uploadProfilepicture(userID: Int,multiBody: MultipartBody.Part): Response<UploadPhotoResponse> {
        return apiServices.profileupload(userID,multiBody)
    }


    suspend fun Getuserdata(token:String): Response<UserDataResponse> {
        return apiServices.GetUserdata(token)
    }

    suspend fun Getuserskill(): Response<UserSkillResponse> {
        return apiServices.GetUserSkill()
    }

    suspend fun GetUserSubskill(id:String): Response<SubSkillResponse> {
        return apiServices.GetSubSkill(id)
    }

    suspend fun workhistory(token:String): Response<WorkHistoryResponse> {
        return apiServices.WorkHistory(token)
    }

    suspend fun getallJobs(token:String): Response<GetjobResponce> {
        return apiServices.Getalljob(token)
    }

    suspend fun getSkills(): Response<SkillResponse> {
        return apiServices.getSkills()
    }

    suspend fun searchJob(reqModel: Any): Response<SearchResponse> {
        return apiServices.search(reqModel)
    }

    suspend fun addJobPerformances(token:String,reqModel: Any): Response<AddJobPrefrences> {
        return apiServices.addJobPerefences(token,reqModel)
    }

    suspend fun applyjob(token:String ,reqModel: Any) :Response<AppyJob>{
        return apiServices.Applyjob(token,reqModel)
    }

    suspend fun Createconcern(token:String ,reqModel: Any) :Response<ConcernResponce>{
        return apiServices.creteconcern(token,reqModel)
    }

    suspend fun GetSalarySlip(token:String ,reqModel: Any) :Response<GetSalarySlipResponse>{
        return apiServices.GetSalarySlip(token,reqModel)
    }

    suspend fun Getallyears(token:String ,reqModel: Any) :Response<GETallyearsResponse>{
        return apiServices.GetAllyear(token,reqModel)
    }

    suspend fun Concernhistory(token:String) :Response<ConcernHistoryResponce>{
        return apiServices.Concernhistory(token)
    }

    suspend fun getSearchSuggestion(reqModel: Any): Response<SearchSuggestionResponse> {
        return apiServices.searchSuggestion(reqModel)
    }
    suspend fun getState(): Response<StateResponse> {
        return apiServices.getstate()
    }

    suspend fun getcity(cityRequestModel: Any) :Response<CityResponce>
    {
        return apiServices.getcity(cityRequestModel)
    }

}