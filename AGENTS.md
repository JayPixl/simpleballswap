## Agent skills

### Issue tracker

Issues and specs live as markdown files under `.scratch/<feature-slug>/` in this repo. See `docs/agents/issue-tracker.md`.

### Triage labels

Five canonical triage roles, label string equal to role name (`needs-triage`, `needs-info`, `ready-for-agent`, `ready-for-human`, `wontfix`), recorded as a `Status:` line in local issue files. See `docs/agents/triage-labels.md`.

### Domain docs

Single-context: one `GLOSSARY.md` plus `docs/adr/` at the repo root. See `docs/agents/domain.md`.

### Releasing

A pushed tag publishes to Modrinth and CurseForge. Agents run the release; the human only supplies the four repository secrets, play-tests both loaders, and edits listings after they are public. Read `docs/agents/releasing.md` before preparing, tagging, or re-running a release.

