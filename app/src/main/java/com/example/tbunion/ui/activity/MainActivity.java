package com.example.tbunion.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.tbunion.R;
import com.example.tbunion.base.BaseFragment;
import com.example.tbunion.ui.fragment.HomeFragment;
import com.example.tbunion.ui.fragment.RedPacketFragment;
import com.example.tbunion.ui.fragment.SearchFragment;
import com.example.tbunion.ui.fragment.SelectedFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity  {
    @BindView(R.id.main_navigation_bar)
    public BottomNavigationView navigationView;
    private HomeFragment homeFragment;
    private  RedPacketFragment redPacketFragment;
    private SelectedFragment selectedFragment;
    private SearchFragment searchFragment;
    private FragmentManager fm;
    private Unbinder mBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBind = ButterKnife.bind(this);
        //initView();
        initFragment();
        initListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mBind!=null){
            mBind.unbind();
        }
    }

    private void initFragment() {
        homeFragment = new HomeFragment();
        redPacketFragment = new RedPacketFragment();
        selectedFragment = new SelectedFragment();
        searchFragment = new SearchFragment();
        fm = getSupportFragmentManager();
        switchFragment(homeFragment);

    }

    private void initListener() {
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.home){
                    switchFragment(homeFragment);
                }else if(item.getItemId()==R.id.selected){
                    switchFragment(selectedFragment);
                }
                else if(item.getItemId()==R.id.red_packet){
                    switchFragment(redPacketFragment);
                }
                else if(item.getItemId()==R.id.search){
                    switchFragment(searchFragment);
                }
                return true;
            }
        });
    }

    //上一次显示的fragment
    private BaseFragment lastOneFragment = null;

    private void switchFragment(BaseFragment targetFragment) {
        //用add和hide的方式来控制fragment的切换
        FragmentTransaction fragmentTransaction=fm.beginTransaction();
        if (!targetFragment.isAdded()) {
            fragmentTransaction.add(R.id.main_page_container,targetFragment);
        }else {

            fragmentTransaction.show(targetFragment);
        }
        if (lastOneFragment != null) {
            fragmentTransaction.hide(lastOneFragment);
        }
        lastOneFragment = targetFragment;
        //fragmentTransaction.replace(R.id.main_page_container,targetFragment);
        fragmentTransaction.commit();
    }

    private void initView() {

    }
}