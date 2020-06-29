package com.dev.alltrack;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import androidx.annotation.NonNull;

import java.util.Map;
import java.util.Set;

public class PreferenceManager {

    private static Context mContext;
    private final SharedPreferences sPref;

    public PreferenceManager(Builder builder) {
        sPref = mContext.getSharedPreferences(builder.name, builder.mode);
    }

    private SharedPreferences getPreferences() {
        if(sPref == null) {
            throw new RuntimeException("Don't use Preference");
        }
        return sPref;
    }

    private Editor editor() {
        return getPreferences().edit();
    }

    public boolean putString(@NonNull String key, @NonNull String value) {
        Editor editor = editor();
        editor.putString(key, value);
        return editor.commit();
    }

    public String getString(@NonNull String key, @NonNull String defValue) {
        return getPreferences().getString(key, defValue);
    }

    public void putStringSet(@NonNull String key, @NonNull Set<String> values) {
        Editor editor = editor();
        editor.putStringSet(key, values);
        editor.commit();
    }

    public Set<String> getStringSet(@NonNull String key, @NonNull Set<String> defValues) {
        return getPreferences().getStringSet(key, defValues);
    }

    public Map<String, ?> getAll() {
        return getPreferences().getAll();
    }

    public void remove(@NonNull String key) {
        editor().remove(key).commit();
    }

    public void clear() {
        editor().clear().commit();
    }

    public static class Builder {

        private String name;
        private int mode;

        public Builder(@NonNull Context context) {
            PreferenceManager.mContext = context;
        }

        public Builder name(@NonNull String name) {
            this.name = name;
            return this;
        }

        public Builder mode(@NonNull int mode) {
            this.mode = mode;
            return this;
        }

        public PreferenceManager build() {
            return new PreferenceManager(this);
        }
    }

}
