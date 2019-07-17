package com.ding.acitvityresultpermission;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.ding.library.ActivityResultPermissionUtils;
import com.ding.library.Listener;
import com.ding.library.permission.PermissionGoSettingsPageUtils;
import com.ding.library.permission.PermissionUtils;

public class MainActivity extends AppCompatActivity {

    private SurfaceView surfaceView;
    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        surfaceView = findViewById(R.id.surfaceView);
    }

    public void openActivity(View view) {
        Intent intent = new Intent(this, OnActivityResultActivity.class);
        ActivityResultPermissionUtils.startActivityForResult(this, intent).activityResult(new Listener.ResultListener() {
            @Override
            public void onResult(Intent data) {
                if (data != null) {
                    String testStr = data.getStringExtra("test");
                    Toast.makeText(MainActivity.this, "openActivity with result is:" + testStr, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "openActivity with result cancel", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void openCamera(View view) {
        ActivityResultPermissionUtils.requestPermissions(this, Manifest.permission.CAMERA).permissions(new Listener.PermissionResultListener() {
            @Override
            public void permissionDenied(String permission, boolean rationale) {
                //rationale if true 仅仅点击了禁止
                if(rationale){
                    Toast.makeText(MainActivity.this, "Denied permission", Toast.LENGTH_SHORT).show();
                }else{
                    //点击禁止权限，并不允许在弹出
                    Toast.makeText(MainActivity.this, "Denied permission with ask never", Toast.LENGTH_SHORT).show();
                    PermissionGoSettingsPageUtils.go(MainActivity.this);
                }
            }

            @Override
            public void permissionGranted() {
                releaseCamera();
                try{
                    camera = Camera.open(0);
                    camera.setPreviewDisplay(surfaceView.getHolder());
                    camera.startPreview();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 对于非正常ROM的处理,暂时未在library中处理
     */
    public void openCamera1(View view) {
        if(!PermissionUtils.permissionIsNormal()) {
            if(PermissionUtils.checkSelfPermissionsWhitNoNoramal(this,Manifest.permission.CAMERA)){
                releaseCamera();
                try {
                    camera = Camera.open(0);
                    camera.setPreviewDisplay(surfaceView.getHolder());
                    camera.startPreview();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                ActivityResultPermissionUtils.requestPermissions(this, Manifest.permission.CAMERA).permissionsWithoutCheck(new Listener.PermissionResultListener() {
                    @Override
                    public void permissionDenied(String permission, boolean rationale) {
                        if (rationale) {
                            Toast.makeText(MainActivity.this, "Denied permission", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Denied permission with ask never", Toast.LENGTH_SHORT).show();
                            PermissionGoSettingsPageUtils.go(MainActivity.this);
                        }
                    }

                    @Override
                    public void permissionGranted() {
                        releaseCamera();
                        try {
                            camera = Camera.open(0);
                            camera.setPreviewDisplay(surfaceView.getHolder());
                            camera.startPreview();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseCamera();
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
}
