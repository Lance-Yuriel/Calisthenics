# Product Overview [DONE]

Lightweight calisthenics club app focused on events, attendance, identity.

## Goals [DONE]
- Event participation (RSVP + schedule)
- Attendance tracking (QR check-in)
- Club identity (badges, skills reference)
- Lightweight gamification (admin badges in MVP)

## Platform (MVP) [DONE]
- **Android first** — Kotlin / Jetpack Compose
- **Backend:** Firebase (Auth + Firestore + Storage)
- **Development Modes:** 
  - `mockDebug`: 100% Offline with high-quality mock data (perfect for screenshots).
  - `prodDebug`: Connected to live Firebase backend.

## Architecture [DONE]
- Clean Architecture with interface-based repositories.
- Flavor-based source set isolation for mock data.

## Spec index [DONE]
- `14_qr_check_in.md`
- `15_membership.md`
- `16_event_authoring.md`
- `10_event_state_lifecycle.md`
- `05_notifications.md`
- `07_skills_explorer.md`
- `06_badges_system.md`
- `17_member_directory.md`
- `18_onboarding.md`
- `19_event_detail_ui.md`
