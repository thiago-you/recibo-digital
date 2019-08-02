package you.dev.recibodigital;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.TextView;

import you.dev.recibodigital.Fragment.FragmentAdd;
import you.dev.recibodigital.Fragment.FragmentConfig;
import you.dev.recibodigital.Fragment.FragmentHome;

public class MainActivity extends AppCompatActivity {

    public static final int FRAGMENT_HOME_ID = 1;
    public static final int FRAGMENT_ADD_ID = 2;
    public static final int FRAGMENT_CONFIG_ID = 3;

    private BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        initUi();
    }

    private void initUi() {
        /* init home fragment */
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragment_content, new FragmentHome()).commit();
    }

    public void switchToFragment(int fragmentId) {
        FragmentManager manager = getSupportFragmentManager();

        /* set fragment */
        switch (fragmentId) {
            case MainActivity.FRAGMENT_HOME_ID:
                manager.beginTransaction().replace(R.id.fragment_content, new FragmentHome()).commit();
                break;
            case MainActivity.FRAGMENT_ADD_ID:
                manager.beginTransaction().replace(R.id.fragment_content, new FragmentAdd()).commit();
                break;
            case MainActivity.FRAGMENT_CONFIG_ID:
                manager.beginTransaction().replace(R.id.fragment_content, new FragmentConfig()).commit();
                break;
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    switchToFragment(MainActivity.FRAGMENT_HOME_ID);
                    return true;
                case R.id.navigation_add:
                    switchToFragment(MainActivity.FRAGMENT_ADD_ID);
                    return true;
                case R.id.navigation_config:
                    switchToFragment(MainActivity.FRAGMENT_CONFIG_ID);
                    return true;
            }

            return false;
        }
    };

    public void selectItem(int fragmentId) {
        /* set fragment */
        switch (fragmentId) {
            case MainActivity.FRAGMENT_HOME_ID:
                navView.findViewById(R.id.navigation_home).performClick();
                break;
            case MainActivity.FRAGMENT_ADD_ID:
                navView.findViewById(R.id.navigation_add).performClick();
                break;
            case MainActivity.FRAGMENT_CONFIG_ID:
                navView.findViewById(R.id.navigation_config).performClick();
                break;
        }
    }
}
