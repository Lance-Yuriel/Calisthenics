# Event State Machine

```
DRAFT --[admin: Publish]--> PUBLISHED
PUBLISHED --[auto: startAt | admin: Start early]--> LIVE
LIVE --[auto: endAt | admin: Close early]--> CLOSED
CLOSED --[admin: Archive]--> ARCHIVED

PUBLISHED --[admin: Cancel]--> CANCELLED
LIVE --[admin: Cancel]--> CANCELLED
```

## Transition rules
- No backward transitions in MVP (except cancel terminal).
- `DRAFT` may be **deleted**; other states use Cancel or Archive.
- Auto LIVE/CLOSED via scheduled job on `startAt` / `endAt`.

## Side effects
| Transition | Side effects |
|------------|----------------|
| → `PUBLISHED` | Visible to members; RSVP/waitlist open; QR valid |
| → `LIVE` | UI “live now” indicator |
| → `CLOSED` | RSVP locked |
| → `CANCELLED` | RSVP locked; QR disabled; notify GOING + WAITLIST |
| → `ARCHIVED` | Hidden from default lists; QR invalid |

## Waitlist side effect
- On `GOING` → `NOT_GOING` while spots available: Cloud Function promotes oldest `WAITLIST` → `GOING`.
