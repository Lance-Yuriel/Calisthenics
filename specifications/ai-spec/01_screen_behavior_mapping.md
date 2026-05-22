# Screen Behavior Mapping

## Home
- Featured next event (nearest `startAt` where state in PUBLISHED/LIVE)
- RSVP chip for that event
- Countdown to `startAt`
- Quick stats (attendance count, badge count)
- Latest announcement / notification

## Events
- List filtered by state (exclude DRAFT, ARCHIVED default)
- Detail: RSVP controls, check-in CTA when QR allowed
- Pending users: not reachable (gated at root)

## QR scan
- Camera → validate → success | already checked in | error

## Badges
- Catalog + earned state per badge

## Profile
- Stats, badges, settings
- Admin entry if `role == admin`

## Admin
- Pending members, events, attendance, notifications, badges, skills

See `data-model/05_state_field_mapping.md` for state-driven UI.
