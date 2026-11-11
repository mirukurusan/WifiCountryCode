# WiFi Country Code

An Xposed module to modify the Wi-Fi country code on Android devices, allowing you to bypass regional restrictions on Wi-Fi channels.

## Features

- Override the Wi-Fi country code with any ISO 3166-1 alpha-2 code
- Enable/disable the hook dynamically without rebooting the framework
- Real-time module activation status detection and configuration sync via LibXposed RemotePreferences
- Quick select common country codes (US, CN, JP, GB, DE)
- Material Design 3 UI built with Jetpack Compose

## Requirements

- Android 10 (API 29) or above
- Rooted device with an Xposed framework supporting LibXposed API 102+ (e.g., [Vector](https://github.com/JingMatrix/Vector))

## Installation

1. Download the latest APK from [Releases](https://github.com/mirukurusan/WifiCountryCode/releases)
2. Install the APK on your device
3. Enable the module in your Xposed framework manager
4. Ensure **System Framework** (`system`) is selected as the scope
5. Reboot your device

## Usage

1. Open the app and navigate to the **Configuration** tab
2. Toggle **Enable Hook** to activate the module
3. Enter a two-letter country code (e.g., `US`, `CN`, `JP`, `GB`) or use the quick select buttons
4. Tap **Save** to apply and sync the configuration

## How It Works

The module runs within `system_server` using **LibXposed API 102** and hooks into `com.android.server.wifi.WifiCountryCode`. Configuration is dynamically read and synchronized via LibXposed `RemotePreferences`.

The module overrides the following methods in `WifiCountryCode`:

**Setter & Update Methods:**
- `setTelephonyCountryCode` — intercepts telephony country code changes and forces the target country code
- `setOverrideCountryCode` — overrides manual country code overrides
- `setDriverCountryCode` — intercepts direct driver country code settings
- `updateCountryCode` — forces the target country code during updates

**Getter Methods:**
- `pickCountryCode` — returns the configured target country code
- `getCountryCode` — returns the configured target country code
- `getCurrentDriverCountryCode` — returns the configured target country code
- `getDriverCountryCode` — returns the configured target country code
- `getConnectingModeCountryCode` — returns the target country code during connection mode setup

This effectively forces the Wi-Fi subsystem to operate under the specified country regulatory domain, unlocking Wi-Fi channels that may be restricted in your region.

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose with Material Design 3
- **Xposed API:** [LibXposed API 102](https://github.com/libxposed/api) & LibXposed Service
- **Serialization:** kotlinx-serialization-json
- **Build:** AGP 9.x, Gradle Kotlin DSL

## License

This project is licensed under the [Apache License 2.0](LICENSE).

## Author

**mirukurusan**

- GitHub: [https://github.com/mirukurusan](https://github.com/mirukurusan)
- Project: [https://github.com/mirukurusan/WifiCountryCode](https://github.com/mirukurusan/WifiCountryCode)
