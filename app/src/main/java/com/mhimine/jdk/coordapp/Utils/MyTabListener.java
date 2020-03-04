package com.mhimine.jdk.coordapp.Utils;

import android.app.Activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;


public class MyTabListener implements ActionBar.TabListener {

    private final Activity mActivity;
    private final Class mClass;//指定要加载的FRagment所对应的类
    private Fragment mFragment;
    private final String mTag;
    public MyTabListener(Activity mActivity,String tag, Class mClass) {
        this.mActivity = mActivity;
        mTag = tag;
        this.mClass = mClass;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
        if (mFragment == null) {
            mFragment = Fragment.instantiate(mActivity, mClass.getName());
            fragmentTransaction.add(android.R.id.content, mFragment, null);
        }
        fragmentTransaction.attach(mFragment);//显示新页面
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
        if (mFragment != null) {
            fragmentTransaction.detach(mFragment);
        }
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

    }
}
