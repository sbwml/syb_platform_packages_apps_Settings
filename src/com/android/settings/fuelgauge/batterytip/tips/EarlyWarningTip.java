/*
 * Copyright (C) 2018 The Android Open Source Project
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

package com.android.settings.fuelgauge.batterytip.tips;

import android.content.Context;
import android.os.Parcel;

import com.android.settings.R;

/**
 * Tip to show early warning if battery couldn't make to usual charging time
 */
public class EarlyWarningTip extends BatteryTip {
    private boolean mPowerSaveModeOn;

    public EarlyWarningTip(@StateType int state, boolean powerSaveModeOn) {
        super(TipType.BATTERY_SAVER, state, false /* showDialog */);
        mPowerSaveModeOn = powerSaveModeOn;
    }

    public EarlyWarningTip(Parcel in) {
        super(in);
        mPowerSaveModeOn = in.readBoolean();
    }

    @Override
    public CharSequence getTitle(Context context) {
        return context.getString(
                mState == StateType.HANDLED
                        ? R.string.battery_tip_early_heads_up_done_title
                        : R.string.battery_tip_early_heads_up_title);
    }

    @Override
    public CharSequence getSummary(Context context) {
        return context.getString(
                mState == StateType.HANDLED
                        ? R.string.battery_tip_early_heads_up_done_summary
                        : R.string.battery_tip_early_heads_up_summary);
    }

    @Override
    public int getIconId() {
        return mState == StateType.HANDLED
                ? R.drawable.ic_perm_device_information_green_24dp
                : R.drawable.ic_battery_alert_24dp;
    }

    @Override
    public void updateState(BatteryTip tip) {
        final EarlyWarningTip earlyHeadsUpTip = (EarlyWarningTip) tip;
        if (mPowerSaveModeOn != earlyHeadsUpTip.mPowerSaveModeOn) {
            mPowerSaveModeOn = earlyHeadsUpTip.mPowerSaveModeOn;
            mState = earlyHeadsUpTip.mPowerSaveModeOn ? StateType.HANDLED : StateType.NEW;
        } else if (mState != StateType.HANDLED) {
            mState = earlyHeadsUpTip.getState();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeBoolean(mPowerSaveModeOn);
    }

    public boolean isPowerSaveModeOn() {
        return mPowerSaveModeOn;
    }

    public static final Creator CREATOR = new Creator() {
        public BatteryTip createFromParcel(Parcel in) {
            return new EarlyWarningTip(in);
        }

        public BatteryTip[] newArray(int size) {
            return new EarlyWarningTip[size];
        }
    };
}