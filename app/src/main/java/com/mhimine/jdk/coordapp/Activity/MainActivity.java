package com.mhimine.jdk.coordapp.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;


import com.mhimine.jdk.coordapp.Fragment.AboutProjectFragment;
import com.mhimine.jdk.coordapp.Fragment.AuthorityManagementFragment;
import com.mhimine.jdk.coordapp.Fragment.CheckManageFragment;
import com.mhimine.jdk.coordapp.Fragment.Fragment1;

import com.mhimine.jdk.coordapp.Fragment.LoginDailogFragment;
import com.mhimine.jdk.coordapp.Fragment.WatchAndShakeFragment;
import com.mhimine.jdk.coordapp.Fragment.WorkAlertFragment;
import com.mhimine.jdk.coordapp.Model.Activation;
import com.mhimine.jdk.coordapp.R;
import com.mhimine.jdk.coordapp.Utils.BackHandlerHelper;
import com.mhimine.jdk.coordapp.Utils.SnackBarUtils;
import com.mhimine.jdk.coordapp.Utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by JDK on 2016/8/28.
 */
public class MainActivity extends BaseActivity implements WatchAndShakeFragment.watchAndShakeFragmentListener, AboutProjectFragment.aboutProjectDrawerIconListener,
        AuthorityManagementFragment.authorityManagementFragmentListener, Fragment1.fragment1Listener, LoginDailogFragment.MyListener,WorkAlertFragment.workAlertFragmentListener,CheckManageFragment.checkManageFragmentListener{
    @Bind(R.id.drawer)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.navigation_view)
    NavigationView mNavigationView;
    private boolean isLogin;
    private boolean isOpen;
    private WatchAndShakeFragment watchAndShakeFragment;
    private LoginDailogFragment loginDailogFragment;
    private CheckManageFragment checkManageFragment;
    private WorkAlertFragment workAlertFragment;
    private Fragment1 fragment1;
    private AboutProjectFragment aboutProjectFragment;
    private AuthorityManagementFragment authorityManagementFragment;
    private Fragment currentFragment;
    private long lastBackKeyDownTick = 0;
    private static final long MAX_DOUBLE_BACK_DURATION = 1500;
    private Activation activation;
    private String position;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Utils.init(this);
        createMainActivity();
    }

    private void createMainActivity() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
