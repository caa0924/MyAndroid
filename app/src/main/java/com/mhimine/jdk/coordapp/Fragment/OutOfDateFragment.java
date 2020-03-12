package com.mhimine.jdk.coordapp.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mhimine.jdk.coordapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OutOfDateFragment extends Fragment {
    static OutOfDateFragment outOfDateFragment;

    public OutOfDateFragment() {
        // Required empty public constructor
    }

    public static OutOfDateFragment newInstance() {
        if (outOfDateFragment == null) {
            outOfDateFragment = new OutOfDateFragment();
        }
        return outOfDateFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_out_of_date, container, false);
    }

}
