# RSVP State Machine

```
(none) --> GOING | NOT_GOING | WAITLIST

GOING <--> NOT_GOING   (while editable)

GOING --[another user leaves]--> (waitlist promotion handled separately)

WAITLIST --[FIFO auto-promote when spot opens]--> GOING

(all locked when event is CLOSED | CANCELLED | ARCHIVED)
```

## Capacity gate
- `GOING` count < `capacity` → new RSVP can be `GOING`
- `GOING` count == `capacity` → new RSVP must be `WAITLIST` (unless changing from GOING to NOT_GOING)

## Fields
- `waitlistedAt` — Timestamp, set when status becomes `WAITLIST`
- `promotedAt` — optional, when auto-promoted to `GOING`
