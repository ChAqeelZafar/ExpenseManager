package com.azdevelopers.expensemanager.viewmodels;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.azdevelopers.expensemanager.helpers.MyApplication;
import com.azdevelopers.expensemanager.models.Entry;
import com.azdevelopers.expensemanager.interfaces.DataInterface;
import com.azdevelopers.expensemanager.repositories.DataRepository;

import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class DataViewModel extends androidx.lifecycle.ViewModel implements DataInterface {
    private DataRepository repository ;
    private static MutableLiveData<List<Entry>> listEntriesMutable = new MutableLiveData<>();
    private static MutableLiveData<List<String>> spinnerItemsMutable = new MutableLiveData<>();

    public void setData(Entry entry){
        repository.setData(entry, this);
    }

    public void init(){
        repository = DataRepository.getInstance();
        listEntriesMutable=new MutableLiveData<>();
        repository.getData(this);
    }
    public LiveData<List<Entry>> getData(){
        return listEntriesMutable;
    }


    @Override
    public void onDatabaseUploadComplete(String res) {
        Toast.makeText(MyApplication.getAppContext(), res, Toast.LENGTH_LONG).show();
        Log.w(TAG, res);
    }

    public void getSpinnerData(){
        repository = DataRepository.getInstance();
        spinnerItemsMutable=new MutableLiveData<>();
        repository.getSpinnersItems(this);
    }
    @Override
    public void onFirestoreUploadComplete(String res) {
        Toast.makeText(MyApplication.getAppContext(), res, Toast.LENGTH_LONG).show();

        Log.w(TAG, res);
    }

    @Override
    public void onGetData(List<Entry> listEntries, String src) {
        Toast.makeText(MyApplication.getAppContext(), src, Toast.LENGTH_LONG).show();
        this.listEntriesMutable.setValue(listEntries);

    }

    @Override
    public void getSpinnerItem(List<String> spinnersItems) {
        spinnerItemsMutable.setValue(spinnersItems);
    }

    @Override
    public void getMessage(String msg) {
        Toast.makeText(MyApplication.getAppContext(), msg, Toast.LENGTH_LONG).show();
    }


}
