# Membership & Auth

## Auth
- Google + email/password
- **Email required** on every account (see `18_onboarding.md`)

## Flow
- Sign up → `pending`
- Admin **approve** → `approved` + push + **email** (Firebase email template / Function)
- Admin **reject** → `rejected`; user may **re-apply** → `pending`

## Approval notifications
- **FCM push** (if token exists)
- **Email always** (to stored email — including Google users)

## Pending / rejected UI
- Pending: full-screen waiting (no tabs)
- Rejected: message + re-apply

## First admin
- Firebase console custom claim before launch

## Directory
- `17_member_directory.md`
