# SECUROX — Advanced Anti-Theft & Device Security

![SECUROX Logo](securox_logo.jpg)

> **"Your Phone. Our Protection."**  
> *Secure Today. Safe Tomorrow.*

SECUROX is an enterprise-grade personal Android anti-theft and mobile device protection application built with Kotlin, Jetpack Compose, Material 3, and Clean Architecture. Designed specifically to protect a user's own Android phones if lost or stolen, SECUROX enables multi-device remote control, authenticated emergency device locking, real-time GPS tracking, and Android Enterprise policy enforcement.

---

## 🌟 Key Features

1. **Multi-Device Security Hub**:
   - Link multiple personal phones and tablets (Phone A: Protected Device, Phone B: Controller, Phone C: Backup).
   - Real-time battery levels, charging status, online/offline presence, and protection status.

2. **Admin Control Dashboard (In-App & Web)**:
   - **In-App Admin Command Center**: Direct master dashboard tab in the Android app featuring target device selector, hardware telemetry (Battery %, Admin Status, Device Owner Status), and instant command dispatch (Emergency Lock, GPS Locate, Siren Alarm, Anti-Shutdown Lockdown, FRP, and Remote Wipe).
   - **Web Admin Control Dashboard** (`web/index.html`): Fully responsive, dark cyber-themed web application allowing device owners to track live GPS on Leaflet/OpenStreetMap, sound loud acoustic anti-theft alarms, and trigger Emergency Lockout from any PC, Mac, iPhone, or web browser!

3. **Remote Emergency Lock**:
   - Trigger immediate hardware screen locking using official Android `DevicePolicyManager.lockNow()`.
   - Complete status pipeline: `PENDING` &rarr; `SENDING` &rarr; `DELIVERED` &rarr; `EXECUTED` / `OFFLINE`.
   - Never fakes success: commands are verified on target hardware before confirmation.

3. **Android Device Administrator**:
   - Official integration via `DeviceAdminReceiver`.
   - Immediate remote screen locking privilege.
   - Unauthorized deactivation alerts and failed unlock attempt monitoring.

4. **Android Enterprise Device Owner Mode**:
   - Enforces `DISALLOW_FACTORY_RESET` to block Settings reset.
   - Enforces `DISALLOW_SAFE_BOOT` to block safe-mode bypasses.
   - Enforces `DISALLOW_UNINSTALL_APPS` to prevent theft removal.
   - Includes one-click ADB provisioning script.

5. **Live Location Radar**:
   - High-precision Google Fused Location Provider engine.
   - Continuous foreground tracking service with persistent non-dismissible notification.
   - Animated visual radar scanner displaying coordinates and accuracy radius (±m).

6. **Anti-Power-Off & Disconnect Guard**:
   - Detects external charger disconnection.
   - Captures critical low-battery telemetry and last known coordinates prior to power exhaustion.
   - Boot completed receiver detects unexpected reboots and restores protection instantly.

7. **SIM Card Security**:
   - Hardware SIM state monitoring (`SIM_STATE_CHANGED`).
   - Detects SIM card removal/swap and raises instant CRITICAL alerts to all linked devices.

8. **Dual-Mode Operation**:
   - **Cloud Sync Mode**: Full Firebase Authentication, Cloud Firestore, and FCM push notifications.
   - **Local Protected Vault Mode**: Standalone offline testing using Android Keystore AES-256-GCM encrypted preferences.

---

## 🏗️ Architecture & Technology Stack

- **Language**: Kotlin 1.9.23
- **UI Framework**: Jetpack Compose + Material 3 (BOM 2024.02.02)
- **Architecture**: Clean Architecture + MVVM (Model-View-ViewModel)
- **Navigation**: Jetpack Navigation Compose
- **Local Cryptography**: Android Keystore + AndroidX Security Crypto (EncryptedSharedPreferences AES-256-GCM)
- **Hardware Integration**: Android DevicePolicyManager (DPM), TelephonyManager, BatteryManager
- **Location Services**: Google Play Services Fused Location Provider 21.2.0
- **Background Tasks**: Android WorkManager 2.9.0
- **Backend / Cloud**: Firebase Cloud Firestore, Firebase Cloud Messaging (FCM), Firebase Cloud Functions

---

## 🚀 Build & Installation Guide

### Prerequisites
- JDK 17 (Temurin 17 or Android Studio JBR)
- Android SDK with Platform 34 (Android 14) and Build-Tools 34.0.0
- Gradle 8.2.1 (provided via `gradlew.bat`)

### Building the Debug APK
Open a terminal in the project root:
```bash
# On Windows
gradlew.bat assembleDebug --no-daemon

# On Linux / macOS
./gradlew assembleDebug --no-daemon
```

The generated APK will be located at:
- `app/build/outputs/apk/debug/app-debug.apk`
- Ready-to-install copy: `SECUROX-debug.apk` in the project root.

### Installing via ADB
```bash
adb install -r SECUROX-debug.apk
```

---

## 🛡️ Device Administrator & Device Owner Setup

### 1. Activating Device Administrator
1. Open **SECUROX** &rarr; navigate to **Security** tab &rarr; select **Device Administrator**.
2. Tap **Activate Device Administrator**.
3. Confirm Android's system prompt granting `force-lock` and `watch-login` permissions.

### 2. Provisioning Android Enterprise Device Owner Mode (Optional / Advanced)
Device Owner mode enables factory reset and safe-boot restrictions:
```bash
# Ensure no Google or other accounts are signed into the target test phone
adb shell dpm set-device-owner com.securox.antitheft/.security.SecuroxDeviceAdminReceiver
```
To verify Device Owner status:
```bash
adb shell dumpsys device_policy
```

---

## ☁️ Firebase Cloud Configuration

SECUROX includes complete cloud artifacts:
- `firestore.rules`: Production-grade security rules with strict ownership verification, state-machine validation for commands, and resource exhaustion bounds.
- `firestore.indexes.json`: Optimized compound indexes for devices, events, commands, and locations.
- `functions/index.js`: Firebase Cloud Functions for FCM command dispatch and offline heartbeat monitoring.

To connect your own Firebase project:
1. Create a Firebase project at [console.firebase.google.com](https://console.firebase.google.com).
2. Download your `google-services.json` and replace `app/google-services.json`.
3. Deploy Firestore rules and Cloud Functions:
   ```bash
   firebase deploy --only firestore:rules,firestore:indexes,functions
   ```

---

## ⚠️ Security & Hardware Limitations Disclaimer

1. **Physical Power Button**: No non-system Android application can prevent a user from forcing a hard hardware shutdown by holding down the physical power button for 10 seconds. SECUROX mitigates this by logging last known location and battery level upon power disconnection and immediately resuming protection on boot.
2. **Bootloader & Recovery Wipe**: A normal Android application cannot prevent an attacker from entering OEM Recovery mode to perform a hardware wipe. To protect against recovery wipe, Android Enterprise Device Owner mode should be enabled alongside OEM bootloader locks.
3. **Transparent Consent**: SECUROX is designed strictly for personal protection. It never acts as hidden spyware, does not record calls or keystrokes, and never deletes user photos or documents.
