package com.example.draftapp.core

import android.content.Context
import com.example.draftapp.feature.home.data.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getUsers(): List<User> {
        return try {
            val jsonString = context.assets.open("users.json").bufferedReader().use { it.readText() }
            Json.decodeFromString<List<User>>(jsonString)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
