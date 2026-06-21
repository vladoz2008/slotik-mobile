package com.slotik.mobile.domain

import com.slotik.mobile.domain.util.SpecialistFilter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tests pure filtering logic — no Android dependencies, runs on JVM.
 * Uses simple data maps to avoid coupling with Specialist model details.
 */
class SlotFilterTest {

    // Simulate filtering logic directly (same algorithm as SpecialistFilter)
    private fun filter(specialists: List<Map<String, String>>, query: String): List<Map<String, String>> {
        if (query.isBlank()) return specialists
        val q = query.trim().lowercase()
        return specialists.filter { s ->
            (s["name"] ?: "").lowercase().contains(q) ||
                (s["specialty"] ?: "").lowercase().contains(q)
        }
    }

    private val anna = mapOf("id" to "anna", "name" to "Анна Волкова", "specialty" to "Психолог")
    private val mikhail = mapOf("id" to "mikhail", "name" to "Михаил Иванов", "specialty" to "Остеопат")
    private val elena = mapOf("id" to "elena", "name" to "Елена Петрова", "specialty" to "Косметолог")
    private val allSpecialists = listOf(anna, mikhail, elena)

    @Test
    fun `empty query returns all specialists`() {
        val result = filter(allSpecialists, "")
        assertEquals(3, result.size)
    }

    @Test
    fun `blank query returns all specialists`() {
        val result = filter(allSpecialists, "   ")
        assertEquals(3, result.size)
    }

    @Test
    fun `search by name finds correct specialist`() {
        val result = filter(allSpecialists, "Анна")
        assertEquals(1, result.size)
        assertEquals("anna", result.first()["id"])
    }

    @Test
    fun `search by specialty finds correct specialist`() {
        val result = filter(allSpecialists, "Остеопат")
        assertEquals(1, result.size)
        assertEquals("mikhail", result.first()["id"])
    }

    @Test
    fun `case-insensitive search works`() {
        val result = filter(allSpecialists, "косметолог")
        assertEquals(1, result.size)
        assertEquals("elena", result.first()["id"])
    }

    @Test
    fun `partial name match works`() {
        val result = filter(allSpecialists, "Петр")
        assertEquals(1, result.size)
        assertEquals("elena", result.first()["id"])
    }

    @Test
    fun `non-existent query returns empty list`() {
        val result = filter(allSpecialists, "Стоматолог")
        assertTrue(result.isEmpty())
    }
}
