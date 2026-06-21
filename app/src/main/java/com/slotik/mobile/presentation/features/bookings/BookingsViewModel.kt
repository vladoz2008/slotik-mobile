package com.slotik.mobile.presentation.features.bookings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.slotik.mobile.data.repository.PersistentSlotikRepository
import com.slotik.mobile.domain.model.Booking
import com.slotik.mobile.domain.model.ProfileSection
import com.slotik.mobile.domain.model.Specialist
import com.slotik.mobile.domain.model.UserProfile
import com.slotik.mobile.domain.usecase.CancelBookingUseCase
import com.slotik.mobile.domain.usecase.ObserveAppStateUseCase
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
    val userProfile: UserProfile? = null,
)

class BookingsViewModel(
    private val observeAppState: ObserveAppStateUseCase,
    private val cancelBookingUseCase: CancelBookingUseCase,
) : ViewModel() {

    private val activeSection = MutableStateFlow(ProfileSection.BOOKINGS)

    val state: StateFlow<BookingsUiState> = combine(
        observeAppState(),
        activeSection,
    ) { repoState, section ->
        BookingsUiState(
            currentBookings = repoState.currentBookings,
            bookingHistory = repoState.bookingHistory,
            favoriteSpecialists = repoState.specialists.filter {
                it.id in repoState.favoriteSpecialistIds
            },
            activeSection = section,
            userProfile = repoState.userProfile,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = with(observeAppState().value) {
            BookingsUiState(
                currentBookings = currentBookings,
                bookingHistory = bookingHistory,
                favoriteSpecialists = specialists.filter { it.id in favoriteSpecialistIds },
                userProfile = userProfile,
            )
        },
    )

    fun openSection(section: ProfileSection) {
        activeSection.value = section
    }

    fun cancelBooking(bookingId: String) {
        viewModelScope.launch { cancelBookingUseCase(bookingId) }
    }

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repository = PersistentSlotikRepository.getInstance(context)
                return BookingsViewModel(
                    ObserveAppStateUseCase(repository),
                    CancelBookingUseCase(repository),
                ) as T
            }
        }
    }
}
