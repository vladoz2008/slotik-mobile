package com.slotik.mobile.presentation.features.bookings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.slotik.mobile.data.repository.PersistentSlotikRepository
import com.slotik.mobile.domain.model.Booking
import com.slotik.mobile.domain.model.ProfileSection
import com.slotik.mobile.domain.model.Specialist
import com.slotik.mobile.domain.repository.SlotikRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class BookingsUiState(
    val currentBookings: List<Booking> = emptyList(),
    val bookingHistory: List<Booking> = emptyList(),
    val favoriteSpecialists: List<Specialist> = emptyList(),
    val activeSection: ProfileSection = ProfileSection.BOOKINGS,
)

class BookingsViewModel(
    private val repository: SlotikRepository,
) : ViewModel() {

    private val activeSection = MutableStateFlow(ProfileSection.BOOKINGS)

    val state: StateFlow<BookingsUiState> = combine(
        repository.state,
        activeSection,
    ) { repoState, section ->
        BookingsUiState(
            currentBookings = repoState.currentBookings,
            bookingHistory = repoState.bookingHistory,
            favoriteSpecialists = repoState.specialists.filter {
                it.id in repoState.favoriteSpecialistIds
            },
            activeSection = section,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = with(repository.state.value) {
            BookingsUiState(
                currentBookings = currentBookings,
                bookingHistory = bookingHistory,
                favoriteSpecialists = specialists.filter { it.id in favoriteSpecialistIds },
            )
        },
    )

    fun openSection(section: ProfileSection) {
        activeSection.value = section
    }

    fun cancelBooking(bookingId: String) {
        viewModelScope.launch { repository.cancelBooking(bookingId) }
    }

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                BookingsViewModel(PersistentSlotikRepository.getInstance(context)) as T
        }
    }
}
