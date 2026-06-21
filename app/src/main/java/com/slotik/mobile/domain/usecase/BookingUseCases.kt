package com.slotik.mobile.domain.usecase

import com.slotik.mobile.domain.model.Booking
import com.slotik.mobile.domain.repository.SlotikRepository

class CreateBookingUseCase(private val repository: SlotikRepository) {
    suspend operator fun invoke(specialistId: String, slotId: String, comment: String): Booking? =
        repository.createBooking(specialistId, slotId, comment)
}

class CancelBookingUseCase(private val repository: SlotikRepository) {
    suspend operator fun invoke(bookingId: String) = repository.cancelBooking(bookingId)
}
