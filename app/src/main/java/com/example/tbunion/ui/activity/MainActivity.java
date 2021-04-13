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

public class MainActivity extends AppCompatActivity  {
    @BindView(R.id.main_navigation_bar)
    public BottomNavigationView navigationView;
    private HomeFragment homeFragment;
    private  RedPacketFragment redPacketFragment;
    private SelectedFragment selectedFragment;
    private SearchFragment searchFragment;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //initView();
        initFragment();
        initListener();
    }

    private void initFragment() {
        homeFragment = new HomeFragment();
        redPacketFragment = new RedPacketFragment();
        selectedFragment = new SelectedFragment();
        searchFragment = new SearchFragment();

        fm = getSupportFragmentManager();

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

    private void switchFragment(BaseFragment targetFragment) {
        FragmentTransaction fragmentTransaction=fm.beginTransaction();
        fragmentTransaction.replace(R.id.main_page_container,targetFragment);
        fragmentTransaction.commit();
    }

    private void initView() {

    }
}