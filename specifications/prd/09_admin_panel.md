# Admin Panel

Access: Profile → Admin (visible only if `role == admin`).

## Sections (MVP)
- **Pending members** — approve / reject queue
- **Events** — create/edit (`16_event_authoring.md`), publish, cancel, start early, close early, archive, show/print QR, RSVP/waitlist lists
- **Attendance** — per-event list; manual override
- **Notifications** — compose broadcast (reminders, updates, cancellations)
- **Badges** — catalog CRUD; assign to member; revoke with reason
- **Skills** — content CRUD (static reference)

## Logging
- Approve/reject member, manual attendance, badge assign/revoke → `adminLogs`

## Not in MVP admin UI
- Auto-badge rule editor
- Group management
- Analytics dashboard
