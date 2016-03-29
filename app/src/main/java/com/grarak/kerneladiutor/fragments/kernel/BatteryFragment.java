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

package com.grarak.kerneladiutor.fragments.kernel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;

import com.grarak.kerneladiutor.R;
import com.grarak.kerneladiutor.elements.DDivider;
import com.grarak.kerneladiutor.elements.cards.PopupCardView;
import com.grarak.kerneladiutor.elements.cards.CardViewItem;
import com.grarak.kerneladiutor.elements.cards.SeekBarCardView;
import com.grarak.kerneladiutor.elements.cards.SwitchCardView;
import com.grarak.kerneladiutor.elements.cards.UsageCardView;
import com.grarak.kerneladiutor.fragments.RecyclerViewFragment;
import com.grarak.kerneladiutor.utils.Utils;
import com.grarak.kerneladiutor.utils.kernel.Battery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willi on 03.01.15.
 */
public class BatteryFragment extends RecyclerViewFragment implements PopupCardView.DPopupCard.OnDPopupCardListener,
        SwitchCardView.DSwitchCard.OnDSwitchCardListener,
        SeekBarCardView.DSeekBarCard.OnDSeekBarCardListener {

    private UsageCardView.DUsageCard mBatteryLevelCard;
    private CardViewItem.DCardView mBatteryVoltageCard, mBatteryTemperature;

    private PopupCardView.DPopupCard mForceFastChargeCard;
    private SeekBarCardView.DSeekBarCard mAcLevelCard;
    private SeekBarCardView.DSeekBarCard mUsbLevelCard;
    private SwitchCardView.DSwitchCard mMtpCard;
    private SwitchCardView.DSwitchCard mScreenOnCard;
    private SwitchCardView.DSwitchCard mFailsafeCard;

    private SeekBarCardView.DSeekBarCard mBlxCard;

    private SwitchCardView.DSwitchCard mCustomChargeRateEnableCard;
    private SeekBarCardView.DSeekBarCard mChargingRateCard;

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        batteryLevelInit();
        batteryVoltageInit();
        batteryTemperatureInit();
        if (Battery.hasForceFastCharge()) forceFastChargeInit();
        if (Battery.hasAcLevel()) acLevelInit();
        if (Battery.hasUsbLevel()) usbLevelInit();
        if (Battery.hasMtp()) mtpInit();
        if (Battery.hasScreenOn()) screenOnInit();
        if (Battery.hasFailsafe()) failsafeInit();
        if (Battery.hasBlx()) blxInit();
        if (Battery.hasChargeRate()) chargerateInit();

        try {
            getActivity().registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        } catch (NullPointerException ignored) {
        }
    }

    @Override
    public void postInit(Bundle savedInstanceState) {
        super.postInit(savedInstanceState);
        if (getCount() < 4) showApplyOnBoot(false);
    }

    private void batteryLevelInit() {
        mBatteryLevelCard = new UsageCardView.DUsageCard();
        mBatteryLevelCard.setText(getString(R.string.battery_level));

        addView(mBatteryLevelCard);
    }

    private void batteryVoltageInit() {
        mBatteryVoltageCard = new CardViewItem.DCardView();
        mBatteryVoltageCard.setTitle(getString(R.string.battery_voltage));

        addView(mBatteryVoltageCard);
    }

    private void batteryTemperatureInit() {
        mBatteryTemperature = new CardViewItem.DCardView();
        mBatteryTemperature.setTitle(getString(R.string.battery_temperature));

        addView(mBatteryTemperature);
    }

    private void forceFastChargeInit() {
        DDivider mChargeDivider = new DDivider();
        mChargeDivider.setText(getString(R.string.usb_charge));

        addView(mChargeDivider);
        mForceFastChargeCard = new PopupCardView.DPopupCard(Battery.getForceFastChargeMenu(getActivity()));
        mForceFastChargeCard.setTitle(getString(R.string.usb_fast_charge));
        mForceFastChargeCard.setDescription(getString(R.string.usb_fast_charge_summary));
        mForceFastChargeCard.setItem(Battery.getForceFastChargeValue());
        mForceFastChargeCard.setOnDPopupCardListener(this);

        addView(mForceFastChargeCard);
    }

    private void acLevelInit() {
        List<String> list = new ArrayList<>();
        for (int i = 10; i < 22; i++)
            list.add((i * 100) + getString(R.string.ma));

        mAcLevelCard = new SeekBarCardView.DSeekBarCard(list);
        mAcLevelCard.setTitle(getString(R.string.ac_level));
        mAcLevelCard.setDescription(getString(R.string.ac_level_summary));
        mAcLevelCard.setProgress((Battery.getAcLevel() /100) - 10);
        mAcLevelCard.setOnDSeekBarCardListener(this);

        addView(mAcLevelCard);
    }

    private void usbLevelInit() {
        List<String> list = new ArrayList<>();
            list.add(String.valueOf(460) + getString(R.string.ma));
        for (int i = 5; i < 11; i++)
            list.add((i * 100) + getString(R.string.ma));

        mUsbLevelCard = new SeekBarCardView.DSeekBarCard(list);
        mUsbLevelCard.setTitle(getString(R.string.usb_level));
        mUsbLevelCard.setDescription(getString(R.string.usb_level_summary));
        mUsbLevelCard.setProgress(Battery.getUsbLevel());
        mUsbLevelCard.setOnDSeekBarCardListener(this);

        addView(mUsbLevelCard);
    }

    private void mtpInit() {
        DDivider mMtpDivider = new DDivider();
        mMtpDivider.setText(getString(R.string.misc_charge));

        addView(mMtpDivider);

        mMtpCard = new SwitchCardView.DSwitchCard();
        mMtpCard.setTitle(getString(R.string.use_mtp));
        mMtpCard.setDescription(getString(R.string.use_mtp_summary));
        mMtpCard.setChecked(Battery.isMtpActive());
        mMtpCard.setOnDSwitchCardListener(this);

        addView(mMtpCard);
    }

    private void screenOnInit() {

        mScreenOnCard = new SwitchCardView.DSwitchCard();
        mScreenOnCard.setTitle(getString(R.string.screen_on));
        mScreenOnCard.setDescription(getString(R.string.screen_on_summary));
        mScreenOnCard.setChecked(Battery.isScreenOnActive());
        mScreenOnCard.setOnDSwitchCardListener(this);

        addView(mScreenOnCard);
    }

    private void failsafeInit() {

        mFailsafeCard = new SwitchCardView.DSwitchCard();
        mFailsafeCard.setTitle(getString(R.string.failsafe));
        mFailsafeCard.setDescription(getString(R.string.failsafe_summary));
        mFailsafeCard.setChecked(Battery.isFailsafeActive());
        mFailsafeCard.setOnDSwitchCardListener(this);

        addView(mFailsafeCard);
    }

    private void blxInit() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 101; i++) list.add(String.valueOf(i));

        mBlxCard = new SeekBarCardView.DSeekBarCard(list);
        mBlxCard.setTitle(getString(R.string.blx));
        mBlxCard.setDescription(getString(R.string.blx_summary));
        mBlxCard.setProgress(Battery.getCurBlx());
        mBlxCard.setOnDSeekBarCardListener(this);

        addView(mBlxCard);
    }

    private void chargerateInit() {

        if (Battery.hasCustomChargeRateEnable()) {
            mCustomChargeRateEnableCard = new SwitchCardView.DSwitchCard();
            mCustomChargeRateEnableCard.setDescription(getString(R.string.custom_charge_rate));
            mCustomChargeRateEnableCard.setChecked(Battery.isCustomChargeRateActive());
            mCustomChargeRateEnableCard.setOnDSwitchCardListener(this);

            addView(mCustomChargeRateEnableCard);
        }

        if (Battery.hasChargingRate()) {
            List<String> list = new ArrayList<>();
            for (int i = 10; i < 151; i++) list.add((i * 10) + getString(R.string.ma));

            mChargingRateCard = new SeekBarCardView.DSeekBarCard(list);
            mChargingRateCard.setTitle(getString(R.string.charge_rate));
            mChargingRateCard.setDescription(getString(R.string.charge_rate_summary));
            mChargingRateCard.setProgress((Battery.getChargingRate() / 10) - 10);
            mChargingRateCard.setOnDSeekBarCardListener(this);

            addView(mChargingRateCard);
        }
    }

    private final BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
            int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);

            if (mBatteryLevelCard != null) mBatteryLevelCard.setProgress(level);
            if (mBatteryVoltageCard != null)
                mBatteryVoltageCard.setDescription(voltage + getString(R.string.mv));
            if (mBatteryTemperature != null) {
                double celsius = (double) temperature / 10;
                mBatteryTemperature.setDescription(Utils.formatCelsius(celsius) + " " + Utils.celsiusToFahrenheit(celsius));
            }
        }
    };

    @Override
    public void onChecked(SwitchCardView.DSwitchCard dSwitchCard, boolean checked) {
        if (dSwitchCard == mCustomChargeRateEnableCard)
            Battery.activateCustomChargeRate(checked, getActivity());
        else if (dSwitchCard == mMtpCard)
            Battery.activateMtp(checked, getActivity());
        else if (dSwitchCard == mScreenOnCard)
            Battery.activateScreenOn(checked, getActivity());
        else if (dSwitchCard == mFailsafeCard)
            Battery.activateFailsafe(checked, getActivity());
    }

    @Override
    public void onItemSelected(PopupCardView.DPopupCard dPopupCard, int position) {
        if (dPopupCard == mForceFastChargeCard) Battery.setForceFastCharge(position, getActivity());
    }

    @Override
    public void onChanged(SeekBarCardView.DSeekBarCard dSeekBarCard, int position) {
    }

    @Override
    public void onStop(SeekBarCardView.DSeekBarCard dSeekBarCard, int position) {
        if (dSeekBarCard == mBlxCard)
            Battery.setBlx(position, getActivity());
        else if (dSeekBarCard == mChargingRateCard)
            Battery.setChargingRate((position * 10) + 100, getActivity());
        else if (dSeekBarCard == mAcLevelCard) Battery.setAcLevel((position * 100) + 1000, getActivity());
        else if (dSeekBarCard == mUsbLevelCard) Battery.setUsbLevel(position_translate(position), getActivity());
    }

    private Integer position_translate(Integer position){		
        Integer result = 0;		
        switch (position){		
            case 0:		
                result = 460;		
                break;		
            case 1:		
                result = 500;		
                break;		
            case 2:		
                result = 600;		
                break;		
            case 3:		
                result = 700;		
                break;		
            case 4:		
                result = 800;		
                break;		
            case 5:		
                result = 900;		
                break;		
            case 6:		
                result = 1000;		
                break;			
        }		
        return result;		
		
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            getActivity().unregisterReceiver(mBatInfoReceiver);
        } catch (IllegalArgumentException ignored) {
        }
    }
}
