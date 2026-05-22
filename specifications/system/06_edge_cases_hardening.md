# Edge Cases & Hardening

## QR / time window
- Before `startAt − 60m` or after `endAt`: reject with “Check-in not open yet” / “Check-in closed”.
- `PUBLISHED` weeks ahead: blocked by time window.

## RSVP / capacity
- Capacity **0**: all `GOING` attempts → `WAITLIST` or NOT_GOING only.
- Race at capacity: transaction; one wins `GOING`.
- Shrink/increase capacity: auto demote/promote with notifications.

## Membership
- Rejected → re-apply → `pending` (not duplicate accounts).
- Pending cannot scan QR or appear in directory.

## Notifications
- Shared inbox: new member sees all history; no read state.

## Concurrency
- Attendance doc id `{eventId}_{userId}`.
- RSVP doc id `{eventId}_{userId}`.

## Cloud Functions (required)
- Auto `LIVE` / `CLOSED` on schedule
- Waitlist FIFO on GOING drop or capacity increase
- QR check-in validation (recommended)
- 24h reminder to GOING
- Optional: capacity shrink on event edit
