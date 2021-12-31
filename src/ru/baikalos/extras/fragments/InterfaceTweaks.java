/*
 * Copyright (C) 2014 The Dirty Unicorns Project
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

package ru.baikalos.extras.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.SystemProperties;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.SwitchPreference;
import android.provider.Settings;
import android.view.View;
import android.util.Log;

import android.content.res.Resources;

import ru.baikalos.extras.BaseSettingsFragment;
import ru.baikalos.extras.PerfProfileDetailsActivity;
import ru.baikalos.extras.R;

import com.aicp.gear.preference.SeekBarPreferenceCham;
import com.aicp.gear.preference.SecureSettingSeekBarPreference;
import com.aicp.gear.preference.SystemSettingColorPickerPreference;

public class InterfaceTweaks extends BaseSettingsFragment {

    private static final String TAG = "InterfaceTweaks";

    private static final String SYSTEM_TWEAKS_SEC_HWC= "system_tweaks_sec_hwc";
    private static final String SYSTEM_PROPERTY_SEC_HWC = "persist.sys.sf.disable_sec_hwc";
    private static final String SYSTEM_TWEAKS_DLSB = "baikalos_dlsb_enabled";

    private static final String PULSE_AMBIENT_LIGHT_COLOR_MODE = "pulse_ambient_light_color_mode";
    private static final String PULSE_AMBIENT_LIGHT_COLOR = "pulse_ambient_light_color";
    private static final String PULSE_AMBIENT_LIGHT = "pulse_ambient_light";

    private Context mContext;

    private SwitchPreference mDisableSecHwc;

    // Rounded Corners
    private static final String SYSUI_ROUNDED_SIZE = "sysui_rounded_size";
    private static final String SYSUI_ROUNDED_CONTENT_PADDING = "sysui_rounded_content_padding";
    private static final String SYSUI_ROUNDED_FWVALS = "sysui_rounded_fwvals";

    private SecureSettingSeekBarPreference mCornerRadius;
    private SecureSettingSeekBarPreference mContentPadding;
    private SwitchPreference mRoundedFwvals;


    private SwitchPreference mEdgeLightEnabledPref;
    private SystemSettingColorPickerPreference mEdgeLightColorPref;
    private ListPreference mEdgeLightColorModePref;

    @Override
    protected int getPreferenceResource() {
        return R.xml.interface_tweaks;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = (Context) getActivity();
        final Resources res = getActivity().getResources();

        final PreferenceScreen screen = getPreferenceScreen();

            mDisableSecHwc = (SwitchPreference) findPreference(SYSTEM_TWEAKS_SEC_HWC);
            if( mDisableSecHwc != null ) { 
                mDisableSecHwc.setChecked(SystemProperties.getBoolean(SYSTEM_PROPERTY_SEC_HWC, false));
                mDisableSecHwc.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        try {
                            Log.e(TAG, "mDisableSecHwc: disable=" + newValue.toString());
                            ((SwitchPreference)preference).setChecked((Boolean) newValue);
                            SystemProperties.set(SYSTEM_PROPERTY_SEC_HWC, newValue.toString());
                        } catch(Exception re) {
                            Log.e(TAG, "onCreate: mDisableSecHwc Fatal! exception", re );
                        }
                        return true;
                    }
                });
            }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setSystemPropertyString(String key, String value) {
        Log.e(TAG, "setSystemPropertyBoolean: key=" + key + ", value=" + value);
        SystemProperties.set(key, value);
    }

    private String getSystemPropertyString(String key, String def) {
        return SystemProperties.get(key,def);
    }

    private boolean getSystemPropertyBoolean(String key) {
        if( SystemProperties.get(key,"0").equals("1") || SystemProperties.get(key,"0").equals("true") ) return true;
	    return false;
    }

    private void setSystemPropertyBoolean(String key, boolean value) {
        String text = value?"1":"0";
        Log.e(TAG, "setSystemPropertyBoolean: key=" + key + ", value=" + value);
        SystemProperties.set(key, text);
    }

}
