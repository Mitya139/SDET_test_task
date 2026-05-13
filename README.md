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
- UI actions are performed through JetBrains Driver SDK components. The SDK locates Swing components and performs robot-backed keyboard and mouse actions instead of changing component state directly.
- CI and Docker run the test inside Xvfb because IntelliJ Platform UI tests need a non-headless AWT/Swing environment.
- The first run downloads Gradle dependencies, IDE Starter artifacts, PyCharm, and the test project, so it requires network access and can take several minutes.

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
