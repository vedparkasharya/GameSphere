# Snapify Creator (Android)

A clean Android-native starter for choosing photos or videos, previewing them, and handing them to Snapchat through Android's supported share intent.

## Open in Android Studio
1. Open the `android` folder.
2. Use JDK 17 and Android SDK 35.
3. Let Gradle sync, then run the `app` configuration on an Android 13+ device or emulator.

## Current MVP
- Android Photo Picker for images and videos (no broad storage permission).
- Basic image preview and selected-media status.
- Share selected media to Snapchat when installed; otherwise show Android's share chooser.

## Important limitation
This app cannot make gallery media become camera-captured media inside Snapchat or remove Snapchat's imported-media indicators. Snapchat controls how shared/imported media is labelled and handled. No private APIs, injection, or bypasses are used.

## Roadmap
- Add crop, rotate, trim, and export controls inspired by the separate `reframe` editor workflow.
- Add robust video preview/transcoding and compatibility checks.
- Add tests and CI build verification.

The `reframe` repository is a web editor, not an Android module; its workflow is a reference for planned editor features rather than code copied into this native app.
