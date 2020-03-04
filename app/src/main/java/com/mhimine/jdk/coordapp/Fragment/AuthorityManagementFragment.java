package com.mhimine.jdk.coordapp.Fragment;

import android.content.Context;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.mhimine.jdk.coordapp.R;
import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by JDK on 2016/8/13.
 */
public class AuthorityManagementFragment extends Fragment implements View.OnClickListener {
    @Bind(R.id.drawerIcon)
    TextView drawerIcon_tv;
    private View v;

    private static AuthorityManagementFragment paramSetFragment;



    public interface authorityManagementFragmentListener{
        public void authorityManagementFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(v!=null){
            ButterKnife.bind(this,v);
            return v;
        }
        v = inflater.inflate(R.layout.fragment_my_paramset, container, false);
        ButterKnife.bind(this, v);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "iconfont.ttf");
        drawerIcon_tv.setTypeface(typeface);
        drawerIcon_tv.setOnClickListener(this);

        return v;
    }

    public static AuthorityManagementFragment newInstance(Context context){
        if( paramSetFragment ==null){
            paramSetFragment =new AuthorityManagementFragment();
        }
       // ParamSetFragment paramSetFragment =new ParamSetFragment();
        return paramSetFragment;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onClick(View v) {
        if(getActivity() instanceof authorityManagementFragmentListener){
            ((authorityManagementFragmentListener) getActivity()).authorityManagementFragment();
        }
    }

}
