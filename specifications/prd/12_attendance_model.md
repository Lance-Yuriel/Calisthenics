# Attendance

## Truth source
- **Primary:** QR check-in (time-windowed — see `14_qr_check_in.md`)
- **Secondary:** Admin override for **approved** members only

## Check-in window (QR)
- From **60 minutes before `startAt`** until **`endAt`**
- Event states allowed: `PUBLISHED`, `LIVE`, `CLOSED` (not `CANCELLED`, `DRAFT`, `ARCHIVED`)

## Rules
- RSVP optional.
- One record per (`userId`, `eventId`); duplicate QR = idempotent.
- **No guest / walk-in credit** without approved account.
- Admin override: any approved member + `adminLogs`.

## Record fields
- `userId`, `eventId`, `source` (`qr` | `admin`), `checkedInAt`, optional `note`, `createdByAdminId`

## Offline
- Queue + sync; server dedupes.
