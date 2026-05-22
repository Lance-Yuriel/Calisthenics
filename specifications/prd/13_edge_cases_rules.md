# Edge Cases

## QR check-in
- **Duplicate scan:** idempotent; show "Already checked in".
- **No RSVP:** allow check-in for any approved member.
- **DRAFT / ARCHIVED / CANCELLED scan:** reject with clear error.
- **Pending member scan:** reject.
- **Offline:** queue locally; server dedupes by `userId` + `eventId`.

## RSVP & waitlist
- **Race at capacity:** Firestore transaction; one writer gets GOING, other gets WAITLIST.
- **FIFO promotion:** single Function handles promotion per freed slot.
- **Capacity lowered below GOING count:** auto-move newest `GOING` → `WAITLIST` until within cap; notify affected members.

## Attendance
- **No account / not approved:** no attendance credit in MVP.
- **Approved walk-in:** QR (in time window) or admin override anytime.

## Events
- **Cancel vs archive:** Cancel = terminal + notify; Archive = hide from default list.
- **Edit time/location:** notify GOING.
- **Delete:** DRAFT only.

## Membership
- **Duplicate approval:** idempotent.

## Badges
- **Duplicate assign:** idempotent.
- **Revoke then re-assign:** allowed; logged.

## Offline (general)
- RSVP and check-in queue; server timestamp wins on conflict.
