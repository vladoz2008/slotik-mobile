package com.slotik.mobile.data.repository

import android.content.Context
import com.slotik.mobile.data.local.PersistedSlotikSnapshot
import com.slotik.mobile.data.local.SlotikPreferencesStorage
import com.slotik.mobile.domain.model.AvailabilitySlot
import com.slotik.mobile.domain.model.Booking
import com.slotik.mobile.domain.model.BookingStatus
import com.slotik.mobile.domain.model.CategoryAccent
import com.slotik.mobile.domain.model.ServiceCategory
import com.slotik.mobile.domain.model.SlotPeriod
import com.slotik.mobile.domain.model.SlotikRepositoryState
import com.slotik.mobile.domain.model.Specialist
import com.slotik.mobile.domain.repository.SlotikRepository
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month
import java.util.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PersistentSlotikRepository private constructor(
    context: Context,
) : SlotikRepository {
    private val storage = SlotikPreferencesStorage(context)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val categories = listOf(
        ServiceCategory("psychology", "Психология", "120 специалистов", CategoryAccent.SKY),
        ServiceCategory("beauty", "Красота", "85 салонов", CategoryAccent.PINK),
        ServiceCategory("medicine", "Медицина", "240 врачей", CategoryAccent.BLUE),
        ServiceCategory("sport", "Спорт", "45 тренеров", CategoryAccent.STONE),
    )

    private val specialists = listOf(
        Specialist(
            id = "anna-volkova",
            name = "Анна Волкова",
            specialty = "Ведущий психолог",
            subtitle = "Онлайн консультация",
            rating = 4.9,
            reviewsCount = 120,
            priceFrom = 3500,
            location = "Онлайн",
            experienceLabel = "Стаж 12 лет",
            categoryId = "psychology",
        ),
        Specialist(
            id = "mikhail-ivanov",
            name = "Михаил Иванов",
            specialty = "Остеопат",
            subtitle = "Метро Сокол",
            rating = 4.9,
            reviewsCount = 85,
            priceFrom = 3000,
            location = "Метро Сокол",
            experienceLabel = "Стаж 10 лет",
            categoryId = "medicine",
        ),
        Specialist(
            id = "elena-petrova",
            name = "Елена Петрова",
            specialty = "Косметолог",
            subtitle = "Онлайн/Офлайн",
            rating = 5.0,
            reviewsCount = 210,
            priceFrom = 2500,
            location = "Онлайн/Офлайн",
            experienceLabel = "Стаж 8 лет",
            categoryId = "beauty",
        ),
    )

    private val staticHistory = listOf(
        Booking(
            id = "history-1",
            specialistId = "barber-archive",
            specialistName = "Barbershop \"Chop-Chop\"",
            specialistSubtitle = "Стрижка бороды",
            serviceName = "Стрижка бороды",
            location = "Салон",
            date = LocalDate.of(2026, Month.SEPTEMBER, 28),
            startTime = LocalTime.of(19, 0),
            endTime = LocalTime.of(19, 45),
            price = 1800,
            discount = 0,
            totalPrice = 1800,
            status = BookingStatus.COMPLETED,
            comment = "",
        ),
    )

    private val baseSlotsBySpecialist = mapOf(
        "anna-volkova" to listOf(
            seedSlot("anna-2026-10-15-0900", "anna-volkova", 15, 9, 0, 10, 0, SlotPeriod.MORNING),
            seedSlot("anna-2026-10-15-1000", "anna-volkova", 15, 10, 0, 11, 0, SlotPeriod.MORNING),
            seedSlot("anna-2026-10-15-1100", "anna-volkova", 15, 11, 0, 12, 0, SlotPeriod.MORNING, false),
            seedSlot("anna-2026-10-15-1230", "anna-volkova", 15, 12, 30, 13, 30, SlotPeriod.DAY),
            seedSlot("anna-2026-10-15-1400", "anna-volkova", 15, 14, 0, 15, 0, SlotPeriod.DAY),
            seedSlot("anna-2026-10-15-1530", "anna-volkova", 15, 15, 30, 16, 30, SlotPeriod.DAY),
            seedSlot("anna-2026-10-15-1600", "anna-volkova", 15, 16, 0, 17, 0, SlotPeriod.DAY),
            seedSlot("anna-2026-10-15-1800", "anna-volkova", 15, 18, 0, 19, 0, SlotPeriod.EVENING, false),
            seedSlot("anna-2026-10-15-1930", "anna-volkova", 15, 19, 30, 20, 30, SlotPeriod.EVENING),
        ),
        "mikhail-ivanov" to listOf(
            seedSlot("mikhail-2026-10-16-1100", "mikhail-ivanov", 16, 11, 0, 12, 0, SlotPeriod.MORNING),
            seedSlot("mikhail-2026-10-16-1300", "mikhail-ivanov", 16, 13, 0, 14, 0, SlotPeriod.DAY),
            seedSlot("mikhail-2026-10-16-1730", "mikhail-ivanov", 16, 17, 30, 18, 30, SlotPeriod.EVENING),
        ),
        "elena-petrova" to listOf(
            seedSlot("elena-2026-10-17-1000", "elena-petrova", 17, 10, 0, 11, 0, SlotPeriod.MORNING),
            seedSlot("elena-2026-10-17-1500", "elena-petrova", 17, 15, 0, 16, 0, SlotPeriod.DAY),
            seedSlot("elena-2026-10-17-1900", "elena-petrova", 17, 19, 0, 20, 0, SlotPeriod.EVENING),
        ),
    )

    private val mutableState = MutableStateFlow(buildState(PersistedSlotikSnapshot()))
    override val state: StateFlow<SlotikRepositoryState> = mutableState.asStateFlow()

    init {
        scope.launch {
            mutableState.value = buildState(storage.readSnapshot())
        }
    }

    override suspend fun completeOnboarding() {
        mutateSnapshot { copy(onboardingCompleted = true) }
    }

    override suspend fun signIn() {
        mutateSnapshot { copy(isAuthorized = true) }
    }

    override suspend fun signOut() {
        mutateSnapshot { copy(isAuthorized = false) }
    }

    override suspend fun createBooking(
        specialistId: String,
        slotId: String,
        comment: String,
    ): Booking? {
        val specialist = specialists.firstOrNull { it.id == specialistId } ?: return null
        val slot = baseSlotsBySpecialist[specialistId]
            ?.firstOrNull { it.id == slotId }
            ?: return null

        val snapshot = storage.readSnapshot()
        if (snapshot.bookings.any { it.id == slotId || it.id.endsWith(slotId) }) {
            return null
        }

        if (snapshot.bookings.any { it.startTime == slot.startTime && it.date == slot.date && it.specialistId == specialistId }) {
            return null
        }

        val booking = Booking(
            id = "booking-${slot.id}-${UUID.randomUUID().toString().take(8)}",
            specialistId = specialist.id,
            specialistName = specialist.name,
            specialistSubtitle = "${specialist.specialty}, ${specialist.experienceLabel}",
            serviceName = specialist.specialty,
            location = specialist.location,
            date = slot.date,
            startTime = slot.startTime,
            endTime = slot.endTime,
            price = specialist.priceFrom,
            discount = 500,
            totalPrice = specialist.priceFrom - 500,
            status = BookingStatus.CONFIRMED,
            comment = comment,
        )

        mutateSnapshot {
            copy(bookings = (bookings + booking).sortedBy { it.date.atTime(it.startTime) })
        }
        return booking
    }

    override suspend fun cancelBooking(bookingId: String) {
        mutateSnapshot {
            copy(bookings = bookings.filterNot { it.id == bookingId })
        }
    }

    override suspend fun toggleFavorite(specialistId: String) {
        mutateSnapshot {
            val next = favorites.toMutableSet()
            if (!next.add(specialistId)) {
                next.remove(specialistId)
            }
            copy(favorites = next)
        }
    }

    private suspend fun mutateSnapshot(transform: PersistedSlotikSnapshot.() -> PersistedSlotikSnapshot) {
        val current = storage.readSnapshot()
        val next = current.transform()
        storage.writeSnapshot(next)
        mutableState.value = buildState(next)
    }

    private fun buildState(snapshot: PersistedSlotikSnapshot): SlotikRepositoryState {
        val occupiedSlotIds = snapshot.bookings.mapNotNull { booking ->
            baseSlotsBySpecialist[booking.specialistId]
                ?.firstOrNull { slot ->
                    slot.date == booking.date && slot.startTime == booking.startTime
                }
                ?.id
        }.toSet()

        val slots = baseSlotsBySpecialist.mapValues { (_, slots) ->
            slots.map { slot ->
                slot.copy(isAvailable = slot.isAvailable && slot.id !in occupiedSlotIds)
            }
        }

        return SlotikRepositoryState(
            onboardingCompleted = snapshot.onboardingCompleted,
            isAuthorized = snapshot.isAuthorized,
            categories = categories,
            specialists = specialists,
            slotsBySpecialist = slots,
            currentBookings = snapshot.bookings.sortedBy { it.date.atTime(it.startTime) },
            bookingHistory = staticHistory,
            favoriteSpecialistIds = snapshot.favorites.ifEmpty { setOf("mikhail-ivanov") },
        )
    }

    companion object {
        @Volatile
        private var instance: PersistentSlotikRepository? = null

        fun getInstance(context: Context): PersistentSlotikRepository =
            instance ?: synchronized(this) {
                instance ?: PersistentSlotikRepository(context.applicationContext).also { instance = it }
            }

        fun seedSlot(
            id: String,
            specialistId: String,
            dayOfMonth: Int,
            startHour: Int,
            startMinute: Int,
            endHour: Int,
            endMinute: Int,
            period: SlotPeriod,
            isAvailable: Boolean = true,
        ): AvailabilitySlot = AvailabilitySlot(
            id = id,
            specialistId = specialistId,
            date = LocalDate.of(2026, Month.OCTOBER, dayOfMonth),
            startTime = LocalTime.of(startHour, startMinute),
            endTime = LocalTime.of(endHour, endMinute),
            period = period,
            isAvailable = isAvailable,
        )
    }
}
