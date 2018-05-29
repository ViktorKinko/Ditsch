package com.bytepace.ditsch.model

sealed class TranslationResult {
    data class Error(val errorMessage: String) : TranslationResult()

    data class ParsedData(val data: Array<String>) : TranslationResult()
}