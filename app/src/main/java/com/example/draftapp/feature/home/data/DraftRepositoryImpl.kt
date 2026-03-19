package com.example.draftapp.feature.home.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DraftRepositoryImpl @Inject constructor(
    private val draftDao: DraftDao
) {
    fun getDrafts(userId: String): Flow<List<Draft>> = draftDao.getDraftsForUser(userId)

    suspend fun saveDraft(draft: Draft) {
        if (draft.id == 0) {
            draftDao.insertDraft(draft)
        } else {
            draftDao.updateDraft(draft)
        }
    }

    suspend fun deleteDraft(draft: Draft) {
        draftDao.deleteDraft(draft)
    }

    suspend fun updateLockStatus(draftId: Int, isLocked: Boolean) {
        draftDao.updateLockStatus(draftId, isLocked)
    }
}
