package com.example.draftapp.feature.home.data

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String
) {
    override fun toString(): String = name
}
