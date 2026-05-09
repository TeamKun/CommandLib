# Adding Arguments

Use this checklist whenever adding a public `*Argument` class.

## 1. Decide Platform Scope

- Confirm the target modules: `common`, `spigot`, `paper`, `forge`, or a subset.
- Prefer a shared public concept only when the return type and accepted syntax are stable across platforms.
- Keep Spigot-only NMS or Bukkit internals in `spigot`.
- Use Paper official `ArgumentTypes` in `paper` when available.
- Use Forge/Minecraft native argument types in `forge` when available.

## 2. Implement The Argument

- Add the public argument class under each target module's `argument` package.
- For Spigot NMS-backed arguments, add:
    - an abstract `NMSArgumentXxx` wrapper,
    - version implementations under `util/nms/argument/v...`,
    - registration ranges in the wrapper static block.
- Convert native Minecraft/NMS values into the public Bukkit/Paper/Forge return type at the module boundary.
- Keep command parsing behavior consistent with Brigadier where possible, including namespace defaults such as
  `minecraft`.
- Avoid `StringArgumentType.greedyString()` unless the argument is intentionally terminal. Greedy arguments consume the
  remaining input and make later arguments impossible or misleading for command trees and completions. For a single
  token that must allow characters rejected by `StringArgumentType.word()` (for example `/` or `@`), use or add a raw
  single-token argument type instead.
- Paper's official command API rejects unknown plain Brigadier `ArgumentType` implementations during lifecycle
  registration. Paper custom argument types must implement Paper's `CustomArgumentType` and expose a known native type
  through `getNativeType()`.
- Registry-backed Bukkit/Paper values should be designed around Paper-native registry key semantics when possible.
  Prefer `ArgumentTypes.resource(RegistryKey...)` for Paper value-returning arguments, and make Spigot match the same
  public input/completion behavior where feasible. Use `resourceKey(...)` only when the public return type is intended
  to be a key (`TypedKey`) rather than the registry value.
- Arguments backed by server collections or registries should provide default completions. Examples include
  advancements, recipes, loot tables, scoreboard objectives, and other registry-backed values.
- If an argument naturally depends on a mutable server object such as a `Scoreboard`, prefer a fluent method such as
  `scoreboard(Supplier<Scoreboard>)` over constructor overloads so existing construction style remains simple and
  extensible.

## 3. Add Lightweight Tests

- Add a focused `XxxArgumentTest` in the matching testing module.
- For Spigot NMS-backed arguments, add `MockNMSArgumentXxx` in `spigot-testing/src/main/java/.../nms/argument`.
- Register shared Spigot NMS mocks in `spigot-testing`'s `CommandTester.defaultNmsMocks()`.
- For Paper native `ArgumentTypes`, update `paper-testing`'s `CommandTester` static stubs.
- Update coverage maps/tests so a new public argument cannot be added without a test:
    - `spigot-testing/src/test/.../ArgumentCoverageTest.java`
    - `paper-testing/src/test/.../ArgumentCoverageTest.java`

## 4. Add Integration Coverage

- Add an execution case to `integration-test/fixtures/common-bukkit/.../ArgumentTest.java` when the argument exists in
  both Spigot and Paper modules.
- For platform-specific arguments, add a reflected optional case in the same fixture so it runs only when that module
  contains the argument.
- `:integration-test:test` includes a lightweight coverage check that fails when a public argument class shared by
  Spigot and Paper is missing from the common Bukkit integration fixture. Add an explicit exclusion there only when a
  real-server case is intentionally not applicable.
- The integration case should:
    - register the argument,
    - execute a real command with representative input,
    - assert the parsed return value through `putResult(...)`,
    - attach an uncaught exception handler that records stack traces.
- If the argument depends on real server registries or command dispatch internals, prefer integration coverage over only
  mock-based tests.
- When adding an integration command for an old target, check whether that target's bot library can decode the
  Brigadier parser in `ServerDeclareCommandsPacket`. If the parser is valid for Minecraft but unsupported by
  MCProtocolLib, do not register that case for the affected target; otherwise the bot can disconnect before the test
  runner gets to execute any commands.

## 5. Update Docs

- Update `docs/design/argument-catalog.md` status and priority.
- Update `docs/roadmap.md` when the roadmap explicitly names the argument.
- Add user-facing docs later if the argument changes public adoption guidance.

## 6. Verify

Run focused checks for touched modules. Typical commands:

```bash
./gradlew :spigot:compileJava :spigot-testing:test
./gradlew :paper:compileJava :paper-testing:test
./gradlew :forge:compileJava
```

For integration fixture source changes, compile representative fixtures:

```bash
cd integration-test/fixtures/1.16.5-paper && ./gradlew compileJava
cd integration-test/fixtures/1.21.0-paper && ./gradlew compileJava
```

Run real Minecraft integration tests when the change depends on server runtime behavior or when requested.
