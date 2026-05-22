# Events & RSVP

Events are the core unit of the app.

## RSVP model
- RSVP expresses **intent only** — not attendance.
- States: `GOING` | `NOT_GOING` | `WAITLIST`.
- **`capacity` is required** on every event (see `16_event_authoring.md`).
- Editable while event is **`PUBLISHED`** or **`LIVE`**.
- **Locked** when event is **`CLOSED`**, **`CANCELLED`**, or **`ARCHIVED`**.

## Member actions
- View upcoming/past events (exclude `DRAFT`; default hide `ARCHIVED`).
- RSVP: join `GOING`, join `WAITLIST` when full, or `NOT_GOING`.
- See waitlist position (optional UI — show “#3 on waitlist”).
- Open QR scanner when check-in allowed (not when `CANCELLED`).

## Admin actions
- Full event authoring and lifecycle (`16_event_authoring.md`, `10_event_state_lifecycle.md`).
- View RSVP lists: GOING, WAITLIST, NOT_GOING.

## Check-in
- See `14_qr_check_in.md` — RSVP **not required** for attendance.
- **Guest without account:** must sign up and be **approved** before attendance counts (`15_membership.md`).
