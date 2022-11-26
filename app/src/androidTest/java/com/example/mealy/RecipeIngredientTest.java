package com.example.mealy;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

import android.widget.EditText;
import android.widget.ListView;

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
public class RecipeIngredientTest {
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
     * Testing the add recipe ingredient functions, as well as the editing function.
     * then checks the Firestore to see if the value is in there. This is also able to test the Firestore functions DeleteFromFirestore and StoreToFirestore.
     */
    @Test
    public void addRecipeIngredient() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnButton("Recipe"); //Click add recipe button

        // Get view for EditText and Spinner and enter the parameters
        solo.enterText((EditText) solo.getView(R.id.Recipe_Entry_RecipeName), "Potato Soup");
        solo.enterText((EditText) solo.getView(R.id.Recipe_Entry_prepTimeHour), "2");
        solo.enterText((EditText) solo.getView(R.id.Recipe_Entry_Servings), "2");
        solo.pressSpinnerItem(0, 1);
        solo.enterText((EditText) solo.getView(R.id.Recipe_Entry_prepTimeHour), "2");
        solo.enterText((EditText) solo.getView(R.id.Recipe_Entry_prepTimeMin), "2");
        solo.enterText((EditText) solo.getView(R.id.Recipe_Entry_Comments), "THIS IS A TEST COMMENT");

        // Now entering Recipe Ingredient fragment
        solo.clickOnImageButton(3);

        solo.pressSpinnerItem(1, 1);
        solo.enterText((EditText) solo.getView(R.id.r_ingredient_name_text), "Potato");
        solo.pressSpinnerItem(2, 1);
        solo.enterText((EditText) solo.getView(R.id.r_ingredient_category_text), "Junk");
        solo.pressSpinnerItem(3, 1);
        solo.enterText((EditText) solo.getView(R.id.r_ingredient_amount_text), "1");
        solo.enterText((EditText) solo.getView(R.id.r_ingredient_description_text), "Test description");
        solo.clickOnButton("SUBMIT");

        ListView ingredientList = (ListView)solo.getView(R.id.ingredient_list);
        // Check the list of ingredients is not empty


        // Create another Recipe Ingredient
        solo.clickOnImageButton(3);

        solo.pressSpinnerItem(1, 1);
        solo.enterText((EditText) solo.getView(R.id.r_ingredient_name_text), "Soup");
        solo.pressSpinnerItem(2, 1);
        solo.enterText((EditText) solo.getView(R.id.r_ingredient_category_text), "Puke");
        solo.pressSpinnerItem(3, 1);
        solo.enterText((EditText) solo.getView(R.id.r_ingredient_amount_text), "1");
        solo.enterText((EditText) solo.getView(R.id.r_ingredient_description_text), "Test description");
        solo.clickOnButton("SUBMIT");

        // Test Editing function for Recipe Ingredient
        solo.clickOnView(ingredientList.getChildAt(0));
        solo.waitForDialogToOpen();
        solo.clickOnButton("Edit");

        solo.pressSpinnerItem(1, 1);
        solo.enterText((EditText) solo.getView(R.id.r_ingredient_name_text), "Tomato");
        solo.pressSpinnerItem(2, 1);
        solo.enterText((EditText) solo.getView(R.id.r_ingredient_category_text), "Veggie");
        solo.pressSpinnerItem(3, 1);
        solo.enterText((EditText) solo.getView(R.id.r_ingredient_amount_text), "1");
        solo.enterText((EditText) solo.getView(R.id.r_ingredient_description_text), "Test description");
        solo.clickOnButton("SUBMIT");

        solo.clickOnButton("SAVE"); //Save the recipe


