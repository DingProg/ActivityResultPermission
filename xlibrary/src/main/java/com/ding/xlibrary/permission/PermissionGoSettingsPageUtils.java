package com.ding.xlibrary.permission;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * 权限请求页适配，不同手机系统跳转到不同的权限请求页
 */

public class PermissionGoSettingsPageUtils {

    private static String packageName ;

    public static void go(Context context) {
        String name = Build.MANUFACTURER;
        packageName = context.getPackageName();
        boolean isGoSettings = false;
        try {
            switch (name) {
                case "HUAWEI":
                    goHuaWeiMainager(context);
                    break;
                case "vivo":
                    goVivoMainager(context);
                    break;
                case "OPPO":
                    goOppoMainager(context);
                    break;
                case "Coolpad":
                    goCoolpadMainager(context);
                    break;
                case "Meizu":
                    goMeizuMainager(context);
                    break;
                case "Xiaomi":
                    goXiaoMiMainager(context);
                    break;
                case "samsung":
                    goSangXinMainager(context);
                    break;
                case "Sony":
                    goSonyMainager(context);
                    break;
                case "LG":
                    goLGMainager(context);
                    break;
                default:
                    isGoSettings = true;
                    goIntentSetting(context);
                    break;
            }
        }catch (Exception e){
            if(!isGoSettings){
                goIntentSetting(context);
            }
        }
    }

    private static void goLGMainager(Context context){
        try {
            Intent intent = new Intent(packageName);
            ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings$AccessLockSummaryActivity");
            intent.setComponent(comp);
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "跳转失败", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            goIntentSetting(context);
        }
    }
    private  static void goSonyMainager(Context context){
        try {
            Intent intent = new Intent(packageName);
            ComponentName comp = new ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity");
            intent.setComponent(comp);
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "跳转失败", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            goIntentSetting(context);
        }
    }

    private static void goHuaWeiMainager(Context context) {
        try {
            Intent intent = new Intent(packageName);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");
            intent.setComponent(comp);
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "跳转失败", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            goIntentSetting(context);
        }
    }

    private static String getMiuiVersion(Context context) {
        String propName = "ro.miui.ui.version.name";
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(
                    new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return line;
    }

    private static void goXiaoMiMainager(Context context) {
        String rom = getMiuiVersion(context);
        Intent intent=new Intent();
        if ("V6".equals(rom) || "V7".equals(rom)) {
            intent.setAction("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            intent.putExtra("extra_pkgname", packageName);
        } else if ("V8".equals(rom) || "V9".equals(rom)) {
            intent.setAction("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
            intent.putExtra("extra_pkgname", packageName);
        } else {
            goIntentSetting(context);
        }
        context.startActivity(intent);
    }

    private static void goMeizuMainager(Context context) {
        try {
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra("packageName", packageName);
            context.startActivity(intent);
        } catch (ActivityNotFoundException localActivityNotFoundException) {
            localActivityNotFoundException.printStackTrace();
            goIntentSetting(context);
        }
    }

    private static void goSangXinMainager(Context context) {
        //三星4.3可以直接跳转
        goIntentSetting(context);
    }

    private static void goIntentSetting(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void goOppoMainager(Context context) {
        doStartApplicationWithPackageName(context,"com.coloros.safecenter");
    }

    /**
     * doStartApplicationWithPackageName("com.yulong.android.security:remote")
     * 和Intent open = getPackageManager().getLaunchIntentForPackage("com.yulong.android.security:remote");
     * startActivity(open);
     * 本质上没有什么区别，通过Intent open...打开比调用doStartApplicationWithPackageName方法更快，也是android本身提供的方法
     */
    private static void goCoolpadMainager(Context context) {
        doStartApplicationWithPackageName(context,"com.yulong.android.security:remote");
      /*  Intent openQQ = getPackageManager().getLaunchIntentForPackage("com.yulong.android.security:remote");
        startActivity(openQQ);*/
    }

    private static void goVivoMainager(Context context) {
        doStartApplicationWithPackageName(context,"com.bairenkeji.icaller");
     /*   Intent openQQ = getPackageManager().getLaunchIntentForPacka55555555ge("com.vivo.securedaemonservice");
        startActivity(openQQ);*/
    }

    /**
     * 此方法在手机各个机型设置中已经失效
     *
     * @return
     */
    private static Intent getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        return localIntent;
    }

    private static void doStartApplicationWithPackageName(Context context,String packagename) {
        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }
        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);
        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = context.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);
        Log.e("PermissionPageManager", "resolveinfoList" + resolveinfoList.size());
        for (int i = 0; i < resolveinfoList.size(); i++) {
            Log.e("PermissionPageManager", resolveinfoList.get(i).activityInfo.packageName + resolveinfoList.get(i).activityInfo.name);
        }
        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packageName参数2 = 参数 packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packageName参数2.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            // 设置ComponentName参数1:packageName参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);
            intent.setComponent(cn);
            try {
                context.startActivity(intent);
            } catch (Exception e) {
                goIntentSetting(context);
                e.printStackTrace();
            }
        }
    }
}
