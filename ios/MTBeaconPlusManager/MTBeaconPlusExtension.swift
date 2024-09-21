import Foundation
import MTBeaconPlus

extension PowerState {
    var asJSObject: Any {
        switch self {
            case .unknown: return "UNKNOWN"
            case .resetting: return "RESETTING"
            case .unsupported: return "UNSUPPORTED"
            case .unauthorized: return "UNAUTHORIZED"
            case .poweredOff: return "POWERED_OFF"
            case .poweredOn: return "POWERED_ON"
        }
    }
}

extension MinewFrame {
    @objc var asJSObject: [AnyHashable: Any] {
        return [
            "slotAdvInterval": slotAdvInterval,
            "slotAdvTxpower": slotAdvTxpower,
            "slotRadioTxpower": slotRadioTxpower,
        ]
    }
}

extension MinewiBeacon {
    override var asJSObject: [AnyHashable: Any] {
        return [
            "major": major,
            "minor": minor,
            "uuid": uuid,
            "txPower": txPower,
        ]
    }
}

extension MinewAccSensor {
    override var asJSObject: [AnyHashable: Any] {
        return [
            "xAxis": xAxis,
            "yAxis": yAxis,
            "zAxis": zAxis,
        ]
    }
}

extension MinewLightSensor {
    override var asJSObject: [AnyHashable: Any] {
        return [
            "luxValue": luxValue,
        ]
    }
}

extension MinewHTSensor {
    override var asJSObject: [AnyHashable: Any] {
        return [
            "humidity": humidity,
            "temperature": temperature,
        ]
    }
}

extension MinewTempSensor {
    override var asJSObject: [AnyHashable: Any] {
        return [
            "temperature": temperature,
        ]
    }
}

extension MTPeripheral {
    var asJSObject: [AnyHashable: Any] {
        var iBeacon : [AnyHashable: Any]?;
        var accSensor : [AnyHashable: Any]?;
        var lightSensor : [AnyHashable: Any]?;
        var htSensor : [AnyHashable: Any]?;
        var tempSensor : [AnyHashable: Any]?;
        for frame in framer.advFrames {
            switch frame.frameType {
                case .FrameiBeacon:
                    iBeacon = frame.asJSObject;
                    break;
                case .FrameAccSensor:
                    accSensor = frame.asJSObject;
                    break;
                case .FrameLightSensor:
                    lightSensor = frame.asJSObject;
                    break;
                case .FrameHTSensor:
                    htSensor = frame.asJSObject;
                    break;
                case .FrameTempSensor:
                    tempSensor = frame.asJSObject;
                    break;
                default:
                    break;
            }
        }
        return [
            "id": identifier,
            "name": framer.name,
            "rssi": framer.rssi,
            "battery": framer.battery,
            "mac": framer.mac,
            "connectable": framer.connectable,
            "iBeacon": iBeacon as Any,
            "accSensor": accSensor as Any,
            "lightSensor": lightSensor as Any,
            "htSensor": htSensor as Any,
            "tempSensor": tempSensor as Any,
        ]
    }
}
