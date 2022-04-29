package edu.quinnipiac.ser210.githubchat.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.util.Map;
import java.util.Set;

/**
 * @author Thomas Kwashnak
 */
public class PreferencesWrapper implements SharedPreferences, PreferencesHolder {

    private static final String PREFERENCES_NAME = "GithubChatPreferences";
    private static final String ENCRYPTED_PREFERENCES_NAME = "EncryptedGithubChatPreferences";

    private final SharedPreferences sharedPreferences;

    public PreferencesWrapper(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES_NAME, 0);
        try {
            prefs = EncryptedSharedPreferences.create(ENCRYPTED_PREFERENCES_NAME, MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC), context,
                                                      EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                                                      EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                                                     );
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sharedPreferences = prefs;
        }
    }

    public static PreferencesWrapper from(Object object) {
        if (object instanceof PreferencesHolder) {
            return ((PreferencesHolder) object).getPreferencesWrapper();
        } else {
            return null;
        }
    }

    @Override
    public Map<String, ?> getAll() {
        return sharedPreferences.getAll();
    }

    @Nullable
    @Override
    public String getString(String s, @Nullable String s1) {
        return sharedPreferences.getString(s, s1);
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String s, @Nullable Set<String> set) {
        return sharedPreferences.getStringSet(s, set);
    }

    @Override
    public int getInt(String s, int i) {
        return sharedPreferences.getInt(s, i);
    }

    @Override
    public long getLong(String s, long l) {
        return sharedPreferences.getLong(s, l);
    }

    @Override
    public float getFloat(String s, float v) {
        return sharedPreferences.getFloat(s, v);
    }

    @Override
    public boolean getBoolean(String s, boolean b) {
        return sharedPreferences.getBoolean(s, b);
    }

    @Override
    public boolean contains(String s) {
        return sharedPreferences.contains(s);
    }

    @Override
    public Editor edit() {
        return sharedPreferences.edit();
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
    }

    public void setString(String key, String value) {
        edit().putString(key, value).apply();
    }

    public void setStringSet(String key, Set<String> set) {
        edit().putStringSet(key, set).apply();
    }

    public void setInt(String key, int i) {
        edit().putInt(key, i).apply();
    }

    public void setLong(String s, long l) {
        edit().putLong(s, l).apply();
    }

    public void setFloat(String s, float f) {
        edit().putFloat(s, f).apply();
    }

    public void setBoolean(String s, boolean b) {
        edit().putBoolean(s, b).apply();
    }

    @Override
    public PreferencesWrapper getPreferencesWrapper() {
        return this;
    }
}
