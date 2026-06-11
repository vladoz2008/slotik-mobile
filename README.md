# Slotik

`Slotik` is a mobile MVP for booking appointments with private specialists without long back-and-forth messaging.

Current implementation is a native Android app on `Kotlin + Jetpack Compose` built from the project materials in parts 12-15:
- MVP definition
- User Flow
- database design
- application architecture
- final Figma screens

## Current Status

As of `2026-06-11`, the project already has a working Android codebase and a compilable MVP flow:

`Onboarding -> Auth -> Home -> Specialists -> Slot selection -> Booking confirmation -> Bookings/Profile`

Implemented now:
- onboarding and simplified auth flow
- home screen with categories and featured specialists
- specialists catalog with search and favorites
- slot selection by date and time period
- booking confirmation with consent and comment
- bookings/profile screen with cancellation
- empty state for search results
- local persistence via `DataStore`

Build verification:

```bash
./gradlew assembleDebug
```

The debug build passes successfully on `2026-06-11`.

## Tech Stack

- `Kotlin`
- `Jetpack Compose`
- `Material 3`
- `Navigation Compose`
- `AndroidX DataStore Preferences`
- `Gradle Kotlin DSL`

## Project Structure

```text
Slotik/
  app/
    src/main/
      java/com/slotik/mobile/
        MainActivity.kt
        presentation/
        domain/
        data/
      res/
  docs/
    assets/figma-final/
    part-15-development.md
    part-15-manual-test-checklist.md
    part-15-commit-plan.md
```

Key files:
- `app/src/main/java/com/slotik/mobile/MainActivity.kt`
- `app/src/main/java/com/slotik/mobile/presentation/SlotikApp.kt`
- `app/src/main/java/com/slotik/mobile/presentation/SlotikViewModel.kt`
- `app/src/main/java/com/slotik/mobile/data/repository/PersistentSlotikRepository.kt`
- `app/src/main/java/com/slotik/mobile/data/local/SlotikPreferencesStorage.kt`
- `app/src/main/java/com/slotik/mobile/domain/model/SlotikModels.kt`

## Design References

The app is implemented from the final Figma frames only. Exported references are stored in:

- `docs/assets/figma-final/01-onboarding.png`
- `docs/assets/figma-final/02-specialists.png`
- `docs/assets/figma-final/03-home.png`
- `docs/assets/figma-final/04-auth.png`
- `docs/assets/figma-final/05-slot.png`
- `docs/assets/figma-final/06-booking.png`
- `docs/assets/figma-final/07-profile.png`
- `docs/assets/figma-final/08-empty.png`

## What Is Next

Next development steps for part 15:
- add actual Git commit history in the repo
- run the app on emulator/device and save screenshots
- refine UI parity against Figma
- extend flow with reschedule/reminder logic
- prepare GitHub link and final Notion evidence
