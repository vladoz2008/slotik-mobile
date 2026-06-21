package com.slotik.mobile.domain.util

import com.slotik.mobile.domain.model.Specialist
import java.util.Locale

object SpecialistFilter {
    fun filter(specialists: List<Specialist>, query: String): List<Specialist> {
        if (query.isBlank()) return specialists
        val normalized = query.trim().lowercase(Locale.getDefault())
        return specialists.filter { specialist ->
            specialist.name.lowercase(Locale.getDefault()).contains(normalized) ||
                specialist.specialty.lowercase(Locale.getDefault()).contains(normalized)
        }
    }
}
