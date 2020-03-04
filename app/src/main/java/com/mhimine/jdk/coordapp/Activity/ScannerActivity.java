package com.mhimine.jdk.coordapp.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.mhimine.jdk.coordapp.R;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends AppCompatActivity {

    private DecoratedBarcodeView mDBV;
    private CaptureManager captureManager;
    private boolean isLightOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        final Button swichLight = (Button) findViewById(R.id.btn_switch);

        mDBV = (DecoratedBarcodeView) findViewById(R.id.dbv_custom);

       // mDBV.setTorchListener(this);
        swichLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLightOn) {
                    mDBV.setTorchOff();
                    isLightOn=false;
                    swichLight.setText("打开闪光灯");
                } else {
                    mDBV.setTorchOn();
                    isLightOn=true;
                    swichLight.setText("关闭闪光灯");
                }
            }
        });

        if (!hasFlash()) {
            swichLight.setVisibility(View.GONE);
        }

        //重要代码，初始化捕获
        captureManager = new CaptureManager(this, mDBV);
        captureManager.initializeFromIntent(getIntent(), savedInstanceState);
        captureManager.decode();

    }


    public void onTorchOn() {
        Toast.makeText(this,"torch on",Toast.LENGTH_LONG).show();
        isLightOn = true;
    }


    public void onTorchOff() {
        Toast.makeText(this,"torch off",Toast.LENGTH_LONG).show();
        isLightOn = false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        captureManager.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        captureManager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        captureManager.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        captureManager.onDestroy();
    }

    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

}
