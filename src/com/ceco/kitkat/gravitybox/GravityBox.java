/*
 * Copyright (C) 2013 Peter Gregus for GravityBox Project (C3C076@xda)
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

package com.ceco.kitkat.gravitybox;

import android.os.Build;
import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class GravityBox implements IXposedHookZygoteInit, IXposedHookInitPackageResources, IXposedHookLoadPackage {
    public static final String PACKAGE_NAME = GravityBox.class.getPackage().getName();
    public static String MODULE_PATH = null;
    private static XSharedPreferences prefs;

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        MODULE_PATH = startupParam.modulePath;
        prefs = new XSharedPreferences(PACKAGE_NAME);
        prefs.makeWorldReadable();

        XposedBridge.log("GB:Hardware: " + Build.HARDWARE);
        XposedBridge.log("GB:Product: " + Build.PRODUCT);
        XposedBridge.log("GB:Device manufacturer: " + Build.MANUFACTURER);
        XposedBridge.log("GB:Device brand: " + Build.BRAND);
        XposedBridge.log("GB:Device model: " + Build.MODEL);
        XposedBridge.log("GB:Device type: " + (Utils.isTablet() ? "tablet" : "phone"));
        XposedBridge.log("GB:Is MTK device: " + Utils.isMtkDevice());
        XposedBridge.log("GB:Is Xperia device: " + Utils.isXperiaDevice());
        XposedBridge.log("GB:Is Moto XT device: " + Utils.isMotoXtDevice());
        XposedBridge.log("GB:Has telephony support: " + Utils.hasTelephonySupport());
        XposedBridge.log("GB:Has Gemini support: " + Utils.hasGeminiSupport());
        XposedBridge.log("GB:Android SDK: " + Build.VERSION.SDK_INT);
        XposedBridge.log("GB:Android Release: " + Build.VERSION.RELEASE);
        XposedBridge.log("GB:ROM: " + Build.DISPLAY);

        SystemWideResources.initResources(prefs);

        // Common
        ModElectronBeam.initZygote(prefs);
        ModVolumeKeySkipTrack.init(prefs);
        ModVolKeyCursor.initZygote(prefs);
        PhoneWrapper.initZygote(prefs);
        ModLowBatteryWarning.initZygote(prefs);
        ModDisplay.initZygote(prefs);
        ModAudio.initZygote(prefs);
        ModHwKeys.initZygote(prefs);
//        TODO: rework for KitKat compatibility
//        ModCallCard.initZygote();
//        ModPhone.initZygote(prefs);
        ModExpandedDesktop.initZygote(prefs);
        ConnectivityServiceWrapper.initZygote();
    }

    @Override
    public void handleInitPackageResources(InitPackageResourcesParam resparam) throws Throwable {

        if (resparam.packageName.equals(ModBatteryStyle.PACKAGE_NAME)) {
            ModBatteryStyle.initResources(prefs, resparam);
        }

        if (resparam.packageName.equals(ModStatusBar.PACKAGE_NAME)) {
            ModStatusBar.initResources(prefs, resparam);
        }

        if (resparam.packageName.equals(ModSettings.PACKAGE_NAME)) {
            ModSettings.initPackageResources(prefs, resparam);
        }

        if (resparam.packageName.equals(ModQuickSettings.PACKAGE_NAME)) {
            ModQuickSettings.initResources(prefs, resparam);
        }

        if (resparam.packageName.equals(ModLockscreen.PACKAGE_NAME)) {
            ModLockscreen.initPackageResources(prefs, resparam);
        }
    }

    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {

        if (lpparam.packageName.equals(SystemPropertyProvider.PACKAGE_NAME)) {
            SystemPropertyProvider.init(lpparam.classLoader);
        }

//        TODO: make AOSP compatible
//        if (lpparam.packageName.equals(ModAudioSettings.PACKAGE_NAME)) {
//            ModAudioSettings.init(prefs, lpparam.classLoader);
//        }

        // Common
        if (lpparam.packageName.equals(ModBatteryStyle.PACKAGE_NAME)) {
            ModBatteryStyle.init(prefs, lpparam.classLoader);
        }

        if (lpparam.packageName.equals(ModLowBatteryWarning.PACKAGE_NAME)) {
            ModLowBatteryWarning.init(prefs, lpparam.classLoader);
        }

        if (lpparam.packageName.equals(ModClearAllRecents.PACKAGE_NAME)) {
            ModClearAllRecents.init(prefs, lpparam.classLoader);
        }

        if (lpparam.packageName.equals(ModPowerMenu.PACKAGE_NAME)) {
            ModPowerMenu.init(prefs, lpparam.classLoader);
        }

//        TODO: rework for KitKat compatibility
//        if (lpparam.packageName.equals(ModCallCard.PACKAGE_NAME)) {
//            ModCallCard.init(prefs, lpparam.classLoader);
//        }

        if (lpparam.packageName.equals(ModQuickSettings.PACKAGE_NAME)) {
            ModQuickSettings.init(prefs, lpparam.classLoader);
        }

        if (lpparam.packageName.equals(ModStatusbarColor.PACKAGE_NAME)) {
            ModStatusbarColor.init(prefs, lpparam.classLoader);
        }

        if (lpparam.packageName.equals(ModStatusBar.PACKAGE_NAME)) {
            ModStatusBar.init(prefs, lpparam.classLoader);
        }

//        TODO: rework for KitKat compatibility
//        if (lpparam.packageName.equals(ModPhone.PACKAGE_NAME) &&
//                Utils.hasTelephonySupport()) {
//            ModPhone.init(prefs, lpparam.classLoader);
//        }

        if (lpparam.packageName.equals(ModSettings.PACKAGE_NAME)) {
            ModSettings.init(prefs, lpparam.classLoader);
        }

        if (lpparam.packageName.equals(ModVolumePanel.PACKAGE_NAME)) {
            ModVolumePanel.init(prefs, lpparam.classLoader);
        }

        if (lpparam.packageName.equals(ModPieControls.PACKAGE_NAME)) {
            ModPieControls.init(prefs, lpparam.classLoader);
        }

        if (lpparam.packageName.equals(ModNavigationBar.PACKAGE_NAME)
                && prefs.getBoolean(GravityBoxSettings.PREF_KEY_NAVBAR_OVERRIDE, false)) {
            ModNavigationBar.init(prefs, lpparam.classLoader);
        }

        if (lpparam.packageName.equals(ModLockscreen.PACKAGE_NAME)) {
            ModLockscreen.init(prefs, lpparam.classLoader);
        }
    }
}