package com.test.kani.songhistory.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesInstance
{
    private static final SharedPreferencesInstance ourInstance = new SharedPreferencesInstance();
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public enum Type {
        Boolean, Float, Integer, Long, String
    }

    public static SharedPreferencesInstance getInstance()
    {
        return ourInstance;
    }

    public Object get(String key, Type type)
    {
        switch( type )
        {
            case Boolean:
                return this.prefs.getBoolean(key, false);
            case Float:
            case Integer:
            case Long:
            case String:
                return this.prefs.getString(key, null);
        }

        return null;
    }

    public void set(String key, Object val)
    {
        if( val instanceof String )
        {
            this.editor.putString(key, (String) val);
        }

        if( val instanceof Boolean )
        {
            this.editor.putBoolean(key, (boolean) val);
        }

        this.editor.apply();    // 비동기적 동작
    }

    public void remove(String key)
    {
        this.editor.remove(key);
        this.editor.commit();   // 동기적 동작
    }

    public void setSharedPreferencesContext(Context context, int mode)
    {
        this.prefs = context.getSharedPreferences(context.getPackageName(), mode);
        this.editor = prefs.edit();
    }

    private SharedPreferencesInstance()
    {
    }
}
