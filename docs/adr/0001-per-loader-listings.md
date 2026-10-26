# One listing per mod loader, not one combined listing

A release produces two jars (Fabric, NeoForge) but each distribution platform attaches loader metadata per version, not per file. We publish one listing per mod loader per platform — four listings per release — so a player filtering for Fabric or NeoForge sees exactly one downloadable jar and no ambiguity about which file to install.

**Considered options**: a single version per platform carrying both jars and both loaders. Rejected because Modrinth's launcher cannot map a file to a loader when a version declares several, and CurseForge would show a Fabric jar to NeoForge users under the same version. Also rejected: a separate pipeline per platform, which would let one platform's release land without the other.

**Consequences**: each release makes four publish calls, partial failure is possible and is reported rather than rolled back, and re-running a tag creates duplicates that must be deleted by hand.
