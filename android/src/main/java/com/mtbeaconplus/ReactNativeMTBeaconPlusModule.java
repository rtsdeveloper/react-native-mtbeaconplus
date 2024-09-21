package com.mtbeaconplus;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.minew.beaconplus.sdk.MTCentralManager;
import com.minew.beaconplus.sdk.MTPeripheral;
import com.minew.beaconplus.sdk.enums.BluetoothState;
import com.minew.beaconplus.sdk.interfaces.MTCentralManagerListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReactNativeMTBeaconPlusModule extends ReactContextBaseJavaModule {
    private static final String NAME = "MTBeaconPlusManager";
    private static ReactApplicationContext reactContext;
    private static BroadcastReceiver mReceiver;

    // Manager
    @Nullable
    private MTCentralManager manager;

    public ReactNativeMTBeaconPlusModule(ReactApplicationContext context) {
        super(context);
        reactContext = context;
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();
                if (action != null && action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                    final int state = intent.getIntExtra(
                            BluetoothAdapter.EXTRA_STATE,
                            BluetoothAdapter.ERROR
                    );
                    sendEvent(Event.StateChangeEvent, JSObjectConverter.fromBluetoothState(state));
                }
            }
        };
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        for (Event event : Event.values()) {
            constants.put(event.name, event.name);
        }
        return constants;
    }

    // Lifecycle -----------------------------------------------------------------------------------

    @ReactMethod
    public void createClient() {
        manager = MTCentralManager.getInstance(reactContext);

        //startservice
        manager.startService();
        if (isBluetoothSupported()) {
            this.startListenForBluetoothStateChange();
        }
    }

    @ReactMethod
    public void destroyClient() {
        // Clear client
        manager.stopService();
        manager = null;
        if (isBluetoothSupported()) {
            this.stopListenForBluetoothStateChange();
        }
    }

    @ReactMethod
    public void startScan() {
        if (manager == null) {
            throw new IllegalStateException("MTBeaconPlusManager not created when tried to start device scan");
        }

        if (this.getCurrentState() == "POWERED_ON") {
            manager.setMTCentralManagerListener(new MTCentralManagerListener() {
                @Override
                public void onScanedPeripheral(List<MTPeripheral> peripherals) {
                    WritableArray devices = Arguments.createArray();
                    for (MTPeripheral mtPeripheral : peripherals) {
                        devices.pushMap(JSObjectConverter.fromPeripheral(mtPeripheral));
                    }
                    sendEvent(Event.ScanEvent, devices);
                }
            });
            manager.clear();
            manager.startScan();
        }
    }

    @ReactMethod
    public void stopScan() {
        if (manager == null) {
            throw new IllegalStateException("MTBeaconPlusManager not created when tried to stop device scan");
        }

        if (this.getCurrentState() == "POWERED_ON") {
            manager.stopScan();
        }
    }

    @ReactMethod
    public void state(Promise promise) {
        promise.resolve(this.getCurrentState());
    }

    // Mark: Private -------------------------------------------------------------------------------
    private void sendEvent(@NonNull Event event, @Nullable Object params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(event.name, params);
    }

    private void startListenForBluetoothStateChange() {
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        reactContext.registerReceiver(mReceiver, filter);
    }

    private void stopListenForBluetoothStateChange() {
        reactContext.unregisterReceiver(mReceiver);
    }

    private static boolean isBluetoothSupported() {
        return BluetoothAdapter.getDefaultAdapter() != null;
    }

    private String getCurrentState() {
        if (manager == null) {
            throw new IllegalStateException("MTBeaconPlusManager not created when tried to stop device scan");
        }
        if (isBluetoothSupported()) {
            final int state = BluetoothAdapter.getDefaultAdapter().getState();
            return JSObjectConverter.fromBluetoothState(state);
        }

        final BluetoothState state = manager.getBluetoothState(reactContext);
        return JSObjectConverter.fromBluetoothState(state);
    }
}
