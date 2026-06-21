package com.slotik.mobile.domain.usecase

import com.slotik.mobile.domain.model.SlotikRepositoryState
import com.slotik.mobile.domain.repository.SlotikRepository
import kotlinx.coroutines.flow.StateFlow

/** Observes the single source of truth for the whole app session. */
class ObserveAppStateUseCase(private val repository: SlotikRepository) {
    operator fun invoke(): StateFlow<SlotikRepositoryState> = repository.state
}

class CompleteOnboardingUseCase(private val repository: SlotikRepository) {
    suspend operator fun invoke() = repository.completeOnboarding()
}

class SignInUseCase(private val repository: SlotikRepository) {
    suspend operator fun invoke() = repository.signIn()
}

class SignOutUseCase(private val repository: SlotikRepository) {
    suspend operator fun invoke() = repository.signOut()
}
