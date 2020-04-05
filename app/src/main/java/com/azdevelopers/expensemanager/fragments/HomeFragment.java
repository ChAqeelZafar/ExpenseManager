package com.azdevelopers.expensemanager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.azdevelopers.expensemanager.R;
import com.azdevelopers.expensemanager.models.Entry;
import com.azdevelopers.expensemanager.viewmodels.DataViewModel;

import java.util.List;

public class HomeFragment extends Fragment {


    private TextView textView;
    DataViewModel dataViewModel;
    private String data = "";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        textView = root.findViewById(R.id.home_text_text);
        dataViewModel = ViewModelProviders.of(this).get(DataViewModel.class);
        dataViewModel.getData().observeForever(new Observer<List<Entry>>() {
            @Override
            public void onChanged(List<Entry> entries) {
                Toast.makeText(getContext(), "FROM HOME: " + entries.size(), Toast.LENGTH_LONG).show();
                for(int i=0; i<entries.size()-1; i++){
                    Entry entry = entries.get(i);
                    data += "ID: " + entry.getId() +
                            "Amount: " + entry.getAmount() +
                            "Cat: " + entry.getCategory() +
                            "Date: " + entry.getDate() +
                            "Location: " +entry.getLocation()+"\n";
                }
                textView.setText(data);
            }
        });
        return root;
    }
}
