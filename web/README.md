# SECUROX — Web Admin Control Dashboard

Welcome to the **SECUROX Web Admin Control Dashboard**.

This web portal is designed for phone owners to locate, monitor, and remotely lock their registered Android devices from any computer, tablet, or secondary smartphone via a web browser.

---

## 🚀 Features

1. **Multi-Device Live Telemetry**:
   - Live Online/Offline detection with pulsing indicators.
   - Real-time battery status & charging state.
   - Device Administrator & Android Enterprise Device Owner activation badges.
   - SIM card state verification.

2. **Master Security Commands**:
   - 🚨 **Remote Emergency Lock**: Sends authenticated `LOCK_NOW` command to trigger `DevicePolicyManager.lockNow()`.
   - 📍 **Real-Time GPS Tracking**: Integrates interactive Leaflet & OpenStreetMap dark map with custom neon green GPS pin and accuracy circle.
   - 🔊 **Acoustic Anti-Theft Siren**: Triggers high-pitch alarm to locate lost phones or alert people nearby.
   - 🚫 **Anti-Shutdown Lockdown**: Arms the boot and power-loss interceptor.
   - 🛡️ **Factory Reset & Safe Boot Protection (FRP)**: Enforces Android Enterprise restrictions.
   - 🗑️ **Remote Data Wipe**: Complete emergency wipe for stolen enterprise devices.

3. **Live Security Audit Trail**:
   - Real-time log stream showing SIM swap attempts, reboot alerts, heartbeat pings, and executed commands.

4. **Android Enterprise ADB Command Generator**:
   - One-click copy for `adb shell dpm set-device-owner ...`.

5. **Direct APK Download**:
   - Instant download link for the latest `SECUROX-debug.apk`.

---

## 💻 How to Run Locally

You can run this dashboard immediately without any setup:

### Method 1: Direct File
Simply double-click `index.html` in your file explorer to open it in Chrome, Edge, Firefox, or Safari!

### Method 2: Local HTTP Server (Python / Node)
```bash
# Using Python
python -m http.server 8080

# Using Node (npx)
npx serve .
```
Then visit `http://localhost:8080` in your web browser.

---

## ☁️ Firebase Hosting Deployment

To deploy this Web Admin Control Dashboard globally to your Firebase project:

```bash
firebase deploy --only hosting
```
