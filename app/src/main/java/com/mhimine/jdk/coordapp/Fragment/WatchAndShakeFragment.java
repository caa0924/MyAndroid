package com.mhimine.jdk.coordapp.Fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.mhimine.jdk.coordapp.R;
import com.mhimine.jdk.coordapp.Utils.FragmentBackHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by JDK on 2016/8/26.
 */
public class WatchAndShakeFragment extends Fragment implements View.OnClickListener,ViewPager.OnPageChangeListener,TextView.OnEditorActionListener,FragmentBackHandler {
    @Bind(R.id.tool_bar)

     Toolbar toolbar;
    @Bind(R.id.drawerIcon)
     TextView drawerIcon;
    @Bind(R.id.watch_tv)
     TextView watch_tv;

    @Bind(R.id.view_pager)
     ViewPager view_pager;


    private ActionBar actionBar;
    //ToolBar三个按钮对应的Fragment
    private List<Fragment> fragmentlist = new ArrayList<>(2);
    private View v;
    private MyFragmentPagerAdapter adapter;
    private static WatchAndShakeFragment watchAndShakeFragment;
    public static WatchAndShakeFragment newInstance(){
        if( watchAndShakeFragment ==null){
            watchAndShakeFragment =new WatchAndShakeFragment();
        }
        return watchAndShakeFragment;
    }

    @Override
    public boolean onBackPressed() {

            return false;

    }
 //通过点击软键盘可以搜索
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {

        }
        return false;
    }


    public interface watchAndShakeFragmentListener{
        public void watchAndShakeFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(v!=null){
            ButterKnife.bind(this, v);
            return v;
        }
         v=inflater.inflate(R.layout.fragment_watch_shake,container,false);
        ButterKnife.bind(this, v);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        actionBar= ((AppCompatActivity)getActivity()).getSupportActionBar();
        getActivity().getSupportFragmentManager().popBackStack();
        setHasOptionsMenu(true);

//        Toolbar toolbar =  (Toolbar)v.findViewById(R.id.tool_bar);
//        //inflater.setSupportActionBar(toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//
//        ButterKnife.bind(this,v);
//        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "iconfont.ttf");
//        drawerIcon_tv.setTypeface(typeface);


        addFragment();
        initWidgets();
        return v;
    }
    private void initWidgets() {
        adapter = new MyFragmentPagerAdapter(getFragmentManager());
        getFragmentManager();
        view_pager.setAdapter(adapter);
        view_pager.addOnPageChangeListener(this);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "iconfont.ttf");
        drawerIcon.setTypeface(typeface);
        drawerIcon.setOnClickListener(this);
        watch_tv.setOnClickListener(this);
        watch_tv.setTypeface(typeface);
        //初始化显示位置
        watch_tv.setSelected(true);
        view_pager.setCurrentItem(1);
    }

    private void addFragment() {
        fragmentlist.add(WatchFragment.getInstance());
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    private void getFocus(){
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return false;
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.watch_tv:
                watch_tv.setSelected(true);
                view_pager.setCurrentItem(1);
                break;
            case R.id.drawerIcon:
                if(getActivity()instanceof watchAndShakeFragmentListener){
                    ((watchAndShakeFragmentListener) getActivity()).watchAndShakeFragment();
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                watch_tv.setSelected(true);
                view_pager.setCurrentItem(0);
                break;
            case 1:
                watch_tv.setSelected(false);
                view_pager.setCurrentItem(1);
                break;

        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


//fragment适配器
    class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
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
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.coord_toolbar_menu, menu);
//        super.onCreateOptionsMenu(menu, inflater);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        final int id = item.getItemId();
//        if (id == R.id.coord_manage) {
//            DialogFileMangementFragment dialog = new DialogFileMangementFragment();
//            dialog.show(getFragmentManager(),"coord");
//        }
//        return super.onOptionsItemSelected(item);
//    }


}
