package com.ding.xlibrary;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;


/**
 * author:DingDeGao
 * time:2019-07-16-15:22
 * function: ActivityResultPermissionUtils
 */
public final class ActivityResultPermissionUtils {

    private static final int TYPE_RESULT = 1;
    private static final int TYPE_PERMISSION = 2;

    /**
     *  startActivityForResult
     * @param hostActivity  if is fragment,please fragment get host activity
     * @param intent request intent
     */
    public static ResultWrap startActivityForResult(FragmentActivity hostActivity, Intent intent){
        ResultWrap resultWrap = new ResultWrap();
        getReplaceFragment(hostActivity,TYPE_RESULT,resultWrap).startActivityForResult(intent,ReplaceFragment.ACTIVITY_REQUEST_CODE);
        return resultWrap;
    }

    /**
     *  requestPermissions
     * @param hostActivity  if is fragment,please fragment get host activity
     * @param permissions 请求的权限列表
     * @return
     */
    public static PermissionsWrap requestPermissions(FragmentActivity hostActivity, String ...permissions){
        return new PermissionsWrap(hostActivity,permissions);
    }


    private static ReplaceFragment getReplaceFragment(FragmentActivity hostActivity,int type, RequestWarp requestWarp){
        FragmentManager fragmentManager = hostActivity.getSupportFragmentManager();
        ReplaceFragment fragment = (ReplaceFragment) fragmentManager.findFragmentByTag("ActivityResultPermissionUtilsResult");
        if(fragment == null){
            fragment = new ReplaceFragment();
            fragmentManager.beginTransaction().add(fragment, "ActivityResultPermissionUtilsResult").commit();
            fragmentManager.executePendingTransactions();
        }
        fragment.setIHandle(new HandleImpl());

        if(type == TYPE_RESULT){
            ResultWrap resultWrap = (ResultWrap) requestWarp;
            fragment.getIHandle().setResultListener(resultWrap.innerResultListener);
        }else if(type == TYPE_PERMISSION){
            PermissionsWrap permissionsWrap = (PermissionsWrap) requestWarp;
            fragment.getIHandle().setPermissionResultListener(permissionsWrap.innerPermissionResultListener);
        }
        return fragment;
    }


    interface RequestWarp{

    }


    public static class PermissionsWrap implements RequestWarp{

        private Listener.PermissionResultListener mPermissionResultListener;

        private FragmentActivity activity;
        private String [] permissions;


        PermissionsWrap(FragmentActivity activity, String[] permissions) {
            this.activity = activity;
            this.permissions = permissions;
        }


        Listener.PermissionResultListener innerPermissionResultListener = new Listener.PermissionResultListener() {
            @Override
            public void permissionDenied(String permission,boolean rationale) {
                if(mPermissionResultListener != null){
                    mPermissionResultListener.permissionDenied(permission,rationale);
                }
            }

            @Override
            public void permissionGranted() {
                if(mPermissionResultListener != null){
                    mPermissionResultListener.permissionGranted();
                }
            }
        };

       public void permissions(Listener.PermissionResultListener permissionResultListener) {
            this.mPermissionResultListener = permissionResultListener;
            if(mPermissionResultListener != null && permissions != null){
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
                    mPermissionResultListener.permissionGranted();
                    return;
                }

                if (PermissionUtils.selfPermissionGranted(activity,permissions)) {
                    mPermissionResultListener.permissionGranted();
                    return;
                }
                request();
            }
        }

        public void permissionsWithoutCheck(Listener.PermissionResultListener permissionResultListener) {
            this.mPermissionResultListener = permissionResultListener;
            if(mPermissionResultListener != null && permissions != null){
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
                    mPermissionResultListener.permissionGranted();
                    return;
                }
                request();
            }
        }


        @RequiresApi(api = Build.VERSION_CODES.M)
        private void request(){
           ReplaceFragment replaceFragment = ActivityResultPermissionUtils.getReplaceFragment(activity,
                   ActivityResultPermissionUtils.TYPE_PERMISSION, this);
           replaceFragment.requestPermissions(permissions,ReplaceFragment.PERMISSION_REQUEST_CODE);
        }
    }


    public static class ResultWrap implements RequestWarp{

        private Listener.ResultListener mResultListener;

        ResultWrap() {
        }

        Listener.ResultListener innerResultListener = new Listener.ResultListener() {

            @Override
            public void onResult(Intent data) {
                if(mResultListener != null){
                    mResultListener.onResult(data);
                }
            }

            @Override
            public void onCancel() {
                if(mResultListener != null){
                    mResultListener.onCancel();
                }
            }
        };

        public void activityResult(Listener.ResultListener resultListener) {
            this.mResultListener = resultListener;
        }

    }




}
