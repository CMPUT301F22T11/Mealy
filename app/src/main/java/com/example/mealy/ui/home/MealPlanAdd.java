package com.example.mealy.ui.home;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentResultListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.example.mealy.R;
import com.example.mealy.functions.DateFunc;
import com.example.mealy.functions.Firestore;
import com.example.mealy.functions.Validate;
import com.example.mealy.ui.home.IRAdd;
import com.example.mealy.ui.home.MealIRList;
import com.example.mealy.ui.ingredientStorage.Ingredient;
import com.example.mealy.ui.recipes.Recipe;
import com.example.mealy.ui.recipes.RecipeIngredientAdd;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * This class contains the DIalogFragment that allows the user to create/edit a RecipeIngredient entry.
 * After hitting the "Save" button, the fragment passes the created (or edited) Recipe Ingredient
 * object back to the RecipeEntry fragment, and ends.
 */
public class MealPlanAdd extends DialogFragment {
    private final MealPlanAdd fragment = this;
    Button Save, startDate, endDate;
    ImageButton AddIR;
    EditText MealPlanTitle;


    ArrayList<Object> IRList = new ArrayList <Object>();
    ArrayList<Recipe> recipeMap = new ArrayList<>();
    ArrayList<Ingredient> ingredientArray = new ArrayList<>();

    ListView RecipeIngredientList;

    ArrayAdapter<Object> IRListAdapter;


    FirebaseFirestore db = FirebaseFirestore.getInstance();


    CollectionReference collectionReference;

    DatePickerDialog datePickerDialog, datePickerDialogEnd;

    String compareStart, compareEnd;

    int IRIndex;

    private static final String TAG = "DocSnippets";

    boolean IRClicked = false, edit = false;

    View view;

    public MealPlanAdd() {
        // Constructor: TODO
    }

    public MealPlanAdd(int i) {
        edit = true;
        // Constructor: TODO
    }

    /**
     * This is the override for the oncreate
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Creates the constructed view for the user to interact with. Initialize all XML elements (TextViews, Spinners, EditTexts, etc).
     *
     * @param inflater LayoutInflater object used to inflate any views in the fragment
     * @param container May be used to generate LayoutParams of the view. Otherwise, this is NULL
     * @param savedInstanceState This crated fragment may be re-constructed from a previous saved state, stored in this parameter. Otherwise, this is NULL.
     *
     * @return Returns the view created.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // Inflates View
        view = inflater.inflate(R.layout.meal_plan, container, false);
        // Initializes Interface

        InitializeSaveButton();
        InitializeTextViews();
        InitializeIRList();
        onCreateFragment();
        InitializeDatePickerStart();
        InitializeDatePickerEnd();

        return view;
    }


    private void onCreateFragment() {
        getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            /**
             * Asks for an instance of recipe ingredient that was created, and returns it be adding it to the list of
             * recipe ingredients.
             * @param requestKey
             * @param bundle bundle that stores the data of the recipe ingredient
             */
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {

                Object thisObj = (Object)  bundle.getParcelable("IR");
                if (IRClicked == false) {
                    if (thisObj instanceof Ingredient) {
                        Ingredient thisIngredient = (Ingredient) thisObj;
                        IRList.add(thisIngredient);
                        IRListAdapter.notifyDataSetChanged();
                        ingredientArray.add(thisIngredient);
//                    recipeIngredientAdapter.add(thisIngredient);
                    }
                    else {
                        Recipe thisRecipe = (Recipe) thisObj;
                        IRList.add(thisRecipe);
                        IRListAdapter.notifyDataSetChanged();
                        recipeMap.add(thisRecipe);
//                    recipeIngredientAdapter.add(thisIngredient);


                    }

                }
                else {
                    if (thisObj instanceof Ingredient) {
                        Ingredient thisIngredient = (Ingredient) thisObj;
                        if (IRList.get(IRIndex) instanceof Recipe) {
                            recipeMap.remove((Recipe) IRList.get(IRIndex));
                        }
                        else {
                            ingredientArray.remove((Ingredient) IRList.get(IRIndex));
                        }
                        ingredientArray.add(thisIngredient);

                        IRList.set(IRIndex, thisIngredient);
                        IRListAdapter.notifyDataSetChanged();
//                    recipeIngredientAdapter.add(thisIngredient);
                    }
                    else {
                        Recipe thisRecipe = (Recipe) thisObj;
                        if (IRList.get(IRIndex) instanceof Recipe) {
                            recipeMap.remove((Recipe) IRList.get(IRIndex));
                        }
                        else {
                            ingredientArray.remove((Ingredient) IRList.get(IRIndex));
                        }
                        recipeMap.add(thisRecipe);
                        IRList.set(IRIndex, thisRecipe);
                        IRListAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void InitializeIRList() {
        IRListAdapter = new MealIRList(this.getActivity(), IRList);

        RecipeIngredientList = view.findViewById(R.id.meal_ir_list);
        RecipeIngredientList.setAdapter(IRListAdapter);

        RecipeIngredientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            /**
             * This function is called when the user wants to perform an action on the recipe ingredient. When the user selects an entry,
             * an Alert Dialog pops up. Then, the user can either choose to edit the ingredient, or remove it.
             * @param adapterView The adapterView where the click occurred
             * @param view The current view of the app
             * @param i Returns the index of the recipe ingredient that was selected
             * @param l THe row id of the recipe ingredient that was clicked
             */
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Ingredient/Recipe")
                        .setMessage("Select an action")
                        .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                IRIndex = i;
                                IRClicked = false;
                                if (IRList.get(IRIndex) instanceof Recipe) {
                                    new IRAdd("R").show(getActivity().getSupportFragmentManager(), "IR");
                                }
                                else {
                                    new IRAdd("I").show(getActivity().getSupportFragmentManager(), "IR");
                                }

                                IRClicked = true;
                            }
                        })
                        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                IRList.remove(i);
                                IRListAdapter.notifyDataSetChanged();

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }



