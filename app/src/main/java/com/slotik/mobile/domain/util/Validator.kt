package com.slotik.mobile.domain.util

object Validator {
    fun isValidEmail(email: String): Boolean {
        if (email.isBlank()) return false
        val atIndex = email.indexOf('@')
        if (atIndex <= 0) return false
        val domain = email.substring(atIndex + 1)
        return domain.contains('.') && domain.length > 2
    }

    fun isValidPassword(password: String): Boolean = password.length >= 6
}
