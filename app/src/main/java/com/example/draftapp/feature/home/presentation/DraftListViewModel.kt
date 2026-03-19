package com.example.draftapp.feature.home.presentation

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.draftapp.core.UserProvider
import com.example.draftapp.feature.home.data.Draft
import com.example.draftapp.feature.home.data.DraftRepositoryImpl
import com.example.draftapp.feature.home.data.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DraftListViewModel @Inject constructor(
    private val repository: DraftRepositoryImpl,
    private val userProvider: UserProvider
) : ViewModel() {

    val users: List<User> = userProvider.getUsers()

    private val _currentUser = MutableStateFlow(users.firstOrNull() ?: User("default", "Default"))
    val currentUser: StateFlow<User> = _currentUser

    @OptIn(ExperimentalCoroutinesApi::class)
    val draftListState: StateFlow<Handlehomestate> = _currentUser
        .flatMapLatest { user ->
            repository.getDrafts(user.id)
                .map { drafts ->
                    Handlehomestate.Success(drafts.map { it.toUiState() })
                }
             /*   .onStart { emit(Handlehomestate.Success) }
                .catch { emit(Handlehomestate.Error(it.message ?: "Unknown error")) }*/
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Handlehomestate.Loading)



    fun switchUser(user: User) {
        _currentUser.value = user
    }

    fun toggleLock(draftId: Int, currentLockStatus: Boolean) {
        viewModelScope.launch {
            repository.updateLockStatus(draftId, !currentLockStatus)
        }
    }

    private fun Draft.toUiState() = homeitemstate(
        id = id,
        title = title,
        description = description ?: "",
        date = java.text.DateFormat.getDateTimeInstance().format(java.util.Date(updatedAt)),
        Locked = isLocked
    )
}
