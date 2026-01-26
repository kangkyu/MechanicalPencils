package com.lininglink.mechanicalpencils.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lininglink.mechanicalpencils.data.model.UserProfile
import com.lininglink.mechanicalpencils.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UserProfileUiState {
    data object Loading : UserProfileUiState()
    data class Success(val profile: UserProfile) : UserProfileUiState()
    data class Error(val message: String) : UserProfileUiState()
}

class UserProfileViewModel(
    private val userId: Int,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserProfileUiState>(UserProfileUiState.Loading)
    val uiState: StateFlow<UserProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = UserProfileUiState.Loading
            userRepository.getUserProfile(userId)
                .onSuccess { profile ->
                    _uiState.value = UserProfileUiState.Success(profile)
                }
                .onFailure { error ->
                    _uiState.value = UserProfileUiState.Error(
                        error.message ?: "Failed to load profile"
                    )
                }
        }
    }
}
