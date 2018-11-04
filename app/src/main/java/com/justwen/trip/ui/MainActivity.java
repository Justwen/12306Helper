package com.justwen.trip.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.justwen.trip.ui.fragment.TicketListFragment;
import com.justwen.trip.util.PermissionManager;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkPermissions()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new TicketListFragment())
                    .commit();
        }
    }

    private boolean checkPermissions() {
        if (PermissionManager.checkPermission(this, Manifest.permission.RECEIVE_SMS)) {
            return true;
        } else {
            PermissionManager.getInstance()
                    .add(Manifest.permission.READ_SMS)
                    .add(Manifest.permission.RECEIVE_SMS)
                    .request(this);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                finish();
                return;
            }
        }
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new TicketListFragment())
                .commit();

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
