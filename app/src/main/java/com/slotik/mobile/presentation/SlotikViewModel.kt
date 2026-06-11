package com.slotik.mobile.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.slotik.mobile.data.repository.PersistentSlotikRepository
import com.slotik.mobile.domain.model.AuthMode
import com.slotik.mobile.domain.model.AvailabilitySlot
import com.slotik.mobile.domain.model.Booking
import com.slotik.mobile.domain.model.BookingStatus
import com.slotik.mobile.domain.model.ProfileSection
import com.slotik.mobile.domain.model.ServiceCategory
import com.slotik.mobile.domain.model.SlotikRepositoryState
import com.slotik.mobile.domain.model.Specialist
import com.slotik.mobile.domain.repository.SlotikRepository
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private data class TransientSlotikState(
    val authMode: AuthMode = AuthMode.LOGIN,
    val email: String = "",
    val password: String = "",
    val searchQuery: String = "",
    val selectedSpecialistId: String? = null,
    val selectedSlotId: String? = null,
    val bookingComment: String = "",
    val bookingConsent: Boolean = false,
    val profileSection: ProfileSection = ProfileSection.BOOKINGS,
)

data class SlotikScreenState(
    val onboardingCompleted: Boolean,
    val isAuthorized: Boolean,
    val authMode: AuthMode,
    val email: String,
    val password: String,
    val searchQuery: String,
    val categories: List<ServiceCategory>,
    val specialists: List<Specialist>,
    val filteredSpecialists: List<Specialist>,
    val selectedSpecialist: Specialist?,
    val selectedSlot: AvailabilitySlot?,
    val availableSlots: List<AvailabilitySlot>,
    val currentBookings: List<Booking>,
    val bookingHistory: List<Booking>,
    val favoriteSpecialists: List<Specialist>,
    val profileSection: ProfileSection,
    val bookingComment: String,
    val bookingConsent: Boolean,
    val featuredSpecialists: List<Specialist>,
) {
    val nextBooking: Booking? = currentBookings
        .filter { it.status == BookingStatus.CONFIRMED }
        .minByOrNull { it.date.atTime(it.startTime) }

    val canSubmitAuth: Boolean = email.isNotBlank() && password.isNotBlank()
    val canConfirmBooking: Boolean = selectedSpecialist != null && selectedSlot != null && bookingConsent
}

