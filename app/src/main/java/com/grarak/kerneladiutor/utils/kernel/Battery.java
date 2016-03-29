/*
 * Copyright (C) 2015 Willi Ye
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

package com.grarak.kerneladiutor.utils.kernel;

import android.content.Context;

import com.grarak.kerneladiutor.R;
import com.grarak.kerneladiutor.utils.Constants;
import com.grarak.kerneladiutor.utils.Utils;
import com.grarak.kerneladiutor.utils.root.Control;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willi on 03.01.15.
 */
public class Battery implements Constants {

    private static String FORCEFASTCHARGE_FILE;

    public static void setChargingRate(int value, Context context) {
        Control.runCommand(String.valueOf(value), CUSTOM_CHARGING_RATE, Control.CommandType.GENERIC, context);
    }

    public static int getChargingRate() {
        return Utils.stringToInt(Utils.readFile(CUSTOM_CHARGING_RATE));
    }

    public static boolean hasChargingRate() {
        return Utils.existFile(CUSTOM_CHARGING_RATE);
    }

    public static void activateCustomChargeRate(boolean active, Context context) {
        Control.runCommand(active ? "1" : "0", CHARGE_RATE_ENABLE, Control.CommandType.GENERIC, context);
    }

    public static boolean isCustomChargeRateActive() {
        return Utils.readFile(CHARGE_RATE_ENABLE).equals("1");
    }

    public static boolean hasCustomChargeRateEnable() {
        return Utils.existFile(CHARGE_RATE_ENABLE);
    }

    public static boolean hasChargeRate() {
        return Utils.existFile(CHARGE_RATE);
    }

    public static void setBlx(int value, Context context) {
        Control.runCommand(String.valueOf(value), BLX, Control.CommandType.GENERIC, context);
    }

    public static int getCurBlx() {
        return Utils.stringToInt(Utils.readFile(BLX));
    }

    public static boolean hasBlx() {
        return Utils.existFile(BLX);
    }

    public static void setForceFastCharge(int value, Context context) {
        Control.runCommand(String.valueOf(value), FORCEFASTCHARGE_FILE, Control.CommandType.GENERIC, context);
    }

    public static int getForceFastChargeValue() {
        return Utils.stringToInt(Utils.readFile(FORCEFASTCHARGE_FILE));
    }

    public static List<String> getForceFastChargeMenu(Context context) {
        List<String> list = new ArrayList<>();
        if (FORCEFASTCHARGE_FILE != null) {
            list.add(context.getString(R.string.disabled));
            list.add(context.getString(R.string.enabled));
            list.add(context.getString(R.string.custom_enabled));
        }
        return list;
    }

    public static boolean hasForceFastCharge() {
        if (FORCEFASTCHARGE_FILE == null)
            for (String file : BATTERY_ARRAY)
                if (Utils.existFile(file)) {
                    FORCEFASTCHARGE_FILE = file;
                    return true;
                }
        return FORCEFASTCHARGE_FILE != null;
    }

    public static void setAcLevel(int value, Context context) {
    Control.runCommand(String.valueOf(value), AC_LEVEL, Control.CommandType.GENERIC, context);
    }

    public static int getAcLevel() {
        return Utils.stringToInt(Utils.readFile(AC_LEVEL));
    }

    public static boolean hasAcLevel() {
        return Utils.existFile(AC_LEVEL);
    }

    public static void setUsbLevel(int value, Context context) {
    Control.runCommand(String.valueOf(value), USB_LEVEL, Control.CommandType.GENERIC, context);
    }

    public static int getUsbLevel() {
        return translate_value_to_position(Utils.stringToInt(Utils.readFile(USB_LEVEL)));
    }

    public static boolean hasUsbLevel() {
        return Utils.existFile(USB_LEVEL);
    }

    private static int translate_value_to_position(int value){		
        int result = 0;		
        switch (value){		
            case 460:		
                result = 0;		
                break;		
            case 500:		
                result = 1;		
                break;		
            case 600:		
                result = 2;		
                break;		
            case 700:		
                result = 3;		
                break;		
            case 800:		
                result = 4;		
                break;		
            case 900:		
                result = 5;		
                break;		
            case 1000:		
                result = 6;		
                break;		
            default:		
                result = 460;		
                break;		
        }		
        return result;		
    }

    public static void activateMtp(boolean active, Context context) {
        String command = active ? "1" : "0";
        Control.runCommand(command, MTP, Control.CommandType.GENERIC, context);
    }

    public static boolean isMtpActive() {
        String value = Utils.readFile(MTP);
        return value.equals("1");
    }

    public static boolean hasMtp() {
        return Utils.existFile(MTP);
    }

    public static void activateScreenOn(boolean active, Context context) {
        String command = active ? "1" : "0";
        Control.runCommand(command, SCREEN_ON, Control.CommandType.GENERIC, context);
    }

    public static boolean isScreenOnActive() {
        String value = Utils.readFile(SCREEN_ON);
        return value.equals("1");
    }

    public static boolean hasScreenOn() {
        return Utils.existFile(SCREEN_ON);
    }

    public static void activateFailsafe(boolean active, Context context) {
        String command = active ? "1" : "0";
        Control.runCommand(command, FAILSAFE, Control.CommandType.GENERIC, context);
    }

    public static boolean isFailsafeActive() {
        String value = Utils.readFile(FAILSAFE);
        return value.equals("1");
    }

    public static boolean hasFailsafe() {
        return Utils.existFile(FAILSAFE);
    }

}
