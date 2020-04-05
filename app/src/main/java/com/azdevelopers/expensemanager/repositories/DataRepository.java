package com.azdevelopers.expensemanager.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.azdevelopers.expensemanager.database.DbHelper;
import com.azdevelopers.expensemanager.database.DbManager;
import com.azdevelopers.expensemanager.helpers.MyApplication;
import com.azdevelopers.expensemanager.interfaces.DataInterface;
import com.azdevelopers.expensemanager.models.Entry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class DataRepository {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DbHelper dbHelper ;
    private SQLiteDatabase sqLiteDb ;
    private List<Entry> listEntries = new ArrayList<>();
    private static DataRepository instance;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private String userId = auth.getUid();

    private DataRepository() {
        dbHelper = new DbHelper(MyApplication.getAppContext());
        sqLiteDb = dbHelper.getWritableDatabase();
    }

    public static DataRepository getInstance(){
        if(instance==null)
            instance = new DataRepository();
        return instance;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) MyApplication.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void getData(DataInterface dataInterface){
        listEntries.clear();
            if (isNetworkAvailable()) {
                getDataFromFirestore(dataInterface);

            } else {
                getDataFromDatabase(dataInterface);
            }


    }




    public void getDataFromFirestore(DataInterface dataInterface){
        final DataInterface delegate = dataInterface;
        db.collection("users").document(userId).collection("expenses")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.getString("id");
                                String amount = document.getString("amount");
                                String cat = document.getString("category");
                                String date = document.getString("date");
                                String location = document.getString("location");
                                listEntries.add(new Entry(id, amount, cat, date, location));
                            }
                            delegate.onGetData(listEntries, "From Firebase");
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }

                    }
                });


    }

    public void getDataFromDatabase(DataInterface dataInterface){

        DataInterface delegate = dataInterface;



        String sortOrder =
                DbManager.TableInfo.COLUMN_NAME_DATE + " DESC";

        Cursor cursor = sqLiteDb.query(
                DbManager.TableInfo.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(DbManager.TableInfo._ID));
            String amount = cursor.getString(cursor.getColumnIndexOrThrow(DbManager.TableInfo.COLUMN_NAME_AMOUNT));
            String cat = cursor.getString(cursor.getColumnIndexOrThrow(DbManager.TableInfo.COLUMN_NAME_CATEGORY));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(DbManager.TableInfo.COLUMN_NAME_DATE));
            String location = cursor.getString(cursor.getColumnIndexOrThrow(DbManager.TableInfo.COLUMN_NAME_LOCATION));
            listEntries.add(new Entry(itemId+"", amount, cat, date, location));
        }
        cursor.close();
        delegate.onGetData(listEntries, "From Database");
    }

    public void setData(Entry entry, DataInterface dataInterface){
        setDataOnDatabase(entry, dataInterface);
    }

    public void setDataOnDatabase(final Entry entry, DataInterface dataInterface){
        DataInterface delegate = dataInterface;
        // Gets the data repository in write mode


// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DbManager.TableInfo.COLUMN_NAME_AMOUNT, entry.getAmount());
        values.put(DbManager.TableInfo.COLUMN_NAME_CATEGORY, entry.getCategory());
        values.put(DbManager.TableInfo.COLUMN_NAME_DATE, entry.getDate());
        values.put(DbManager.TableInfo.COLUMN_NAME_LOCATION, entry.getLocation());


// Insert the new row, returning the primary key value of the new row
        long newRowId = sqLiteDb.insert(DbManager.TableInfo.TABLE_NAME, null, values);
        if(newRowId>0) {
            delegate.onDatabaseUploadComplete("Update Database with id: "+ newRowId);
            entry.setId(newRowId + "");
            if (isNetworkAvailable()) {
                setDataOnFirestore(entry, dataInterface);
            }
        }else{
            delegate.onDatabaseUploadComplete("Failed to Update Database with id: "+ newRowId);
        }
    }


    public void setDataOnFirestore(final Entry entry, final DataInterface dataInterface){
        new AsyncTask<Void, Void, Void>(){
            DataInterface delegate = dataInterface;
            @Override
            protected Void doInBackground(Void... voids) {

                db.collection("users").document(userId).collection("expenses")
                        .add(entry)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                delegate.onFirestoreUploadComplete("DocumentSnapshot added with ID: " + documentReference.getId());
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                delegate.onFirestoreUploadComplete(e.getLocalizedMessage());
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
                return null;
            }


        }.execute();
    }


    public void getSpinnersItems(final DataInterface spinnerListener){
        final DataInterface delegate = spinnerListener;
        final List<String> spinnersItems = new ArrayList<>();
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {

                db.collection("users").document(userId).collection("expenses")
                        .document("categories").get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot document = task.getResult();
                                if(document==null){
                                    spinnersItems.add("Education");
                                    spinnersItems.add("Food");
                                    delegate.getMessage("HardCode Added");
                                    delegate.getSpinnerItem(spinnersItems);
                                }else{
                                    String[] arr = (String[]) document.get("dungeon_group");
                                    for(int i=0; i<arr.length; i++){
                                        spinnersItems.add(arr[i]);
                                    }
                                    delegate.getMessage("Firestore Added");
                                    delegate.getSpinnerItem(spinnersItems);
                                }
                            }
                        });
                return null;
            }


        }.execute();
    }
}
