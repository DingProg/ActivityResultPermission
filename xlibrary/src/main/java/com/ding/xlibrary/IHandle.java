package com.ding.xlibrary;

import android.app.Activity;
import android.content.Intent;

/**
 * author:DingDeGao
 * time:2019-07-16-14:53
 * function: IHandle
 */
public interface IHandle {

    /**
     * ActivityResult Handle
     *
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     */
    void onActivityResultHandle(int resultCode, Intent data);

    /**
     * PermissionsResultHandle
     * @param activity     hostActivity
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either
     *                     . Never null.
     */
    void onRequestPermissionsResultHandle(Activity activity, String[] permissions, int[] grantResults);


    /**
     *
     * @param resultListener  ResultListener {@link Listener.ResultListener}
     */
    void setResultListener(Listener.ResultListener resultListener);

    /**
     *
     * @param permissionResultListener  PermissionResultListener {@link Listener.PermissionResultListener}
     */
    void setPermissionResultListener(Listener.PermissionResultListener permissionResultListener);

}
