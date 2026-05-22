# Security Rules Overview

## Authentication
- All writes require Firebase Auth.

## Members (`approved` only)
- Read: own `users` doc, published `events`, own `rsvps`, own `userBadges`, published `skills`, `notifications` (broadcast)
- Write: own profile fields; own `rsvps` when event allows RSVP
- **Cannot** write `attendance` directly (QR via Cloud Function or validated client + rules)
- **Pending / rejected:** read own user doc only; no other collections

## Admins
- Full read/write on `events`, `rsvps`, `attendance`, `badges`, `userBadges`, `skills`, `notifications`
- Approve/reject: update `users.memberStatus`
- Assign/revoke badges; manual attendance; `adminLogs` create

## Attendance
- Create via trusted path (Cloud Function recommended) validating event state + approved user
- Admin can create/update with `source: admin`

## Bootstrap
- First admin: Firebase custom claim `admin: true` set outside app

## Implementation note
- Enforce `memberStatus == approved` in rules using `get(/users/$(request.auth.uid))`
