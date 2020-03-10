package com.mhimine.jdk.coordapp.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mhimine.jdk.coordapp.R;
import com.mhimine.jdk.coordapp.Utils.Convert;


/**
 * Created by JDK on 2016/8/4.
 */
public class UpLoaddingInformationFragment extends Fragment {
    private View view;


    public static UpLoaddingInformationFragment newInstance(Context context){
        UpLoaddingInformationFragment singleFragment =new UpLoaddingInformationFragment();
        return singleFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_up_load_info, container, false);//得到对应的布局文件

        return view;
    }



}
