package com.slotik.mobile.presentation.features.booking

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.slotik.mobile.data.repository.PersistentSlotikRepository
import com.slotik.mobile.domain.model.AvailabilitySlot
import com.slotik.mobile.domain.model.Booking
import com.slotik.mobile.domain.model.Specialist
import com.slotik.mobile.domain.repository.SlotikRepository
import com.slotik.mobile.domain.util.BookingValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class BookingUiState(
    val selectedSpecialist: Specialist? = null,
    val availableSlots: List<AvailabilitySlot> = emptyList(),
    val selectedSlot: AvailabilitySlot? = null,
    val comment: String = "",
    val consent: Boolean = false,
) {
    val canConfirm: Boolean get() = BookingValidator.canConfirm(
        specialistId = selectedSpecialist?.id,
        slotId = selectedSlot?.id,
        consent = consent,
    )
}

private data class BookingTransientState(
    val selectedSpecialistId: String? = null,
    val selectedSlotId: String? = null,
    val comment: String = "",
    val consent: Boolean = false,
)

class BookingViewModel(
    private val repository: SlotikRepository,
) : ViewModel() {

    private val transient = MutableStateFlow(BookingTransientState())

    val state: StateFlow<BookingUiState> = combine(
        repository.state,
        transient,
    ) { repoState, t ->
        val specialist = repoState.specialists.firstOrNull { it.id == t.selectedSpecialistId }
            ?: repoState.specialists.firstOrNull()
        val slots = specialist?.let { repoState.slotsBySpecialist[it.id].orEmpty() }.orEmpty()
        val slot = slots.firstOrNull { it.id == t.selectedSlotId }
            ?: slots.firstOrNull { it.isAvailable }
        BookingUiState(
            selectedSpecialist = specialist,
            availableSlots = slots,
            selectedSlot = slot,
            comment = t.comment,
            consent = t.consent,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BookingUiState(),
    )

    fun selectSpecialist(specialistId: String) {
        transient.value = BookingTransientState(
            selectedSpecialistId = specialistId,
        )
    }

    fun selectSlot(slotId: String) {
        transient.value = transient.value.copy(selectedSlotId = slotId)
    }

    fun updateComment(value: String) {
        transient.value = transient.value.copy(comment = value)
    }

    fun updateConsent(value: Boolean) {
        transient.value = transient.value.copy(consent = value)
    }

    fun confirmBooking(onBooked: (Booking?) -> Unit) {
        val current = state.value
        val specialist = current.selectedSpecialist ?: return
        val slot = current.selectedSlot ?: return
        if (!current.canConfirm) return

        viewModelScope.launch {
            val booking = repository.createBooking(
                specialistId = specialist.id,
                slotId = slot.id,
                comment = current.comment,
            )
            transient.value = BookingTransientState(
                selectedSpecialistId = specialist.id,
            )
            onBooked(booking)
        }
    }

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                BookingViewModel(PersistentSlotikRepository.getInstance(context)) as T
        }
    }
}
