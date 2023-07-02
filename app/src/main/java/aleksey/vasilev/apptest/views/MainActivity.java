package aleksey.vasilev.apptest.views;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import aleksey.vasilev.apptest.R;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        setBottomNavigation();
    }

    private void setBottomNavigation() {
        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_bar);
        bottomNavigationView.setOnItemSelectedListener((item -> {
                    final NavController navController = Navigation.findNavController(this, R.id.my_nav_host_fragment);
                    if (item.getItemId() == R.id.home_icon) {
                        navController.navigate(R.id.home_fragment);
                    } else if (item.getItemId() == R.id.person_icon) {
                        navController.navigate(R.id.person_fragment);
                    } else if (item.getItemId() == R.id.wallet_icon) {
                        navController.navigate(R.id.wallet_fragment);
                    } else {
                        navController.navigate(R.id.options_fragment);
                    }
                    return true;
                })
        );
    }
}