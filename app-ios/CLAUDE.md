# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is the iOS app for DroidKaigi 2025 conference. For general project information, architecture diagrams, and getting started guide, see [README.md](./README.md).

## Build and Development Commands

### Building the App
```bash
# Build the main app
xcodebuild build -project DroidKaigi2025.xcodeproj -scheme DroidKaigi2025 -configuration Debug

# Build for release
xcodebuild build -project DroidKaigi2025.xcodeproj -scheme DroidKaigi2025 -configuration Release

# Build specific packages
cd Core && swift build
cd Native && swift build
```

### Running Tests
```bash
# Run component tests
xcodebuild test -project DroidKaigi2025.xcodeproj -scheme ComponentTests -destination 'platform=iOS Simulator,name=iPhone 15 Pro'

# Run use case tests
xcodebuild test -project DroidKaigi2025.xcodeproj -scheme UseCaseTests -destination 'platform=iOS Simulator,name=iPhone 15 Pro'

# Run package tests
cd Core && swift test
cd Native && swift test

# Run Core Package tests on Ubuntu (cross-platform support)
cd Core && swift test  # Works on Ubuntu/Linux environments
```

### Available Schemes
- `DroidKaigi2025` - Main app
- `ComponentTests` - UI component tests
- `Model` - Data model library
- `Presentation` - Presentation layer
- `Root` - Root navigation
- `UseCaseTests` - Business logic tests

## Architecture

See [README.md](./README.md#-architecture) for detailed architecture documentation with diagrams.

## Technical Requirements & Features

See [README.md](./README.md#-getting-started) for technical requirements, dependencies, and feature list.

## Code Quality Tools

See [README.md](./README.md#-development) for code quality tools, linting, formatting, and Makefile commands.

## Claude-Specific Development Notes

### Important Context
- The project is part of a larger multi-platform repository
- Android app documentation in root README.md shows architectural decisions that may influence iOS development
- Uses modern Swift 6 features including strict concurrency checking
- Linting and formatting tools are configured and should be used before completing work

## Important Build and Debug Notes
### Build Issues
- **Swift Dependencies Macro Error**: The project may encounter macro validation errors with swift-dependencies package when building. This is a known issue with the package itself, not your code changes.
  - Error: "cannot load module 'SwiftDiagnostics' built with SDK 'macosx15.5' when using SDK 'iphonesimulator18.5'"
  - **Workaround**: Open the project in Xcode and build from there, which handles the macro plugin correctly
  - Alternative: Build the app without the DependenciesMacrosPlugin target by commenting it out temporarily
  - The app binary installed on simulator may still work despite build errors

### Debugging
- **Use Mobile MCP for debugging**: When testing UI changes and functionality on iOS simulators, use Mobile MCP tools instead of relying solely on build success
  - `mobile_launch_app`, `mobile_take_screenshot`, `mobile_list_elements_on_screen` etc.
  - This allows verification of UI changes even when build has macro-related errors

### Code Quality Checks
- **Always run lint/format/test before completing work**:
  - `make lint` - Check for linting issues
  - `make lint-fix` - Auto-fix linting issues
  - `make format` - Format code with swift-format
  - `make test` - Run all tests
  - Ensure all checks pass and fixes are applied before considering work complete

### KMP Integration
- **Sponsor data integration**: Currently using mock data in `SponsorUseCaseImpl` as KMP doesn't have sponsorsRepository yet
  - When KMP adds sponsor repository, update `SponsorUseCaseImpl.load()` to use actual data
  - Follow the pattern used in `TimetableUseCaseImpl` for reference