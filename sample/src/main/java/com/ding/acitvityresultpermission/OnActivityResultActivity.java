package com.ding.acitvityresultpermission;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class OnActivityResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
    }


    public void back(View view) {
        finish();
    }

    public void backResult(View view) {
        Intent intent = new Intent();
        intent.putExtra("test","back result");
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

}
