package com.mhimine.jdk.coordapp.Adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class fragmentAdapter extends PagerAdapter {
    private List<View> viewList;
    private List<String> titleList;

    @Override
    //必须实现
    public int getCount() {
        return titleList.size();
    }
    public fragmentAdapter(List<View> viewList,List<String> titleList){
        this.viewList=viewList;
        this.titleList=titleList;
    }

    @Override
    //必须实现
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    @Override
    //必须实现  销毁
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));

    }

    //移动进入下一页
    @Override
    //必须实现  实例化
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
