# Cross-System Rules

- **RSVP ≠ attendance** — RSVP is intent; attendance is truth via QR or admin.
- **QR = attendance truth** for member check-in (static event QR).
- **Capacity** — `GOING` ≤ `capacity`; overflow → `WAITLIST`; FIFO auto-promote on free slot.
- **Badges (MVP):** admin-assigned only.
- **Membership gate:** only `approved` users can RSVP, scan QR, earn badges.
- **Guests:** no attendance without approved account.
- **Event state gates:**
  - RSVP editable: `PUBLISHED`, `LIVE`
  - QR valid: state in `PUBLISHED`/`LIVE`/`CLOSED` AND time in [`startAt−60m`, `endAt`] (not `CANCELLED`)
  - Cancelled: notify `GOING` + `WAITLIST`; QR off
- **Notifications:** push + in-app; auto 24h reminder → `GOING` only.
- **Admin actions** → `adminLogs`.
