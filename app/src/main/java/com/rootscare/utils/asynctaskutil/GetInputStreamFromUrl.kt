package com.rootscare.utils.asynctaskutil

import android.os.AsyncTask
import java.io.InputStream
import java.net.URL

class GetInputStreamFromUrl(internal var callBackTrigger: CallBackAfterFetchingInputStream) : AsyncTask<String, Void, InputStream>() {

    interface CallBackAfterFetchingInputStream{
        fun onCallback(result: InputStream?)
    }

    companion object{
        private val TAG = GetInputStreamFromUrl::class.java.simpleName
    }


    override fun doInBackground(vararg fileUrl: String?): InputStream? {

        try {
            return URL(fileUrl[0]).openStream()
        }catch (e:Exception){
            return null
        }

    }

    override fun onPostExecute(result: InputStream?) {
        super.onPostExecute(result)
        if (result!=null) {
            callBackTrigger.onCallback(result)
        }

    }
}