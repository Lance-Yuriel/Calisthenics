# Admin Panel [DONE]

Access: Profile → Admin (visible only if `role == admin`).

## Sections (MVP) [DONE]
- **Pending members** — approve / reject queue
- **Events** — create/edit, publish, cancel, start early, close early, archive, show QR.
- **Skills** — content CRUD (static reference)
- **Attendance** — per-event list; manual override
- **Notifications** — compose broadcast (reminders, updates, cancellations)
- **Badges** — catalog CRUD; assign to member; revoke with reason

## Logging [DONE]
- Approve/reject member, manual attendance, badge assign/revoke → `adminLogs`

## Not in MVP admin UI [DONE]
- Auto-badge rule editor
- Group management
- Analytics dashboard
