package tencent.tmgp.sgame.service;

import android.app.Service;
import android.content.SharedPreferences;
import android.widget.Toast;

public abstract class Zservice extends Service {
    public SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();
        sp = getSharedPreferences(getPackageName() + "_preferences", 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void ipInt(String k, int v) {
        sp.edit().putInt(k, v).commit();
    }

    public int opInt(String k, int v) {
        return sp.getInt(k, v);
    }

    public void ipBoolean(String k, boolean v) {
        sp.edit().putBoolean(k, v).commit();
    }

    public boolean opBoolean(String k, boolean v) {
        return sp.getBoolean(k, v);
    }

    public void toast(String s) {
        toast(s, 0);
    }

    public void toast(String s, int i) {
        Toast.makeText(this, s, i).show();
    }

}
