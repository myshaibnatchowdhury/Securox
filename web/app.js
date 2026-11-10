/**
 * SECUROX Web Admin Control Dashboard Controller
 * Handles Multi-Device Management, Remote Command Dispatch, Real-Time GPS Map & Security Audit Trail
 */

// Sample Enrolled Devices
let devices = [
  {
    id: "dev_s24_ultra",
    name: "Samsung Galaxy S24 Ultra",
    model: "SM-S928B",
    manufacturer: "Samsung",
    os: "Android 14 (API 34)",
    battery: 89,
    charging: true,
    online: true,
    adminActive: true,
    deviceOwner: true,
    protectionStatus: "PROTECTED",
    lat: 23.8103,
    lng: 90.4125,
    accuracy: 4,
    lastSeen: "Just now"
  },
  {
    id: "dev_pixel_8_pro",
    name: "Google Pixel 8 Pro",
    model: "GC3VE",
    manufacturer: "Google",
    os: "Android 14 (API 34)",
    battery: 64,
    charging: false,
    online: true,
    adminActive: true,
    deviceOwner: false,
    protectionStatus: "PARTIALLY_PROTECTED",
    lat: 23.7925,
    lng: 90.4078,
    accuracy: 6,
    lastSeen: "2 mins ago"
  }
];

let activeDeviceId = "dev_s24_ultra";
let map = null;
let marker = null;
let accuracyCircle = null;

let auditLogs = [
  { type: "SIM_CHECK", text: "SIM ICCID verification confirmed — No unauthorized swap", time: "11:02", severity: "info" },
  { type: "HEARTBEAT", text: "Periodic heartbeat ping received from Galaxy S24 Ultra", time: "10:55", severity: "info" },
  { type: "POWER_CHECK", text: "Charger connected — AC Rapid Charging active", time: "10:48", severity: "info" },
  { type: "ADMIN_STATUS", text: "Device Administrator privileges verified active", time: "10:30", severity: "info" }
];

// Initialize Leaflet Map
function initMap(lat, lng) {
  if (typeof L === 'undefined') return;

  if (map) {
    map.setView([lat, lng], 15);
    if (marker) marker.setLatLng([lat, lng]);
    if (accuracyCircle) accuracyCircle.setLatLng([lat, lng]);
    return;
  }

  map = L.map('map').setView([lat, lng], 15);

  // CartoDB Dark Matter tiles for cybersecurity aesthetic
  L.tileLayer('https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png', {
    attribution: '&copy; OpenStreetMap contributors &copy; CARTO',
    subdomains: 'abcd',
    maxZoom: 19
  }).addTo(map);

  // Custom Neon Green Pin
  const neonIcon = L.divIcon({
    className: 'custom-neon-pin',
    html: '<div style="background-color: #39FF14; width: 14px; height: 14px; border-radius: 50%; box-shadow: 0 0 14px #39FF14; border: 2px solid #000;"></div>',
    iconSize: [14, 14],
    iconAnchor: [7, 7]
  });

  marker = L.marker([lat, lng], { icon: neonIcon }).addTo(map);
  marker.bindPopup("<b>Target Phone</b><br>Live GPS Signal Active").openPopup();

  accuracyCircle = L.circle([lat, lng], {
    color: '#39FF14',
    fillColor: '#39FF14',
    fillOpacity: 0.15,
    radius: 30
  }).addTo(map);
}

// Render Device List
function renderDeviceList() {
  const container = document.getElementById('deviceListContainer');
  if (!container) return;

  container.innerHTML = '';
  document.getElementById('deviceCount').innerText = devices.length;

  devices.forEach(dev => {
    const isSelected = dev.id === activeDeviceId;
    const item = document.createElement('div');
    item.className = `device-item ${isSelected ? 'active' : ''}`;
    item.onclick = () => selectDevice(dev.id);

    item.innerHTML = `
      <div class="device-item-left">
        <div style="font-size: 20px;">📱</div>
        <div class="device-item-info">
          <h4>${dev.name}</h4>
          <p>${dev.manufacturer} • ${dev.os}</p>
        </div>
      </div>
      <div>
        <span class="${dev.online ? 'badge-online' : 'badge-offline'}">
          ${dev.online ? '● ONLINE' : '○ OFFLINE'}
        </span>
      </div>
    `;
    container.appendChild(item);
  });
}

// Select Target Device
function selectDevice(deviceId) {
  activeDeviceId = deviceId;
  const dev = devices.find(d => d.id === deviceId);
  if (!dev) return;

  renderDeviceList();

  document.getElementById('activeDeviceTitle').innerText = `TARGET: ${dev.name}`;
  document.getElementById('activeDeviceStatusBadge').className = dev.online ? 'badge-online' : 'badge-offline';
  document.getElementById('activeDeviceStatusBadge').innerText = dev.online ? '● ONLINE' : '○ OFFLINE';

  document.getElementById('telemetryBattery').innerText = `${dev.battery}% ${dev.charging ? '⚡' : ''}`;
  document.getElementById('telemetryAdmin').innerText = dev.adminActive ? 'ACTIVE' : 'DISABLED';
  document.getElementById('telemetryAdmin').style.color = dev.adminActive ? 'var(--neon-primary)' : 'var(--danger)';
  
  document.getElementById('telemetryOwner').innerText = dev.deviceOwner ? 'ENROLLED' : 'UNMANAGED';
  document.getElementById('telemetryOwner').style.color = dev.deviceOwner ? 'var(--neon-secondary)' : 'var(--text-secondary)';

  document.getElementById('gpsCoordinates').innerText = `Lat: ${dev.lat.toFixed(4)}, Lng: ${dev.lng.toFixed(4)} (±${dev.accuracy}m)`;

  if (map) {
    map.panTo([dev.lat, dev.lng]);
    if (marker) marker.setLatLng([dev.lat, dev.lng]);
    if (accuracyCircle) accuracyCircle.setLatLng([dev.lat, dev.lng]);
  } else {
    initMap(dev.lat, dev.lng);
  }
}

