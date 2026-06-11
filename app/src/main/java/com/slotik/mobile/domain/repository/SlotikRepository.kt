package com.slotik.mobile.domain.repository

import com.slotik.mobile.domain.model.Booking
import com.slotik.mobile.domain.model.SlotikRepositoryState
import kotlinx.coroutines.flow.StateFlow

interface SlotikRepository {
    val state: StateFlow<SlotikRepositoryState>

    suspend fun completeOnboarding()
    suspend fun signIn()
    suspend fun signOut()
    suspend fun createBooking(
        specialistId: String,
        slotId: String,
        comment: String,
    ): Booking?

    suspend fun cancelBooking(bookingId: String)
    suspend fun toggleFavorite(specialistId: String)
}
