/*
 * Author: Andrei Vasiliu <andrei.vasiliu@intel.com>
 * Copyright (c) 2016 Intel Corporation.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package tinyb;

import java.util.*;
import java.time.Duration;

public class BluetoothManager
{
    private long nativeInstance;
    private static BluetoothManager inst;

    static {
        try {
            System.loadLibrary("javatinyb");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load.\n" + e);
            System.exit(-1);
        }
    }

    public native BluetoothType getBluetoothType();

    private native BluetoothObject find(int type, String name, String identifier, BluetoothObject parent, long milliseconds);

    /** Find a BluetoothObject of a type matching type. If parameters name,
      * identifier and parent are not null, the returned object will have to
      * match them.
      * It will first check for existing objects. It will not turn on discovery
      * or connect to devices.
      * @parameter type specify the type of the object you are
      * waiting for, NONE means anything.
      * @parameter name optionally specify the name of the object you are
      * waiting for (for Adapter or Device)
      * @parameter identifier optionally specify the identifier of the object you are
      * waiting for (UUID for GattService, GattCharacteristic or GattDescriptor, address
      * for Adapter or Device)
      * @parameter parent optionally specify the parent of the object you are
      * waiting for
      * @parameter timeout the function will return after timeout time, a
      * value of zero means wait forever. If object is not found during this time null will be returned.
      * @return An object matching the name, identifier, parent or null if not found before
      * timeout expires or event is canceled.
      */
    public BluetoothObject find(BluetoothType type, String name, String identifier, BluetoothObject parent, Duration timeout) {
        return find(type.ordinal(), name, identifier, parent, timeout.toNanos() / 1000000);
    }


    /** Find a BluetoothObject of a type matching type. If parameters name,
      * identifier and parent are not null, the returned object will have to
      * match them.
      * It will first check for existing objects. It will not turn on discovery
      * or connect to devices.
      * @parameter type specify the type of the object you are
      * waiting for, NONE means anything.
      * @parameter name optionally specify the name of the object you are
      * waiting for (for Adapter or Device)
      * @parameter identifier optionally specify the identifier of the object you are
      * waiting for (UUID for GattService, GattCharacteristic or GattDescriptor, address
      * for Adapter or Device)
      * @parameter parent optionally specify the parent of the object you are
      * waiting for
      * @return An object matching the name, identifier and parent.
      */
    public BluetoothObject find(BluetoothType type, String name, String identifier, BluetoothObject parent) {
        return find(type, name, identifier, parent, Duration.ZERO);
    }

    /** Find a BluetoothObject of type T. If parameters name, identifier and
      * parent are not null, the returned object will have to match them.
      * It will first check for existing objects. It will not turn on discovery
      * or connect to devices.
      * @parameter name optionally specify the name of the object you are
      * waiting for (for Adapter or Device)
      * @parameter identifier optionally specify the identifier of the object you are
      * waiting for (UUID for GattService, GattCharacteristic or GattDescriptor, address
      * for Adapter or Device)
      * @parameter parent optionally specify the parent of the object you are
      * waiting for
      * @parameter timeout the function will return after timeout time, a
      * value of zero means wait forever. If object is not found during this time null will be returned.
      * @return An object matching the name, identifier, parent or null if not found before
      * timeout expires or event is canceled.
      */
    public <T extends BluetoothObject>  T find(String name, String identifier, BluetoothObject parent, Duration timeout) {
        return (T) find(T.class_type().ordinal(), name, identifier, parent, timeout.toNanos() / 1000000);
    }

    /** Find a BluetoothObject of type T. If parameters name, identifier and
      * parent are not null, the returned object will have to match them.
      * It will first check for existing objects. It will not turn on discovery
      * or connect to devices.
      * @parameter name optionally specify the name of the object you are
      * waiting for (for Adapter or Device)
      * @parameter identifier optionally specify the identifier of the object you are
      * waiting for (UUID for GattService, GattCharacteristic or GattDescriptor, address
      * for Adapter or Device)
      * @parameter parent optionally specify the parent of the object you are
      * waiting for
      * @return An object matching the name, identifier and parent.
      */
    public <T extends BluetoothObject>  T find(String name, String identifier, BluetoothObject parent) {
        return (T) find(name, identifier, parent, Duration.ZERO);
    }

    /** Return a BluetoothObject of a type matching type. If parameters name,
      * identifier and parent are not null, the returned object will have to
      * match them. Only objects which are already in the system will be returned.
      * @parameter type specify the type of the object you are
      * waiting for, NONE means anything.
      * @parameter name optionally specify the name of the object you are
      * waiting for (for Adapter or Device)
      * @parameter identifier optionally specify the identifier of the object you are
      * waiting for (UUID for GattService, GattCharacteristic or GattDescriptor, address
      * for Adapter or Device)
      * @parameter parent optionally specify the parent of the object you are
      * waiting for
      * @return An object matching the name, identifier, parent or null if not found.
      */
    public BluetoothObject getObject(BluetoothType type, String name,
                                String identifier, BluetoothObject parent) {
        return getObject(type.ordinal(), name, identifier, parent);
    }
    private native BluetoothObject getObject(int type, String name,
                                    String identifier, BluetoothObject parent);

    /** Return a List of BluetoothObject of a type matching type. If parameters name,
      * identifier and parent are not null, the returned object will have to
      * match them. Only objects which are already in the system will be returned.
      * @parameter type specify the type of the object you are
      * waiting for, NONE means anything.
      * @parameter name optionally specify the name of the object you are
      * waiting for (for Adapter or Device)
      * @parameter identifier optionally specify the identifier of the object you are
      * waiting for (UUID for GattService, GattCharacteristic or GattDescriptor, address
      * for Adapter or Device)
      * @parameter parent optionally specify the parent of the object you are
      * waiting for
      * @return A vector of object matching the name, identifier, parent.
      */
    public List<BluetoothObject> getObjects(BluetoothType type, String name,
                                    String identifier, BluetoothObject parent) {
        return getObjects(type.ordinal(), name, identifier, parent);
    }
    private native List<BluetoothObject> getObjects(int type, String name,
                                    String identifier, BluetoothObject parent);

    /** Returns a list of BluetoothAdapters available in the system
      * @return A list of BluetoothAdapters available in the system
      */
    public native List<BluetoothAdapter> getAdapters();

    /** Returns a list of discovered BluetoothDevices
      * @return A list of discovered BluetoothDevices
      */
    public native List<BluetoothDevice> getDevices();

    /** Returns a list of available BluetoothGattServices
      * @return A list of available BluetoothGattServices
      */
    public native List<BluetoothGattService> getServices();

    /** Sets a default adapter to use for discovery.
      * @return TRUE if the device was set
      */
    public native boolean setDefaultAdapter(BluetoothAdapter adapter);

    /** Turns on device discovery on the default adapter if it is disabled.
      * @return TRUE if discovery was successfully enabled
      */
    public native boolean startDiscovery();

    /** Turns off device discovery on the default adapter if it is enabled.
      * @return TRUE if discovery was successfully disabled
      */
    public native boolean stopDiscovery();

    private native void init();
    private native void delete();
    private BluetoothManager()
    {
        init();
    }

    /** Returns an instance of BluetoothManager, to be used instead of constructor.
      * @return An initialized BluetoothManager instance.
      */
    public static synchronized BluetoothManager getBluetoothManager()
    {
        if (inst == null)
        {
            inst = new BluetoothManager();
            inst.init();
        }
        return inst;
    }

    protected void finalize()
    {
        delete();
    }
}
