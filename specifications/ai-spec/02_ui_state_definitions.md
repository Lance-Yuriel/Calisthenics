# UI State Definitions

## Global
| State | When | UI |
|-------|------|-----|
| `loading` | Initial fetch | Skeleton or spinner |
| `unauthenticated` | No Firebase session | Login screen |
| `pending` | `memberStatus=pending` | Waiting approval (no tabs) |
| `rejected` | `memberStatus=rejected` | Rejected + re-apply |
| `authenticated` | Approved member | Main tabs |

## Home
| State | UI |
|-------|-----|
| `featured_live` | LIVE chip on featured card |
| `featured_upcoming` | Countdown to check-in open |
| `no_upcoming` | Empty card; admin: + Create event |
| `announcement_empty` | Hide announcement card |

## Events list
| State | UI |
|-------|-----|
| `upcoming_empty` | “No upcoming sessions” |
| `past_empty` | “No past sessions” |
| `cancelled_badge` | Red/grey “Cancelled” on row |

## Event detail
| State | RSVP UI |
|-------|---------|
| `editable` | Going / Not going buttons |
| `waitlisted` | “#N on waitlist” + Not going |
| `full_going` | Going disabled or → waitlist on tap |
| `locked` | Text only (Closed/Cancelled) |
| `checkin_open` | Scan button enabled |
| `checkin_closed` | Scan disabled + subtitle reason |

## QR scanner
| State | UI |
|-------|-----|
| `scanning` | Camera viewfinder |
| `success` | Success dialog → dismiss |
| `already` | Dialog “Already checked in” |
| `error` | **Modal** with reason (invalid QR, too early, cancelled, not approved) |

## Badges
| State | UI |
|-------|-----|
| `earned` | Full color |
| `unearned` | Greyed in catalog |

## Skills
| State | UI |
|-------|-----|
| `empty` | “No skills published yet” |

## Directory
| State | UI |
|-------|-----|
| `search_no_results` | “No members match” |
