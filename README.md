# Changelists Settings UI Test

[![UI Tests](https://github.com/Mitya139/SDET_test_task/actions/workflows/ui-tests.yml/badge.svg)](https://github.com/Mitya139/SDET_test_task/actions/workflows/ui-tests.yml)

This project contains an IntelliJ Platform UI integration test for the following scenario:

1. Start PyCharm with a public GitHub project.
2. Open Settings.
3. Navigate to Version Control > Changelists.
4. Enable `Create changelists automatically`.
5. Verify that the checkbox is selected.
6. Confirm the dialog with OK.

The test is implemented with JetBrains IDE Starter and Driver SDK.

## Implementation Notes

- The test opens a pinned PyCharm build and a public GitHub project pinned to a specific commit, which keeps the scenario reproducible.
- UI elements are located through JetBrains Driver SDK components, while checkbox, button, and keyboard interactions are performed as UI actions rather than by mutating Swing state directly.
- CI and Docker run the test inside Xvfb because IntelliJ Platform UI tests need a non-headless AWT/Swing environment.
- The first run downloads Gradle dependencies, IDE Starter artifacts, PyCharm, and the test project, so it requires network access and can take several minutes.

## Design Choices

- Settings are opened with the IDE shortcut to stay close to the real user scenario.
- The test project is imported with `prepareProjectCleanImport()` to avoid leaking IDE project files between runs.
- The checkbox is selected with a Driver SDK click only when it is not already selected, then the selected state is verified explicitly.
- CI uploads Gradle reports and IDE Starter artifacts on every run because UI test failures usually need logs and snapshots for diagnosis.

## Versions

- Kotlin: 2.3.0
- JDK: 25
- IntelliJ Starter / Driver SDK: 262.4852.50
- Tested IDE: PyCharm 262.4852.46
- Test project commit: 55cfb0e6021d86e957025bc40d8de3b0cb686e99

## Run Locally

Requirements:

- JDK 25
- A desktop session that can show Swing UI

```bash
./gradlew test
```

On Windows:

```powershell
.\gradlew.bat test
```

## Run Published Docker Image

The Docker image runs the test inside Xvfb.

It can be run with:

```bash
docker run --rm mitya139/changelists-settings-ui-test:latest
```

## Build Docker Image Locally

You can also build the same image from this repository:

```bash
docker build -t changelists-settings-ui-test .
docker run --rm changelists-settings-ui-test
```

The first run downloads Gradle dependencies, the IDE Starter artifacts, PyCharm, and the test project. It can take several minutes.

To reuse Gradle and IDE downloads between runs:

```bash
docker run --rm \
  -v gradle-cache:/home/tester/.gradle \
  changelists-settings-ui-test
```

## CI

The repository includes a GitHub Actions workflow that runs the same UI test on Ubuntu with Xvfb:

```text
.github/workflows/ui-tests.yml
```

The workflow uploads Gradle test reports and IDE Starter logs as artifacts on every run, including failed runs.