        // True if there is an ingredient called Tomato in the recipe fragment.
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference recipeRef = rootRef.collection("RecipeIngredients");
        Query query = recipeRef.whereEqualTo("Name", "Tomato");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                assertTrue(task.isSuccessful());
                for (DocumentSnapshot document : task.getResult()) {
                    assertTrue(document.exists());
                    assertTrue((String) document.getData().get("Category") == "Veggie");
                    assertTrue((String) document.getData().get("Recipe Name") == "Potato Soup");
                }

            }
        });

        // True if there is an ingredient called Soup in the recipe fragment.
        query = recipeRef.whereEqualTo("Name", "Soup");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                assertTrue(task.isSuccessful());
                for (DocumentSnapshot document : task.getResult()) {
                    assertTrue(document.exists());
                    assertTrue((String) document.getData().get("Category") == "Puke");
                    assertTrue((String) document.getData().get("Recipe Name") == "Potato Soup");
                }

            }
        });

        // Deleting recipe ingredients from Firestore
        Firestore.deleteFromFirestore("RecipeIngredients", "Tomato");
        Firestore.deleteFromFirestore("RecipeIngredients", "Soup");
        query = recipeRef.whereEqualTo("Name", "Tomato");

        // False if there is not a recipe ingredient called Tomato
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                assertTrue(task.isSuccessful());
                for (DocumentSnapshot document : task.getResult()) {
                    assertFalse(document.exists());
                }

            }
        });

        query = recipeRef.whereEqualTo("Name", "Soup");

        // False if there is not a recipe ingredient called Soup
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                assertTrue(task.isSuccessful());
                for (DocumentSnapshot document : task.getResult()) {
                    assertFalse(document.exists());
                }

            }
        });



        // Deleting the recipe from Firestore
        Firestore.deleteFromFirestore("Recipe", "Potato Soup");

        // False if there is not a recipe called Potato Soup
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

    /**
     * Testing the deleting function for a recipe ingredient.
     * then checks the Firestore to see if the value is in there. This is also able to test the Firestore functions DeleteFromFirestore and StoreToFirestore.
     */
    @Test
    public void removeRecipeIngredients() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnButton("Recipe"); //Click add recipe button

        // Get view for EditText and Spinner and enter the parameters
        solo.enterText((EditText) solo.getView(R.id.Recipe_Entry_RecipeName), "Donkey Soup");
        solo.enterText((EditText) solo.getView(R.id.Recipe_Entry_prepTimeHour), "1");
        solo.enterText((EditText) solo.getView(R.id.Recipe_Entry_Servings), "1");
        solo.pressSpinnerItem(0, 1);
        solo.enterText((EditText) solo.getView(R.id.Recipe_Entry_prepTimeHour), "2");
        solo.enterText((EditText) solo.getView(R.id.Recipe_Entry_prepTimeMin), "2");
        solo.enterText((EditText) solo.getView(R.id.Recipe_Entry_Comments), "THIS IS A TEST COMMENT");

        // Now entering Recipe Ingredient fragment
        solo.clickOnImageButton(3);

        solo.pressSpinnerItem(1, 1);
        solo.enterText((EditText) solo.getView(R.id.r_ingredient_name_text), "Donkey");
        solo.pressSpinnerItem(2, 1);
        solo.enterText((EditText) solo.getView(R.id.r_ingredient_category_text), "Animal");
        solo.pressSpinnerItem(3, 1);
        solo.enterText((EditText) solo.getView(R.id.r_ingredient_amount_text), "5");
        solo.enterText((EditText) solo.getView(R.id.r_ingredient_description_text), "Test description");
        solo.clickOnButton("SUBMIT");

        ListView ingredientList = (ListView)solo.getView(R.id.ingredient_list);
        // Check the list of ingredients is not empty


        // Create another Recipe Ingredient
        solo.clickOnImageButton(3);

        solo.pressSpinnerItem(1, 1);
        solo.enterText((EditText) solo.getView(R.id.r_ingredient_name_text), "Soup");
        solo.pressSpinnerItem(2, 1);
        solo.enterText((EditText) solo.getView(R.id.r_ingredient_category_text), "Yummy Food");
        solo.pressSpinnerItem(3, 1);
        solo.enterText((EditText) solo.getView(R.id.r_ingredient_amount_text), "5");
        solo.enterText((EditText) solo.getView(R.id.r_ingredient_description_text), "Test description");
        solo.clickOnButton("SUBMIT");

        // Test deleting function for Recipe Ingredient
        solo.clickOnView(ingredientList.getChildAt(0));
        solo.waitForDialogToOpen();
        solo.clickOnButton("Delete");

        solo.clickOnButton("SAVE"); //Save the recipe


        // True if there is an ingredient called Soup in the recipe fragment.
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference recipeRef = rootRef.collection("RecipeIngredients");
        Query query = recipeRef.whereEqualTo("Name", "Soup");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                assertTrue(task.isSuccessful());
                for (DocumentSnapshot document : task.getResult()) {
                    assertTrue(document.exists());
                    assertTrue((String) document.getData().get("Category") == "Yummy Food");
                    assertTrue((String) document.getData().get("Recipe Name") == "Donkey Soup");
                }

            }
        });

        // Deleting "Soup" from Firestore
        Firestore.deleteFromFirestore("RecipeIngredients", "Soup");
        query = recipeRef.whereEqualTo("Name", "Soup");

        // False if there is not a recipe ingredient called Donkey
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                assertTrue(task.isSuccessful());
                for (DocumentSnapshot document : task.getResult()) {
                    assertFalse(document.exists());
                }

            }
        });

        query = recipeRef.whereEqualTo("Name", "Donkey");

        // False if there is a recipe ingredient called Donkey, this checks the delete function for the Recipe Ingredient.
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                assertTrue(task.isSuccessful());
                for (DocumentSnapshot document : task.getResult()) {
                    assertFalse(document.exists());
                }

            }
        });



        // Deleting the recipe from Firestore
        Firestore.deleteFromFirestore("Recipe", "Potato Soup");

        // False if there is not a recipe called Potato Soup
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
