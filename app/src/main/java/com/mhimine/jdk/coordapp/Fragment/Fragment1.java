package com.mhimine.jdk.coordapp.Fragment;

import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mhimine.jdk.coordapp.R;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Fragment1 extends android.support.v4.app.Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private static Fragment1 fragment1;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.tablayout)
    TabLayout tabLayout;
    @Bind(R.id.drawerIcon)
    TextView drawerIcon;
    @Bind(R.id.watch_tv)
    TextView watch_tv;
    List<View> viewList = new ArrayList<>();
    List<String> titleList = new ArrayList<>();
    private View v;
    private List<Fragment> fragmentlist = new ArrayList<>(2);
    private MyNewFragmentPagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.activity_fragment1, container, false);
        Fragment_1 fragment_1 = new Fragment_1();
        Fragment_2 fragment_2 = new Fragment_2();
        Fragment_3 fragment_3 = new Fragment_3();
        titleList.add("设备参数预警");
        titleList.add("报警信息处理");
        titleList.add("超时工作预警");
//        inflater = LayoutInflater.from(getContext());
//        View tab1 = inflater.inflate(R.layout.paf_fragment_layout, null);
//        View tab2 = inflater.inflate(R.layout.fragment_fragment_2, null);
//        viewList.add(tab1);
//        viewList.add(tab2);
        ButterKnife.bind(this, v);
//        fragmentAdapter adapter = new fragmentAdapter(viewList, titleList);
//        viewpager.setAdapter(adapter);
//        tabLayout.setupWithViewPager(viewpager);
//        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "iconfont.ttf");
//        drawerIcon.setTypeface(typeface);
//        drawerIcon.setOnClickListener(this);
        fragmentlist.add(fragment_1);
        fragmentlist.add(fragment_2);
        fragmentlist.add(fragment_3);
        initWidgets();
        return v;
    }

    private void initWidgets() {
        adapter = new MyNewFragmentPagerAdapter(getFragmentManager());
        getFragmentManager();
        viewpager.setAdapter(adapter);
        viewpager.addOnPageChangeListener(this);
        viewpager.setOffscreenPageLimit(2);

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewpager);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "iconfont.ttf");
        drawerIcon.setTypeface(typeface);
        drawerIcon.setOnClickListener(this);
        watch_tv.setOnClickListener(this);
        watch_tv.setTypeface(typeface);
        //初始化显示位置
        watch_tv.setSelected(true);
        viewpager.setCurrentItem(1);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                watch_tv.setSelected(true);
                viewpager.setCurrentItem(0);
                break;
            case 1:
                watch_tv.setSelected(false);
                viewpager.setCurrentItem(1);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public interface fragment1Listener {
        public void fragment1();
    }

    @Override
    public void onClick(View v) {
        if (getActivity() instanceof fragment1Listener) {
            ((fragment1Listener) getActivity()).fragment1();
        }

    }


    public static Fragment1 newInstance() {
        if (fragment1 == null) {
            fragment1 = new Fragment1();
        }
        return fragment1;
    }

    public class MyNewFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyNewFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentlist.get(position);
        }

        @Override
        public int getCount() {
            return fragmentlist.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }

}