//        //透明状态栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        ButterKnife.bind(this);

        showDefaultFragment();


        mNavigationView.setItemIconTintList(null);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                isOpen = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                isOpen = false;
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
        initNavigationViewItemSelected();

    }

    public void showDefaultFragment() {
//        if (Integer.parseInt(position) == -1) {
//            if (watchAndShakeFragment == null) {
//                watchAndShakeFragment = WatchAndShakeFragment.newInstance();
//            }
//            addFragment(R.id.activity_main, watchAndShakeFragment);
//            currentFragment = watchAndShakeFragment;
//        } else {

        // 2）读取数据：
        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        try {
            isLogin = sharedPreferences.getBoolean("isLogin", true);
        } catch (Exception e) {
            isLogin=true;
            e.printStackTrace();
        }


        if (isLogin) {
            //步骤2： 实例化SharedPreferences.Editor对象
            SharedPreferences.Editor editor = sharedPreferences.edit();
            //步骤3：将获取过来的值放入文件
            editor.putBoolean("isLogin", false);
            //步骤4：提交
            editor.apply();

            if (loginDailogFragment == null) {
                loginDailogFragment = LoginDailogFragment.newInstance();
            }
            addFragment(R.id.activity_main, loginDailogFragment);
            currentFragment = loginDailogFragment;
        } else {
            if (watchAndShakeFragment == null) {
                watchAndShakeFragment = WatchAndShakeFragment.newInstance();
            }

            addFragment(R.id.activity_main, watchAndShakeFragment);
            currentFragment = watchAndShakeFragment;
        }

//        }
    }

    public void initNavigationViewItemSelected() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nacigation_menu_equipment_check:
                        if (watchAndShakeFragment == null) {
                            watchAndShakeFragment = WatchAndShakeFragment.newInstance();
                        }
                        switchFragment(currentFragment, watchAndShakeFragment);

                        break;
                    case R.id.navigation_work_alert:
                        if (workAlertFragment == null) {
                            workAlertFragment = WorkAlertFragment.newInstance(MainActivity.this);
                        }
                        switchFragment(currentFragment, workAlertFragment);
                        break;
                    case R.id.navigation_menu_information_deal:
                        if (fragment1 == null) {
                            fragment1 = Fragment1.newInstance();
                        }
                        switchFragment(currentFragment, fragment1);
                        break;
                    case R.id.navigation_check:
                        if (checkManageFragment == null) {
                            checkManageFragment = CheckManageFragment.newInstance();
                        }
                        switchFragment(currentFragment, checkManageFragment);
                        break;
                    case R.id.navigation_about_authority:
                        if (authorityManagementFragment == null) {
                            authorityManagementFragment = AuthorityManagementFragment.newInstance(MainActivity.this);
                        }
                        switchFragment(currentFragment, authorityManagementFragment);
                        break;
                    case R.id.navigation_cancel:
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        //步骤3：将获取过来的值放入文件
                        editor.putBoolean("isLogin", true);
                        //步骤4：提交
                        editor.apply();
                        if (loginDailogFragment == null) {
                            loginDailogFragment = LoginDailogFragment.newInstance();
                        }
                        switchFragment(currentFragment, loginDailogFragment);
                        break;

                }
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                return false;
            }
        });
    }

    public void switchFragment(Fragment from, Fragment to) {
        if (currentFragment != to) {
            currentFragment = to;
            if (!to.isAdded()) {
                getFragmentTransaction().hide(from).add(R.id.activity_main, to).commit();
            } else {
                getFragmentTransaction().hide(from).show(to).commit();
            }
        }
    }


    @Override
    public void watchAndShakeFragment() {
        if (!isOpen) {
            //LEFT和RIGHT指的是现存DrawerLayout的方向
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    }
    @Override
    public void workAlertFragment(){
        if (!isOpen) {
            //LEFT和RIGHT指的是现存DrawerLayout的方向
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    @Override
    public void onBackPressed() {
        long currentTick = System.currentTimeMillis();
        if (!BackHandlerHelper.handleBackPress(this)) {
            if (isOpen) {
                mDrawerLayout.closeDrawer(mNavigationView);
                isOpen = false;
            } else {
                if (currentTick - lastBackKeyDownTick > MAX_DOUBLE_BACK_DURATION) {
                    SnackBarUtils.makeShort(getWindow().getDecorView(), "再按一次退出").danger();
                    lastBackKeyDownTick = currentTick;
                } else {
//                    finish();
//                    System.exit(0);
                    exitApp();
                }
            }
        }
    }

    @Override
    public void aboutProjectDrawerIcon() {
        if (!isOpen) {
            //LEFT和RIGHT指的是现存DrawerLayout的方向
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    public void quitOnClick(View v) {
//        finish();
//        System.exit(0);
        exitApp();
    }

    private void exitApp() {

        finish();
        System.exit(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void authorityManagementFragment() {
        if (!isOpen) {
            //LEFT和RIGHT指的是现存DrawerLayout的方向
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }

    }

    @Override
    public void fragment1() {
        if (!isOpen) {
            //LEFT和RIGHT指的是现存DrawerLayout的方向
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    @Override
    public void sendContent(String info) {
        if (info != null && !"".equals(info)) {
            if (Integer.parseInt(info) == 1) {
                if (watchAndShakeFragment == null) {
                    watchAndShakeFragment = WatchAndShakeFragment.newInstance();
                }
                switchFragment(currentFragment, watchAndShakeFragment);

            }
        }
    }

    @Override
    public void checkManageFragment() {
        if (!isOpen) {
            //LEFT和RIGHT指的是现存DrawerLayout的方向
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    }
}
