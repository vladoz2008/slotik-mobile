package com.slotik.mobile.presentation.features.specialists

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.slotik.mobile.data.repository.PersistentSlotikRepository
import com.slotik.mobile.domain.model.Specialist
import com.slotik.mobile.domain.usecase.ObserveAppStateUseCase
import com.slotik.mobile.domain.usecase.SearchSpecialistsUseCase
import com.slotik.mobile.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SpecialistsUiState(
    val searchQuery: String = "",
    val selectedCategoryId: String? = null,
    val allSpecialists: List<Specialist> = emptyList(),
    val filteredSpecialists: List<Specialist> = emptyList(),
    val favoriteIds: Set<String> = emptySet(),
)

class SpecialistsViewModel(
    private val observeAppState: ObserveAppStateUseCase,
    private val searchSpecialists: SearchSpecialistsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val selectedCategory = MutableStateFlow<String?>(null)

    val state: StateFlow<SpecialistsUiState> = combine(
        observeAppState(),
        searchQuery,
        selectedCategory,
    ) { repoState, query, categoryId ->
        SpecialistsUiState(
            searchQuery = query,
            selectedCategoryId = categoryId,
            allSpecialists = repoState.specialists,
            filteredSpecialists = searchSpecialists(repoState.specialists, query, categoryId),
            favoriteIds = repoState.favoriteSpecialistIds,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = with(observeAppState().value) {
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

    fun selectCategory(categoryId: String?) {
        selectedCategory.value = categoryId
    }

    fun clearSearchQuery() {
        searchQuery.value = ""
    }

    fun clearFilters() {
        searchQuery.value = ""
        selectedCategory.value = null
    }

    fun toggleFavorite(specialistId: String) {
        viewModelScope.launch { toggleFavoriteUseCase(specialistId) }
    }

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repository = PersistentSlotikRepository.getInstance(context)
                return SpecialistsViewModel(
                    ObserveAppStateUseCase(repository),
                    SearchSpecialistsUseCase(),
                    ToggleFavoriteUseCase(repository),
                ) as T
            }
        }
    }
}
