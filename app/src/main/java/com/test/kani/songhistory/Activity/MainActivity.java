package com.test.kani.songhistory.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.test.kani.songhistory.R;
import com.test.kani.songhistory.fragment.AddHistoryFragment;
import com.test.kani.songhistory.fragment.ViewHistoryFragment;

public class MainActivity extends AppCompatActivity
{
    public static String id;
    public static boolean admin;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddHistoryFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener()
    {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
        {
            Fragment selectedFragment = null;

            switch (menuItem.getItemId())
            {
                case R.id.view_history_menu:
                    selectedFragment = new ViewHistoryFragment();
                    break;
                case R.id.add_history_menu:
                    selectedFragment = new AddHistoryFragment();
                    break;
                case R.id.my_info_menu:
//                    selectedFragment = new CallVisitFragment();
//                    if( !(boolean) MainActivity.myInfoMap.get("officer") )
//                    {
//                        Toast.makeText(getApplicationContext(), "용사는 이용할 수 없는 서비스입니다.", Toast.LENGTH_SHORT).show();
//                        return false;
//                    }
                    break;
//                case R.id.outsider_management_menu:
//                    selectedFragment = new OutsiderManagementFragment();
//                    if( !(boolean) MainActivity.myInfoMap.get("officer") )
//                    {
//                        Toast.makeText(getApplicationContext(), "용사는 이용할 수 없는 서비스입니다.", Toast.LENGTH_SHORT).show();
//                        return false;
//                    }
//                    break;
            }

            if( selectedFragment != null )
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

            return true;
        }
    };
}
