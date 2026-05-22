# Event Authoring (Admin)

## Create flow
1. **New event** → `DRAFT`.
2. **Required:** `title`, `startAt`, `endAt`, `capacity` (integer ≥ 0).
3. **Optional:** `description`, `location`, `locationUrl`, `coverImage`.
4. Times in **club local timezone** (fixed app config, e.g. `Pacific/Auckland`).
5. Validate: `endAt` > `startAt`.
6. **Save draft** or **Publish** → `PUBLISHED` + QR generated.

## Capacity & waitlist
- `GOING` ≤ `capacity` (capacity **0** = all RSVPs go to `WAITLIST` or only NOT_GOING).
- Full → new RSVP = `WAITLIST` (FIFO by `waitlistedAt`).
- Spot frees → auto-promote oldest `WAITLIST` → `GOING` + notify.
- **Increase capacity** → auto-promote `WAITLIST` FIFO until cap filled or waitlist empty.
- **Decrease capacity** → auto-demote newest `GOING` → `WAITLIST` + notify.

## RSVP member behavior
- Clear RSVP → **delete document** (no response).
- Leave waitlist → set **`NOT_GOING`**.
- Show waitlist **position** to member (e.g. “#3”).

## Edit after publish
- Editable until **`ARCHIVED`**.
- Change `startAt`, `endAt`, or `location` → notify **`GOING` + `WAITLIST`**.

## Cancel
- From **`PUBLISHED`** or **`LIVE` only** (not from `CLOSED` — use **Archive**).
- → `CANCELLED`; QR off; RSVP locked; notify `GOING` + `WAITLIST`.
- Stays on main list with **Cancelled** label until admin **Archives**.

## Archive paths
- `CLOSED` → `ARCHIVED` (manual)
- `CANCELLED` → `ARCHIVED` (manual)
- `CLOSED` cannot → `CANCELLED`

## Delete
- **`DRAFT` only**

## Admin actions
- Publish, Start early, Close early, Cancel, Archive
- Show/Print QR, RSVP lists, attendance override
