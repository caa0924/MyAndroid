package com.mhimine.jdk.coordapp.Activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.mhimine.jdk.coordapp.Adapter.DeviceCheckAdapter;
import com.mhimine.jdk.coordapp.Adapter.EquipmentInfoAdapter;
import com.mhimine.jdk.coordapp.ObjectClass.DeviceCheck;
import com.mhimine.jdk.coordapp.ObjectClass.EquipmentInfo;
import com.mhimine.jdk.coordapp.R;
import com.mhimine.jdk.coordapp.Utils.Utils;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceManagerDetailActivity extends AppCompatActivity {

    String namespace = "http://tempuri.org/";
    String Url = "http://47.92.68.57:8099/WebService_MySql_Eq_Management.asmx?WSDL";
    String methodName = "SelectDeviceInfo";
    private List<String> equiplist = new ArrayList<>();
    ArrayList<String> equip_list = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_manager_detail);
        SoapObject soapObject = Utils.callWS(namespace, methodName,
                Url, null);
        if (soapObject != null) {

            String detail = soapObject.getProperty("SelectDeviceInfoResult").toString();
            //Toast.makeText(LoginActivity.this, detail, Toast.LENGTH_SHORT).show();

            try {
                //将JSON字符串转换为List的结构
                List<Map<String, Object>> list = Utils.convertJSON2List(
                        detail, "Result_List", new String[]{"equip_name",
                                "equip_number", "equip_type", "file_number", "craft_number", "equip_state", "factory_name", "factory_number", "factory_time", "inService_time", "belong_department", "equip_classify", "equip_value", "service_years", "equip_principal"});
                initUser(list);
                Resources res = getResources();
                String[] equip_info = res.getStringArray(R.array.equipInfo);
                List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();

                for (int i = 0; i < equip_info.length; i++) {
                    Map<String, Object> listItem = new HashMap<String, Object>();
                    listItem.put("header", equip_info[i]);

                    listItem.put("second", equiplist.get(i));

                    listItems.add(listItem);
                }
                initEquip(listItems);
                EquipmentInfoAdapter adapter =new EquipmentInfoAdapter(this,R.layout.equipment_info,equip_list);
                //SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.equiplistview, new String[]{"header", "second"}, new int[]{R.id.tv_equipInfo, R.id.tv_equipInfoValue});
                ListView listView = (ListView) findViewById(R.id.list1);
                listView.setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("This is null...");
        }
    }

    private void initUser(List<Map<String, Object>> list) {
        equiplist.add(list.get(0).get("equip_name").toString());
        equiplist.add(list.get(0).get("equip_number").toString());
        equiplist.add(list.get(0).get("equip_type").toString());
        equiplist.add(list.get(0).get("file_number").toString());
        equiplist.add(list.get(0).get("craft_number").toString());
        equiplist.add(list.get(0).get("equip_state").toString());
        equiplist.add(list.get(0).get("factory_name").toString());
        equiplist.add(list.get(0).get("factory_number").toString());
        equiplist.add(list.get(0).get("factory_time").toString());
        equiplist.add(list.get(0).get("inService_time").toString());
        equiplist.add(list.get(0).get("belong_department").toString());
        equiplist.add(list.get(0).get("equip_classify").toString());
        equiplist.add(list.get(0).get("equip_value").toString());
        equiplist.add(list.get(0).get("service_years").toString());
        equiplist.add(list.get(0).get("equip_principal").toString());
        // EquipmentInfo equipmentInfo = new EquipmentInfo(l.get("equip_name").toString(), l.get("equip_number").toString(), l.get("equip_type").toString(), l.get("file_number").toString(), l.get("craft_number").toString(), l.get("equip_state").toString(), l.get("factory_name").toString(), l.get("factory_number").toString(), l.get("factory_time").toString(), l.get("inService_time").toString(), l.get("belong_department").toString(), l.get("equip_classify").toString(), l.get("equip_value").toString(), l.get("service_years").toString(), l.get("equip_principal").toString());

    }
    private void initEquip(List<Map<String, Object>> list) {
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> l = list.get(i);
            //DeviceCheck user = new DeviceCheck(R.mipmap.equipment, l.get("device_number").toString(), l.get("check_time").toString(), l.get("check_number").toString());
            equip_list.add(l.get("header").toString());
            equip_list.add(l.get("second").toString());
        }

    }
}