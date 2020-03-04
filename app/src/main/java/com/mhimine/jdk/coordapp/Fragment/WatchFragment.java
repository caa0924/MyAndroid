package com.mhimine.jdk.coordapp.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mhimine.jdk.coordapp.Activity.DeviceDetailsActivity;
import com.mhimine.jdk.coordapp.Activity.DeviceManagerDetailActivity;
import com.mhimine.jdk.coordapp.Activity.ScannerActivity;
import com.mhimine.jdk.coordapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WatchFragment extends Fragment {
    @Bind(R.id.myViewPager)
    ViewPager myViewPager;
    @Bind(R.id.watch_tl)
    TabLayout tabLayout;
    Fragment singleFragment;
    Fragment multiFragment;
    Fragment services_RecordFragment;
    Fragment work_alertFragment;
    List<Fragment> fragmentList;
    private List<String> mTitleList = new ArrayList<>(4);
    View v;
    static WatchFragment watchFragment;

    public static WatchFragment getInstance() {
        if (watchFragment == null) {
            watchFragment = new WatchFragment();
        }
        return watchFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (v != null) {
            ButterKnife.bind(this, v);
            return v;
        }
        v = inflater.inflate(R.layout.paf_fragment_layout, container, false);
        ButterKnife.bind(this, v);
        InitVariable();
        MyAdapter myAdapter = new MyAdapter(getChildFragmentManager());
        myViewPager.setAdapter(myAdapter);
        myViewPager.setOffscreenPageLimit(4);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(myViewPager);
        Button button = (Button) v.findViewById(R.id.bt_scanner);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customScan();
            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getActivity(), "内容为空", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "扫描成功", Toast.LENGTH_LONG).show();
                String equip_code = intentResult.getContents();
                Bundle bundle = new Bundle();
                bundle.putString("equip_code",equip_code );
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(getActivity(), DeviceDetailsActivity.class);
                startActivity(intent);
                Toast.makeText(getActivity(), equip_code, Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void InitVariable() {
        mTitleList.add("未巡检设备");
        mTitleList.add("已巡检设备");
        singleFragment = SingleFragment.newInstance(getActivity());
        multiFragment = MultiFragment.newInstance(getActivity());
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(singleFragment);
        fragmentList.add(multiFragment);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }

    public void customScan() {
        //Activity中的初始化方式
//        new IntentIntegrator(this)
//                .setOrientationLocked(false)
//                .setCaptureActivity(CustomScanActivity.class) // 设置自定义的activity是CustomActivity
//                .initiateScan(); // 初始化扫描
//        //Fragment中的初始化方式
        IntentIntegrator.forSupportFragment(this)
                .setOrientationLocked(false)
                .setCaptureActivity(ScannerActivity.class)
                .initiateScan();

    }
}
