package com.slotik.mobile.presentation.features.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.slotik.mobile.data.repository.PersistentSlotikRepository
import com.slotik.mobile.domain.model.Booking
import com.slotik.mobile.domain.model.BookingStatus
import com.slotik.mobile.domain.model.ServiceCategory
import com.slotik.mobile.domain.model.Specialist
import com.slotik.mobile.domain.repository.SlotikRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class HomeUiState(
    val categories: List<ServiceCategory> = emptyList(),
    val featuredSpecialists: List<Specialist> = emptyList(),
    val nextBooking: Booking? = null,
)

class HomeViewModel(
    private val repository: SlotikRepository,
) : ViewModel() {

    val state: StateFlow<HomeUiState> = repository.state.map { repoState ->
        HomeUiState(
            categories = repoState.categories,
            featuredSpecialists = repoState.specialists.take(2),
            nextBooking = repoState.currentBookings
                .filter { it.status == BookingStatus.CONFIRMED }
                .minByOrNull { it.date.atTime(it.startTime) },
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = with(repository.state.value) {
            HomeUiState(
                categories = categories,
                featuredSpecialists = specialists.take(2),
                nextBooking = currentBookings
                    .filter { it.status == BookingStatus.CONFIRMED }
                    .minByOrNull { it.date.atTime(it.startTime) },
            )
        },
    )

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                HomeViewModel(PersistentSlotikRepository.getInstance(context)) as T
        }
    }
}
