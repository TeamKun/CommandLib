# Claude Code Instructions

This repository contains the CommandLib library itself.

For repository maintenance, bug fixes, tests, and implementation work, read:

- `docs/agents/repository.md`
- `docs/agents/testing.md`
- `docs/agents/bukkit-test.md` when touching `spigot-testing`

Do not treat `.claude/skills/commandlib` as repository-maintenance instructions.
That skill is for downstream library users who want to generate or understand
CommandLib usage code.

Prefer focused Gradle verification for touched modules. Do not rewrite unrelated
code, generated files, or user changes.
