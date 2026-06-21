package com.slotik.mobile.domain

import com.slotik.mobile.domain.util.BookingValidator
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BookingLogicTest {

    // --- canConfirm ---

    @Test
    fun `canConfirm returns true when all fields are present and consent given`() {
        assertTrue(
            BookingValidator.canConfirm(
                specialistId = "anna-volkova",
                slotId = "slot-001",
                consent = true,
            ),
        )
    }

    @Test
    fun `canConfirm returns false when specialistId is null`() {
        assertFalse(
            BookingValidator.canConfirm(
                specialistId = null,
                slotId = "slot-001",
                consent = true,
            ),
        )
    }

    @Test
    fun `canConfirm returns false when slotId is null`() {
        assertFalse(
            BookingValidator.canConfirm(
                specialistId = "anna-volkova",
                slotId = null,
                consent = true,
            ),
        )
    }

    @Test
    fun `canConfirm returns false when consent is false`() {
        assertFalse(
            BookingValidator.canConfirm(
                specialistId = "anna-volkova",
                slotId = "slot-001",
                consent = false,
            ),
        )
    }

    @Test
    fun `canConfirm returns false when all fields are null or false`() {
        assertFalse(
            BookingValidator.canConfirm(
                specialistId = null,
                slotId = null,
                consent = false,
            ),
        )
    }

    // --- isSlotOccupied ---

    @Test
    fun `isSlotOccupied returns true when slot is in existing bookings`() {
        val existingIds = setOf("slot-001", "slot-002")
        assertTrue(
            BookingValidator.isSlotOccupied("anna-volkova", "slot-001", existingIds),
        )
    }

    @Test
    fun `isSlotOccupied returns false when slot is not in existing bookings`() {
        val existingIds = setOf("slot-001", "slot-002")
        assertFalse(
            BookingValidator.isSlotOccupied("anna-volkova", "slot-003", existingIds),
        )
    }

    @Test
    fun `isSlotOccupied returns false for empty existing bookings`() {
        assertFalse(
            BookingValidator.isSlotOccupied("anna-volkova", "slot-001", emptySet()),
        )
    }
}
