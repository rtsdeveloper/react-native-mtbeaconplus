import Foundation
import MTBeaconPlus

@objc
public protocol MTBeaconPlusManagerDelegate {
    func dispatchEvent(_ name: String, value: Any)
}

@objc
public class MTBeaconPlusManager : NSObject {

    // Delegate is used to send events to
    @objc
    public var delegate: MTBeaconPlusManagerDelegate!

    // RxBlutoothKit's manager
    private var manager : MTCentralManager!

    @objc
    public override init() {
        super.init()
        manager = MTCentralManager.sharedInstance()
        manager?.stateBlock = { (state: PowerState) in
            self.dispatchEvent(MTBeaconPlusEvent.stateChangeEvent, value: state.asJSObject)
        }
    }

    @objc
    public func invalidate() {
        // Disposables
        manager = nil
    }

    deinit {
        // We don't use deinit.
        // User should call invalidate() before destruction of this object.
        // In such case observables can call [weak self] closures properly.
    }
    
    private func dispatchEvent(_ event: String, value: Any) {
        delegate?.dispatchEvent(event, value: value)
    }

    // Start BLE scanning.
    @objc
    public func startScan() {
        DispatchQueue.main.async {
            if self.manager?.state == PowerState.poweredOn {
                self.manager?.startScan { (devices) in
                    var scannedPeripherals: Array<Any> = Array()
                    for peripheral in devices! {
                        scannedPeripherals.append(peripheral.asJSObject)
                    }
                    self.dispatchEvent(MTBeaconPlusEvent.scanEvent, value: scannedPeripherals)
                }
            }
        }
    }

    // Stop BLE scanning.
    @objc
    public func stopScan() {
        if manager?.state == PowerState.poweredOn {
            manager?.stopScan()
        }
    }

    // Get state.
    @objc
    public func state(_ resolve: Resolve, reject: Reject) {
        resolve(manager?.state.asJSObject)
    }
}
