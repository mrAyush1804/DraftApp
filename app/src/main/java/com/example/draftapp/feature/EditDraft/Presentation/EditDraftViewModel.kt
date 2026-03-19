package com.example.draftapp.feature.EditDraft.Presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.draftapp.feature.home.data.Draft
import com.example.draftapp.feature.home.data.DraftDao
import com.example.draftapp.feature.home.data.DraftRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditDraftViewModel @Inject constructor(
    private val repository: DraftRepositoryImpl,
    private val draftDao: DraftDao
) : ViewModel() {

    private val _draftState = MutableStateFlow<EditDraftState>(EditDraftState.Loading)
    val draftState: StateFlow<EditDraftState> = _draftState

    private var currentDraft: Draft? = null
    private var userId: String = "user_1"

    fun setUserId(id: String) {
        userId = id
    }

    fun loadDraft(draftId: Int) {
        if (draftId == -1) {
            _draftState.value = EditDraftState.New
            return
        }

        viewModelScope.launch {
            val draft = draftDao.getDraftById(draftId)
            if (draft != null) {
                currentDraft = draft
                _draftState.value = EditDraftState.Loaded(draft)
            } else {
                _draftState.value = EditDraftState.Error("Draft not found")
            }
        }
    }

    fun saveDraft(title: String, description: String, isLocked: Boolean) {
        viewModelScope.launch {
            val draft = currentDraft?.copy(
                title = if (currentDraft?.isLocked == true) currentDraft!!.title else title,
                description = description,
                isLocked = isLocked,
                updatedAt = System.currentTimeMillis()
            ) ?: Draft(
                userId = userId,
                title = title,
                description = description,
                isLocked = isLocked,
                updatedAt = System.currentTimeMillis()
            )
            repository.saveDraft(draft)
            _draftState.value = EditDraftState.Saved
        }
    }
}

sealed class EditDraftState {
    object Loading : EditDraftState()
    object New : EditDraftState()
    data class Loaded(val draft: Draft) : EditDraftState()
    object Saved : EditDraftState()
    data class Error(val message: String) : EditDraftState()
}
