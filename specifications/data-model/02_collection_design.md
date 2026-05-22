# Collection Design

## Decision (MVP)
- **Top-level collections** with composite doc IDs (not subcollections):
  - `rsvps/{eventId}_{userId}`
  - `attendance/{eventId}_{userId}`

## Indexes
- `events`: `state` + `startAt`
- `rsvps`: `eventId` + `status`
- `notifications`: `createdAt` desc, `type`

## Club config
- `config/club`: `timezone`, `name`, `logoUrl`, `checkInOpenMinutes` (default 60)
