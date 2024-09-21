// @flow
'use strict'

/**
 * Device Bluetooth Low Energy state.
 */
export const State = {
  /**
   * The current state of the manager is unknown; an update is imminent.
   */
  Unknown: 'UNKNOWN',
  /**
   * The connection with the system service was momentarily lost; an update is imminent.
   */
  Resetting: 'RESETTING',
  /**
   * The platform does not support Bluetooth low energy.
   */
  Unsupported: 'UNSUPPORTED',
  /**
   * The app is not authorized to use Bluetooth low energy.
   */
  Unauthorized: 'UNAUTHORIZED',
  /**
   * Bluetooth is currently powered off.
   */
  PoweredOff: 'POWERED_OFF',
  /**
   * Bluetooth is currently powered on and available to use.
   */
  PoweredOn: 'POWERED_ON'
}
