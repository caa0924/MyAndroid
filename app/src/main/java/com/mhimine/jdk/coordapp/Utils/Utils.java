package com.mhimine.jdk.coordapp.Utils;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Utils {
    private static Context context;

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        Utils.context = context.getApplicationContext();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("u should init first");
    }

    /**
     * @param nameSpace  WS的命名空间
     * @param methodName WS的方法名
     * @param wsdl       WS的wsdl的完整路径名
     * @param params     WS的方法所需要的参数
     * @return SoapObject对象
     */
    public static SoapObject callWS(String nameSpace, String methodName,
                                    String wsdl, Map<String, Object> params) {
        final String SOAP_ACTION = nameSpace + methodName;
        SoapObject soapObject = new SoapObject(nameSpace, methodName);

        if ((params != null) && (!params.isEmpty())) {
            Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
            //Iterator<Entry<String, Object>> it = params.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> e = (Map.Entry<String, Object>) ((Iterator) it).next();
                soapObject.addProperty(e.getKey(), e.getValue());
            }
        }

        //SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER12);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapSerializationEnvelope.VER12);
        envelope.bodyOut = soapObject;

        // 兼容.NET开发的Web Service
        envelope.dotNet = true;
        envelope.encodingStyle = "UTF-8";

        HttpTransportSE ht = new HttpTransportSE(wsdl);
        try {
            ht.getServiceConnection();
            ht.call(SOAP_ACTION, envelope);

            if (envelope.getResponse() != null) {
                SoapObject result = (SoapObject) envelope.bodyIn;
                return result;
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }
        return null;
    }

    /**
     * @param result JSON字符串
     * @param name   JSON数组名称
     * @param fields JSON字符串所包含的字段
     * @return 返回List<Map   <   String       ,       Object>>类型的列表，Map<String,Object>对应于 "id":"1"的结构
     */
    public static List<Map<String, Object>> convertJSON2List(String result,
                                                             String name, String[] fields) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            JSONArray array = new JSONObject(result).getJSONArray(name);

            for (int i = 0; i < array.length(); i++) {
                JSONObject object = (JSONObject) array.opt(i);
                Map<String, Object> map = new HashMap<String, Object>();
                for (String str : fields) {
                    map.put(str, object.get(str));
                }
                list.add(map);
            }
        } catch (JSONException e) {
            Log.e("error", e.getMessage());
        }
        return list;
    }


}
