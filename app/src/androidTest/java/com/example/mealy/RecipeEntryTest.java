package com.example.mealy;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.app.Activity;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.robotium.solo.Solo;

import com.example.mealy.functions.Firestore;

/**
 * Test class for MainActivity. All the UI tests are written here. Robotium test framework is used
 */
@RunWith(AndroidJUnit4.class)
public class RecipeEntryTest {
    /**
     * Test class for MainActivity. All the UI tests are written here. Robotium test framework is
     used
     */
    private Solo solo;
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);


    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    /**
     * Testing the add recipe functions then checks the Firestore to see if the value is in there
     */
    @Test
    public void addRecipe(){
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnButton("Recipe"); //Click add recipe button

        // Get view for EditText and Spinner and enter the parameters
        solo.enterText((EditText) solo.getView(R.id.Recipe_Entry_RecipeName), "Tomato Soup");
        solo.enterText((EditText) solo.getView(R.id.Recipe_Entry_prepTime), "1");
        solo.enterText((EditText) solo.getView(R.id.Recipe_Entry_Servings), "1");
        solo.pressSpinnerItem(0, 2);
        solo.enterText((EditText) solo.getView(R.id.Recipe_Entry_Comments), "THIS IS A TEST COMMENT");
        solo.clickOnButton("SAVE"); //Click save recipe button

        // True if there is a recipe called Tomato Soup
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference recipeRef = rootRef.collection("Recipe");
        Query query = recipeRef.whereEqualTo("Recipe Name", "Tomato Soup");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                assertTrue(task.isSuccessful());
                for (DocumentSnapshot document : task.getResult()) {
                    assertTrue(document.exists());
                }

            }
        });

        // Deleting the entry from the Firestore
        Firestore.DeleteFromFirestore("Recipe", "Tomato Soup");

        // False if there is not a recipe called Tomato Soup
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                assertTrue(task.isSuccessful());
                for (DocumentSnapshot document : task.getResult()) {
                    assertFalse(document.exists());
                }

            }
        });
    }

}