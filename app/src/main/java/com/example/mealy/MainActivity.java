package com.example.mealy;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mealy.ui.home.HomeFragment;
import com.example.mealy.ui.ingredientStorage.IngredientFragment;
import com.example.mealy.ui.recipes.RecipeFragment;
import com.example.mealy.ui.shoppingList.ShoppingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    // for image upload
    public static Context contextOfApplication;

    RecipeFragment recipeFragment = new RecipeFragment();
    IngredientFragment ingredientFragment = new IngredientFragment();
    HomeFragment homeFragment = new HomeFragment();
    ShoppingFragment shoppingFragment = new ShoppingFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // for image upload
        contextOfApplication = getApplicationContext();

        BottomNavigationView navView = findViewById(R.id.nav_view);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();

        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
                        return true;
                    case R.id.navigation_dashboard: // ingredients
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, ingredientFragment).commit();
                        return true;
                    case R.id.navigation_notifications: // recipe
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,recipeFragment).commit();
                        return true;
                    case R.id.navigation_shoppingList:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,shoppingFragment).commit();
                        return true;
                }

                return false;
            }
        });


    }

    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }

}