# Admin Panel

Access: Profile → Admin (visible only if `role == admin`).

## Sections (MVP)
- **Pending members** [DONE] — approve / reject queue
- **Events** [DONE] — create/edit, publish, cancel, start early, close early, archive, show QR.
- **Skills** [DONE] — content CRUD (static reference)
- **Attendance** — per-event list; manual override
- **Notifications** — compose broadcast (reminders, updates, cancellations)
- **Badges** — catalog CRUD; assign to member; revoke with reason

## Logging
- Approve/reject member, manual attendance, badge assign/revoke → `adminLogs`

## Not in MVP admin UI
- Auto-badge rule editor
- Group management
- Analytics dashboard
