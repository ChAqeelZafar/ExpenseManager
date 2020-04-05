package com.azdevelopers.expensemanager.interfaces;

import com.azdevelopers.expensemanager.models.Entry;

import java.util.List;

public interface DataInterface {
    public void onDatabaseUploadComplete(String res);
    public void onFirestoreUploadComplete(String res);
    public void onGetData(List<Entry> listEntries, String src);
    public void getSpinnerItem(List<String> spinnersItems);
    public void getMessage(String msg);
}
