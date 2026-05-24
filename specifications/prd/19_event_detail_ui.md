# Event Detail (Member UI) [DONE]

## Header [DONE]
- Title, cover image (if any), date/time (club TZ), location (+ link if URL)
- State chip: Live, Cancelled, Closed, etc.

## RSVP (editable states only) [DONE]
- Buttons: **Going** | **Not going**
- When full and user not GOING: tap Going → **Waitlist** + show position (#N)
- When on waitlist: show position + **Not going** to leave waitlist
- When CLOSED / CANCELLED / ARCHIVED: RSVP read-only text

## Check-in [DONE]
- **Scan check-in** button if in QR time window + state allows
- Else disabled with reason (“Opens in 2h”, “Check-in closed”, “Cancelled”)

## Countdown (optional on detail) [DONE]
- To check-in open or to start

## Admin-only section [DONE]
- Lifecycle actions, edit, QR show/print, RSVP tables, attendance list
