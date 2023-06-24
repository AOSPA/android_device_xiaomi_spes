/*
 * Copyright (C) 2021 crDroid Android Project
 * Copyright (C) 2021 Chaldeaprjkt
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

package co.aospa.settings.refreshrate;

import android.content.Context;
import android.provider.Settings;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.view.Display;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RefreshTileService extends TileService {
    private static final String KEY_MIN_REFRESH_RATE = "min_refresh_rate";

    private Context context;
    private Tile tile;

    private final List<Integer> availableRates = new ArrayList<>();
    private int activeRateMin;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Display.Mode mode = context.getDisplay().getMode();
        Display.Mode[] modes = context.getDisplay().getSupportedModes();
        for (Display.Mode m : modes) {
            int rate = (int) Math.round(m.getRefreshRate());
            if (m.getPhysicalWidth() == mode.getPhysicalWidth() &&
                m.getPhysicalHeight() == mode.getPhysicalHeight()) {
                availableRates.add(rate);
            }
        }
        syncFromSettings();
    }

    private int getSettingOf(String key) {
        float rate = Settings.System.getFloat(context.getContentResolver(), key, 90);
        int active = availableRates.indexOf((int) Math.round(rate));
        return Math.min(active, 0);
    }

    private void syncFromSettings() {
        activeRateMin = getSettingOf(KEY_MIN_REFRESH_RATE);
    }

    private void updateTileView() {
        String displayText;
        int min = availableRates.get(activeRateMin);
        displayText = String.format(Locale.US, min ? "%d Hz", min);
        tile.setContentDescription(displayText);
        tile.setSubtitle(displayText);
        tile.setState(min ? Tile.STATE_ACTIVE);
        tile.updateTile();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        tile = getQsTile();
        syncFromSettings();
        updateTileView();
    }

    @Override
    public void onClick() {
        super.onClick();
        syncFromSettings();
        updateTileView();
    }
}
