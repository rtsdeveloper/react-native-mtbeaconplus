
# rn-mtbeaconplus

React Native module for Bluetooth Low Energy (BLE) scanning and device discovery.


## Table of Contents
 - Installation
 - Usage/Examples
 - Method & Events
 - Authors
 - Contributing

## Documentation
Methods
- startScan(listener: (devices: ?Collection) => void) - Starts scanning for nearby devices.
- stopScan() Stops the ongoing scan.
- state(): Promise<$Keys<typeof State>> Gets the current Bluetooth state.
- onStateChange(listener: (state: ?String) => void) Subscribes to state change events.
- offStateChange() Unsubscribes from state change events.
- destroy() Destroys the manager instance.

Events
- ScanEvent: Triggered when new devices are scanned.
- StateChangeEvent: Triggered when the Bluetooth state changes.
- RestoreStateEvent: Triggered when the internal state is restored.
- DisconnectionEvent: Triggered when a device disconnects.


## Installation

Installation with npm:

```bash
  npm i rn-mtbeaconplus --save
  yarn add rn-mtbeaconplus
```
Android Installation :
To link the native module:

```bash
 react-native link rn-mtbeaconplus
```
iOS Installation :
```bash
 pod 'react-native-mtbeacon-plus-swift', :path => '../node_modules/rn-mtbeaconplus'
```
## Usage/Examples

```javascript
import React, { useEffect } from 'react';
// Importing from your package
import { useMTBeaconPlusManager } from 'rn-mtbeaconplus';

const BeaconScanner = () => {
  const { startScan, stopScan, getState, onStateChange, offStateChange } = useMTBeaconPlusManager();

  useEffect(() => {
    // Fetch the initial Bluetooth state and log it
    getState().then((state) => {
      console.log('Initial Bluetooth State:', state);
    });

    // Subscribe to state changes and log them
    onStateChange((newState) => {
      console.log('Bluetooth State Changed:', newState);
    });

    // Cleanup on unmount
    return () => {
      offStateChange();
      stopScan();
    };
  }, []);

  const handleStartScan = () => {
    // Start scanning and log the scanned devices
    startScan((scannedDevices) => {
      console.log('Scanned Devices:', scannedDevices);
    });
  };

  const handleStopScan = () => {
    stopScan();
    console.log('Scan stopped');
  };

  // Automatically start scanning when the component mounts
  useEffect(() => {
    handleStartScan();

    return () => {
      handleStopScan();
    };
  }, []);

  return null;
};

export default BeaconScanner;

```


## Troubleshooting

- Ensure that your app has the necessary permissions to access Bluetooth.
- Check if the device supports BLE functionality.
- Verify that the native module is properly linked and compiled.
## Authors

- [RTS-Developer](https://github.com/rtsdeveloper/rn-mtbeaconplus)


## Contributing

Contributions are welcome! Please feel free to submit pull requests or issues.