package com.rootscare.data.datasource.api

import android.content.Context
import android.util.Log
import okhttp3.*
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


object ApiClient {

    var mRetrofit: Retrofit? = null
    private var credential: String? = null
    val CONVERTER_TYPE_JACKSON = "CONVERTER_TYPE_JACKSON"
    val CONVERTER_TYPE_GSON = "CONVERTER_TYPE_GSON"


    fun retrofit(context: Context?, apiServerUrl: String, converter_type: String): Retrofit? {

        if (mRetrofit == null && context != null) {
            val httpClient = OkHttpClient.Builder()

            //Timeout
            httpClient.readTimeout(ApiConfig.READ_TIMEOUT, TimeUnit.SECONDS)
            httpClient.connectTimeout(ApiConfig.CONNECT_TIMEOUT, TimeUnit.SECONDS)
            httpClient.writeTimeout(ApiConfig.WRITE_TIMEOUT, TimeUnit.SECONDS)

            //            //Cache
            //            int cacheSize = 10 * 1024 * 1024; // 10 MiB
            //            Cache cache = new Cache(context.getCacheDir(), cacheSize);
            //            httpClient.cache(cache);

            //Header Interceptor
            httpClient.addInterceptor(LoggingInterceptor())

            // Authentication Interceptor
            credential = Credentials.basic("admin", "12345")
            val interceptor_credential = AuthenticationInterceptor(credential!!)
            if (!httpClient.interceptors().contains(interceptor_credential)) {
                httpClient.addInterceptor(interceptor_credential)
            }

            val okHttpClient = httpClient.build()
            if (converter_type == CONVERTER_TYPE_JACKSON) {
                mRetrofit = Retrofit.Builder()
                    .baseUrl(apiServerUrl)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // For RXAndroid
                    .addConverterFactory(JacksonConverterFactory.create())
                    .client(okHttpClient)
                    .build()
            } else {
                mRetrofit = Retrofit.Builder()
                    .baseUrl(apiServerUrl)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // For RXAndroid
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()
            }
        }
        return mRetrofit
    }

    private class LoggingInterceptor : Interceptor {

        //        .header("Authorization", "asdfghjklLKJHGFDSA")
        //        .header("Content-Type", "application/json; charset=utf-8")

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()
            val request = original.newBuilder()
                .header(ApiConfig.API_KEY, ApiConfig.API_KEY_VALUE)
                .addHeader("Content-Type", "application/json")
                .method(original.method, original.body)
                .build()
            val t1 = System.nanoTime()
            var requestLog = String.format(
                "Sending request %s on %s%n%s",
                request.url, chain.connection(), request.headers
            )
            if (request.method.compareTo("post", ignoreCase = true) == 0) {
                requestLog = "\n" + requestLog + "\n" + bodyToString(request)
            }
            Log.e("CITIZEN_HTTP_REQ_HEADER", "request\n$requestLog")

            val response = chain.proceed(request)
            val t2 = System.nanoTime()

            val responseLog = String.format(
                "Received response for %s in %.1fms%n%s",
                response.request.url, (t2 - t1) / 1e6, response.headers
            )

            val bodyString = response.body!!.string()

            Log.e("CITIZEN_HTTP_RESPONSE", "response\n$responseLog\n$bodyString")

            return response.newBuilder()
                .body(ResponseBody.create(response.body!!.contentType(), bodyString))
                .build()
        }
    }

    class AuthenticationInterceptor(private val authToken: String) : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()

            val builder = original.newBuilder()
                .header("Authorization", authToken)

            val request = builder.build()
            return chain.proceed(request)
        }
    }

    private fun bodyToString(request: Request): String {

        return try {
            val copy = request.newBuilder().build()
            val buffer = Buffer()
            copy.body!!.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: IOException) {
            "did not work"
        }

    }
}
