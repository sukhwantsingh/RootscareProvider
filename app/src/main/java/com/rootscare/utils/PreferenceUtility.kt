package com.rootscare.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import java.io.IOException
import java.util.*

object PreferenceUtility {
    const val DEVICE_TOKEN = "token"

    /**
     * getAppSharedPreference returns Application Shared Preference
     *
     * @param activity
     * @return SharedPreferences
     */
    private fun getAppSharedPreference(
        preferenceName: String?,
        activity: Activity
    ): SharedPreferences {
        return activity.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
    }

    private fun getAppSharedPreference(
        preferenceName: String?,
        context: Context
    ): SharedPreferences {
        return context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
    }

    fun loadLocalePreference(context: Context, langCode: String): String {
        val myLocale = Locale(langCode)
        val config = Configuration()
        config.setLocale(myLocale)
        config.setLayoutDirection(myLocale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        return context.resources.configuration.locale.displayName
    }

    fun getCurrentLocale(context: Context): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            context.resources.configuration.locale
        }
    }

    fun updateLocale(context: Context, languageCode: String) {
        val res = context.resources
        val dm = res.displayMetrics
        val conf = res.configuration
        val locale = Locale(languageCode)
        conf.setLocale(Locale(languageCode)) // API 17+ only.
        res.updateConfiguration(conf, dm)
        conf.setLayoutDirection(locale)
    }
    /* public static void loadLocalePreference1(Context context) {
         Locale locale = getCurrentLocale(context);
         Log.d(TAG, "loadLocalePreference1: "+locale.getLanguage());
         Configuration config = new Configuration();
         config.setLocale(locale);
         config.setLayoutDirection(locale);
         context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
     }*/
    /**
     * getAppPrefsEditor
     *
     * @param activity
     * @return editor
     */
    private fun getAppPrefsEditor(
        preferenceName: String?,
        activity: Activity
    ): SharedPreferences.Editor {
        val prefs = activity.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
        //        prefsEditor.clear();
//        prefsEditor.commit();
        return prefs.edit()
    }

    private fun getAppPrefsEditor(
        preferenceName: String?,
        context: Context
    ): SharedPreferences.Editor {
        val prefs = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
        return prefs.edit()
    }

    /**
     * saveObjectInAppPreference save given Object into Application
     * SharedPreference
     *
     * @param activity
     * @param object
     * @param prefName
     */
    fun saveObjectInAppPreference(activity: Activity, `object`: Any?, prefName: String?) {
        val prefsEditor = getAppPrefsEditor(prefName, activity)
        try {
            prefsEditor.putString(
                prefName,
                if (`object` != null) ObjectSerializer.serialize(`object`) else null
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        prefsEditor.commit()
    }

    fun saveObjectInAppPreference(context: Context, `object`: Any?, prefName: String?) {
        val prefsEditor = getAppPrefsEditor(prefName, context)
        try {
            prefsEditor.putString(
                prefName,
                if (`object` != null) ObjectSerializer.serialize(`object`) else null
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        prefsEditor.commit()
    }

    /**
     * getObjectInAppPreference returns saved Object from Application
     * SharedPreference
     *
     * @param activity
     * @param prefName
     * @return Object
     */
    fun getObjectInAppPreference(activity: Activity, prefName: String?): Any? {
        var prefObject: Any? = null
        val prefs = getAppSharedPreference(prefName, activity)
        val serializeString = prefs.getString(prefName, null)
        try {
            if (serializeString != null) {
                prefObject = ObjectSerializer.deserialize(serializeString)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        return prefObject
    }

    fun clearPrefsEditor(preferenceName: String?, activity: Context) {
        val prefs = activity.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }

    fun getObjectInAppPreference(context: Context, prefName: String?): Any? {
        var prefObject: Any? = null
        val prefs = getAppSharedPreference(prefName, context)
        val serializeString = prefs.getString(prefName, null)
        try {
            if (serializeString != null) {
                prefObject = ObjectSerializer.deserialize(serializeString)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        return prefObject
    }

    fun saveIntInPreference(activity: Context, key: String?, value: Int) {
        val prefsEditor = getAppPrefsEditor(key, activity)
        prefsEditor.putInt(key, value)
        prefsEditor.commit()
    }

    fun saveStringInPreference(activity: Activity, key: String?, value: String?) {
        val prefsEditor = getAppPrefsEditor(key, activity)
        prefsEditor.putString(key, value)
        prefsEditor.commit()
    }

    fun saveBooleanInPreference(activity: Activity, key: String?, value: Boolean) {
        val prefsEditor = getAppPrefsEditor(key, activity)
        prefsEditor.putBoolean(key, value)
        prefsEditor.apply()
    }

    fun saveStringInPreference(activity: Context, key: String?, value: String?) {
        val prefsEditor = getAppPrefsEditor(key, activity)
        prefsEditor.putString(key, value)
        prefsEditor.commit()
    }

    fun getIntFromPreference(activity: Context, key: String?): Int {
        val prefs = getAppSharedPreference(key, activity)
        return prefs.getInt(key, 0)
    }

    fun getStringFromPreference(activity: Activity, key: String?): String? {
        val prefs = getAppSharedPreference(key, activity)
        return prefs.getString(key, "")
    }

    fun getStringFromPreference(activity: Context, key: String?): String? {
        val prefs = getAppSharedPreference(key, activity)
        return prefs.getString(key, "")
    }

    fun getBooleanFromPreference(activity: Context, key: String?): Boolean {
        val prefs = getAppSharedPreference(key, activity)
        return prefs.getBoolean(key, false)
    }
}