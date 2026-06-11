package com.slotik.mobile.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.slotik.mobile.domain.model.ProfileSection
import com.slotik.mobile.presentation.features.auth.AuthScreen
import com.slotik.mobile.presentation.features.booking.BookingConfirmationScreen
import com.slotik.mobile.presentation.features.booking.SlotSelectionScreen
import com.slotik.mobile.presentation.features.bookings.ProfileScreen
import com.slotik.mobile.presentation.features.home.HomeScreen
import com.slotik.mobile.presentation.features.onboarding.OnboardingScreen
import com.slotik.mobile.presentation.features.specialists.EmptySpecialistsScreen
import com.slotik.mobile.presentation.features.specialists.SpecialistsScreen
import com.slotik.mobile.presentation.navigation.SlotikDestination

@Composable
fun SlotikApp(
    state: SlotikScreenState,
    viewModel: SlotikViewModel,
    modifier: Modifier = Modifier,
) {
    when {
        !state.onboardingCompleted -> OnboardingScreen(onContinue = viewModel::completeOnboarding)
        !state.isAuthorized -> AuthScreen(
            authMode = state.authMode,
            email = state.email,
            password = state.password,
            canSubmit = state.canSubmitAuth,
            onAuthModeChange = viewModel::updateAuthMode,
            onEmailChange = viewModel::updateEmail,
            onPasswordChange = viewModel::updatePassword,
            onSubmit = viewModel::submitAuth,
        )
        else -> SlotikAuthenticatedApp(
            state = state,
            viewModel = viewModel,
            modifier = modifier,
        )
    }
}

@Composable
private fun SlotikAuthenticatedApp(
    state: SlotikScreenState,
    viewModel: SlotikViewModel,
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = remember(backStack) {
        SlotikDestination.entries.firstOrNull { it.route == backStack?.destination?.route } ?: SlotikDestination.HOME
    }

    NavHost(
        navController = navController,
        startDestination = SlotikDestination.HOME.route,
        modifier = modifier,
    ) {
        composable(SlotikDestination.HOME.route) {
            HomeScreen(
                categories = state.categories,
                nextBooking = state.nextBooking,
                featuredSpecialists = state.featuredSpecialists,
                onOpenSearch = { navController.navigate(SlotikDestination.SPECIALISTS.route) },
                onOpenSpecialist = { specialistId ->
                    viewModel.selectSpecialist(specialistId)
                    navController.navigate(SlotikDestination.SLOT_SELECTION.route)
                },
                onOpenBookingDetails = { navController.navigate(SlotikDestination.BOOKINGS.route) },
                onNavigate = { destination ->
                    navController.navigate(destination.route) {
                        launchSingleTop = true
                    }
                },
            )
        }

        composable(SlotikDestination.SPECIALISTS.route) {
            if (state.filteredSpecialists.isEmpty()) {
                EmptySpecialistsScreen(
                    onBackToSearch = viewModel::clearSearchQuery,
                    onResetFilters = viewModel::clearSearchQuery,
                    onNavigate = { destination ->
                        navController.navigate(destination.route) {
                            launchSingleTop = true
                        }
                    },
                )
            } else {
                SpecialistsScreen(
                    searchQuery = state.searchQuery,
                    specialists = state.filteredSpecialists,
                    favoriteIds = state.favoriteSpecialists.map { it.id }.toSet(),
                    onSearchChange = viewModel::updateSearchQuery,
                    onFavoriteClick = viewModel::toggleFavorite,
                    onSpecialistClick = { specialistId ->
                        viewModel.selectSpecialist(specialistId)
                        navController.navigate(SlotikDestination.SLOT_SELECTION.route)
                    },
                    onNavigate = { destination ->
                        navController.navigate(destination.route) {
                            launchSingleTop = true
                        }
                    },
                )
            }
        }

        composable(SlotikDestination.SLOT_SELECTION.route) {
            SlotSelectionScreen(
                specialist = state.selectedSpecialist,
                slots = state.availableSlots,
                selectedSlot = state.selectedSlot,
                onBack = { navController.popBackStack() },
                onSelectSlot = viewModel::selectSlot,
                onContinue = { navController.navigate(SlotikDestination.BOOKING_CONFIRMATION.route) },
            )
        }

        composable(SlotikDestination.BOOKING_CONFIRMATION.route) {
            BookingConfirmationScreen(
                specialist = state.selectedSpecialist,
                selectedSlot = state.selectedSlot,
                comment = state.bookingComment,
                consent = state.bookingConsent,
                canConfirm = state.canConfirmBooking,
                onBack = { navController.popBackStack() },
                onCommentChange = viewModel::updateBookingComment,
                onConsentChange = viewModel::updateBookingConsent,
                onConfirm = {
                    viewModel.confirmBooking {
                        navController.navigate(SlotikDestination.BOOKINGS.route) {
                            popUpTo(SlotikDestination.HOME.route)
                        }
                    }
                },
            )
        }

        composable(SlotikDestination.BOOKINGS.route) {
            ProfileScreen(
                currentRoute = currentRoute,
                currentBookings = state.currentBookings,
                bookingHistory = state.bookingHistory,
                favoriteSpecialists = state.favoriteSpecialists,
                section = ProfileSection.BOOKINGS,
                onSectionChange = viewModel::openBookingsSection,
                onCancelBooking = viewModel::cancelBooking,
                onOpenSpecialist = { specialistId ->
                    viewModel.selectSpecialist(specialistId)
                    navController.navigate(SlotikDestination.SLOT_SELECTION.route)
                },
                onNavigate = { destination ->
                    navController.navigate(destination.route) {
                        launchSingleTop = true
                    }
                },
                onSignOut = viewModel::signOut,
            )
        }

        composable(SlotikDestination.PROFILE.route) {
            ProfileScreen(
                currentRoute = currentRoute,
                currentBookings = state.currentBookings,
                bookingHistory = state.bookingHistory,
                favoriteSpecialists = state.favoriteSpecialists,
                section = state.profileSection,
                onSectionChange = viewModel::openBookingsSection,
                onCancelBooking = viewModel::cancelBooking,
                onOpenSpecialist = { specialistId ->
                    viewModel.selectSpecialist(specialistId)
                    navController.navigate(SlotikDestination.SLOT_SELECTION.route)
                },
                onNavigate = { destination ->
                    navController.navigate(destination.route) {
                        launchSingleTop = true
                    }
                },
                onSignOut = viewModel::signOut,
            )
        }
    }
}
