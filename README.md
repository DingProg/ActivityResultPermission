# 概览  
一个用于帮助OnActivityResult，PermissionRequest 接耦的库，不需要依赖Activity中的回调.  

### 与RxActivityResult和RxPermission有什么不同？  
1. 不需要依赖RxJava，但也支持链式调用.
2. 功能更丰富，两者结合，使用更加方便，并支持部分国产Rom的权限判断，跳转权限设置页面.
3. 代码量少，可以选择拷贝代码直接放入项目.


## 开始使用 （minSdkVersion >= 14）

[![](https://www.jitpack.io/v/DingProg/ActivityResultPermission.svg)](https://www.jitpack.io/#DingProg/ActivityResultPermission)

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
	implementation 'com.github.DingProg:ActivityResultPermission:v0.0.1'
}

```

### OnActivityResult
```java
 Intent intent = new Intent(this, OnActivityResultActivity.class);
 //通过ActivityResultPermissionUtils 去启动Activity，并注册其回调
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
``` 

### Permissions处理   
```java
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
```   
如果对于一些非正常Rom（如oppo，vivo手机），可以调用PermissionUtils.checkSelfPermissionsWhitNoNoramal(context,permissions);   

请求权限时，调用ActivityResultPermissionUtils.requestPermissions(this, permission).permissionsWithoutCheck();

关于更多请查看sample

### 原理
接耦合Activity中的回调，有两种方式
- 一种是启动一个新的Activity来判断权限，获取ActivityForResult的结果.
- 一种是添加一个Fragment，在Fragment中获取回调，进行解耦合.

RxActivityResult，RxPermission采用都是前者，而此处采用了后者，使用Fragment，优点Fragment占用资源更少.  