    /**
     * Initializes the save button.
     */
    private void InitializeSaveButton() {
        Save = (Button) view.findViewById(R.id.Meal_Plan_Submitbutton);

        Save.setOnClickListener(new View.OnClickListener() {
            /**
             * Once the save button is clicked, get the user's inputs for all the different fields, and use it to create a
             * new Recipe Ingredient. Using Parcelable, the Recipe Ingredient gets passed back to the Recipe DialogFragment.
             *
             * @param view returns the current view of the activity
             */
            @Override
            public void onClick(View view) {
                Log.d("AHAHA", "This is my message");

                if (ValidData()) {

                    String mealPlanName = GetMPName();

                    requireActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();


                    HashMap<String, Object> data = GetData();


                    Firestore.storeToFirestore("MealPlan", mealPlanName, data);

                }


            }
        });
    }

    private HashMap<String, Object> GetData() {

        HashMap<String, Object> data = new HashMap<>();

        String IRNameText = MealPlanTitle.getText().toString();


        // Figure out how to attach image
        // Figure out how to attach ingredient

        data.put("Name", IRNameText);

//        HashMap<String, Map> data = new HashMap<>();

        data.put("Recipes", recipeMap);

//        HashMap<String, ArrayList<Ingredient>> data = new HashMap<>();

//        Ingredient thisIngredientArray[] = new Ingredient[ingredientArray.size()];
//
//        for (int x = 0; x < ingredientArray.size(); x++) {
//            thisIngredientArray[x] = ingredientArray.get(x);
//        }

        data.put("Ingredients", ingredientArray);

        data.put("Start Date", startDate.getText().toString());
        data.put("End Date", endDate.getText().toString());



        return data;
    }

//    private HashMap<String, Map> GetDataRecipes() {
//
//        HashMap<String, Map> data = new HashMap<>();
//
//        data.put("Recipes", recipeMap);
//
//        return data;
//    }
//
//    private HashMap<String, ArrayList<Ingredient>> GetDataIngredients() {
//
//        HashMap<String, ArrayList<Ingredient>> data = new HashMap<>();
//
////        Ingredient thisIngredientArray[] = new Ingredient[ingredientArray.size()];
////
////        for (int x = 0; x < ingredientArray.size(); x++) {
////            thisIngredientArray[x] = ingredientArray.get(x);
////        }
//
//        data.put("Recipes", ingredientArray);
//
//
//
//        return data;
//    }


