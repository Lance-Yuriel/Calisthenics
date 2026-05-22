# Event Lifecycle

## States & transitions
(See `16_event_authoring.md` — unchanged.)

## Events tab lists

### Upcoming
- `PUBLISHED`, `LIVE`
- **`CANCELLED`** — stays here with **Cancelled** badge until **Archived**

### Past (members)
- **`CLOSED`** only
- **`CANCELLED`:** stays in **Upcoming** with badge until admin **Archives**
- **`ARCHIVED`:** **hidden** from all member lists

### Past / admin
- Admins see archived events in **admin panel** event list, not member Past tab.

## QR window
- `startAt − 60m` → `endAt`; see `14_qr_check_in.md`.
