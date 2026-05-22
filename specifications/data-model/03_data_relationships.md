# Data Relationships

```
User 1--* RSVP *--1 Event
User 1--* Attendance *--1 Event
User 1--* UserBadge *--1 Badge (catalog)
User 1--* AdminLog (as actor)
Admin *--* Event (createdBy)
```

## Key separations
- RSVP and Attendance are **independent** — no foreign key from RSVP to Attendance.
- Badge awards do **not** require attendance in MVP.
- Membership (`memberStatus`) gates all member-facing relations.