    /**
     * Initializes all the EditText elements.
     */
    private void InitializeTextViews() {
        MealPlanTitle = (EditText) view.findViewById(R.id.Meal_Plan_Name);
        AddIR = view.findViewById(R.id.Meal_Plan_Add);

        AddIR.setOnClickListener(new View.OnClickListener() {

            /**
             * This function initiates the RecipeIngredient fragment to be used to create an
             * instance of a recipe ingredient class.
             * @param view give the current view of the app
             */
            @Override
            public void onClick(View view) {

                IRClicked = false;
                new IRAdd().show(getActivity().getSupportFragmentManager(), "IR");
                Log.wtf("idk", "Here2");



            }
        });

    }

    private void InitializeDatePickerStart() {
        startDate = view.findViewById(R.id.Meal_Plan_start);

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                // months index from 0-11
                month = month + 1;
                startDate.setText(Integer.toString(year)+"-"+Integer.toString(month)+"-"+Integer.toString(day));
                compareStart = Integer.toString(year)+"-"+Integer.toString(month)+"-"+Integer.toString(day);
            }
        };

        // create the date from whatever was input by the user
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);
        // the minimum expiration date is today
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + 86400000);

        startDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                datePickerDialog.show();

            }
        });
    }

    private void InitializeDatePickerEnd() {
        endDate = view.findViewById(R.id.Meal_Plan_end);

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                // months index from 0-11
                month = month + 1;
                endDate.setText(Integer.toString(year)+"-"+Integer.toString(month)+"-"+Integer.toString(day));
                compareEnd = Integer.toString(year)+"-"+Integer.toString(month)+"-"+Integer.toString(day);
            }
        };
        // create the date from whatever was input by the user
        Calendar cal = Calendar.getInstance();

//        String compareEnd = Integer.toString(year)+"-"+Integer.toString(month)+"-"+Integer.toString(day);
//        compareEnd = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialogEnd = new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);
        // the minimum expiration date is today
        datePickerDialogEnd.getDatePicker().setMinDate(System.currentTimeMillis() + 86400000);

        endDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                datePickerDialogEnd.show();

            }
        });
    }

    /**
     * Gets the name for the Recipe Ingredient the user gave for this current instacne of a Recipe Ingredient.
     *
     * @return Returns the name of the recipe ingredient that was created.
     */
    private String GetMPName() {
        return MealPlanTitle.getText().toString();
    }

    /**
     * Checks if the user has filled in all required fields. If not, then it returns an error prompting the user to
     * fill in said fields.
     *
     * @return returns true if the data is valid. False otherwise.
     */
    private boolean ValidData(){

        String mealPlan = MealPlanTitle.getText().toString();
        String startD = startDate.getText().toString();
        String endD = endDate.getText().toString();

        System.out.println("Start date format: " + compareStart);
        System.out.println("End date format: " + compareEnd);

        String[] startDateComponents = compareStart.split("-");
        String[] endDateComponents = compareEnd.split("-");

        int startYear = Integer.parseInt(startDateComponents[0]);
        int startMonth = Integer.parseInt(startDateComponents[1]);
        int startDay = Integer.parseInt(startDateComponents[2]);

        int endYear = Integer.parseInt(endDateComponents[0]);
        int endMonth = Integer.parseInt(endDateComponents[1]);
        int endDay = Integer.parseInt(endDateComponents[2]);

        boolean isValid = true;

        if (Validate.isEmpty(mealPlan)) {
            MealPlanTitle.setError("Please give a name to the Meal Plan");
            isValid =  false;
        }

        if (startD.equals("Start Date") || Validate.isEmpty(startD)) {
            startDate.setError("Please select a starting date!");
            isValid =  false;
        } else if (endD.equals("End Date")|| Validate.isEmpty(endD)) {
            endDate.setError("Please select an ending date!");
            isValid =  false;
        }
        // compare start date with end date
        else if ((startYear > endYear) || ((startYear == endYear) && ((startMonth > endMonth) || ((startMonth == endMonth) && (startDay > endDay))))) {
            startDate.setError("Start date has to come before end date!");
            endDate.setError("Start date has to come before end date!");
            isValid = false;
        }

        if (IRList.size() == 0) {
            Toast errorToast;
            errorToast = Toast.makeText(this.getActivity(), "Error, please add ingredient/recipe to the meal plan.", Toast.LENGTH_SHORT);
            errorToast.show();
            isValid = false;
        }


        return isValid;

    }
}
