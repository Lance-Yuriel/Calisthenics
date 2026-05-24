# RSVP State Model [DONE]

## Status values [DONE]
- `GOING` — confirmed spot (counts toward `capacity`)
- `NOT_GOING` — declined
- `WAITLIST` — no spot available; ordered queue

## Per user + event [DONE]
```
(no document) --[member: GOING | NOT_GOING | WAITLIST]--> status set
GOING <--> NOT_GOING (while RSVP editable)
GOING --[capacity full + new RSVP]--> (n/a — new users get WAITLIST)
WAITLIST --[spot opens: FIFO auto-promote]--> GOING
```

## Capacity rules [DONE]
- `GOING` count must never exceed `event.capacity`.
- When `GOING` decreases, promote **oldest** `WAITLIST` by `waitlistedAt` (Cloud Function recommended).

## Editable when [DONE]
- `event.state` is `PUBLISHED` or `LIVE`
- `user.memberStatus` is `approved`

## Locked when [DONE]
- `event.state` is `CLOSED`, `CANCELLED`, or `ARCHIVED`
- User is `pending` or `rejected`

## Notifications on promotion [DONE]
- Waitlist → GOING: push + in-app “Spot opened” message.
