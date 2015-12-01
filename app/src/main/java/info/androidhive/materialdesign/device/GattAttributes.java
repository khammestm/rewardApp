/**
 * Created by rshir on 28.11.2015.

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

package info.androidhive.materialdesign.device;

import java.util.HashMap;

/**
 * This class includes a small subset of standard GATT attributes for Mi Band bracelet.
 */
public class GattAttributes {
    private static HashMap<String, String> attributes = new HashMap();
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    public static String MI_BAND_UUID = "0000fee0-0000-1000-8000-00805f9b34fb";
    public static String MI_STEP_MEASUREMENT = "0000ff06-0000-1000-8000-00805f9b34fb";

    static {
        // MI Band devices list.
        attributes.put(MI_BAND_UUID, "Band");
        attributes.put("0000180a-0000-1000-8000-00805f9b34fb", "Device Information Service");
        // MI Band Characteristics.
        attributes.put(MI_STEP_MEASUREMENT, "Step Measurement");
        attributes.put("00002a29-0000-1000-8000-00805f9b34fb", "Manufacturer Name String");
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}
