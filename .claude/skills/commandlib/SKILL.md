---
description: Load CommandLib API from the Gradle source cache and generate or explain CommandLib code. Usage: /commandlib <request>
allowed-tools: Bash Read Glob Grep
---

## Step 1 — Extract CommandLib sources

Run the extraction script:

```bash
bash .claude/skills/commandlib/scripts/extract-sources.sh
```

The script prints one of:
- A JAR path → sources were extracted to `/tmp/commandlib-sources/`
- `LOCAL_BUILD` → CommandLib source tree is available locally at the project root

## Step 2 — Read the key source files

**If extracted JAR** (base path: `/tmp/commandlib-sources/`):

- `README.md`
- `net/kunmc/lab/commandlib/CommandLib.java`
- `net/kunmc/lab/commandlib/Command.java`
- `net/kunmc/lab/commandlib/CommandContext.java`
- `net/kunmc/lab/commandlib/CommonCommand.java`
- `net/kunmc/lab/commandlib/AbstractArgumentBuilder.java`
- `net/kunmc/lab/commandlib/ArgumentBuilder.java`
- `net/kunmc/lab/commandlib/CommonArgument.java`
- All `.java` files under `net/kunmc/lab/commandlib/argument/`

**If LOCAL_BUILD** (base path: project root):

- `README.md`
- `bukkit/src/main/java/net/kunmc/lab/commandlib/CommandLib.java`
- `bukkit/src/main/java/net/kunmc/lab/commandlib/Command.java`
- `bukkit/src/main/java/net/kunmc/lab/commandlib/CommandContext.java`
- `common/src/main/java/net/kunmc/lab/commandlib/CommonCommand.java`
- `common/src/main/java/net/kunmc/lab/commandlib/AbstractArgumentBuilder.java`
- `bukkit/src/main/java/net/kunmc/lab/commandlib/ArgumentBuilder.java`
- `common/src/main/java/net/kunmc/lab/commandlib/CommonArgument.java`
- All `.java` files under `bukkit/src/main/java/net/kunmc/lab/commandlib/argument/`

## Step 3 — Apply conventions

Read and apply the conventions in `.claude/skills/commandlib/references/conventions.md`.

## Step 4 — Respond to the request

Respond in the same language as the user's request.
If asked to generate code, output a complete working implementation.
If asked how to do something, explain with a code example.

$ARGUMENTS
