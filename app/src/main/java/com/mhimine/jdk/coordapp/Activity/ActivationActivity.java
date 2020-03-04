package com.mhimine.jdk.coordapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.mhimine.jdk.coordapp.Model.Activation;
import com.mhimine.jdk.coordapp.R;
import com.mhimine.jdk.coordapp.Utils.GetDeviceId;

public class ActivationActivity extends AppCompatActivity {

    Activation activation;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String deviceId = GetDeviceId.getDeviceId(this);
        activation = new Activation(deviceId);
        String activationFromFile = GetDeviceId.getActivationCode(this);

        if (activation.verificationResults(activationFromFile)) {
            Intent intent = new Intent(ActivationActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            setContentView(R.layout.activity_activation);

            final EditText serialNum = (EditText)findViewById(R.id.serial_num);
            final EditText activationEt = (EditText)findViewById(R.id.activation_et);
            final Button okBtn = (Button)findViewById(R.id.activation_btn);
            final Button exitBtn = (Button)findViewById(R.id.exit_sys);
            serialNum.setKeyListener(null);
            serialNum.setText(deviceId);

            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String activationTe = activationEt.getText().toString();
                    if (activation.verificationResults(activationTe)) {
                        GetDeviceId.saveActivationcode(activationTe, v.getContext());
//                        Intent intent = new Intent(ActivationActivity.this, MainActivity.class);
//                        startActivity(intent);
                        Intent intent = new Intent();
                        intent.setClass(ActivationActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(),"激活码错误！",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            exitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    System.exit(0);
                }
            });
        }

    }
}
