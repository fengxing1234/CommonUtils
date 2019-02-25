package com.im.picc.plugin.common.permission;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.im.picc.plugin.common.utils.LogUtil;

public class PermissionActivity extends AppCompatActivity {

    protected static final String TAG = "PermissionActivity";

    public static final int PERMISSIONS_REQUEST_INIT = 0x0010;


    public static final String[] PERMISSION_INIT = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        LogUtil.d(TAG, requestCode);

        switch (requestCode) {
            case PERMISSIONS_REQUEST_INIT:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    if (PermissionHelp.somePermissionPermanentlyDenied(this, permissions)) {
                        Log.d(TAG, "onRequestPermissionsResult: 设置界面");
                        Log.d(TAG, "onRequestPermissionsResult: 永久拒绝");
                        PermissionHelp.goSettings2Permissions(this, "跳转设置界面", "去设置", 800);
                    } else {
                        Log.d(TAG, "onRequestPermissionsResult: 拒绝权限");
                    }
                }
                break;
        }
    }
}
