# Membership & Auth [DONE]

## Auth [DONE]
- Google + email/password
- **Email required** on every account (see `18_onboarding.md`)

## Flow [DONE]
- Sign up → `pending`
- Admin **approve** → `approved` + push + **email** (Firebase email template / Function)
- Admin **reject** → `rejected`; user may **re-apply** → `pending`

## Approval notifications [DONE]
- **FCM push** (if token exists)
- **Email always** (to stored email — including Google users)

## Pending / rejected UI [DONE]
- Pending: full-screen waiting (no tabs)
- Rejected: message + re-apply

## First admin [DONE]
- Firebase console custom claim before launch

## Directory [DONE]
- `17_member_directory.md`
