package com.example.mealy;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

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
@FixMethodOrder(MethodSorters.DEFAULT)
public class FoodEntryTest {
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
     * Testing the add Ingredient functions then checks the Firestore to see if the value is in there
     * Afterwards, it will go to the IngredientList to DisplayIngredientInfo to see if the values
     * are correct and to observe the functionality of DisplayIngredientInfo
     * This is also able to test the Firestore functions DeleteFromFirestore and StoreToFirestore
     */
    @Test
    public void addViewIngredient(){

        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnButton("Ingredient"); //Click add recipe button

        // Get view for EditText and Spinner and enter the parameters
        solo.enterText((EditText) solo.getView(R.id.ingredientName), "Tomato");
        solo.enterText((EditText) solo.getView(R.id.quantity), "6");
        solo.pressSpinnerItem(0, 1);
        solo.pressSpinnerItem(1, 1);
        solo.pressSpinnerItem(2, 2);

        solo.enterText((EditText) solo.getView(R.id.descriptionText), "THIS IS A TEST DESCRIPTION");

        solo.clickOnButton("Expiry Date");
        solo.clickOnButton("OK");

        solo.clickOnButton("Save"); //Click save recipe button

        // True if there is an Ingredient called Tomato
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference recipeRef = rootRef.collection("Ingredients");
        Query query = recipeRef.whereEqualTo("Description", "THIS IS A TEST DESCRIPTION");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                assertTrue(task.isSuccessful());
                for (DocumentSnapshot document : task.getResult()) {
                    assertTrue(document.exists());
                }

            }
        });

        // Viewing the ingredient that we just added using the DisplayIngredientInfo
        solo.clickOnView(solo.getView(R.id.navigation_dashboard));
        solo.clickOnText("Tomato");
        assertTrue(solo.waitForText("Raw Food", 1, 2000));
        assertTrue(solo.waitForText("Fridge", 1, 2000));
        assertTrue(solo.waitForText("single", 1, 2000));
        assertTrue(solo.waitForText("THIS IS A TEST DESCRIPTION", 1, 2000));
        solo.clickOnButton("Delete");
        solo.clickOnButton("Delete");



        // Deleting the entry from the Firestore
        //Firestore.DeleteFromFirestore("Ingredients", "Tomato");

        // False if there is not an Ingredient called Tomato
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