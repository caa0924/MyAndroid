package com.mhimine.jdk.coordapp.Activity;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mhimine.jdk.coordapp.R;
import com.mhimine.jdk.coordapp.Utils.Utils;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.Map;

import static com.mhimine.jdk.coordapp.R.id.bt_regist_cancel;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;

    private EditText et_regist_user;
    private EditText et_regist_password;
    private EditText et_regist_password_again;
    private EditText et_regist_group;
    private EditText et_regist_telephone;
    private Button bt_regist_save, bt_regist_cancel;
    String namespace = "http://tempuri.org/";
    String Url = "http://47.92.68.57:8099/WebService_MySql_Eq_Management.asmx?WSDL";
    String methodName = "InsertIntoTbUser";
    Map<String, Object> params = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        iniviews();
    }

    private void iniviews() {
        et_regist_user = (EditText) findViewById(R.id.et_regist_user);
        et_regist_password = (EditText) findViewById(R.id.et_regist_password);
        et_regist_password_again = (EditText) findViewById(R.id.et_regist_password_again);
        et_regist_group = (EditText) findViewById(R.id.et_regist_group);
        et_regist_telephone = (EditText) findViewById(R.id.et_regist_telephone);
        bt_regist_save = (Button) findViewById(R.id.bt_regist_save);// 注册按钮
        bt_regist_cancel = (Button) findViewById(R.id.bt_regist_cancel);
        bt_regist_save.setOnClickListener(this);
        bt_regist_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_regist_save:
                String username = et_regist_user.getText().toString();
                String password = et_regist_password.getText().toString().trim();
                String password_again = et_regist_password_again.getText().toString().trim();
                String user_group = et_regist_group.getText().toString().trim();
                String user_telephone = et_regist_telephone.getText().toString().trim();
                // 非空验证
                if (username.isEmpty() || password.isEmpty() || password_again.isEmpty() || user_group.isEmpty() || user_telephone.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "未完善信息", Toast.LENGTH_SHORT).show();
                    break;
                } else if (!password.equals(password_again)) {
                    Toast.makeText(RegisterActivity.this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
                    break;
                } else if (JudgeRegist(regist(username, password, user_group, user_telephone)) == true) {
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        //注意：不能用save方法进行注册
                        finish();
                    }
                    case R.id.bt_regist_cancel:
                        finish();

                    default:
                        break;
                }
        }


    private int regist(String user_num, String user_password, String user_group, String
            user_telephone) {

        params.put("username", user_num);
        params.put("password", user_password);
        params.put("department", user_group);
        params.put("phone", user_telephone);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        SoapObject soapObject = Utils.callWS(namespace, methodName,
                Url, params);
        if (soapObject != null) {
            String detail = soapObject.getProperty("InsertIntoTbUserResult").toString();
            return Integer.parseInt(detail);
        } else {
            System.out.println("This is null...");
        }


        return 0;

    }

    public boolean JudgeRegist(int i) {
        if (i >= 1) {
            return true;
        } else {
            return false;
        }
    }
}



