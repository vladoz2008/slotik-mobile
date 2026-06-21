package com.slotik.mobile.presentation.features.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.slotik.mobile.data.repository.PersistentSlotikRepository
import com.slotik.mobile.domain.model.AuthMode
import com.slotik.mobile.domain.usecase.CompleteOnboardingUseCase
import com.slotik.mobile.domain.usecase.ObserveAppStateUseCase
import com.slotik.mobile.domain.usecase.SignInUseCase
import com.slotik.mobile.domain.usecase.SignOutUseCase
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
    private val observeAppState: ObserveAppStateUseCase,
    private val completeOnboardingUseCase: CompleteOnboardingUseCase,
    private val signInUseCase: SignInUseCase,
    private val signOutUseCase: SignOutUseCase,
) : ViewModel() {

    private val transient = MutableStateFlow(
        Triple(AuthMode.LOGIN, "", ""),
    )

    val state: StateFlow<AuthUiState> = combine(
        observeAppState(),
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
            onboardingCompleted = observeAppState().value.onboardingCompleted,
            isAuthorized = observeAppState().value.isAuthorized,
        ),
    )

    fun completeOnboarding() {
        viewModelScope.launch { completeOnboardingUseCase() }
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
        viewModelScope.launch { signInUseCase() }
    }

    fun signOut() {
        viewModelScope.launch {
            signOutUseCase()
            transient.value = Triple(AuthMode.LOGIN, "", "")
        }
    }

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repository = PersistentSlotikRepository.getInstance(context)
                return AuthViewModel(
                    ObserveAppStateUseCase(repository),
                    CompleteOnboardingUseCase(repository),
                    SignInUseCase(repository),
                    SignOutUseCase(repository),
                ) as T
            }
        }
    }
}
