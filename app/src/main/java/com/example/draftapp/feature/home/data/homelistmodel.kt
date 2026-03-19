package com.example.draftapp.feature.home.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "draft_table")
data class Draft(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @SerialName("user_id")
    val userId: String = "",
    @SerialName("title")
    val title: String = "",
    @SerialName("description")
    val description: String? = null,
    @SerialName("author")
    val author: String? = null,
    @SerialName("is_locked")
    val isLocked: Boolean = false,
    @SerialName("updated_at")
    val updatedAt: Long = System.currentTimeMillis(),
    @Embedded @SerialName("source")
    val source: Source? = null
) : java.io.Serializable

@Serializable
data class Source(
    @SerialName("id")
    val sourceId: String? = null,
    @SerialName("name")
    val sourceName: String? = null
) : java.io.Serializable
