package com.example.mealy.functions;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Firestore {

    private static Map<String, Object> data;

    /** Stores your data into Firestore
     *
     * @param CollectionName Name of collection in Database (e.g. Programming Languages)
     * @param document Title of Item (e.g. Java)
     * @param data  HashMap<String, String> of the data you want to store into the database (e.g. <"Founder", "James Gosling">)
     */
    public static void storeToFirestore(String CollectionName, String document, HashMap<String, String> data ) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String TAG = "!!!";
        final CollectionReference collectionReference = db.collection(CollectionName);

        collectionReference
                .document(document)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // These are a method which gets executed when the task is succeeded
                        Log.d(TAG, "Data has been added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.d(TAG, "Data could not be added!" + e.toString());
                    }
                });
    }

    /** Stores your data into Firestore
     *
     * @param CollectionName Name of collection in Database (e.g. Programming Languages)
     * @param document Title of Item (e.g. Java)
     * @param data  Map<String, Object> of the data you want to store into the database
     */
    public static void storeToFirestore(String CollectionName, String document, Map<String, Object> data ) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String TAG = "!!!";
        final CollectionReference collectionReference = db.collection(CollectionName);

        collectionReference
                .document(document)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // These are a method which gets executed when the task is succeeded
                        Log.d(TAG, "Data has been added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.d(TAG, "Data could not be added!" + e.toString());
                    }
                });
    }


    public static void deleteFromFirestore(String CollectionName, String document) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String TAG = "!!!";
        final CollectionReference collectionReference = db.collection(CollectionName);

        collectionReference
                .document(document)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // These are a method which gets executed when the task is succeeded
                        Log.d(TAG, "Data has been removed successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.d(TAG, "Data could not be removed!" + e.toString());
                    }
                });
    }

}
