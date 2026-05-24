# Event Lifecycle [DONE]

## States & transitions [DONE]
(See `16_event_authoring.md` — unchanged.)

## Events tab lists [DONE]

### Upcoming [DONE]
- `PUBLISHED`, `LIVE`
- **`CANCELLED`** — stays here with **Cancelled** badge until **Archived**

### Past (members) [DONE]
- **`CLOSED`** only
- **`CANCELLED`:** stays in **Upcoming** with badge until admin **Archives**
- **`ARCHIVED`:** **hidden** from all member lists

### Past / admin [DONE]
- Admins see archived events in **admin panel** event list, not member Past tab.

## QR window [DONE]
- `startAt − 60m` → `endAt`; see `14_qr_check_in.md`.
