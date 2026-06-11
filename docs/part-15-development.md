# Part 15 Development Notes

## Goal

Turn the Slotik documentation package into a real Android MVP that follows the earlier project parts instead of starting from disconnected mockups.

## Source Materials Used

This implementation is based on:
- part 12 `User Flow`
- part 13 `database design`
- part 14 `application architecture`
- final Figma screens from part 3/design materials
- part 15 assignment requirements

## Actual Result on 2026-06-11

The project now exists as a real Android application in the repository root, not in a separate `mobile-app/` directory.

What is already implemented:
- Android project bootstrap with Gradle wrapper
- `Compose` application setup
- project theme and color system
- screen navigation
- repository state model
- local persistence for onboarding, auth flag, bookings, favorites
- booking creation flow
- booking cancellation flow
- favorites toggle
- empty search state

## Implemented MVP Flow

1. User opens onboarding.
2. User continues to auth screen.
3. User enters any non-empty credentials.
4. User lands on the home screen.
5. User opens specialists catalog.
6. User selects a specialist.
7. User chooses a free slot.
8. User confirms booking.
9. Booking appears in the bookings/profile section.
10. Booking can be cancelled, and the slot becomes available again.

## Code Map

### App Entry

- `app/src/main/java/com/slotik/mobile/MainActivity.kt`
  - app bootstrap
  - Compose root
  - ViewModel creation

### Navigation and Screen State

- `app/src/main/java/com/slotik/mobile/presentation/SlotikApp.kt`
  - top-level app flow
  - authenticated navigation graph
- `app/src/main/java/com/slotik/mobile/presentation/SlotikViewModel.kt`
  - screen state composition
  - booking/auth/search actions
- `app/src/main/java/com/slotik/mobile/presentation/navigation/SlotikDestination.kt`
  - app routes

### Screens

- `presentation/features/onboarding/OnboardingScreen.kt`
- `presentation/features/auth/AuthScreen.kt`
- `presentation/features/home/HomeScreen.kt`
- `presentation/features/specialists/SpecialistsScreen.kt`
- `presentation/features/booking/SlotSelectionScreen.kt`
- `presentation/features/booking/BookingConfirmationScreen.kt`
- `presentation/features/bookings/ProfileScreen.kt`

### Reusable UI

- `presentation/components/SlotikComponents.kt`
  - layout container
  - top bar
  - primary button
  - cards
  - bottom navigation
  - chips and avatar

### Domain Layer

- `domain/model/SlotikModels.kt`
  - categories
  - specialists
  - availability slots
  - bookings
  - screen sections and statuses
- `domain/repository/SlotikRepository.kt`
  - repository contract

### Data Layer

- `data/repository/PersistentSlotikRepository.kt`
  - seeded MVP data
  - booking and cancellation logic
  - favorites
  - state rebuild
- `data/local/SlotikPreferencesStorage.kt`
  - DataStore-based persistence

### Resources and Theme

- `presentation/theme/Color.kt`
- `presentation/theme/Theme.kt`
- `presentation/theme/Type.kt`
- `app/src/main/res/values/strings.xml`
- `app/src/main/res/values/colors.xml`
- `app/src/main/res/values/themes.xml`
- `app/src/main/res/drawable/ic_slotik_launcher.xml`

## Connection to Earlier Parts

### Part 12: User Flow

Implemented directly:
- primary booking scenario
- cancellation scenario
- empty state for no results

Not yet implemented:
- reschedule flow
- specialist schedule editor
- network conflict state

### Part 13: Database Design

The code mirrors the data model in simplified MVP form:
- `Specialist`
- `AvailabilitySlot`
- `Booking`
- booking status
- favorite specialist state

For the first local MVP, the full server ERD is reduced to a client-safe structure with seeded data and persisted user state.

### Part 14: Architecture

The app follows the required separation:

`UI -> Presentation -> Domain -> Data`

Within the current MVP:
- `presentation` handles screens and state orchestration
- `domain` contains shared models and repository contract
- `data` contains persistence and seeded repository logic

## Design Reference Set

The final approved Figma references are stored in:

- `docs/assets/figma-final/01-onboarding.png`
- `docs/assets/figma-final/02-specialists.png`
- `docs/assets/figma-final/03-home.png`
- `docs/assets/figma-final/04-auth.png`
- `docs/assets/figma-final/05-slot.png`
- `docs/assets/figma-final/06-booking.png`
- `docs/assets/figma-final/07-profile.png`
- `docs/assets/figma-final/08-empty.png`

## Verification

Verified locally on `2026-06-11`:
- project syncs and builds with Gradle
- `./gradlew assembleDebug` succeeds

Not yet verified in this repository session:
- launch on configured emulator
- visual QA against live running APK
- manual capture of runtime screenshots

## Runtime Screenshots

First real runtime screenshots from the emulator were added to the repository:

- `docs/assets/runtime-screenshots/01-home-runtime.png`
- `docs/assets/runtime-screenshots/02-specialists-runtime.png`
- `docs/assets/runtime-screenshots/03-slot-selection-runtime.png`
- `docs/assets/runtime-screenshots/04-booking-confirmation-runtime.png`
- `docs/assets/runtime-screenshots/05-bookings-runtime.png`
- `docs/assets/runtime-screenshots/06-profile-favorites-runtime.png`
- `docs/assets/runtime-screenshots/07-auth-runtime.png`

These screenshots are useful for:
- part 15 evidence in Notion
- comparison against final Figma frames
- tracking visible UI/UX issues before the next polish pass

## Visible UI/UX Issues From The First Runtime Pass

The first runtime screenshots already show several rough spots that should be fixed next:

- profile tabs break layout on narrow width, especially `Избранное`
- settings list bottom area is clipped in the profile/favorites screen
- bookings screen content is too dense and visually overloaded
- auth screen has a stray circular element on the left side near the primary button
- some screens need tighter spacing and better vertical rhythm to match Figma
- slot-selection layout is technically working, but still looks too raw compared with the final design

These issues are not blockers for the MVP flow itself, but they are real polish tasks for the next UI iteration.

## Problems and Decisions

### Decision 1: Build the Android project in the repository root

Reason:
- the current repository is already the app workspace
- simpler structure for part 15 reporting
- avoids fake nesting with `mobile-app/`

### Decision 2: Use `DataStore` instead of a temporary in-memory-only repository

Reason:
- part 15 expects real project structure and local data handling
- persisted onboarding/bookings/favorites makes the MVP feel closer to a real app

### Decision 3: Start with the client booking flow only

Reason:
- it is the core value from the MVP definition
- it matches the most important User Flow path
- specialist cabinet and rescheduling can be layered afterward

## Remaining Work for Part 15

- add actual git commits in meaningful stages
- run the app in emulator/device and capture screenshots
- refine screen parity against Figma
- add reschedule flow
- add reminder model/placeholder flow
- update Notion page with real repository evidence and screenshots
