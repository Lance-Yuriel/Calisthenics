# Product Overview

Lightweight calisthenics club app focused on events, attendance, identity.

## Goals
- Event participation (RSVP + schedule)
- Attendance tracking (QR check-in)
- Club identity (badges, skills reference)
- Lightweight gamification (admin badges in MVP)

## Non-goals
- Social feed
- Chat
- Workout tracking
- Nutrition tracking
- AI coaching

## Platform (MVP)
- **Android first** — Kotlin / Jetpack Compose in `AndroidStudioProjects/Calisthenics`
- **Backend:** Firebase (Auth + Firestore + FCM)
- **Architect for iOS later** — shared Firebase project, no Android-specific business logic in client-only hacks

## Spec index
- `14_qr_check_in.md` — member scans static event QR
- `15_membership.md` — open signup, admin approval
- `16_event_authoring.md` — capacity, waitlist, cancel, edit rules
- `10_event_state_lifecycle.md` — state transitions incl. CANCELLED
- `05_notifications.md` — push + inbox, auto reminders
- `07_skills_explorer.md` — static content + media
- `06_badges_system.md` — admin-only badges in MVP
- `17_member_directory.md` — names + roles
- `18_onboarding.md`, `19_event_detail_ui.md`
- Polish: directory, inbox bell, email on approve, ai-spec screens

## Backend (MVP)
- Firebase Auth, Firestore, FCM, Cloud Functions (see `system/06_edge_cases_hardening.md`)
