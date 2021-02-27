package com.fab.rent.Navigation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.fab.rent.R;
//import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class UserHme extends AppCompatActivity {

//    private ChipNavigationBar chipNavigationBar;
//    private Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_hme);

//        chipNavigationBar = findViewById(R.id.chipNavigation);
//
//        chipNavigationBar.setItemSelected(R.id.home, true);
//        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
//
//        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(int i) {
//                switch (i) {
//                    case R.id.home:
//                        fragment = new HomeFragment();
//                        break;
//                    case R.id.search:
//                        fragment = new SearchFragment();
//                        break;
//
//                    case R.id.myrentals:
//                        fragment = new MyRentals();
//                        break;
//
//                    case R.id.settings:
//                        fragment = new SettingsFragment();
//                        break;

                }

//                if (fragment != null) {
//                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
//                }
//            }
//        });
//    }
}