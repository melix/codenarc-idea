# CodeNarc IntelliJ Plugin

## Version 7.0.4

New features:
- Compatible with IntelliJ IDEA 2025.2+ (based on IntelliJ Plugins v2):
  - Quick Fixes for CodeNarc violations ported to the new API
  - Automated tests reworked to use the new API
- [CodeNarc 3.6.0](https://github.com/CodeNarc/CodeNarc/releases/tag/v3.6.0)
  - Supports [Disabling rules from comments](https://codenarc.org/codenarc-configuring-rules.html#disabling-rules-from-comments)
- Groovy 4+
- Gradle 9.2.0
- Default run configuration for debugging the plugin in IDEA

## Running tests

Run all tests:
```bash
./gradlew clean test --info --stacktrace
```

Run a single specific test:
```bash
./gradlew clean test --tests "*UnnecessaryDotClassInspectionToolSpec" --info --stacktrace
```

## Running local IDEA

To run the plugin locally in IDEA:
```bash
./gradlew runIde
```

## Build

To build the plugin run `./gradlew clean buildPlugin`.
Take the file from `build/distributions/codenarc-idea-7.0.4.zip` and install it in IDEA.

## Upgrading CodeNarc

1. Change the CodeNarc version in `build.gradle`
2. Regenerate the IDEA inspection classes from CodeNarc with `./gradlew run`
   - existing inspection classes manual removal from `org.codenarc.idea.inspections` might be required
3. Test `./gradlew test --info --stacktrace`
4. Build `./gradlew buildPlugin`
5. Take the file from `build/distributions/codenarc-idea-7.0.4.zip` and install it in IDEA.

## History

This is an update to this project:

https://github.com/seanleblanc/CodeNarc-Updated

which was an update to this project:

https://github.com/melix/CodenarcNG

which was a fork the original project.

CodeNarc-Updated will be replaced with this plugin.

## Known Issues

There is [a random issue with `StackOverflowError`](https://github.com/melix/codenarc-idea/issues/45) which should have no impact on the IDE.


## Beta Channel
Feel free to join the beta channel here: https://plugins.jetbrains.com/plugins/beta/5925

![add custom repositories](static/images/add-repository-1.png)

![add custom repository](static/images/add-repository-2.png)