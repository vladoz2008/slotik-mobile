package com.slotik.mobile.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.slotik.mobile.domain.model.ProfileSection
import com.slotik.mobile.presentation.features.auth.AuthScreen
import com.slotik.mobile.presentation.features.auth.AuthViewModel
import com.slotik.mobile.presentation.features.booking.BookingConfirmationScreen
import com.slotik.mobile.presentation.features.booking.BookingViewModel
import com.slotik.mobile.presentation.features.booking.SlotSelectionScreen
import com.slotik.mobile.presentation.features.bookings.BookingsViewModel
import com.slotik.mobile.presentation.features.bookings.ProfileScreen
import com.slotik.mobile.presentation.features.home.HomeViewModel
import com.slotik.mobile.presentation.features.home.HomeScreen
import com.slotik.mobile.presentation.features.onboarding.OnboardingScreen
import com.slotik.mobile.presentation.features.specialists.EmptySpecialistsScreen
import com.slotik.mobile.presentation.features.specialists.SpecialistsScreen
import com.slotik.mobile.presentation.features.specialists.SpecialistsViewModel
import com.slotik.mobile.presentation.navigation.SlotikDestination

@Composable
fun SlotikApp(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.factory(context))
    val authState by authViewModel.state.collectAsStateWithLifecycle()

    when {
        !authState.onboardingCompleted -> OnboardingScreen(
            onContinue = authViewModel::completeOnboarding,
        )
        !authState.isAuthorized -> AuthScreen(
            authMode = authState.authMode,
            email = authState.email,
            password = authState.password,
            canSubmit = authState.canSubmit,
            onAuthModeChange = authViewModel::updateAuthMode,
            onEmailChange = authViewModel::updateEmail,
            onPasswordChange = authViewModel::updatePassword,
            onSubmit = authViewModel::submit,
        )
        else -> SlotikAuthenticatedApp(
            onSignOut = authViewModel::signOut,
            modifier = modifier,
        )
    }
}

