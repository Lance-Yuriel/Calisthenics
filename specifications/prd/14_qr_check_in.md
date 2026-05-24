# QR Check-in [DONE]

## Model [DONE]
- QR created when admin **publishes** (`DRAFT` has no QR).
- QR encodes: `eventId` (static for life of event).
- Same code for in-app **Show QR**.

## Check-in time window [DONE]
- Allowed only when **now** is between:
  - **`startAt` − 60 minutes** and **`endAt`**
- And `event.state` is **`PUBLISHED`**, **`LIVE`**, or **`CLOSED`**
- And **not** `CANCELLED`, `DRAFT`, or `ARCHIVED`

> Note: Check-in is **not** allowed weeks early while merely `PUBLISHED`; the 60-minute rule gates access.

## Member flow [DONE]
1. Open **Scan QR** from **home** or **event detail**.
2. Parse `eventId`; member must be **authenticated** and **`approved`**.
3. Server validates time window + event state.
4. Create attendance (`source: qr`) or show **“Already checked in”**.

## Admin flow [DONE]
- **Show QR** (full-screen) + **Print / Download PDF** from event detail (any admin).

## Rules [DONE]
- RSVP **not required**.
- **No attendance** for non-members / pending / rejected — must sign up and be approved first (no retroactive guest credit in MVP).
- **Offline:** queue scan; server dedupes `userId` + `eventId`.

## Admin manual attendance [DONE]
- Any **approved** member; optional note; `source: admin`; log `adminLogs`.
- Not limited to RSVP `GOING`; not limited to check-in window (admin correction allowed).

## Security (MVP) [DONE]
- Static QR; physical poster trust model.
