package com.ding.library;


import android.app.Fragment;
import android.content.Intent;

import androidx.annotation.NonNull;

/**
 * author:DingDeGao
 * time:2019-07-16-14:30
 * function: ReplaceFragment
 */
public class ReplaceFragment extends Fragment {

    static final int ACTIVITY_REQUEST_CODE = 100;
    static final int PERMISSION_REQUEST_CODE = 101;


    private IHandle iHandle;

    public void setIHandle(IHandle iHandle) {
        this.iHandle = iHandle;
    }

    public IHandle getIHandle(){
        return iHandle;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != ACTIVITY_REQUEST_CODE) return;
        if(iHandle != null){
            iHandle.onActivityResultHandle(resultCode,data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != PERMISSION_REQUEST_CODE) return;
        if(iHandle != null){
            iHandle.onRequestPermissionsResultHandle(getActivity(),permissions,grantResults);
        }
    }
}
