package com.mhimine.jdk.coordapp.Fragment;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mhimine.jdk.coordapp.Activity.MainActivity;
import com.mhimine.jdk.coordapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;


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
        return view;
    }

    public static WorkAlertFragment newInstance(Context context) {
        if (workAlertFragment == null) {
            workAlertFragment = new WorkAlertFragment();
        }
        return workAlertFragment;
    }

    @Override
    public void onClick(View v) {

        if (getActivity() instanceof workAlertFragmentListener) {
            ((workAlertFragmentListener) getActivity()).workAlertFragment();
        }


    }
}
