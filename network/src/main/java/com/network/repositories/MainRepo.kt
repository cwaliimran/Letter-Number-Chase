package com.network.repositories

import com.network.network.ApiClient
import com.network.network.ApiInterface
import com.network.network.BaseApiResponse


class MainRepo : BaseApiResponse() {
    private val apiService: ApiInterface by lazy {
        ApiClient.getInstance()!!.create(ApiInterface::class.java)
    }



//    val currentUserMutableLiveData: SingleLiveEvent<AuthResponse> by lazy {
//        SingleLiveEvent()
//    }
//
//    suspend fun currentUser() {
//        currentUserMutableLiveData.value = null
//        currentUserMutableLiveData.postValue(AppClass.getCurrentUser())
//    }

    /*  val loginMutableLiveData: SingleLiveEvent<NetworkResult<AuthResponse>> by lazy {
          SingleLiveEvent()
      }

      suspend fun login(email: String, password: String) {
          loginMutableLiveData.value = null
          loginMutableLiveData.postValue(NetworkResult.Loading())
          loginMutableLiveData.postValue(safeApiCall { apiService.login(email, password) })
      }

      val forgotPasswordMutableLiveData: SingleLiveEvent<NetworkResult<ModelForgotPassword>> by lazy {
          SingleLiveEvent()
      }

      suspend fun forgotPassword(email: String) {
          forgotPasswordMutableLiveData.value = null
          forgotPasswordMutableLiveData.postValue(NetworkResult.Loading())
          forgotPasswordMutableLiveData.postValue(safeApiCall { apiService.forgotPassword(email) })
      }

      val resetPasswordMutableLiveData: SingleLiveEvent<NetworkResult<ModelResetPassword>> by lazy {
          SingleLiveEvent()
      }

      suspend fun resetPassword(email: String, password: String, token: String) {
          resetPasswordMutableLiveData.value = null
          resetPasswordMutableLiveData.postValue(NetworkResult.Loading())
          resetPasswordMutableLiveData.postValue(safeApiCall {
              apiService.resetPassword(
                  email,
                  password,
                  password,
                  token
              )
          })
      }
  */


}


