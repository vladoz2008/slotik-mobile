package com.slotik.mobile.domain.model

import java.time.LocalDate
import java.time.LocalTime

enum class CategoryAccent {
    SKY,
    PINK,
    BLUE,
    STONE,
}

enum class AuthMode {
    LOGIN,
    REGISTER,
}

enum class SlotPeriod {
    MORNING,
    DAY,
    EVENING,
}

enum class BookingStatus {
    CONFIRMED,
    COMPLETED,
}

enum class ProfileSection {
    BOOKINGS,
    FAVORITES,
}

data class ServiceCategory(
    val id: String,
    val title: String,
    val subtitle: String,
    val accent: CategoryAccent,
)

data class Specialist(
    val id: String,
    val name: String,
    val specialty: String,
    val subtitle: String,
    val rating: Double,
    val reviewsCount: Int,
    val priceFrom: Int,
    val location: String,
    val experienceLabel: String,
    val categoryId: String,
)

data class AvailabilitySlot(
    val id: String,
    val specialistId: String,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val period: SlotPeriod,
    val isAvailable: Boolean,
)

data class Booking(
    val id: String,
    val specialistId: String,
    val specialistName: String,
    val specialistSubtitle: String,
    val serviceName: String,
    val location: String,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val price: Int,
    val discount: Int,
    val totalPrice: Int,
    val status: BookingStatus,
    val comment: String,
)

data class SlotikRepositoryState(
    val onboardingCompleted: Boolean,
    val isAuthorized: Boolean,
    val categories: List<ServiceCategory>,
    val specialists: List<Specialist>,
    val slotsBySpecialist: Map<String, List<AvailabilitySlot>>,
    val currentBookings: List<Booking>,
    val bookingHistory: List<Booking>,
    val favoriteSpecialistIds: Set<String>,
)
