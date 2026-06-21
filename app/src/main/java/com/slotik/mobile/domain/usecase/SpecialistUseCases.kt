package com.slotik.mobile.domain.usecase

import com.slotik.mobile.domain.model.Specialist
import com.slotik.mobile.domain.repository.SlotikRepository
import com.slotik.mobile.domain.util.SpecialistFilter

/** Filters specialists by free-text query and an optional service category. */
class SearchSpecialistsUseCase {
    operator fun invoke(
        specialists: List<Specialist>,
        query: String,
        categoryId: String? = null,
    ): List<Specialist> {
        val byCategory = if (categoryId == null) {
            specialists
        } else {
            specialists.filter { it.categoryId == categoryId }
        }
        return SpecialistFilter.filter(byCategory, query)
    }
}

class ToggleFavoriteUseCase(private val repository: SlotikRepository) {
    suspend operator fun invoke(specialistId: String) = repository.toggleFavorite(specialistId)
}
