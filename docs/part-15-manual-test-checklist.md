# Part 15 Manual Test Checklist

Status date: `2026-06-11`

## Build and Project Setup

- [x] Android project exists and opens as a Gradle project
- [x] Gradle wrapper is configured
- [x] `./gradlew assembleDebug` completes successfully
- [ ] App launch verified on emulator
- [ ] App launch verified on physical device

## Core MVP Flow

- [x] Onboarding screen exists
- [x] Auth screen exists
- [x] Home screen exists
- [x] Specialists screen exists
- [x] Slot selection screen exists
- [x] Booking confirmation screen exists
- [x] Profile/bookings screen exists
- [x] Empty state screen exists

## Navigation

- [x] Onboarding leads to auth
- [x] Auth leads to main app flow
- [x] Home opens specialists
- [x] Specialists opens slot selection
- [x] Slot selection opens booking confirmation
- [x] Bottom navigation switches between main sections

## Data and Persistence

- [x] Seeded specialists are available
- [x] Seeded slots are available
- [x] Booking can be created in repository logic
- [x] Booking can be cancelled in repository logic
- [x] Favorites can be toggled
- [x] Onboarding state is persisted
- [x] Auth flag is persisted
- [x] Bookings are persisted
- [x] Favorites are persisted

## Still Needs Runtime QA

- [ ] Full booking flow tested on a running emulator
- [ ] Cancellation visually checked in UI
- [ ] Search empty state visually checked in UI
- [ ] Favorite toggle visually checked in UI
- [ ] Layout compared screen-by-screen with final Figma frames
