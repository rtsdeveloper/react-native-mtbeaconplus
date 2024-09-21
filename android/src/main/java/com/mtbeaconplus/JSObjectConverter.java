package com.mtbeaconplus;

import android.bluetooth.BluetoothAdapter;
import androidx.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.minew.beaconplus.sdk.MTFrameHandler;
import com.minew.beaconplus.sdk.MTPeripheral;
import com.minew.beaconplus.sdk.enums.BluetoothState;
import com.minew.beaconplus.sdk.enums.FrameType;
import com.minew.beaconplus.sdk.frames.AccFrame;
import com.minew.beaconplus.sdk.frames.HTFrame;
import com.minew.beaconplus.sdk.frames.IBeaconFrame;
import com.minew.beaconplus.sdk.frames.LightFrame;
import com.minew.beaconplus.sdk.frames.MinewFrame;
import com.minew.beaconplus.sdk.frames.TemperatureFrame;

import java.util.ArrayList;

public class JSObjectConverter {

    interface Metadata {
        String ID = "id";
        String NAME = "name";
        String MAC = "mac";
        String RSSI = "rssi";
        String BATTERY = "battery";
        String CONNECTABLE = "connectable";
        String IBEACON = "iBeacon";
        String ACC_SENSOR = "accSensor";
        String LIGHT_SENSOR = "lightSensor";
        String HT_SENSOR = "htSensor";
        String TEMP_SENSOR = "tempSensor";
    }

    static public WritableMap fromPeripheral(@NonNull MTPeripheral mtPeripheral) {
        // get FrameHandler of a device.
        MTFrameHandler mtFrameHandler = mtPeripheral.mMTFrameHandler;
        String mac = mtFrameHandler.getMac(); 		//mac address of device
        String name = mtFrameHandler.getName();		// name of device
        int battery = mtFrameHandler.getBattery();	//battery
        int rssi = mtFrameHandler.getRssi();		//rssi
        boolean isConnectable = mtFrameHandler.isConnectable();		//connectable

        WritableMap result = Arguments.createMap();
        result.putString(Metadata.ID, mac);
        result.putString(Metadata.MAC, mac);
        result.putString(Metadata.NAME, name);
        result.putInt(Metadata.RSSI, rssi);
        result.putInt(Metadata.BATTERY, battery);
        result.putBoolean(Metadata.CONNECTABLE, isConnectable);

        final ArrayList<MinewFrame> advFrames = mtFrameHandler.getAdvFrames();
        for (MinewFrame mtFrame : advFrames) {
            FrameType type = mtFrame.getFrameType();
            switch(type) {
                case FrameiBeacon:
                    result.putMap(Metadata.IBEACON, fromIBeaconFrame((IBeaconFrame)mtFrame));
                    break;
                case FrameAccSensor:
                    result.putMap(Metadata.ACC_SENSOR, fromAccFrame((AccFrame)mtFrame));
                    break;
                case FrameLightSensor:
                    result.putMap(Metadata.LIGHT_SENSOR, fromLightFrame((LightFrame)mtFrame));
                    break;
                case FrameHTSensor:
                    result.putMap(Metadata.HT_SENSOR, fromHTFrame((HTFrame)mtFrame));
                    break;
                case FrameTempSensor:
                    result.putMap(Metadata.TEMP_SENSOR, fromTemperatureFrame((TemperatureFrame)mtFrame));
                    break;
                default:
                    break;
            }
        }
        return result;
    }

    static public WritableMap fromMinewFrame(@NonNull MinewFrame mtMinewFrame) {
        WritableMap result = Arguments.createMap();
        result.putInt("slotAdvInterval", mtMinewFrame.getAdvInterval());
        result.putInt("slotAdvTxpower", mtMinewFrame.getAdvtxPower());
        result.putInt("slotRadioTxpower", mtMinewFrame.getRadiotxPower());
        return result;
    }

    static public WritableMap fromIBeaconFrame(@NonNull IBeaconFrame mtIBeaconFrame) {
        WritableMap result = Arguments.createMap();
        result.putInt("major", mtIBeaconFrame.getMajor());
        result.putInt("minor", mtIBeaconFrame.getMinor());
        result.putString("uuid", mtIBeaconFrame.getUuid());
        result.putInt("txPower", mtIBeaconFrame.getTxPower());
        return result;
    }

    static public WritableMap fromAccFrame(@NonNull AccFrame mtFrame) {
        WritableMap result = Arguments.createMap();
        result.putDouble("xAxis", mtFrame.getXAxis());
        result.putDouble("yAxis", mtFrame.getYAxis());
        result.putDouble("zAxis", mtFrame.getZAxis());
        return result;
    }

    static public WritableMap fromLightFrame(@NonNull LightFrame mtFrame) {
        WritableMap result = Arguments.createMap();
        result.putInt("luxValue", mtFrame.getLuxValue());
        return result;
    }


    static public WritableMap fromHTFrame(@NonNull HTFrame mtFrame) {
        WritableMap result = Arguments.createMap();
        result.putDouble("humidity", mtFrame.getHumidity());
        result.putDouble("temperature", mtFrame.getTemperature());
        return result;
    }

    static public WritableMap fromTemperatureFrame(@NonNull TemperatureFrame mtFrame) {
        WritableMap result = Arguments.createMap();
        result.putDouble("temperature", mtFrame.getValue());
        return result;
    }

    static public String fromBluetoothState(@NonNull int bluetoothState) {
        switch (bluetoothState) {
            case BluetoothAdapter.STATE_ON:
                return "POWERED_ON";
            case BluetoothAdapter.STATE_OFF:
                return "POWERED_OFF";
            case BluetoothAdapter.STATE_TURNING_OFF:
            case BluetoothAdapter.STATE_TURNING_ON:
                return "RESETTING";
            default:
                return "UNKNOWN";
        }
    }

    static public String fromBluetoothState(@NonNull BluetoothState bluetoothState) {
        switch (bluetoothState) {
            case BluetoothStatePowerOn:
                return "POWERED_ON";
            case BluetoothStatePowerOff:
                return "POWERED_OFF";
            case BluetoothStateNotSupported:
                return "UNSUPPORTED";
            default:
                return "UNKNOWN";
        }
    }
}
