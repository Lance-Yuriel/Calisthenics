# Badge State Machine (per user + badge)

## MVP (admin-driven)
```
NOT_EARNED --[admin: assign]--> EARNED
EARNED --[admin: revoke + reason]--> NOT_EARNED
```

## Catalog badge (definition)
- `active` | `archived` — admin can retire badge from catalog without deleting awards.

## Future (auto badges)
```
NOT_EARNED --[attendance rule met]--> EARNED
```

Auto rules deferred post-MVP (see `prd/06_badges_system.md`).
