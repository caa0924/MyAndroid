package com.mhimine.jdk.coordapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.mhimine.jdk.coordapp.Activity.DeviceDetailsActivity;
import com.mhimine.jdk.coordapp.Activity.MainActivity;
import com.mhimine.jdk.coordapp.Activity.ScannerActivity;
import com.mhimine.jdk.coordapp.Adapter.DeviceCheckAdapter;
import com.mhimine.jdk.coordapp.ObjectClass.DeviceCheck;
import com.mhimine.jdk.coordapp.R;
import com.mhimine.jdk.coordapp.Utils.Utils;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by JDK on 2016/8/4.
 */
public class SingleFragment extends Fragment {
    private List<DeviceCheck> userList = new ArrayList<>();
    String namespace = "http://tempuri.org/";
    String Url = "http://47.92.68.57:8099/WebService_MySql_Eq_Management.asmx?WSDL";
    String methodName = "Select";
    private View view;


    public static SingleFragment newInstance(Context context) {
        SingleFragment singleFragment = new SingleFragment();
        return singleFragment;
    }

    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        view = inflater.inflate(R.layout.fragment_watch_single, container, false);//得到对应的布局文件

        SoapObject soapObject = Utils.callWS(namespace, methodName,
                Url, null);
        if (soapObject != null) {

            String detail = soapObject.getProperty("SelectResult").toString();

            try {
                //将JSON字符串转换为List的结构
                List<Map<String, Object>> list = Utils.convertJSON2List(
                        detail, "Result_List", new String[]{"id",
                                "device_number", "check_number", "check_time"});
                initUser(list);
                DeviceCheckAdapter adapter = new DeviceCheckAdapter(getActivity(), R.layout.user_item, userList);
                final ListView listView = (ListView) view.findViewById(R.id.lv_listview);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        Bundle bundle = new Bundle();
                        bundle.putString("equip_code", userList.get(arg2).getDevice_number());
                        Intent intent = new Intent();
                        intent.putExtras(bundle);
                        intent.setClass(getActivity(), DeviceDetailsActivity.class);
                      //  Log.i("userList",userList.get(arg2).getDevice_number());

                        startActivity(intent);
                    }


                });


            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("This is null...");
        }

        return view;
    }


    private void initUser(List<Map<String, Object>> list) {
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> l = list.get(i);
            DeviceCheck user = new DeviceCheck(R.mipmap.equipment, l.get("device_number").toString(), l.get("check_time").toString(), l.get("check_number").toString());
            userList.add(user);
        }

    }


}
