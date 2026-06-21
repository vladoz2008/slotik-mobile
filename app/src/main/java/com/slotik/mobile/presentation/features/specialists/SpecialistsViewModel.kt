package com.slotik.mobile.presentation.features.specialists

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.slotik.mobile.data.repository.PersistentSlotikRepository
import com.slotik.mobile.domain.model.Specialist
import com.slotik.mobile.domain.repository.SlotikRepository
import com.slotik.mobile.domain.util.SpecialistFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SpecialistsUiState(
    val searchQuery: String = "",
    val allSpecialists: List<Specialist> = emptyList(),
    val filteredSpecialists: List<Specialist> = emptyList(),
    val favoriteIds: Set<String> = emptySet(),
)

class SpecialistsViewModel(
    private val repository: SlotikRepository,
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    val state: StateFlow<SpecialistsUiState> = combine(
        repository.state,
        searchQuery,
    ) { repoState, query ->
        SpecialistsUiState(
            searchQuery = query,
            allSpecialists = repoState.specialists,
            filteredSpecialists = SpecialistFilter.filter(repoState.specialists, query),
            favoriteIds = repoState.favoriteSpecialistIds,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = with(repository.state.value) {
            SpecialistsUiState(
                allSpecialists = specialists,
                filteredSpecialists = specialists,
                favoriteIds = favoriteSpecialistIds,
            )
        },
    )

    fun updateSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun clearSearchQuery() {
        searchQuery.value = ""
    }

    fun toggleFavorite(specialistId: String) {
        viewModelScope.launch { repository.toggleFavorite(specialistId) }
    }

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                SpecialistsViewModel(PersistentSlotikRepository.getInstance(context)) as T
        }
    }
}
