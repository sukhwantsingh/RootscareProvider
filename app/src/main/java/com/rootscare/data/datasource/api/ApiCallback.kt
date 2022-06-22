package com.rootscare.data.datasource.api

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class ApiCallback<M> : Callback<M> {

    abstract fun onSuccess(model: M?)

    abstract fun onFailure(code: Int, msg: String)

    abstract fun onThrowable(t: Throwable)

    abstract fun onFinish()

    override fun onResponse(call: Call<M>, response: Response<M>) {

        if (response.isSuccessful) {
            onSuccess(response.body())
            println("response: " + response.body()!!.toString())
            //System.out.println("response: "+response.body());
        } else {
            //            onFailure(response.code(), response.errorBody().toString());
            onFailure(response.code(), response.message())
        }
        onFinish()
    }

    override fun onFailure(call: Call<M>, t: Throwable) {
        onThrowable(t)
        onFinish()
    }

    companion object {

        private val TAG = "ApiCallback"
    }
}
