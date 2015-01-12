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

package com.timeszoro.edemacare;

import java.util.HashMap;
import java.util.UUID;

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
public class EdemaAttributes {
    private static HashMap<String, String> attributes = new HashMap();
    public static String EDEMA_MEASUREMENT = "0000ffa0-0000-1000-8000-00805f9b34fb";
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    public static String EDEMA_NOTUFY_MEASUREMENT = "0000ffa1-0000-1000-8000-00805f9b34fb";
    public static String EDEMA_IMPEDANCE_MEASUREMENT = "0000ffa3-0000-1000-8000-00805f9b34fb";
    public static String EDEMA_PHA_MEASUREMENT = "0000ffa4-0000-1000-8000-00805f9b34fb";
    public static String EDEMA_FRE_MEASUREMENT = "0000ffa5-0000-1000-8000-00805f9b34fb";


    public static UUID getUUID(String str){
        return UUID.fromString(str);
    }
}
