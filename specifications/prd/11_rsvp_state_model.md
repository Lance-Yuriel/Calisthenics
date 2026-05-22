# RSVP State Model

## Status values
- `GOING` — confirmed spot (counts toward `capacity`)
- `NOT_GOING` — declined
- `WAITLIST` — no spot available; ordered queue

## Per user + event
```
(no document) --[member: GOING | NOT_GOING | WAITLIST]--> status set
GOING <--> NOT_GOING (while RSVP editable)
GOING --[capacity full + new RSVP]--> (n/a — new users get WAITLIST)
WAITLIST --[spot opens: FIFO auto-promote]--> GOING
```

## Capacity rules
- `GOING` count must never exceed `event.capacity`.
- When `GOING` decreases, promote **oldest** `WAITLIST` by `waitlistedAt` (Cloud Function recommended).

## Editable when
- `event.state` is `PUBLISHED` or `LIVE`
- `user.memberStatus` is `approved`

## Locked when
- `event.state` is `CLOSED`, `CANCELLED`, or `ARCHIVED`
- User is `pending` or `rejected`

## Notifications on promotion
- Waitlist → GOING: push + in-app “Spot opened” message.
