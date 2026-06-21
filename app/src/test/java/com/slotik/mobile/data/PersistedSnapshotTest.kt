package com.slotik.mobile.data

import com.slotik.mobile.data.local.PersistedSlotikSnapshot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PersistedSnapshotTest {

    @Test
    fun `default snapshot has onboardingCompleted false`() {
        val snapshot = PersistedSlotikSnapshot()
        assertFalse(snapshot.onboardingCompleted)
    }

    @Test
    fun `default snapshot has isAuthorized false`() {
        val snapshot = PersistedSlotikSnapshot()
        assertFalse(snapshot.isAuthorized)
    }

    @Test
    fun `default snapshot has empty bookings list`() {
        val snapshot = PersistedSlotikSnapshot()
        assertTrue(snapshot.bookings.isEmpty())
    }

    @Test
    fun `default snapshot has empty favorites set`() {
        val snapshot = PersistedSlotikSnapshot()
        assertTrue(snapshot.favorites.isEmpty())
    }

    @Test
    fun `copy only changes specified field - onboardingCompleted`() {
        val original = PersistedSlotikSnapshot()
        val updated = original.copy(onboardingCompleted = true)
        assertTrue(updated.onboardingCompleted)
        assertFalse(updated.isAuthorized)
        assertTrue(updated.bookings.isEmpty())
    }

    @Test
    fun `copy only changes specified field - isAuthorized`() {
        val original = PersistedSlotikSnapshot()
        val updated = original.copy(isAuthorized = true)
        assertTrue(updated.isAuthorized)
        assertFalse(updated.onboardingCompleted)
    }

    @Test
    fun `favorites set operations work correctly`() {
        val snapshot = PersistedSlotikSnapshot(favorites = setOf("specialist-1"))
        val updated = snapshot.copy(favorites = snapshot.favorites + "specialist-2")
        assertEquals(2, updated.favorites.size)
        assertTrue("specialist-1" in updated.favorites)
        assertTrue("specialist-2" in updated.favorites)
    }

    @Test
    fun `removing from favorites set works`() {
        val snapshot = PersistedSlotikSnapshot(favorites = setOf("a", "b", "c"))
        val updated = snapshot.copy(favorites = snapshot.favorites - "b")
        assertEquals(2, updated.favorites.size)
        assertFalse("b" in updated.favorites)
    }
}
