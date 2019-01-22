package com.justwen.trip.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;

import com.justwen.trip.ui.fragment.TicketListFragment;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
    }

    @SuppressLint("CheckResult")
    private void checkPermissions() {
        new RxPermissions(this)
                .requestEachCombined(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) {
                        if (permission.granted) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(android.R.id.content, new TicketListFragment())
                                    .commit();

                        } else {
                            finish();
                        }
                    }
                });

    }
}
