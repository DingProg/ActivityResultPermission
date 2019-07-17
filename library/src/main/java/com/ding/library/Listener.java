package com.ding.library;

import android.content.Intent;

/**
 * author:DingDeGao
 * time:2019-07-16-15:43
 * function: result listener
 */
public interface Listener {

    interface ResultListener{
        void onResult(Intent data);
        void onCancel();
    }

    interface PermissionResultListener{
        /**
         * permissionDenied
         * @param permission Denied permission(if have one denied returned)
         * @param rationale  if false, Denied permission with ask never again,you need go settings
         */
        void permissionDenied(String permission,boolean rationale);

        /**
         * permissionGranted
         */
        void permissionGranted();
    }
}
