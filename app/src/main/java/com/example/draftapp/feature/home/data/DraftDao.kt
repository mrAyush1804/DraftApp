package com.example.draftapp.feature.home.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DraftDao {

    @Query("SELECT * FROM draft_table WHERE userId = :userId ORDER BY updatedAt DESC")
    fun getDraftsForUser(userId: String): Flow<List<Draft>>

    @Query("SELECT * FROM draft_table WHERE id = :draftId LIMIT 1")
    suspend fun getDraftById(draftId: Int): Draft?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDraft(draft: Draft)

    @Update
    suspend fun updateDraft(draft: Draft)

    @Delete
    suspend fun deleteDraft(draft: Draft)

    @Query("UPDATE draft_table SET isLocked = :isLocked WHERE id = :draftId")
    suspend fun updateLockStatus(draftId: Int, isLocked: Boolean)
}
