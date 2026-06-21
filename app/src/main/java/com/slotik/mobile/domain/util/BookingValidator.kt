package com.slotik.mobile.domain.util

object BookingValidator {
    fun canConfirm(
        specialistId: String?,
        slotId: String?,
        consent: Boolean,
    ): Boolean = specialistId != null && slotId != null && consent

    fun isSlotOccupied(
        bookingSpecialistId: String,
        bookingSlotId: String,
        existingBookingIds: Set<String>,
    ): Boolean = bookingSlotId in existingBookingIds
}
