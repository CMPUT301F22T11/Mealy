package com.example.mealy;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mealy.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    Button testButton;
    Button Home_Add_Recipe_Entry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.mealy.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        testButton = findViewById(R.id.buttonTest);
        Home_Add_Recipe_Entry = findViewById(R.id.Home_Add_Recipe_Entry);
        //This is for testing, to set the button to your view, modify it in test view
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open whatever fragment you want

                //Test Fragment. Just give the layout as an input for TestFragment, Format: TestFragment(R.layout.xmlFileName)
                //new TestFragment(R.layout.food_entry).show(getSupportFragmentManager(),"test");

                // Food Entry Layout For Testing
                new FoodEntry().show(getSupportFragmentManager(),"test");
            }
        });

        Home_Add_Recipe_Entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RecipeEntry().show(getSupportFragmentManager(),"test");
            }
        });
    }

}