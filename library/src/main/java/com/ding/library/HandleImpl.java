package com.ding.library;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

/**
 * author:DingDeGao
 * time:2019-07-16-14:54
 * function: HandleImpl
 */
public class HandleImpl implements IHandle{


    private Listener.ResultListener resultListener;
    private Listener.PermissionResultListener permissionResultListener;

    HandleImpl(){

    }

    @Override
    public void onActivityResultHandle(int resultCode, Intent data) {
        if(resultListener != null){
            if(resultCode == Activity.RESULT_OK){
                resultListener.onResult(data);
            }else{
                resultListener.onCancel();
            }
        }
    }

    @Override
    public void onRequestPermissionsResultHandle(Activity activity,String[] permissions, int[] grantResults) {
        if(permissionResultListener == null) return;
        int permissionCount = 0;
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                // 权限申请被拒绝
                if(activity != null){
                    permissionResultListener.permissionDenied(permissions[i],
                            ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i]));
                }else{
                    permissionResultListener.permissionDenied(permissions[i],false);
                }
                return;
            }
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                // 权限申请被同意
                permissionCount++;
            }
        }
        if(permissionCount == permissions.length){
            permissionResultListener.permissionGranted();
        }
    }

    @Override
    public void setResultListener(Listener.ResultListener resultListener) {
        this.resultListener = resultListener;
    }

    @Override
    public void setPermissionResultListener(Listener.PermissionResultListener permissionResultListener) {
        this.permissionResultListener = permissionResultListener;
    }



}
