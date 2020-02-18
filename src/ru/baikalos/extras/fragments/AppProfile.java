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

import android.os.ServiceManager;
import android.os.RemoteException;


import android.content.res.Resources;

import ru.baikalos.extras.BaseSettingsFragment;
import ru.baikalos.extras.R;
import com.aicp.gear.preference.SeekBarPreferenceCham;

public class AppProfile extends BaseSettingsFragment
            implements Preference.OnPreferenceChangeListener {

    private static final String TAG = "ApplicationProfile";

    private static final String APP_PROFILE_RESTRICTED = "app_profile_restricted";
    private static final String APP_PROFILE_PERF = "app_profile_performance";
    private static final String APP_PROFILE_THERM = "app_profile_thermal";
    private static final String APP_PROFILE_BRIGHTNESS = "app_profile_brightness";
    private static final String APP_PROFILE_CAMERA_HAL1 = "app_profile_camera_hal1";
    private static final String APP_PROFILE_PINNED = "app_profile_pinned";
    private static final String APP_PROFILE_DISABLE_TWL = "app_profile_disable_twl";

    private String mPackageName;
    private Context mContext;

    private SwitchPreference mAppRestricted;
    private ListPreference mAppPerfProfile;
    private ListPreference mAppThermProfile;
    private ListPreference mAppBrightnessProfile;


    //IBaikalServiceController mBaikalService;

    public AppProfile(String packageName) {
        mPackageName = packageName; 
    }

    @Override
    protected int getPreferenceResource() {
        return R.xml.app_profile;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = (Context) getActivity();
        final Resources res = getActivity().getResources();


        boolean perfProf  = SystemProperties.get("baikal.eng.perf", "0").equals("1") ||
                            SystemProperties.get("spectrum.support", "0").equals("1");

        boolean thermProf  = SystemProperties.get("baikal.eng.therm", "0").equals("1");

        try {

        mAppRestricted = (SwitchPreference) findPreference(APP_PROFILE_RESTRICTED);
        if( mAppRestricted != null ) { 
            //mAppRestricted.setChecked(mBaikalService.isAppRestrictedProfile(mPackageName));
            mAppRestricted.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
              public boolean onPreferenceChange(Preference preference, Object newValue) {
                //int val = Integer.parseInt(newValue.toString());
                //DiracAudioEnhancerService.du.setHeadsetType(mContext, val);
                try {
                    //mBaikalService.setAppPriority(mPackageName, ((Boolean)newValue) ? -1 : 0 );
                    Log.e(TAG, "mAppRestricted: mPackageName=" + mPackageName + ",setRestricted=" + (Boolean)newValue);
                } catch(Exception re) {
                    Log.e(TAG, "onCreate: mAppRestricted Fatal! exception", re );
                }
                return true;
              }
            });
        }


        //initBaikalAppOp(APP_PROFILE_PINNED,BaikalServiceManager.OP_PINNED);
        //initBaikalAppOp(APP_PROFILE_DISABLE_TWL,BaikalServiceManager.OP_DISABLE_TWL);

        mAppPerfProfile = (ListPreference) findPreference(APP_PROFILE_PERF);
        if( mAppPerfProfile != null ) { 
            if(!perfProf) {
                mAppPerfProfile.setVisible(false);
            } else {
                //String profile = mBaikalService.getAppPerfProfile(mPackageName);
                //Log.e(TAG, "getAppPerfProfile: mPackageName=" + mPackageName + ",getProfile=" + profile);
                //mAppPerfProfile.setValue(profile);
                mAppPerfProfile.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                  public boolean onPreferenceChange(Preference preference, Object newValue) {
                    try {
                        //mBaikalService.setAppPerfProfile(mPackageName, newValue.toString() );
                        Log.e(TAG, "mAppPerfProfile: mPackageName=" + mPackageName + ",setProfile=" + newValue.toString());
                    } catch(Exception re) {
                        Log.e(TAG, "onCreate: mAppPerfProfile Fatal! exception", re );
                    }
                    return true;
                  }
                });
            }
        }

        mAppThermProfile = (ListPreference) findPreference(APP_PROFILE_THERM);
        if( mAppThermProfile != null ) {
            if(!thermProf) {
                mAppThermProfile.setVisible(false);
            } else {
                //String profile = mBaikalService.getAppThermProfile(mPackageName);
                //Log.e(TAG, "getAppThermProfile: mPackageName=" + mPackageName + ",getProfile=" + profile);
                //mAppThermProfile.setValue(profile);
                mAppThermProfile.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                  public boolean onPreferenceChange(Preference preference, Object newValue) {
                    try {
                        //mBaikalService.setAppThermProfile(mPackageName, newValue.toString() );
                        Log.e(TAG, "mAppThermProfile: mPackageName=" + mPackageName + ",setProfile=" + newValue.toString());
                    } catch(Exception re) {
                        Log.e(TAG, "onCreate: mAppThermProfile Fatal! exception", re );
                    }
                    return true;
                  }
                });
            }
        }

            mAppBrightnessProfile = (ListPreference) findPreference(APP_PROFILE_BRIGHTNESS);
            if( mAppBrightnessProfile != null ) {
                //int brightness = mBaikalService.getAppBrightness(mPackageName);
                //Log.e(TAG, "getAppBrightness: mPackageName=" + mPackageName + ",brightness=" + brightness);
                //mAppBrightnessProfile.setValue(Integer.toString(brightness));
                mAppBrightnessProfile.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                  public boolean onPreferenceChange(Preference preference, Object newValue) {
                    try {
                        int val = Integer.parseInt(newValue.toString());
                        //mBaikalService.setAppBrightness(mPackageName, val );
                        Log.e(TAG, "setAppBrightness: mPackageName=" + mPackageName + ",brightness=" + val);
                    } catch(Exception re) {
                        Log.e(TAG, "onCreate: setAppBrightness Fatal! exception", re );
                    }
                    return true;
                  }
                });
            }

        
        } catch(Exception re) {
            Log.e(TAG, "onCreate: Fatal! exception", re );
        }

    }


    private void initBaikalAppOp(String XML_KEY, int baikalOption) {
        try {
            SwitchPreference pref = (SwitchPreference) findPreference(XML_KEY);
            if( pref != null ) { 
                //pref.setChecked(mBaikalService.getAppOption(mPackageName,baikalOption) == 1);
                pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    try {
                        //mBaikalService.setAppOption(mPackageName, baikalOption, ((Boolean)newValue) ? 1 : 0 );
                        Log.e(TAG, "setAppOption: mPackageName=" + mPackageName + ",option="+ baikalOption + ", value=" + (Boolean)newValue);
                    } catch(Exception re) {
                        Log.e(TAG, "onCreate: setAppOption Fatal! exception", re );
                    }
                    return true;
                }
                });
            }
        } catch(Exception re) {
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
