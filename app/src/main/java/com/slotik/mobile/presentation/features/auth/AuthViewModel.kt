package com.slotik.mobile.presentation.features.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.slotik.mobile.data.repository.PersistentSlotikRepository
import com.slotik.mobile.domain.model.AuthMode
import com.slotik.mobile.domain.repository.SlotikRepository
import com.slotik.mobile.domain.util.Validator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class AuthUiState(
    val onboardingCompleted: Boolean = false,
    val isAuthorized: Boolean = false,
    val authMode: AuthMode = AuthMode.LOGIN,
    val email: String = "",
    val password: String = "",
) {
    val canSubmit: Boolean get() = email.isNotBlank() && password.isNotBlank()
    val isEmailValid: Boolean get() = email.isBlank() || Validator.isValidEmail(email)
    val isPasswordValid: Boolean get() = password.isBlank() || Validator.isValidPassword(password)
}

class AuthViewModel(
    private val repository: SlotikRepository,
) : ViewModel() {

    private val transient = MutableStateFlow(
        Triple(AuthMode.LOGIN, "", ""),
    )

    val state: StateFlow<AuthUiState> = combine(
        repository.state,
        transient,
    ) { repoState, (mode, email, password) ->
        AuthUiState(
            onboardingCompleted = repoState.onboardingCompleted,
            isAuthorized = repoState.isAuthorized,
            authMode = mode,
            email = email,
            password = password,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AuthUiState(
            onboardingCompleted = repository.state.value.onboardingCompleted,
            isAuthorized = repository.state.value.isAuthorized,
        ),
    )

    fun completeOnboarding() {
        viewModelScope.launch { repository.completeOnboarding() }
    }

    fun updateAuthMode(mode: AuthMode) {
        transient.value = transient.value.copy(first = mode)
    }

    fun updateEmail(value: String) {
        transient.value = transient.value.copy(second = value)
    }

    fun updatePassword(value: String) {
        transient.value = transient.value.copy(third = value)
    }

    fun submit() {
        if (!state.value.canSubmit) return
        viewModelScope.launch { repository.signIn() }
    }

    fun signOut() {
        viewModelScope.launch {
            repository.signOut()
            transient.value = Triple(AuthMode.LOGIN, "", "")
        }
    }

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                AuthViewModel(PersistentSlotikRepository.getInstance(context)) as T
        }
    }
}
