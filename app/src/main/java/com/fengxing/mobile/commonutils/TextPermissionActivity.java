package com.fengxing.mobile.commonutils;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.im.picc.plugin.common.permission.PermissionActivity;
import com.im.picc.plugin.common.permission.PermissionHelp;

public class TextPermissionActivity extends PermissionActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initPermissions();
    }

    private void initPermissions() {
        if (PermissionHelp.hasPermission(TextPermissionActivity.this, PERMISSION_INIT)) {

        }
        PermissionHelp.requestPermissions(TextPermissionActivity.this, "请开启权限", PERMISSIONS_REQUEST_INIT, PERMISSION_INIT);
    }
}
