/*
 * Copyright (C) 2017-2018 AICP
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

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ListView;

import ru.baikalos.extras.BaseSettingsFragment;
import ru.baikalos.extras.Constants;
import ru.baikalos.extras.PreferenceMultiClickHandler;
import ru.baikalos.extras.R;
import ru.baikalos.gear.preference.LongClickablePreference;
import ru.baikalos.extras.utils.Util;

import android.os.SystemProperties;

import com.plattysoft.leonids.ParticleSystem;

import java.util.Random;

public class Dashboard extends BaseSettingsFragment {

    private static final String PREF_BAIKALOS_LOGO = "baikalos_logo";
    private static final String PREF_BAIKALOS_OTA = "baikalos_ota";
    private static final String PREF_LOG_IT = "log_it";

    private static final String PREF_BAIKALOS_PARTS = "device_part";
    private static final String PREF_BAIKALOS_PARTS_PACKAGE_NAME = "org.omnirom.device";


    private static final Intent INTENT_OTA = new Intent().setComponent(new ComponentName(
            Constants.BAIKALOS_OTA_PACKAGE, Constants.BAIKALOS_OTA_ACTIVITY));

    private LongClickablePreference mBaikalOSLogo;
    private Preference mBaikalOSOTA;
    private Preference mBaikalOSParts;

    private Random mRandom = new Random();
    private int mLogoClickCount = 0;

    @Override
    protected int getPreferenceResource() {
        return R.xml.dashboard;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PackageManager pm = getActivity().getPackageManager();

        boolean perfProf  = SystemProperties.get("baikal.eng.perf", "0").equals("1");
        boolean thermProf  = SystemProperties.get("baikal.eng.therm", "0").equals("1");

        Preference profilesCategory = findPreference("app_setings_cat");

        if( !perfProf && !thermProf ) {
            if( profilesCategory != null ) {
                getPreferenceScreen().removePreference(profilesCategory);
            }
        }

        mBaikalOSLogo = (LongClickablePreference) findPreference(PREF_BAIKALOS_LOGO);

        mBaikalOSOTA = findPreference(PREF_BAIKALOS_OTA);
        if (!Util.isPackageEnabled(Constants.BAIKALOS_OTA_PACKAGE, pm)) {
            mBaikalOSOTA.getParent().removePreference(mBaikalOSOTA);
        }

        // DeviceParts
        mBaikalOSParts = findPreference(PREF_BAIKALOS_PARTS);
        if (!Util.isPackageEnabled(PREF_BAIKALOS_PARTS_PACKAGE_NAME, pm)) {
            mBaikalOSParts.getParent().removePreference(mBaikalOSParts);
        }

        Preference logIt = findPreference(PREF_LOG_IT);
        Util.requireRoot(getActivity(), logIt);

        mBaikalOSLogo.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                int firstRandom = mRandom.nextInt(91 - 0);
                int secondRandom = mRandom.nextInt(181 - 90) + 90;
                int thirdRandom = mRandom.nextInt(181 - 0);

                // Let's color the star randomly
                Drawable star = getResources().getDrawable(R.drawable.star_white_border, null);
                int randomColor;
                randomColor = Color.rgb(
                        Color.red(mRandom.nextInt(0xFFFFFF)),
                        Color.green(mRandom.nextInt(0xFFFFFF)),
                        Color.blue(mRandom.nextInt(0xFFFFFF)));
                star.setTint(randomColor);

                ParticleSystem ps = new ParticleSystem(getActivity(), 100, star, 3000);
                ps.setScaleRange(0.7f, 1.3f);
                ps.setSpeedRange(0.1f, 0.25f);
                ps.setAcceleration(0.0001f, thirdRandom);
                ps.setRotationSpeedRange(firstRandom, secondRandom);
                ps.setFadeOut(200, new AccelerateInterpolator());
                ps.oneShot(getView(), 100);

                mBaikalOSLogo.setLongClickBurst(2000/((++mLogoClickCount)%5+1));
                return true;
            }
        });
        mBaikalOSLogo.setOnLongClickListener(R.id.logo_view, 1000,
                new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                            int firstRandom = mRandom.nextInt(91 - 0);
                            int secondRandom = mRandom.nextInt(181 - 90) + 90;
                            int thirdRandom = mRandom.nextInt(181 - 0);

                            Drawable star =
                                    getResources().getDrawable(R.drawable.star_alternative, null);

                            ParticleSystem ps = new ParticleSystem(getActivity(), 100, star, 3000);
                            ps.setScaleRange(0.7f, 1.3f);
                            ps.setSpeedRange(0.1f, 0.25f);
                            ps.setAcceleration(0.0001f, thirdRandom);
                            ps.setRotationSpeedRange(firstRandom, secondRandom);
                            ps.setFadeOut(1000, new AccelerateInterpolator());
                            ps.oneShot(getView(), 100);
                            return true;
                        }
                });
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference == mBaikalOSOTA || preference == mBaikalOSLogo) {
            startActivity(INTENT_OTA);
            return true;
        } else {
            return super.onPreferenceTreeClick(preference);
        }
    }
}
