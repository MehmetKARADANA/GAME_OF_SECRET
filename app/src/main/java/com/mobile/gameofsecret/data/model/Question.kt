package com.mobile.gameofsecret.data.model


data class Question(
    val id: Int?=0,
    val question: String?=""
)
/*
data class Question(
    val id: Int?=0,
    var question: String? = null,
    val translations: List<Translation>? = null  // Dil çevirilerinin listesi
)*/

data class Questions(
    val id: Int? = 0,
    var question: String? = null,
    val translations: List<Translation> = emptyList()
)

data class Translation(
    val language: String="",
    val text: String=""
)
