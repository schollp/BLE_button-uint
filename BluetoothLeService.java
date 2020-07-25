/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.bluetoothlegatt;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.List;
import java.util.UUID;

/**
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE device.
 */
public class BluetoothLeService extends Service {
    private final static String TAG = BluetoothLeService.class.getSimpleName();

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;

    private int S1;
    private int S2;
    private int S3;
    private int S4;
    private int S5;
    private int S6;
    private int S7;

    private int S11;
    private int S12;
    private int S21;
    private int S22;
    private int S31;
    private int S32;
    private int S41;
    private int S42;
    private int S51;
    private int S52;
    private int S61;
    private int S62;
    private int S71;
    private int S72;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bluetooth.le.EXTRA_DATA";

    public final static UUID UUID_HEART_RATE_MEASUREMENT =
            UUID.fromString(SampleGattAttributes.HM_10);

    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                Log.i(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.i(TAG, "Attempting to start service discovery:" +
                        mBluetoothGatt.discoverServices());

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server.");
                broadcastUpdate(intentAction);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }
    };

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        /*
        // This is special handling for the Heart Rate Measurement profile.  Data parsing is
        // carried out as per profile specifications:
        // http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
            int flag = characteristic.getProperties();
            int format = -1;
            if ((flag & 0x01) != 0) {
                format = BluetoothGattCharacteristic.FORMAT_UINT16;
                Log.d(TAG, "Heart rate format UINT16.");
            } else {
                format = BluetoothGattCharacteristic.FORMAT_UINT8;
                Log.d(TAG, "Heart rate format UINT8.");
            }
            final int heartRate = characteristic.getIntValue(format, 1);
            Log.d(TAG, String.format("Received heart rate: %d", heartRate));
            intent.putExtra(EXTRA_DATA, String.valueOf(heartRate));
        } else {
            // For all other profiles, writes the data formatted in HEX.
            final byte[] data = characteristic.getValue();
            if (data != null && data.length > 0) {
                final StringBuilder stringBuilder = new StringBuilder(data.length);
                for(byte byteChar : data)
                    stringBuilder.append(String.format("%02X ", byteChar));
                intent.putExtra(EXTRA_DATA, new String(data) + "\n" + stringBuilder.toString());
            }
        }
        */

        Log.w(TAG, "broadcastUpdate()");

        final byte[] data = characteristic.getValue();

        Log.v(TAG, "data.length: " + data.length);

        if (data != null && data.length > 0) {
            final StringBuilder stringBuilder = new StringBuilder(data.length);
            byte i=0;
            for(byte byteChar : data) {
                if (i == 0) {
                    S11=(byteChar<<8) ;
                    //Log.v(TAG, String.format("%02X ", S11));
                    Log.e("Binary Format S11",String.format("%16s", Integer.toBinaryString(S11)).replace(' ', '0'));
                    stringBuilder.append(String.format("%02X ", byteChar));
                }

                if (i == 1) {
                    S12=byteChar & 0xff;
                    //Log.v(TAG, String.format("%02X ", S12));
                    Log.e("Binary Format S12",String.format("%16s", Integer.toBinaryString(S12)).replace(' ', '0'));
                    stringBuilder.append(String.format("%02X ", byteChar));
                    S1=S11+S12;
                    Log.e("x Binary Format S1",String.format("%16s", Integer.toBinaryString(S1)).replace(' ', '0'));

                }

                if (i == 2) {
                    S21=(byteChar<<8);
                    //Log.v(TAG, String.format("%02X ", S21));
                    Log.e("Binary Format S21",String.format("%16s", Integer.toBinaryString(S21)).replace(' ', '0'));
                    stringBuilder.append(String.format("%02X ", byteChar));
                }

                if (i == 3) {
                    S22=byteChar & 0xff;
                    //Log.v(TAG, String.format("%02X ", S22));
                    Log.e("Binary Format S22",String.format("%16s", Integer.toBinaryString(S22)).replace(' ', '0'));
                    stringBuilder.append(String.format("%02X ", byteChar));
                    S2=S21+S22;
                    Log.e("x Binary Format S2",String.format("%16s", Integer.toBinaryString(S2)).replace(' ', '0'));
                }

                if (i == 4) {
                    S31=(byteChar<<8);
                    //Log.v(TAG, String.format("%02X ", S31));
                    Log.e("Binary Format S31",String.format("%16s", Integer.toBinaryString(S31)).replace(' ', '0'));
                    stringBuilder.append(String.format("%02X ", byteChar));
                }

                if (i == 5) {
                    S32=byteChar & 0xff;
                    //Log.v(TAG, String.format("%02X ", S32));
                    Log.e("Binary Format S32",String.format("%16s", Integer.toBinaryString(S32)).replace(' ', '0'));
                    stringBuilder.append(String.format("%02X ", byteChar));
                    S3=S31+S32;
                    Log.e("x Binary Format S3",String.format("%16s", Integer.toBinaryString(S3)).replace(' ', '0'));

                }

                if (i == 6) {
                    S41=(byteChar<<8);
                    //Log.v(TAG, String.format("%02X ", S41));
                    Log.e("Binary Format S41",String.format("%16s", Integer.toBinaryString(S41)).replace(' ', '0'));
                    stringBuilder.append(String.format("%02X ", byteChar));
                }

                if (i == 7) {
                    S42=byteChar & 0xff;
                    //Log.v(TAG, String.format("%02X ", S42));
                    Log.e("Binary Format S42",String.format("%16s", Integer.toBinaryString(S42)).replace(' ', '0'));
                    stringBuilder.append(String.format("%02X ", byteChar));
                    S4=S41+S42;
                    Log.e("x Binary Format S4",String.format("%16s", Integer.toBinaryString(S4)).replace(' ', '0'));

                }

                if (i == 8) {
                    S51=(byteChar<<8);
                   // Log.v(TAG, String.format("%02X ", S51));
                    Log.e("Binary Format S51",String.format("%16s", Integer.toBinaryString(S51)).replace(' ', '0'));
                    stringBuilder.append(String.format("%02X ", byteChar));
                }

                if (i == 9) {
                    S52=byteChar & 0xff;
                   // Log.v(TAG, String.format("%02X ", S52));
                    Log.e("Binary Format S52",String.format("%16s", Integer.toBinaryString(S52)).replace(' ', '0'));
                    stringBuilder.append(String.format("%02X ", byteChar));
                    S5=S51+S52;
                    Log.e("x Binary Format S5",String.format("%16s", Integer.toBinaryString(S5)).replace(' ', '0'));

                }

                if (i == 10) {
                    S61=(byteChar<<8);
                   // Log.v(TAG, String.format("%02X ", S61));
                    Log.e("Binary Format S61",String.format("%16s", Integer.toBinaryString(S61)).replace(' ', '0'));
                    stringBuilder.append(String.format("%02X ", byteChar));
                }

                if (i == 11) {
                    S62=byteChar & 0xff;
                    // Log.v(TAG, String.format("%02X ", S52));
                    Log.e("Binary Format S62",String.format("%16s", Integer.toBinaryString(S62)).replace(' ', '0'));
                    stringBuilder.append(String.format("%02X ", byteChar));
                    S6=S61+S62;
                    Log.e("x Binary Format S6",String.format("%16s", Integer.toBinaryString(S6)).replace(' ', '0'));
                }

                if (i == 12) {
                    S71=(byteChar<<8);
                 //   Log.v(TAG, String.format("%02X ", S71));
                    Log.e("Binary Format S71",String.format("%16s", Integer.toBinaryString(S71)).replace(' ', '0'));
                    stringBuilder.append(String.format("%02X ", byteChar));
                }

                if (i == 13) {
                    S72=byteChar & 0xff;
                    Log.v(TAG, String.format("%02X ", S72));
                    Log.e("Binary Format S72",String.format("%16s", Integer.toBinaryString(S72)).replace(' ', '0'));
                    stringBuilder.append(String.format("%02X ", byteChar));
                    S7=S71+S72;
                    Log.e("x Binary Format S7",String.format("%16s", Integer.toBinaryString(S7)).replace(' ', '0'));
                }



                //Log.v(TAG, String.format("%02X ", byteChar));

                i++;
            }








            intent.putExtra(EXTRA_DATA, new String(String.valueOf(S1))+"; "+new String(String.valueOf(S2))+"; "+new String(String.valueOf(S3))+"; "+new String(String.valueOf(S4))+"; "+new String(String.valueOf(S5))+"; "+new String(String.valueOf(S6))+"; "+new String(String.valueOf(S7)) + "\n" );
        }

        sendBroadcast(intent);
    }

    public class LocalBinder extends Binder {
        BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     *
     * @return Return true if the connection is initiated successfully. The connection result
     *         is reported asynchronously through the
     *         {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     *         callback.
     */
    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

        // This is specific to Heart Rate Measurement.
        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) return null;

        return mBluetoothGatt.getServices();
    }

    public void writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        mBluetoothGatt.writeCharacteristic(characteristic);
    }
}
