# Releasing

How a release is cut. Agents do the mechanics. The human supplies credentials, play-tests the build, and is the only one who can edit something after it is public.

## What only the human can do

**One-time — repository secrets** (Settings → Secrets and variables → Actions). Agents cannot read, verify, or create these:

| Secret | Value |
| --- | --- |
| `MODRINTH_ID` | Modrinth project ID (the ID, not the slug) |
| `MODRINTH_TOKEN` | Modrinth personal access token with version-create permission |
| `CURSEFORGE_ID` | CurseForge numeric project ID |
| `CURSEFORGE_TOKEN` | CurseForge API token |

If a publish step fails with an auth error, the human must rotate the secret. Ask them to update the repository secret; never ask for the value in chat.

**Per release — play-test** both loaders against the supported Cobblemon:

1. Shift + right-click a Pokémon while holding a different ball: the interaction wheel opens, the ball changes, the held ball is consumed (default config).
2. Repeat with the ball it already has: a chat message says so, and nothing is consumed.
3. Shift + right-click does not throw the ball; plain right-click still does.

An agent can build, launch both dev clients, and read logs for exceptions and mixin failures, but cannot click the wheel. If the human explicitly waives the play-test, an agent may release on build and log evidence, and must say the waiver out loud.

**After publishing — corrections.** Tokens live only in Actions, so agents cannot edit a live listing. An agent prepares the exact replacement text; the human pastes it into the platform UI.

**Approval.** If the human asked for this release, take it all the way to the tag without stopping. If an agent is proposing the release, get a go-ahead before pushing the tag. Everything before the tag is reversible.

## Agent procedure

1. **Version.** Pick the next SemVer. `mod_version` in `gradle.properties` must equal the tag without its `v`.
2. **Notes.** Add `## [x.y.z] - YYYY-MM-DD` to `CHANGELOG.md`, written for players: what changed for them, not commits, refactors, CI, or dependency bookkeeping. This text is published verbatim to all four listings. Prereleases use the base version's section (`v1.2.0-beta.1` reads `1.2.0`).
3. **Compatibility.** If the target Cobblemon changed, update the dependency range in `fabric/src/main/resources/fabric.mod.json` and `neoforge/src/main/resources/META-INF/neoforge.mods.toml`, and the `cobblemon_version` / Kotlin pins in `gradle.properties`.
4. **Verify.** Run `.\gradlew.bat build`, then `:fabric:runClient` and `:neoforge:runClient` on the supported Cobblemon. Grep both `latest.log` files for `Exception`, `Mixin apply failed`, and the mod id. Then get the human play-test above.
5. **Commit and push.**
6. **Tag and push the tag.** `git tag vX.Y.Z && git push origin vX.Y.Z`. The tag is the publish trigger.
7. **Watch the run.** `gh` is not installed; poll `https://api.github.com/repos/JayPixl/simpleballswap/actions/runs` with a User-Agent header, then the run's `/jobs` endpoint. Report the four publish outcomes from the steps, not just the job conclusion.

## Hard rules

- Publish to Modrinth and CurseForge only. No GitHub Releases, no Actions artifacts.
- One listing per loader per platform: four per release (see ADR 0001).
- A manual "Run workflow" builds and verifies only. It must never publish; it does not need changelog notes.
- Never generate notes from `git log`. Preflight fails if the section is missing; do not add a fallback.
- Never move, delete, or re-push a tag that has already published. Re-running a tag creates duplicate listings.
- Do not tag until both loaders pass verification.

## When a run fails

- **Preflight failure** (bad tag, version mismatch, missing secret, missing or empty changelog section): nothing was built or published. Fix the cause, then delete the tag locally and remotely if it was already pushed, and tag again.
- **A publish step failed after others succeeded**: some listings now exist. Do not re-run the tag. Report exactly which listings landed. Either delete those listings by hand and re-run, or cut a new patch version.
- **Build failure**: nothing published. Fix, push, move the tag's commit only if the tag has not published anything.

## Reference

- `.github/workflows/release.yml` — preflight, build, four publish steps, summary.
- `docs/adr/0001-per-loader-listings.md` — why four listings.
- `docs/adr/0002-curated-release-notes.md` — why notes are hand-written.
- `GLOSSARY.md` — release, release version, listing.

## Environment notes

- Windows development machine. Default `java` is 1.8; builds need JDK 21 via `JAVA_HOME` (`C:\Program Files\Eclipse Adoptium\jdk-21.0.2.13-hotspot`).
- Full local build is roughly 6 minutes; a release run is roughly 8–10 minutes including setup and publishing.
- First release, `v1.2.0`, shipped on 2026-10-26 with commit-log notes before this policy existed.
