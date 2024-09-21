// @flow
'use strict'

import { NativeModules, NativeEventEmitter } from 'react-native'
import { State } from './TypeDefinition'

/**
 * Native BLE Module interface
 * @private
 */
export interface MTBeaconPlusModuleInterface {
  // NativeModule methods

  addListener(string): void;
  removeListeners(number): void;

  // Lifecycle

  /**
   * Creates new native module internally. Only one module
   * is allowed to be instantiated.
   * @private
   */
  createClient(): void;

  /**
   * Destroys previously instantiated module. This function is
   * only safe when previously BleModule was created.
   * @private
   */
  destroyClient(): void;

  // Scanning

  /**
   * Starts device scan.
   *
   * scanning.
   * @param {?Function} callback Platform dependent options
   * @private
   */
  startScan(): void;

  /**
   * Stops device scan.
   * @private
   */
  stopScan(): void;

  /**
   * Get current bluetooth state.
   * @private
   */
  state(): Promise<$Keys<typeof State>>;

  // Events

  /**
   * New scanned event arrived
   * @private
   */
  ScanEvent: string;

  /**
   * Characteristic value update broadcasted due to registered notification as
   * @private
   */
  ReadEvent: string;

  /**
   * BLE Manager changed its state as $Keys<typeof State>
   * @private
   */
  StateChangeEvent: string;

  /**
   * BLE Manager restored its internal state
   * @private
   */
  RestoreStateEvent: string;

  /**
   * Device disconnected
   * @private
   */
  DisconnectionEvent: string;
}

export const MTBeaconPlusModule: MTBeaconPlusModuleInterface = NativeModules.MTBeaconPlusManager
export const EventEmitter = NativeEventEmitter
