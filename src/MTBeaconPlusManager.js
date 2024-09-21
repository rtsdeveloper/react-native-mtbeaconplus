// @flow
'use strict'
import { MTBeaconPlusModule, EventEmitter } from './MTBeaconPlusModule'
import { useEffect, useRef } from 'react';
import { State } from './TypeDefinition';

/**
 * Custom hook for MTBeaconPlus functionality.
 */
export const useMTBeaconPlusManager = () => {
  // Refs to store subscriptions (equivalent to class properties)
  const scanEventSubscription = useRef<?EventEmitter>(null);
  const stateChangeEventSubscription = useRef<?EventEmitter>(null);
  const eventEmitter = new EventEmitter(MTBeaconPlusModule);

  // Create client on mount
  useEffect(() => {
    MTBeaconPlusModule.createClient();

    return () => {
      // Clean up on unmount
      if (stateChangeEventSubscription.current != null) {
        stateChangeEventSubscription.current.remove();
        stateChangeEventSubscription.current = null;
      }
      MTBeaconPlusModule.destroyClient();
    };
  }, []);

  // Start scanning
  const startScan = (listener: (devices: ?Collection) => void) => {
    stopScan(); // Ensure no scan is already running
    scanEventSubscription.current = eventEmitter.addListener(
      MTBeaconPlusModule.ScanEvent,
      listener
    );
    MTBeaconPlusModule.startScan();
  };

  // Stop scanning
  const stopScan = () => {
    if (scanEventSubscription.current != null) {
      scanEventSubscription.current.remove();
      scanEventSubscription.current = null;
    }
    MTBeaconPlusModule.stopScan();
  };

  // Get current state
  const getState = (): Promise<$Keys<typeof State>> => {
    return MTBeaconPlusModule.state();
  };

  // Listen for state changes
  const onStateChange = (listener: (state: ?String) => void) => {
    stateChangeEventSubscription.current = eventEmitter.addListener(
      MTBeaconPlusModule.StateChangeEvent,
      listener
    );
  };

  // Remove state change listener
  const offStateChange = () => {
    if (stateChangeEventSubscription.current != null) {
      stateChangeEventSubscription.current.remove();
      stateChangeEventSubscription.current = null;
    }
  };

  return {
    startScan,
    stopScan,
    getState,
    onStateChange,
    offStateChange,
  };
};
