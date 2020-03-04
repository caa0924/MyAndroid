package com.mhimine.jdk.coordapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.mhimine.jdk.coordapp.ObjectClass.DeviceCheck;
import com.mhimine.jdk.coordapp.R;
import java.util.List;

public class DeviceCheckAdapter extends ArrayAdapter<DeviceCheck> {
    private int resourceId;

    public DeviceCheckAdapter(Context content, int textViewResourceId, List<DeviceCheck> object) {
        super(content, textViewResourceId, object);
        resourceId = textViewResourceId;
    }
@Override
    public View getView(int position, View convertView, ViewGroup parant)
{
    DeviceCheck device=getItem(position);
    View view= LayoutInflater.from(getContext()).inflate(resourceId,parant,false);
    ImageView equipImage=(ImageView)view.findViewById(R.id.equip);
    TextView deviceNumber=(TextView)view.findViewById(R.id.device_number);
    TextView checkNumber=(TextView)view.findViewById(R.id.check_number);
    TextView checkTime=(TextView)view.findViewById(R.id.check_time);
    equipImage.setImageResource(device.getId());
    deviceNumber.setText(device.getDevice_number());
    checkNumber.setText(device.getCheck_number());
    checkTime.setText(device.getCheck_time());
    return view;

}
}
