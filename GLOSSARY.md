# Simple Ball Swap

A Cobblemon addon for Minecraft 1.21.1, shipped for two mod loaders and distributed through two platforms.

## Language

**Release**:
One version of the mod made available to players, identified by a single git tag.
_Avoid_: build, deploy, publish

**Release version**:
The version number identifying a release, written without a `v` prefix. The tag `v1.3.0` denotes release version `1.3.0`.
_Avoid_: tag number, build number, mod version

**Mod loader**:
The runtime that loads the mod into Minecraft. Simple Ball Swap ships for two: Fabric and NeoForge.
_Avoid_: platform, modding API, flavor

**Distribution platform**:
A site where players obtain releases. Simple Ball Swap publishes to two: Modrinth and CurseForge.
_Avoid_: store, repository, marketplace

**Listing**:
One release version published for one mod loader on one distribution platform. A release produces four listings: two mod loaders × two distribution platforms.
_Avoid_: post, entry, version (when a listing is meant)

**Changelog**:
The hand-written, player-facing notes for a release, kept in `CHANGELOG.md`. The section for a release version is published verbatim to every listing.
_Avoid_: commit log, generated release notes

