package com.bytepace.ditsch.network

import com.bytepace.ditsch.model.Translation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * Created by Viktor on 15.03.2018.
 */
interface YandexTranslateApi {
    @Headers("Accept: */*")
    @GET("/api/v1.5/tr.json/translate")
    fun translate(@Query("key") key: String,
                  @Query("lang") lang: String,
                  @Query("text") textToTranslate: String): Call<Translation>
}