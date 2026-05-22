# AI / Implementation Prompt Pack

Use when generating Android (Compose) + Firebase code. Source of truth: `specifications/` folder.

## Stack
- Kotlin, Jetpack Compose, Material 3
- Firebase Auth (Google + Email), Firestore, Storage, FCM
- Cloud Functions (Node or Kotlin): waitlist FIFO, LIVE/CLOSED scheduler, QR validate, 24h reminder, approval email

## Screens to implement (MVP)
1. Login (Google + Email)
2. Pending / Rejected
3. Welcome onboarding (one screen)
4. Home (featured, stats, announcement, Scan, bell)
5. Events Upcoming / Past + Event detail
6. QR scanner + result dialogs
7. Badges catalog
8. Skills list + detail
9. Profile + edit bio/photo
10. Members directory + read-only member card
11. Notifications inbox
12. Admin panel (members queue, events CRUD, notifications, badges, skills)

## Non-negotiable rules
- `memberStatus=approved` gate on all member APIs
- QR only `startAt-60m` .. `endAt`, states PUBLISHED/LIVE/CLOSED
- RSVP doc id `{eventId}_{userId}`
- Cancelled in Upcoming with badge; Archived hidden from members
- Email required on all auth providers

## Reference PRDs
- `14_qr_check_in.md`, `16_event_authoring.md`, `15_membership.md`, `18_onboarding.md`, `19_event_detail_ui.md`
