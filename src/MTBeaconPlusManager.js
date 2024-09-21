// @flow
'use strict'
import { MTBeaconPlusModule, EventEmitter } from './MTBeaconPlusModule'
import { State } from './TypeDefinition'

export class MTBeaconPlusManager {
    // Scan subscriptions
    _scanEventSubscription: ?EventEmitter
    _stateChangeEventSubscription: ?EventEmitter
    // Listening to events
    _eventEmitter: EventEmitter
  /**
   * Creates an instance .
   */
  constructor() {
    this._eventEmitter = new EventEmitter(MTBeaconPlusModule)
    MTBeaconPlusModule.createClient()
  }

  destroy() {
    if (this.stateChangeEventSubscription != null) {
      this.stateChangeEventSubscription.remove()
      this.stateChangeEventSubscription = null
    }
    // Destroy native module object
    MTBeaconPlusModule.destroyClient()
  }

  startScan(listener: (devices: ?Collection) => void) {
    this.stopScan()
    this._scanEventSubscription = this._eventEmitter.addListener(MTBeaconPlusModule.ScanEvent, listener)
    MTBeaconPlusModule.startScan()
  }

  stopScan() {
    if (this._scanEventSubscription != null) {
      this._scanEventSubscription.remove()
      this._scanEventSubscription = null
    }
    MTBeaconPlusModule.stopScan()
  }

  state(): Promise<$Keys<typeof State>> {
    return MTBeaconPlusModule.state();
  }

  onStateChange(listener: (state: ?String) => void) {
    this.stateChangeEventSubscription = this._eventEmitter.addListener(MTBeaconPlusModule.StateChangeEvent, listener)
  }

  offStateChange() {
    if (this.stateChangeEventSubscription != null) {
      this.stateChangeEventSubscription.remove()
      this.stateChangeEventSubscription = null
    }
  }
}
