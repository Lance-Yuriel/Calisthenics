# Event Authoring (Admin) [DONE]

## Create flow [DONE]
1. **New event** → `PUBLISHED` (MVP defaults to direct publish).
2. **Required:** `title`, `startAt`, `endAt`, `capacity` (integer ≥ 0).
3. **Optional:** `description`, `location`, `locationUrl`, `coverImage`.
4. Image upload to Firebase Storage supported.
5. Times in device local timezone (MVP).
6. **Publish** → `PUBLISHED` + QR generated.

## Capacity & waitlist [DONE]
- `GOING` ≤ `capacity` (capacity **0** = all RSVPs go to `WAITLIST` or only NOT_GOING).
- Full → new RSVP = `WAITLIST` (FIFO by `waitlistedAt`).
- Spot frees → auto-promote oldest `WAITLIST` → `GOING` + notify.
- **Increase capacity** → auto-promote `WAITLIST` FIFO until cap filled or waitlist empty.
- **Decrease capacity** → auto-demote newest `GOING` → `WAITLIST` + notify.

## RSVP member behavior [DONE]
- Clear RSVP → **delete document** (no response).
- Leave waitlist → set **`NOT_GOING`**.
- Show waitlist **position** to member (e.g. “#3”).

## Edit after publish [DONE]
- Editable until **`ARCHIVED`**.
- Change `startAt`, `endAt`, or `location` → notify **`GOING` + `WAITLIST`**.

## Cancel [DONE]
- From **`PUBLISHED`** or **`LIVE` only** (not from `CLOSED` — use **Archive**).
- → `CANCELLED`; QR off; RSVP locked; notify `GOING` + `WAITLIST`.
- Stays on main list with **Cancelled** label until admin **Archives**.

## Archive paths [DONE]
- `CLOSED` → `ARCHIVED` (manual)
- `CANCELLED` → `ARCHIVED` (manual)
- `CLOSED` cannot → `CANCELLED`

## Delete [DONE]
- **`DRAFT` only**

## Admin actions [DONE]
- Publish, Start early, Close early, Cancel, Archive
- Show/Print QR, RSVP lists, attendance override
