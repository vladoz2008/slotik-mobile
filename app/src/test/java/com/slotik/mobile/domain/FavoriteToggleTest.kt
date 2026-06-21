package com.slotik.mobile.domain

import com.slotik.mobile.domain.util.FavoriteManager
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FavoriteToggleTest {

    @Test
    fun `adding a specialist to empty favorites works`() {
        val result = FavoriteManager.toggle(emptySet(), "specialist-1")
        assertTrue("specialist-1" in result)
    }

    @Test
    fun `toggling an existing favorite removes it`() {
        val favorites = setOf("specialist-1", "specialist-2")
        val result = FavoriteManager.toggle(favorites, "specialist-1")
        assertFalse("specialist-1" in result)
        assertTrue("specialist-2" in result)
    }

    @Test
    fun `toggling a new specialist adds it`() {
        val favorites = setOf("specialist-1")
        val result = FavoriteManager.toggle(favorites, "specialist-2")
        assertTrue("specialist-1" in result)
        assertTrue("specialist-2" in result)
    }

    @Test
    fun `double toggle returns to original state`() {
        val favorites = setOf("specialist-1")
        val afterAdd = FavoriteManager.toggle(favorites, "specialist-2")
        val afterRemove = FavoriteManager.toggle(afterAdd, "specialist-2")
        assertFalse("specialist-2" in afterRemove)
        assertTrue("specialist-1" in afterRemove)
    }

    @Test
    fun `isFavorite returns true for existing entry`() {
        val favorites = setOf("specialist-1")
        assertTrue(FavoriteManager.isFavorite(favorites, "specialist-1"))
    }

    @Test
    fun `isFavorite returns false for missing entry`() {
        val favorites = setOf("specialist-1")
        assertFalse(FavoriteManager.isFavorite(favorites, "specialist-2"))
    }

    @Test
    fun `isFavorite returns false for empty set`() {
        assertFalse(FavoriteManager.isFavorite(emptySet(), "specialist-1"))
    }
}
