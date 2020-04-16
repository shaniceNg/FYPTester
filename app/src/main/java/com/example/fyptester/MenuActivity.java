package com.example.fyptester;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MenuActivity extends AppCompatActivity {
    ChipNavigationBar bottomNav;
    FragmentManager fragmentManager;
    private static final String TAG=MenuActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        bottomNav=findViewById(R.id.bottom_nav);
        if (savedInstanceState==null){
            bottomNav.setItemSelected(R.id.nav_home,true);
            fragmentManager =getSupportFragmentManager();
            HomeFragment homeFragment = new HomeFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container,homeFragment)
                    .commit();
        }

        bottomNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                Fragment fragment = null;
                switch (id){
                    case R.id.nav_home:
                        fragment=new HomeFragment();
                        break;
                    case R.id.nav_map:
                        fragment=new MapFragment();
                        break;
                    case R.id.nav_settings:
                        fragment=new SettingsFragment();
                        break;

                }
                if(fragment!=null)
                {
                    fragmentManager=getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container,fragment)
                            .commit();
                }
                else {
                    Log.e(TAG, "Error in creating fragment");
                }
            }
        });
    }
}