@Composable
private fun SlotikAuthenticatedApp(
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.factory(context))
    val specialistsViewModel: SpecialistsViewModel = viewModel(
        factory = SpecialistsViewModel.factory(context),
    )
    val bookingViewModel: BookingViewModel = viewModel(factory = BookingViewModel.factory(context))
    val bookingsViewModel: BookingsViewModel = viewModel(
        factory = BookingsViewModel.factory(context),
    )

    val homeState by homeViewModel.state.collectAsStateWithLifecycle()
    val specialistsState by specialistsViewModel.state.collectAsStateWithLifecycle()
    val bookingState by bookingViewModel.state.collectAsStateWithLifecycle()
    val bookingsState by bookingsViewModel.state.collectAsStateWithLifecycle()

    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = remember(backStack) {
        SlotikDestination.entries.firstOrNull {
            it.route == backStack?.destination?.route
        } ?: SlotikDestination.HOME
    }

    NavHost(
        navController = navController,
        startDestination = SlotikDestination.HOME.route,
        modifier = modifier,
    ) {
        composable(SlotikDestination.HOME.route) {
            HomeScreen(
                categories = homeState.categories,
                nextBooking = homeState.nextBooking,
                featuredSpecialists = homeState.featuredSpecialists,
                onOpenSearch = {
                    specialistsViewModel.selectCategory(null)
                    navController.navigate(SlotikDestination.SPECIALISTS.route)
                },
                onOpenSpecialist = { specialistId ->
                    bookingViewModel.selectSpecialist(specialistId)
                    navController.navigate(SlotikDestination.SLOT_SELECTION.route)
                },
                onOpenBookingDetails = { navController.navigate(SlotikDestination.BOOKINGS.route) },
                onOpenCategory = { categoryId ->
                    specialistsViewModel.selectCategory(categoryId)
                    navController.navigate(SlotikDestination.SPECIALISTS.route)
                },
                onNavigate = { destination ->
                    navController.navigate(destination.route) { launchSingleTop = true }
                },
                userFirstName = homeState.userFirstName,
            )
        }

        composable(SlotikDestination.SPECIALISTS.route) {
            if (specialistsState.filteredSpecialists.isEmpty()) {
                EmptySpecialistsScreen(
                    onBackToSearch = specialistsViewModel::clearFilters,
                    onResetFilters = specialistsViewModel::clearFilters,
                    onNavigate = { destination ->
                        navController.navigate(destination.route) { launchSingleTop = true }
                    },
                )
            } else {
                SpecialistsScreen(
                    searchQuery = specialistsState.searchQuery,
                    specialists = specialistsState.filteredSpecialists,
                    favoriteIds = specialistsState.favoriteIds,
                    onSearchChange = specialistsViewModel::updateSearchQuery,
                    onFavoriteClick = specialistsViewModel::toggleFavorite,
                    onSpecialistClick = { specialistId ->
                        bookingViewModel.selectSpecialist(specialistId)
                        navController.navigate(SlotikDestination.SLOT_SELECTION.route)
                    },
                    onNavigate = { destination ->
                        navController.navigate(destination.route) { launchSingleTop = true }
                    },
                )
            }
        }

        composable(SlotikDestination.SLOT_SELECTION.route) {
            SlotSelectionScreen(
                specialist = bookingState.selectedSpecialist,
                slots = bookingState.availableSlots,
                selectedSlot = bookingState.selectedSlot,
                onBack = { navController.popBackStack() },
                onSelectSlot = bookingViewModel::selectSlot,
                onContinue = { navController.navigate(SlotikDestination.BOOKING_CONFIRMATION.route) },
            )
        }

        composable(SlotikDestination.BOOKING_CONFIRMATION.route) {
            BookingConfirmationScreen(
                specialist = bookingState.selectedSpecialist,
                selectedSlot = bookingState.selectedSlot,
                comment = bookingState.comment,
                consent = bookingState.consent,
                canConfirm = bookingState.canConfirm,
                onBack = { navController.popBackStack() },
                onCommentChange = bookingViewModel::updateComment,
                onConsentChange = bookingViewModel::updateConsent,
                onConfirm = {
                    bookingViewModel.confirmBooking { _ ->
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
                currentBookings = bookingsState.currentBookings,
                bookingHistory = bookingsState.bookingHistory,
                favoriteSpecialists = bookingsState.favoriteSpecialists,
                section = ProfileSection.BOOKINGS,
                onSectionChange = bookingsViewModel::openSection,
                onCancelBooking = bookingsViewModel::cancelBooking,
                onOpenSpecialist = { specialistId ->
                    bookingViewModel.selectSpecialist(specialistId)
                    navController.navigate(SlotikDestination.SLOT_SELECTION.route)
                },
                onNavigate = { destination ->
                    navController.navigate(destination.route) { launchSingleTop = true }
                },
                onSignOut = onSignOut,
                userName = bookingsState.userProfile?.fullName ?: "Пользователь",
                userPhone = bookingsState.userProfile?.phone ?: "",
            )
        }

        composable(SlotikDestination.PROFILE.route) {
            ProfileScreen(
                currentRoute = currentRoute,
                currentBookings = bookingsState.currentBookings,
                bookingHistory = bookingsState.bookingHistory,
                favoriteSpecialists = bookingsState.favoriteSpecialists,
                section = bookingsState.activeSection,
                onSectionChange = bookingsViewModel::openSection,
                onCancelBooking = bookingsViewModel::cancelBooking,
                onOpenSpecialist = { specialistId ->
                    bookingViewModel.selectSpecialist(specialistId)
                    navController.navigate(SlotikDestination.SLOT_SELECTION.route)
                },
                onNavigate = { destination ->
                    navController.navigate(destination.route) { launchSingleTop = true }
                },
                onSignOut = onSignOut,
                userName = bookingsState.userProfile?.fullName ?: "Пользователь",
                userPhone = bookingsState.userProfile?.phone ?: "",
            )
        }
    }
}
