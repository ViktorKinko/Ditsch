package com.bytepace.ditsch.utils

import com.bytepace.ditsch.network.YandexTranslateApiImpl
import java.util.*

/**
 * Created by Viktor on 15.03.2018.
 */
class RandomTranslator {
    private val translateAPI = YandexTranslateApiImpl()
    private val allLangs = arrayOf("am", "ar", "hy", "bn", "az", "ml", "sq", "mt", "mk", "en", "mi",
            "mr", "mhr", "af", "mn", "eu", "de", "ba", "ne", "be", "no", "bn", "pa", "my", "pap",
            "bg", "fa", "bs", "pl", "cy", "pt", "hu", "ro", "vi", "ht", "ceb", "gl", "sr", "nl",
            "si", "mrj", "sk", "el", "sl", "ka", "sw", "gu", "su", "da", "tg", "he", "th", "yi",
            "tl", "id", "ta", "ga", "tt", "it", "te", "is", "tr", "es", "udm", "kk", "uz", "kn",
            "uk", "ca", "ur", "ky", "fi", "zh", "fr", "ko", "hi", "xh", "hr", "km", "cs", "lo",
            "sv", "la", "gd", "lv", "et", "lt", "eo", "lb", "jv", "mg", "ja", "ms", "ru")

    fun makeRandomTranslateChain(sourceText: String, chainLength: Int, defaultLang: String): String {
        val startLang = if (defaultLang in allLangs) {
            defaultLang
        } else {
            "en"
        }
        val langChain = createRandomLangChain(chainLength)
        var result = translateString(sourceText, startLang, langChain[0])
        for (i in 0 until chainLength) {
            result = translateString(result, langChain[i], langChain[i + 1])
        }
        return translateString(result, langChain.last(), startLang)
    }

    private fun translateString(text: String, startLanguage: String, endLanguage:String): String{
        return translateAPI.translate(text, "$startLanguage-${endLanguage}")
    }

    private fun createRandomLangChain(chainLength: Int): ArrayList<String> {
        val random = Random()
        random.setSeed(System.currentTimeMillis())
        val list = ArrayList<String>()
        for (i in 0..chainLength) {
            list.add(pickRandomLang(random))
        }
        return list
    }

    private fun pickRandomLang(random: Random): String {
        return allLangs[Math.abs(random.nextInt()) % allLangs.size]
    }
}