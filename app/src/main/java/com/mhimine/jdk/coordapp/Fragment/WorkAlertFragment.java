package com.mhimine.jdk.coordapp.Fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mhimine.jdk.coordapp.Activity.DeviceDetailsActivity;
import com.mhimine.jdk.coordapp.Activity.LoginActivity;
import com.mhimine.jdk.coordapp.Activity.OutOfDateActivity;
import com.mhimine.jdk.coordapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.mhimine.jdk.coordapp.Fragment.LoginDailogFragment.loginDailogFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class WorkAlertFragment extends Fragment implements View.OnClickListener {
    private static WorkAlertFragment workAlertFragment;
    private View view;
    @Bind(R.id.drawerIcon)
    TextView drawerIcon;
    @Bind(R.id.tv_keep_alert)
    TextView tv_keepAlert;
    @Bind(R.id.tv_check_alert)
    TextView tv_checkAlert;
    @Bind(R.id.cl_outOfDate)
    ConstraintLayout layout_outOfDate;
    private FragmentListener fragmentListener;
    public interface workAlertFragmentListener {
        public void workAlertFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_work_alert, container, false);
        ButterKnife.bind(this, view);
        TextPaint tp = tv_keepAlert.getPaint();
        TextPaint tpp = tv_checkAlert.getPaint();
        tp.setFakeBoldText(true);
        tpp.setFakeBoldText(true);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "iconfont.ttf");
        drawerIcon.setTypeface(typeface);
        drawerIcon.setOnClickListener(this);
        layout_outOfDate.setOnClickListener(this);
        return view;
    }

    public static WorkAlertFragment newInstance(Context context) {
        if (workAlertFragment == null) {
            workAlertFragment = new WorkAlertFragment();
        }
        return workAlertFragment;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.drawerIcon:
                if (getActivity() instanceof workAlertFragmentListener) {
                    ((workAlertFragmentListener) getActivity()).workAlertFragment();
                }
                break;
            case R.id.cl_outOfDate:
                fragmentListener.sendInfo("outofdate");
                break;
        }
    }
    public interface FragmentListener {
        void sendInfo(String info);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        // 1）写入数据：
        //步骤1：创建一个SharedPreferences对象
        //sharedPreferences= activity.getSharedPreferences("data", Context.MODE_PRIVATE);
        fragmentListener = (FragmentListener) context;

    }

}
