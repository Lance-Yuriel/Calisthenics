# Notifications

## Inbox access
- **Home:** bell icon → inbox screen
- **Profile:** Notifications row → same inbox

## Inbox UI
- Chronological list (newest first)
- Shared broadcast docs — **no read state / no badge count** in MVP
- Tap item → detail (title, body, optional event link)

## Types & audiences
(Unchanged — see previous table.)

## Email (approval)
- Always send on `member_approved` via Cloud Function + email provider / Firebase extension.

## Home
- Latest `announcement` card only (not whole inbox).
