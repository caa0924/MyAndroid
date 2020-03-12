package com.mhimine.jdk.coordapp.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mhimine.jdk.coordapp.R;

import butterknife.Bind;

/**
 * A simple {@link Fragment} subclass.
 */
public class KeepAlertFragment extends Fragment {


    public KeepAlertFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_keep_alert, container, false);
    }

}
