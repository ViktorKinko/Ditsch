package com.bytepace.ditsch.network

import com.bytepace.ditsch.model.Translation
import com.bytepace.ditsch.model.TranslationResult
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

/**
 * Created by Viktor on 15.03.2018.
 */
class YandexTranslateApiImpl {
    private val BASE_URL = "https://translate.yandex.net/"
    private val API_KEY = "trnsl.1.1.20180315T060745Z.2d0bfbf157f1e8a5.2ea6c4859ac14bc7edfde91a104a39002b0ba3a0"
    private val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    private var api: YandexTranslateApi = retrofit.create(YandexTranslateApi::class.java)

    fun translate(text: String, lang: String): TranslationResult {
        val call: Call<Translation> = api.translate(API_KEY, lang, text)
        val t: Translation = execute(call)
        t.text ?: return TranslationResult.Error("No response")
        return if (t.text.size == 0) {
            TranslationResult.Error("No translation")
        } else {
            TranslationResult.ParsedData(t.text.toTypedArray())
        }
    }

    private fun <T> execute(call: Call<T>): Translation {
        try {
            val response = call.execute()
            response ?: return Translation()
            response.body() ?: return Translation()
            return response.body() as Translation
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Translation()
    }
}