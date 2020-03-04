package com.mhimine.jdk.coordapp.Fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mhimine.jdk.coordapp.Activity.RegisterActivity;
import com.mhimine.jdk.coordapp.R;
import com.mhimine.jdk.coordapp.Utils.Utils;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginDailogFragment extends Fragment implements View.OnClickListener {

    static LoginDailogFragment loginDailogFragment;
    String namespace = "http://tempuri.org/";
    String Url = "http://47.92.68.57:8099/WebServices_Device_Management.asmx?WSDL";
    String methodName = "Check_User";
    public View v;
    private MyListener myListener;
    @Bind(R.id.et_password)
    EditText editTextPassword;
    @Bind(R.id.et_name)
    EditText editTextName;
    @Bind(R.id.btn_login)
    Button btn_Login;
    @Bind(R.id.btn_register)
    Button btn_register;
    @Bind(R.id.app_name)
    TextView txt_appName;
    Map<String, Object> params = new HashMap<>();
    SharedPreferences sharedPreferences;

    public LoginDailogFragment() {
        // Required empty public constructor
    }

    public static LoginDailogFragment newInstance() {
        if (loginDailogFragment == null) {
            loginDailogFragment = new LoginDailogFragment();
        }
        return loginDailogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_login_dailog, container, false);
        ButterKnife.bind(this, v);
        btn_Login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        Typeface typeface=Typeface.createFromAsset(getActivity().getAssets(),"yingbixingshu.ttf");
        txt_appName.setTypeface(typeface);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                String name = editTextName.getText().toString();
                String password = editTextPassword.getText().toString();
                if (name.isEmpty()&&password.isEmpty()){
                    Toast.makeText(getActivity(),"登录名或密码不能为空",Toast.LENGTH_SHORT).show();
                }
                myListener.sendContent(String.valueOf(Login(name,password)));//将内容进行回传
                break;
            case R.id.btn_register:
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    private int Login(String name,String password) {
        int isLogin = 0;
        try {

            params.put("username", name);
            params.put("password", password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        SoapObject soapObject = Utils.callWS(namespace, methodName,
                Url, params);
        if (soapObject != null) {
            String detail = soapObject.getProperty("Check_UserResult").toString();
            return Integer.parseInt(detail);
        } else {
            System.out.println("This is null...");
        }
        return isLogin;
    }

    public interface MyListener {
        void sendContent(String info);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // 1）写入数据：
        //步骤1：创建一个SharedPreferences对象
        //sharedPreferences= activity.getSharedPreferences("data", Context.MODE_PRIVATE);
        myListener = (MyListener) getActivity();
    }
}
