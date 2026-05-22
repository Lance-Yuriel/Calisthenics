# Attendance State Machine

## Per user + event
```
NOT_ATTENDED --[member QR scan | admin override]--> ATTENDED
ATTENDED --[duplicate QR scan]--> ATTENDED (no-op, show message)
```

## Preconditions for QR transition
- User: `memberStatus == approved`
- Event: `state in (PUBLISHED, LIVE, CLOSED)` and not `CANCELLED`
- Time: `now` between `startAt - 60min` and `endAt`

## Sources
- `qr` — member scan
- `admin` — manual override (walk-in, correction)

## Revocation
- Attendance is **not revoked** in MVP when badge revoked.
- Admin delete attendance: TBD (future)
