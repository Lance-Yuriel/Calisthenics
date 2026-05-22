# State Field Mapping

| Entity | Field | Values | Controls |
|--------|-------|--------|----------|
| Event | `state` | DRAFT…ARCHIVED, CANCELLED | UI tabs, RSVP, QR |
| Event | `capacity` | int ≥ 0 | GOING cap |
| User | `memberStatus` | pending, approved, rejected | Gate |
| RSVP | `status` | GOING, NOT_GOING, WAITLIST | + waitlist position |
| Config | `checkInOpenMinutes` | default 60 | QR window start |

## QR allowed when
```
state in (PUBLISHED, LIVE, CLOSED)
AND NOT CANCELLED
AND now >= startAt - checkInOpenMinutes
AND now <= endAt
```

## Events tab
- **Upcoming:** PUBLISHED, LIVE, CANCELLED (labelled)
- **Past:** CLOSED, ARCHIVED, old CANCELLED (optional: move archived off past)

## Home featured
- LIVE first, else soonest upcoming (not cancelled)
