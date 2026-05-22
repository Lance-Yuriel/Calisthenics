# Firestore Schema (MVP)

## Collections

### `users/{userId}`
- `email` (required), `displayName`, `photoUrl`, `bio` (max 280)
- `hasSeenOnboarding`: boolean
- `role`: `member` | `admin`
- `memberStatus`: `pending` | `approved` | `rejected`
- `fcmTokens`: string[]
- `stats`: { `attendanceCount`, `badgeCount` } (denormalized, optional)
- `createdAt`, `updatedAt`

### `events/{eventId}`
- `title`, `description`, `location`, `locationUrl` (optional)
- `startAt`, `endAt` (Timestamp)
- **`capacity`**: number (required)
- `coverImageUrl` (optional)
- `state`: `DRAFT` | `PUBLISHED` | `LIVE` | `CLOSED` | `CANCELLED` | `ARCHIVED`
- `qrPayload`: string (static)
- `cancelledAt`, `cancelReason` (optional, when CANCELLED)
- `createdBy`, `createdAt`, `updatedAt`

### `rsvps/{rsvpId}` (doc id: `{eventId}_{userId}`)
- `eventId`, `userId`
- `status`: `GOING` | `NOT_GOING` | `WAITLIST`
- `waitlistedAt` (when WAITLIST)
- `updatedAt`

### `attendance/{attendanceId}` (doc id: `{eventId}_{userId}`)
- `eventId`, `userId`
- `source`: `qr` | `admin`
- `checkedInAt` (server Timestamp)
- `createdByAdminId` (optional)

### `badges/{badgeId}` (catalog)
- `name`, `description`, `imageUrl`
- `active`: boolean

### `userBadges/{docId}` (doc id: `{userId}_{badgeId}`)
- `userId`, `badgeId`
- `earnedAt`, `awardedByAdminId`, `note`
- `revokedAt` (optional), `revokedByAdminId`, `revokeReason`

### `skills/{skillId}`
- `name`, `category`, `difficultyTag`, `description`, `imageUrl`, `videoUrl`, `order`, `published`

### `notifications/{notificationId}`
- `title`, `body`, `type`, `eventId` (optional), `userId` (optional target)
- `createdAt`, `createdByAdminId`

### `adminLogs/{logId}`
- `actorId`, `action`, `targetType`, `targetId`, `reason`, `createdAt`

### `config/club` (singleton)
- `timezone` (IANA string)
- `name`, `logoUrl`
- `checkInOpenMinutes` (default 60)

### `notifications/{id}` fields
- `type`: `announcement` | `event_reminder` | `event_update` | `event_cancelled` | `waitlist_promoted` | `member_approved`
