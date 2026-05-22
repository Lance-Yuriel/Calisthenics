# Onboarding & Auth requirements

## Email required
- Every account must have a **verified email** stored on `users` doc.
- **Google sign-in:** require email scope; block signup if email missing.
- **Email/password:** standard verification flow.
- **Future OIDC** (Facebook, Instagram, etc.): must return email or **block signup** with message to use Google/email.

## First open after approval
- One screen (dismissible):
  - Title: “Welcome to [Club name]”
  - Body: RSVP on **Events**, check in by scanning **QR** at the session (opens 60 min before start).
  - Button: **Got it** → Home
- Shown once per user (`hasSeenOnboarding: true` on user doc).

## Pending screen
- Message: application under review.
- No navigation tabs (or disabled).

## Rejected screen
- Message: not approved; option **Apply again** → `pending`.
