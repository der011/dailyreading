package com.daily.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.daily.R;

public class Preferences extends PreferenceActivity {
    
    public static final String PREF_AUTO_UPDATE = "PREF_AUTO_UPDATE";
    public static final String PREF_MIN_MAG = "PREF_MIN_MAG";
    public static final String PREF_UPDATE_FREQ = "PREF_UPDATE_FREQ";
    public static final String PREF_FONT_SIZE = "PREF_FONT_SIZE";
    public static final String PREF_ARCHIVE_SIZE = "PREF_ARCHIVE_SIZE";
    
    SharedPreferences prefs;
    
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.userpreferences); 
        
    }
    
    public enum ArchiveSize {
        DAY(1), WEEK(7), MONTH(30), YEAR(365), UNLIMITED(0);
        
        private int size;

        ArchiveSize(int size) {
            this.size = size;
        }
        
        public static ArchiveSize fromString(String str) {
            for (ArchiveSize value: ArchiveSize.values()) {
                if (value.name().equalsIgnoreCase(str)) {
                    return value; 
                }
            }
            
            return ArchiveSize.MONTH; // Return Month as default if unknown...
        }
        
        public int getDays() {
            return this.size;
        }
    }
    
    
}
