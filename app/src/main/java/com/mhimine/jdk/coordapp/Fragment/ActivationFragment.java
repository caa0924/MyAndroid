package com.mhimine.jdk.coordapp.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mhimine.jdk.coordapp.R;
import butterknife.Bind;


public class ActivationFragment  extends Fragment {
    @Bind(R.id.tool_bar)
    Toolbar toolbar;
    private View v;

    private static ActivationFragment activationFragment;
    public static ActivationFragment newInstance(){
        if( activationFragment ==null){
            activationFragment =new ActivationFragment();
        }
        return activationFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.activity_activation,container,false);
        //((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        //getActivity().getSupportFragmentManager().popBackStack();
        return v;
    }
}