class SlotikViewModel(
    private val repository: SlotikRepository,
) : ViewModel() {
    private val transientState = MutableStateFlow(TransientSlotikState())

    val state: StateFlow<SlotikScreenState> = combine(
        repository.state,
        transientState,
    ) { repositoryState, transient ->
        val specialists = repositoryState.specialists
        val selectedSpecialist = specialists.firstOrNull {
            it.id == transient.selectedSpecialistId
        } ?: specialists.firstOrNull()

        val availableSlots = selectedSpecialist?.let { specialist ->
            repositoryState.slotsBySpecialist[specialist.id].orEmpty()
        }.orEmpty()

        val selectedSlot = availableSlots.firstOrNull {
            it.id == transient.selectedSlotId
        } ?: availableSlots.firstOrNull { it.isAvailable }

        val normalizedQuery = transient.searchQuery.trim().lowercase(Locale.getDefault())
        val filteredSpecialists = if (normalizedQuery.isBlank()) {
            specialists
        } else {
            specialists.filter { specialist ->
                specialist.name.lowercase(Locale.getDefault()).contains(normalizedQuery) ||
                    specialist.specialty.lowercase(Locale.getDefault()).contains(normalizedQuery)
            }
        }

        SlotikScreenState(
            onboardingCompleted = repositoryState.onboardingCompleted,
            isAuthorized = repositoryState.isAuthorized,
            authMode = transient.authMode,
            email = transient.email,
            password = transient.password,
            searchQuery = transient.searchQuery,
            categories = repositoryState.categories,
            specialists = specialists,
            filteredSpecialists = filteredSpecialists,
            selectedSpecialist = selectedSpecialist,
            selectedSlot = selectedSlot,
            availableSlots = availableSlots,
            currentBookings = repositoryState.currentBookings,
            bookingHistory = repositoryState.bookingHistory,
            favoriteSpecialists = specialists.filter { it.id in repositoryState.favoriteSpecialistIds },
            profileSection = transient.profileSection,
            bookingComment = transient.bookingComment,
            bookingConsent = transient.bookingConsent,
            featuredSpecialists = specialists.take(2),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = buildInitialState(repository.state.value),
    )

    fun completeOnboarding() {
        viewModelScope.launch {
            repository.completeOnboarding()
        }
    }

    fun updateAuthMode(mode: AuthMode) {
        transientState.value = transientState.value.copy(authMode = mode)
    }

    fun updateEmail(value: String) {
        transientState.value = transientState.value.copy(email = value)
    }

    fun updatePassword(value: String) {
        transientState.value = transientState.value.copy(password = value)
    }

    fun submitAuth() {
        if (state.value.canSubmitAuth.not()) return
        viewModelScope.launch {
            repository.signIn()
        }
    }

    fun signOut() {
        viewModelScope.launch {
            repository.signOut()
            transientState.value = transientState.value.copy(
                password = "",
                bookingComment = "",
                bookingConsent = false,
                profileSection = ProfileSection.BOOKINGS,
            )
        }
    }

    fun updateSearchQuery(value: String) {
        transientState.value = transientState.value.copy(searchQuery = value)
    }

    fun clearSearchQuery() {
        transientState.value = transientState.value.copy(searchQuery = "")
    }

    fun selectSpecialist(specialistId: String) {
        transientState.value = transientState.value.copy(
            selectedSpecialistId = specialistId,
            selectedSlotId = null,
            bookingComment = "",
            bookingConsent = false,
        )
    }

    fun selectSlot(slotId: String) {
        transientState.value = transientState.value.copy(selectedSlotId = slotId)
    }

    fun updateBookingComment(value: String) {
        transientState.value = transientState.value.copy(bookingComment = value)
    }

    fun updateBookingConsent(value: Boolean) {
        transientState.value = transientState.value.copy(bookingConsent = value)
    }

    fun confirmBooking(onBooked: (Booking?) -> Unit) {
        val currentState = state.value
        val specialist = currentState.selectedSpecialist ?: return
        val slot = currentState.selectedSlot ?: return
        if (currentState.canConfirmBooking.not()) return

        viewModelScope.launch {
            val booking = repository.createBooking(
                specialistId = specialist.id,
                slotId = slot.id,
                comment = currentState.bookingComment,
            )
            transientState.value = transientState.value.copy(
                selectedSlotId = null,
                bookingComment = "",
                bookingConsent = false,
                profileSection = ProfileSection.BOOKINGS,
            )
            onBooked(booking)
        }
    }

    fun cancelBooking(bookingId: String) {
        viewModelScope.launch {
            repository.cancelBooking(bookingId)
        }
    }

    fun toggleFavorite(specialistId: String) {
        viewModelScope.launch {
            repository.toggleFavorite(specialistId)
        }
    }

    fun openBookingsSection(section: ProfileSection) {
        transientState.value = transientState.value.copy(profileSection = section)
    }

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SlotikViewModel(
                    repository = PersistentSlotikRepository.getInstance(context),
                ) as T
            }
        }

        private fun buildInitialState(repositoryState: SlotikRepositoryState): SlotikScreenState {
            val specialist = repositoryState.specialists.firstOrNull()
            val slot = specialist?.let {
                repositoryState.slotsBySpecialist[it.id].orEmpty().firstOrNull { availabilitySlot -> availabilitySlot.isAvailable }
            }
            return SlotikScreenState(
                onboardingCompleted = repositoryState.onboardingCompleted,
                isAuthorized = repositoryState.isAuthorized,
                authMode = AuthMode.LOGIN,
                email = "",
                password = "",
                searchQuery = "",
                categories = repositoryState.categories,
                specialists = repositoryState.specialists,
                filteredSpecialists = repositoryState.specialists,
                selectedSpecialist = specialist,
                selectedSlot = slot,
                availableSlots = repositoryState.slotsBySpecialist[specialist?.id].orEmpty(),
                currentBookings = repositoryState.currentBookings,
                bookingHistory = repositoryState.bookingHistory,
                favoriteSpecialists = repositoryState.specialists.filter { it.id in repositoryState.favoriteSpecialistIds },
                profileSection = ProfileSection.BOOKINGS,
                bookingComment = "",
                bookingConsent = false,
                featuredSpecialists = repositoryState.specialists.take(2),
            )
        }

        private val ruLocale: Locale = Locale.forLanguageTag("ru")

        val dayFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM", ruLocale)
        val shortDayFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMM", ruLocale)
        val weekdayFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE", ruLocale)
        val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm", ruLocale)
    }
}
