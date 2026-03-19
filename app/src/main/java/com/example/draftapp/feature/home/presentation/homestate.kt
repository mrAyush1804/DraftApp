package com.example.draftapp.feature.home.presentation


data class homeitemstate(
    val id :Int,
    val  title: String,
    val  description : String,
    val  date: String,

    val Locked : Boolean = true
)

sealed class Handlehomestate {
    object Loading : Handlehomestate()
    data class Error(val message: String) : Handlehomestate()
    data class Success(val data: List<homeitemstate>) : Handlehomestate()
}
