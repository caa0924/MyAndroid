package com.mhimine.jdk.coordapp.Fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mhimine.jdk.coordapp.Activity.MainActivity;
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
    //String Url = "http://47.92.68.57:8099/WebServices_Device_Management.asmx?WSDL";
    String Url = "http://47.92.68.57:8099/WebService_MySql_Eq_Management.asmx?WSDL";
    String methodName = "Check_User";
    public View v;
    private MyListener myListener;
    private Boolean password_display = false;
    @Bind(R.id.et_password)
    EditText editTextPassword;
    @Bind(R.id.et_name)
    EditText editTextName;
    @Bind(R.id.btn_login)
    Button btn_Login;
    @Bind({R.id.iv_swich_passwrod})
    ImageView iv_Switch_password;
    @Bind(R.id.delete_username)
    ImageView iv_delete_username;
    //    @Bind(R.id.btn_register)
//    TextView txt_appName;
    public final static int TASK_LOOP_COMPLETE = 0;
    String name;
    String password;
    Map<String, Object> params = new HashMap<>();
    SharedPreferences sharedPreferences;
    ProgressDialog pd;

    @SuppressLint("ValidFragment")
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
        iv_Switch_password.setOnClickListener(this);
        iv_delete_username.setOnClickListener(this);
        iv_delete_username.setOnClickListener(this);
//        Typeface typeface=Typeface.createFromAsset(getActivity().getAssets(),"yingbixingshu.ttf");
//        txt_appName.setTypeface(typeface);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                name = editTextName.getText().toString();
                password = editTextPassword.getText().toString();


                pd = ProgressDialog.show(loginDailogFragment.getContext(), "登录", "正在登录…");
                if (name.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getActivity(), "请输入用户名跟密码", Toast.LENGTH_SHORT).show();
                    messageListener.sendEmptyMessage(TASK_LOOP_COMPLETE);
                } else {
                    new Thread() {
                        public void run() {
                            //sleep(5000);
                            int data = Login(name, password);
                            if (data == 0) {
                                Looper.prepare();
                                Toast.makeText(getActivity(), "请输入正确的用户名跟密码", Toast.LENGTH_SHORT).show();
                                messageListener.sendEmptyMessage(TASK_LOOP_COMPLETE);
                                Looper.loop();
                            } else {
                                myListener.sendContent(String.valueOf(data));//将内容进行回传
                                messageListener.sendEmptyMessage(TASK_LOOP_COMPLETE);
                            }
                        }
                    }.start();
                }

                break;
            case R.id.iv_swich_passwrod:
                //密码可见
                if (password_display == false) {
                    iv_Switch_password.setImageResource(R.mipmap.show_psw);
                    editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    password_display = true;
                } else {
                    //密码不可见
                    iv_Switch_password.setImageResource(R.mipmap.show_psw_press);
                    editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    password_display = false;
                }
                break;
            case R.id.delete_username:
                editTextName.setText("");
                break;
        }
    }

    private Handler messageListener = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case TASK_LOOP_COMPLETE:
                    pd.dismiss();
                    break;

            }
        }
    };

    private int Login(String name, String password) {
        int isLogin = 0;
        params.put("username", name);
        params.put("password", password);
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
    public void onAttach(Context context) {
            super.onAttach(context);
        // 1）写入数据：
        //步骤1：创建一个SharedPreferences对象
        //sharedPreferences= activity.getSharedPreferences("data", Context.MODE_PRIVATE);
        myListener = (MyListener) context;
    }
}


