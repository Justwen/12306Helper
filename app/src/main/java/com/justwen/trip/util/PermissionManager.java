package com.justwen.trip.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justwen on 2018/10/21.
 */
public class PermissionManager {

    private List<String> mPermissionList;

    public PermissionManager() {
        mPermissionList = new ArrayList<>();
    }

    public static PermissionManager getInstance() {
        return new PermissionManager();
    }

    public PermissionManager add(String permission) {
        mPermissionList.add(permission);
        return this;
    }

    public void request(Activity activity, int requestCode) {
        String[] permissions = new String[mPermissionList.size()];
        permissions = mPermissionList.toArray(permissions);
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    public void request(Activity activity) {
        request(activity, 0);
    }

    public static boolean checkPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
