package com.fengxing.mobile.plugin.common.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

public class PermissionHelp {


    /**
     * 是否有需要的权限
     *
     * @param context
     * @param pers
     * @return
     */
    public static boolean hasPermission(Context context, String... pers) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        for (String per : pers) {

            if (ContextCompat.checkSelfPermission(context, per) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    /**
     * @param obj         context Activity 或者 Fragment
     * @param rationale
     * @param requestCode
     * @param permission
     */
    public static void requestPermissions(final Object obj, String rationale, final int requestCode, final String... permission) {
        checkObjectContext(obj);

        Context context;
        if (obj instanceof Activity) {
            context = (Activity) obj;
        } else {
            context = ((Fragment) obj).getContext();
        }

        boolean isShouldRationale = false;
        for (String per : permission) {
            isShouldRationale = isShouldRationale || shouldShowRequestPermissionRationale(context, per);
        }

        if (isShouldRationale) {
            AlertDialog alertDialog = new AlertDialog.Builder(context)
                    .setTitle("请求权限")
                    .setMessage(rationale)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            executePermissionsRequest(obj, requestCode, permission);
                        }
                    })
                    .setNegativeButton("取消 ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
            if (alertDialog != null) {
                alertDialog.show();
            }
        } else {
            executePermissionsRequest(obj, requestCode, permission);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void executePermissionsRequest(Object obj, int requestCode, String[] permission) {
        if (obj instanceof Activity) {
            ActivityCompat.requestPermissions((Activity) obj, permission, requestCode);
        } else if (obj instanceof Fragment) {
            ((Fragment) obj).requestPermissions(permission, requestCode);
        } else if (obj instanceof android.app.Fragment) {
            ((android.app.Fragment) obj).requestPermissions(permission, requestCode);
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private static boolean shouldShowRequestPermissionRationale(Object obj, String permission) {
        if (obj instanceof Activity) {
            return ActivityCompat.shouldShowRequestPermissionRationale((Activity) obj, permission);
        } else if (obj instanceof Fragment) {
            return ((Fragment) obj).shouldShowRequestPermissionRationale(permission);
        } else if (obj instanceof android.app.Fragment) {
            return ((android.app.Fragment) obj).shouldShowRequestPermissionRationale(permission);
        } else {
            return false;
        }
    }

    /**
     * 校验obj实例
     *
     * @param obj
     */
    private static void checkObjectContext(Object obj) {
        boolean isActivity = obj instanceof Activity;
        boolean isFragment = obj instanceof Fragment;
        boolean isAppFragment = obj instanceof android.app.Fragment;
        boolean isMinSdkM = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;

        if (!(isActivity || isFragment || (isMinSdkM && isAppFragment))) {
            if (isAppFragment) {
                throw new IllegalArgumentException(
                        "Target SDK needs to be greater than 23 if caller is android.app.Fragment");
            } else {
                throw new IllegalArgumentException("Caller must be an Activity or a Fragment.");
            }
        }
    }

    public static boolean somePermissionPermanentlyDenied(Object obj, String[] permissions) {
        for (String per : permissions) {
            if (permissionPermanentlyDenied(obj, per)) {
                return true;
            }
        }
        return false;
    }

    public static boolean permissionPermanentlyDenied(Object obj, String permission) {
        return !shouldShowRequestPermissionRationale(obj, permission);
    }

    public static void goSettings2Permissions(final Object object, String str, String str1, final int requestCode) {
        checkObjectContext(object);
        Context ctx;
        final Activity activity = getActivity(object);
        if (null == activity) {
            return;
        }
        if (object instanceof Context) {
            ctx = (Context) object;
        } else {
            ctx = ((Fragment) object).getContext();
        }
        AlertDialog dialog = new AlertDialog.Builder(ctx)
                .setTitle(str)
                .setMessage(str)
                .setPositiveButton(str1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                        intent.setData(uri);
                        startForResult(object, intent, requestCode);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        if (dialog != null) {
            dialog.show();
        }
    }

    private static Activity getActivity(Object object) {
        if (object instanceof Activity) {
            return ((Activity) object);
        } else if (object instanceof Fragment) {
            return ((Fragment) object).getActivity();
        } else if (object instanceof android.app.Fragment) {
            return ((android.app.Fragment) object).getActivity();
        } else {
            return null;
        }
    }

    @TargetApi(11)
    private static void startForResult(Object object, Intent intent, int requestCode) {
        if (object instanceof Activity) {
            ((Activity) object).startActivityForResult(intent, requestCode);
        } else if (object instanceof Fragment) {
            ((Fragment) object).startActivityForResult(intent, requestCode);
        } else if (object instanceof android.app.Fragment) {
            ((android.app.Fragment) object).startActivityForResult(intent, requestCode);
        }
    }
}