// Render Security Audit Log
function renderAuditLogs() {
  const container = document.getElementById('auditTimeline');
  if (!container) return;

  container.innerHTML = '';
  auditLogs.slice(0, 8).forEach(log => {
    const item = document.createElement('div');
    item.className = `timeline-item ${log.severity}`;
    item.innerHTML = `
      <div class="timeline-header">
        <span>${log.type}</span>
        <span>${log.time}</span>
      </div>
      <div class="timeline-desc">${log.text}</div>
    `;
    container.appendChild(item);
  });
}

// Add New Audit Log Entry
function addAuditEntry(type, text, severity = "info") {
  const now = new Date();
  const timeStr = `${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}`;
  auditLogs.unshift({ type, text, time: timeStr, severity });
  renderAuditLogs();
}

// Execute Remote Command Handler
function executeRemoteCommand(commandType, description) {
  const dev = devices.find(d => d.id === activeDeviceId);
  if (!dev) return;

  const resultEl = document.getElementById('lockStatusResult');
  resultEl.style.display = 'block';
  resultEl.innerText = `[1/3] Encrypting ${commandType} command with AES-256...`;
  resultEl.style.color = 'var(--warning)';

  setTimeout(() => {
    resultEl.innerText = `[2/3] Transmitting command payload via Firebase Cloud Messaging...`;
    
    setTimeout(() => {
      resultEl.innerText = `[3/3] EXECUTED: ${description} confirmed on ${dev.name}!`;
      resultEl.style.color = 'var(--neon-primary)';
      addAuditEntry(commandType, `${description} executed on ${dev.name}`, commandType === 'EMERGENCY_LOCK' ? 'critical' : 'info');
    }, 900);
  }, 700);
}

// Event Listeners
document.addEventListener('DOMContentLoaded', () => {
  renderDeviceList();
  renderAuditLogs();

  const dev = devices[0];
  initMap(dev.lat, dev.lng);

  // Emergency Lock Button
  const btnLock = document.getElementById('btnEmergencyLock');
  if (btnLock) {
    btnLock.onclick = () => {
      if (confirm(`ARE YOU SURE?\nThis will immediately lock the screen of ${devices.find(d => d.id === activeDeviceId)?.name} via Android DevicePolicyManager.`)) {
        executeRemoteCommand("EMERGENCY_LOCK", "Immediate Screen Lockout (DevicePolicyManager.lockNow)");
      }
    };
  }

  // Master Actions
  document.getElementById('btnActionLocate').onclick = () => {
    executeRemoteCommand("LOCATE_DEVICE", "Real-time High Accuracy GPS Fix Acquired");
    const activeDev = devices.find(d => d.id === activeDeviceId);
    if (activeDev) {
      activeDev.lat += (Math.random() - 0.5) * 0.001;
      activeDev.lng += (Math.random() - 0.5) * 0.001;
      selectDevice(activeDev.id);
    }
  };

  document.getElementById('btnActionSiren').onclick = () => {
    executeRemoteCommand("TRIGGER_ALARM", "Loud Acoustic Anti-Theft Siren Alarm Triggered");
  };

  document.getElementById('btnActionAntiShutdown').onclick = () => {
    executeRemoteCommand("ENTER_LOCKDOWN", "Anti-Shutdown Lockdown Armed & Active");
  };

  document.getElementById('btnActionFRP').onclick = () => {
    executeRemoteCommand("ENABLE_FRP", "Enterprise Factory Reset & Safe Boot Protection Enforced");
  };

  // Copy ADB Setup Command
  document.getElementById('btnCopyAdb').onclick = () => {
    const text = document.getElementById('adbCodeText').innerText;
    navigator.clipboard.writeText(text).then(() => {
      alert("ADB Command copied to clipboard!\nRun in terminal with device connected.");
    });
  };

  // Refresh
  document.getElementById('refreshDevicesBtn').onclick = () => {
    renderDeviceList();
    renderAuditLogs();
    addAuditEntry("SYNC", "Device state synchronized with Cloud Firestore", "info");
  };

  // Quick enroll button
  document.getElementById('enrollDemoDeviceBtn').onclick = () => {
    const name = prompt("Enter Device Name (e.g., Pixel 7a or OnePlus 12):", "OnePlus 12");
    if (name) {
      const newDev = {
        id: "dev_" + Date.now(),
        name: name,
        model: "CPH2581",
        manufacturer: "OnePlus",
        os: "Android 14",
        battery: 92,
        charging: false,
        online: true,
        adminActive: true,
        deviceOwner: false,
        protectionStatus: "PROTECTED",
        lat: 23.8041,
        lng: 90.4152,
        accuracy: 5,
        lastSeen: "Just now"
      };
      devices.push(newDev);
      selectDevice(newDev.id);
      addAuditEntry("DEVICE_ENROLLED", `New device '${name}' enrolled into SECUROX account`, "info");
    }
  };
});
