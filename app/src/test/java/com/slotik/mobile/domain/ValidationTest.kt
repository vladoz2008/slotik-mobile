package com.slotik.mobile.domain

import com.slotik.mobile.domain.util.Validator
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidationTest {

    // --- Email ---

    @Test
    fun `valid email with at and dot passes`() {
        assertTrue(Validator.isValidEmail("user@example.com"))
    }

    @Test
    fun `email without at sign fails`() {
        assertFalse(Validator.isValidEmail("userexample.com"))
    }

    @Test
    fun `email without domain dot fails`() {
        assertFalse(Validator.isValidEmail("user@example"))
    }

    @Test
    fun `blank email fails`() {
        assertFalse(Validator.isValidEmail(""))
        assertFalse(Validator.isValidEmail("   "))
    }

    @Test
    fun `email with subdomain passes`() {
        assertTrue(Validator.isValidEmail("user@mail.example.com"))
    }

    // --- Password ---

    @Test
    fun `password with 6 characters passes`() {
        assertTrue(Validator.isValidPassword("abc123"))
    }

    @Test
    fun `password with more than 6 characters passes`() {
        assertTrue(Validator.isValidPassword("securePassword1"))
    }

    @Test
    fun `password shorter than 6 characters fails`() {
        assertFalse(Validator.isValidPassword("abc"))
        assertFalse(Validator.isValidPassword("12345"))
    }

    @Test
    fun `blank password fails`() {
        assertFalse(Validator.isValidPassword(""))
    }
}
